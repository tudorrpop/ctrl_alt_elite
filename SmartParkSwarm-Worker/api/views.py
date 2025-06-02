from django.utils.timezone import now
from django.utils.dateparse import parse_datetime
from rest_framework import generics, views, response, status
from django import http
from .models import ParkingLot, ParkingSpot, VehicleEntry
from .serializers import ParkingLotSerializer, ParkingSpotSerializer, VehicleEntrySerializer
import os
import csv
import json
from .orchestrator import orchestratorservice

class ParkingLotSetup(generics.CreateAPIView):
    """
    Create the ParkingLot if it does not exist (only runs on first launch).
    """
    serializer_class = ParkingLotSerializer

    def post(self, request, *args, **kwargs):
        if ParkingLot.objects.exists():
            return response.Response({"message": "ParkingLot already exists!"}, status=400)
        
        x_forwarded_for = request.META.get('HTTP_X_FORWARDED_FOR')
        ip = x_forwarded_for.split(',')[0] if x_forwarded_for else request.META.get('REMOTE_ADDR')

        data = request.data.copy()
        data['orchestrator_ip'] = "orchestrator.salmonpebble-d8e2875c.polandcentral.azurecontainerapps.io"

        serializer = self.get_serializer(data=data)
        serializer.is_valid(raise_exception=True)
        parking_lot = serializer.save()

        spots = []
        for i in range(1, parking_lot.capacity + 1):
            spots.append(ParkingSpot(
                lot_uuid=parking_lot,
                spot_number=i,
                is_occupied=False
            ))
        ParkingSpot.objects.bulk_create(spots)

        return response.Response(serializer.data, status=201)

class ParkingLotDetail(generics.RetrieveAPIView):
    """
    Retrieve the only ParkingLot.
    """
    serializer_class = ParkingLotSerializer

    def get(self, request, *args, **kwargs):
        parking_lot = ParkingLot.objects.first()
        if not parking_lot:
            raise http.Http404("No parking lot found")
        serializer = ParkingLotSerializer(parking_lot)
        return response.Response(serializer.data, status=200)

class ParkingSpotList(generics.ListCreateAPIView):
    """
    List all the parking spots in the current parking lot
    """
    serializer_class = ParkingSpotSerializer

    def get_queryset(self):
        queryset = ParkingSpot.objects.all()
        parkinglot = self.request.query_params.get('parkinglot')
        if parkinglot is not None:
            queryset = queryset.filter(lot_uuid=parkinglot)
        return queryset
    
class ParkingSpotDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    Get information about a specific parking spot.
    """
    serializer_class = ParkingSpotSerializer
    queryset = ParkingSpot.objects.all()

class VehicleEntryList(generics.ListCreateAPIView):
    """
    List the vehicle entry history.
    """
    serializer_class = VehicleEntrySerializer

    def get_queryset(self):
        queryset = VehicleEntry.objects.all()
        
        # Filter by parking spot if provided
        parkingspot = self.request.query_params.get('parkingspot')
        if parkingspot:
            queryset = queryset.filter(spot_uuid=parkingspot)

        # Filter by time interval if provided
        start_time = self.request.query_params.get('start_time')
        end_time = self.request.query_params.get('end_time')
        
        if start_time:
            parsed_start = parse_datetime(start_time)
            if parsed_start:
                queryset = queryset.filter(exit_time__gte=parsed_start) | queryset.filter(exit_time__isnull=True)
        
        if end_time:
            parsed_end = parse_datetime(end_time)
            if parsed_end:
                queryset = queryset.filter(entry_time__lte=parsed_end)

        return queryset
    
    def create(self, request, *args, **kwargs):
        spot_id = request.data.get('spot_uuid')

        if not spot_id:
            return response.Response({'error': 'spot_uuid is required.'}, status=400)

        try:
            spot = ParkingSpot.objects.get(uuid=spot_id)
        except ParkingSpot.DoesNotExist:
            return response.Response({'error': 'Parking spot not found.'}, status=404)

        if spot.is_occupied:
            return response.Response({'error': 'Parking spot is already occupied.'}, status=400)

        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        qr_code = serializer.validated_data.get('qr_code')
        
        if orchestratorservice.scan_qr_on_entry(qr_code):
            self.perform_create(serializer)
            spot.is_occupied = True
            spot.save()
            parking_lot = ParkingLot.objects.first()
            orchestratorservice.send_update_signal(parking_lot.self_id)
            headers = self.get_success_headers(serializer.data)
            return response.Response(serializer.data, status=201, headers=headers)
        else:
            return response.Response({'error': 'User is already parked in another lot.'}, status=400)     
    
class VehicleEntryDetail(generics.RetrieveUpdateDestroyAPIView):
    """
    Show details about a specific vehicle entry.
    """
    serializer_class = VehicleEntrySerializer
    queryset = VehicleEntry.objects.all()

class VehicleExitView(generics.UpdateAPIView):
    """
    Vehicle exits the parking lot.
    """
    serializer_class = VehicleEntrySerializer
    queryset = VehicleEntry.objects.all()
    lookup_field = 'qr_code'

    
    def put(self, request, *args, **kwargs):
        qr_code = kwargs.get('qr_code')

        if not orchestratorservice.scan_qr_on_exit(qr_code):
            return response.Response({'error': 'User is not in this parkinglot.'}, status=400)     
        
        try:
            entry = VehicleEntry.objects.get(qr_code=qr_code, active=True)
        except VehicleEntry.DoesNotExist:
            return response.Response({'error': 'Active vehicle with this QR code not found.'}, status=404)

        entry.exit_time = now()
        entry.active = False
        entry.save()

        spot = entry.spot_uuid
        spot.is_occupied = False
        spot.save()
        parking_lot = ParkingLot.objects.first()
        orchestratorservice.send_update_signal(parking_lot.self_id)

        return response.Response({'message': 'Vehicle exit registered successfully.'}, status=200)
    
class MetricFileView(views.APIView):
    """
    Serve a specific CSV metric file by name using DRF Response.
    """

    def get(self, request, metric_name):
        if metric_name == "free-spots-current":
            total_free = ParkingSpot.objects.filter(is_occupied=False).count()
            return response.Response(
                {"free_spots": total_free},
                status=status.HTTP_200_OK
            )
        
        base_dir = os.path.dirname(os.path.abspath(__file__))
        file_path = os.path.join(base_dir, '..', 'metrics', f'{metric_name}.csv')

        if not os.path.exists(file_path):
            return response.Response(
                {"detail": "Metric file not found."},
                status=status.HTTP_404_NOT_FOUND
            )

        with open(file_path, mode='r', newline='', encoding='utf-8') as csvfile:
            data = list(csv.DictReader(csvfile))
    
        json_content = json.dumps(data, indent=4)
        parsed_json = json.loads(json_content)

        return response.Response(
            parsed_json,
            # content_type='text/json',
            status=status.HTTP_200_OK
        )
from django.utils.timezone import now
from rest_framework import generics, views, response
from django import http
from .models import ParkingLot, ParkingSpot, VehicleEntry
from .serializers import ParkingLotSerializer, ParkingSpotSerializer, VehicleEntrySerializer

class ParkingLotSetup(generics.CreateAPIView):
    """
    Create the ParkingLot if it does not exist (only runs on first launch).
    """
    serializer_class = ParkingLotSerializer

    def post(self, request, *args, **kwargs):
        if ParkingLot.objects.exists():
            return response.Response({"message": "ParkingLot already exists!"}, status=400)

        serializer = self.get_serializer(data=request.data)
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
        parkingspot = self.request.query_params.get('parkingspot')
        if parkingspot is not None:
            queryset = queryset.filter(spot_id=parkingspot)
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
        self.perform_create(serializer)

        spot.is_occupied = True
        spot.save()

        headers = self.get_success_headers(serializer.data)
        return response.Response(serializer.data, status=201, headers=headers)
    
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

        return response.Response({'message': 'Vehicle exit registered successfully.'}, status=200)
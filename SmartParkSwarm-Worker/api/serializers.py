from rest_framework import serializers
from .models import ParkingLot, ParkingSpot, VehicleEntry

class ParkingLotSerializer(serializers.ModelSerializer):
    class Meta:
        model = ParkingLot
        fields = ('__all__')
        
class ParkingSpotSerializer(serializers.ModelSerializer):
    class Meta:
        model = ParkingSpot
        fields = ('__all__')

class VehicleEntrySerializer(serializers.ModelSerializer):
    class Meta:
        model = VehicleEntry
        fields = ('__all__')
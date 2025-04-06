from django.contrib import admin
from .models import ParkingLot, ParkingSpot, VehicleEntry

# Register your models here.
admin.site.register(ParkingLot)
admin.site.register(ParkingSpot)
admin.site.register(VehicleEntry)
from django.db import models
import uuid

# Create your models here.

class ParkingLot(models.Model):
    uuid = models.UUIDField(primary_key=True, unique=True, editable=False, default=uuid.uuid4)
    name = models.CharField(max_length=255)
    location = models.CharField(max_length=255)
    capacity = models.IntegerField(default=50)
    created_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.name


class ParkingSpot(models.Model):
    uuid = uuid = models.UUIDField(primary_key=True, unique=True, editable=False, default=uuid.uuid4)
    lot_uuid = models.ForeignKey(ParkingLot, on_delete=models.CASCADE)
    spot_number = models.IntegerField(unique=True)
    is_occupied = models.BooleanField()

    def __str__(self):
        return str(self.spot_number)
    
class VehicleEntry(models.Model):
    uuid = uuid = models.UUIDField(primary_key=True, unique=True, editable=False, default=uuid.uuid4)
    spot_uuid = models.ForeignKey(ParkingSpot, on_delete=models.DO_NOTHING)
    license_plate = models.CharField(max_length=10)
    qr_code = models.CharField(max_length=255, blank=False)
    entry_time = models.DateTimeField(auto_now_add=True)
    exit_time = models.DateTimeField(auto_now=False, null=True, blank=True)
    active = models.BooleanField()

    def __str__(self):
        return f"{self.license_plate} - {self.entry_time}"
    
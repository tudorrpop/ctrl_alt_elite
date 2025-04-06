from django.urls import path
from . import views

urlpatterns = [
    path('setup/', views.ParkingLotSetup.as_view()),
    path('ParkingLot/', views.ParkingLotDetail.as_view()),
    path('ParkingSpot/', views.ParkingSpotList.as_view()),
    path('ParkingSpot/<uuid:pk>/', views.ParkingSpotDetail.as_view()),
    path('VehicleEntry/', views.VehicleEntryList.as_view()),
    path('VehicleEntry/<uuid:pk>/', views.VehicleEntryDetail.as_view()),
    path('VehicleEntry/exit/<str:qr_code>/', views.VehicleExitView.as_view(), name='vehicle-exit'),
]
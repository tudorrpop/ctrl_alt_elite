import requests
from ..models import ParkingLot

def get_orchestrator_base_url():
    lot = ParkingLot.objects.first()
    if not lot or not lot.orchestrator_ip:
        raise ValueError("Orchestrator IP is not set in the database.")
    return f"http://{lot.orchestrator_ip}:8083"

def scan_qr_on_entry(qr_code):
    base_url = get_orchestrator_base_url()
    api_url = f"{base_url}/worker/markParked?uuid={qr_code}"
    res = requests.post(api_url)
    return res.json()

def scan_qr_on_exit(qr_code):
    base_url = get_orchestrator_base_url()
    api_url = f"{base_url}/worker/unmarkParked?uuid={qr_code}"
    res = requests.post(api_url)
    return res.json()

def send_update_signal():
    base_url = get_orchestrator_base_url()
    api_url = f"{base_url}/worker/update"
    res = requests.put(api_url)
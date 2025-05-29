import requests

def scan_qr_on_entry(qr_code):
    api_url = f"http://localhost:8083/worker/markParked?uuid={qr_code}"
    res = requests.post(api_url)
    return res.json()

def scan_qr_on_exit(qr_code):
    api_url = f"http://localhost:8083/worker/unmarkParked?uuid={qr_code}"
    res = requests.post(api_url)
    return res.json()

def send_update_signal():
    api_url = "http://localhost:8083/worker/update"
    res = requests.put(api_url)
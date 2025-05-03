# Setting up the development environment

## Requirements:
- python3
- pip3
- virtualenv (can be installed using pip3 with: ```pip install python3-virtualenv```)

## Setup

### Setup from CLI:
In order to prepare the environment from CLI, you need to open a shell and run the following commands:
```bash
git clone https://github.com/tudorrpop/ctrl_alt_elite.git
cd ctrl_alt_elite/SmartParkSwarm-Worker
virtualenv venv
source venv/bin/activate
pip install requirements.txt
```

### Setup from Visual Studio Code
1. Install the Python extension on Visual Studio Code. 
2. Open the SmartParkSwarm-Worker folder from Visual Studio Code (not the whole repository, only the child folder named SmartParkSwarm-Worker).
3. Press ```Ctrl + Shift + P``` and choose the option ```Python: Create Environment```.
4. Choose option ```Venv```.
5. Select the preferred python interpreter.
6. Select the file ```requirements.txt```.

## Run the application
On the first time running the application, you will need to also perform the DB migration for development:
```bash
python3 manage.py migrate
```

In order to run the app, you need to do it from a shell:
```bash
python3 manage.py runserver 0.0.0.0:8000
```

You also need to add the ip address of the host where the server runs to [smartparkswarm_worker/settings.py](./smartparkswarm_worker/settings.py) in order to be able to query the API from outside that host. You can do that by simply appending the IP address to the **ALLOWED_HOSTS** on line 28.

Then you can initialize the worker by running the following command:
```bash
./parking-system.sh --setup-parkinglot --worker-ip <IP> --parkinglot-name <NAME> --parkinglot-location <LOCATION> --parkinglot-capacity <CAPACITY>
```

## Simulating Parking Hardware
In order to simulate the hardware of the parking lot, you can use the [parking-system.sh](./parking-system.sh) script. This script has the functionality to simulate the following functionalities: scan the QR code to enter the parking lot, enter a parking spot & exit the parking lot. The script also provides some tools to initialize the parking lot (first init) and retrieve the existing parking spots for a specific worker. In order to get more info on how to use the script you can run **./parking-system.sh --help**.

## Application endpoints
The application offers the following endpoints that you can use to interact with the API:
- **"/api/setup/"**:
    - POST -> Initialize the worker parking lot database with the specified info.
- **"/api/ParkingLot/"**:
    - GET -> Retrieve information about the parking lot.
- **"/api/ParkingSpot/"**:
    - GET -> Retrieve all the parking spots in the lot and information about those.
- **"/api/ParkingSpot/{id}"**:
    - GET -> Retrieve information about a single parking spot.
    - PUT -> Update the specified parking spot.
- **"/api/VehicleEntry/"**:
    - GET -> Retrieve the history of the vehicles entries.
    - POST -> Add a vehicle entry.
- **"/api/VehicleEntry/{id}"**:
    - GET -> Retrieve information about a single vehicle entry.
    - PUT -> Edit a vehicle entry.
    - DELETE -> Delete a vehicle entry.
- **"/api/VehicleEntry/exit/{id}"**:
    - PUT -> Mark a vehicle as exiting the parking lot.

You can use either a http request wrapper (like Postman) or the Web view in order to interact with these endpoints. If you want to use the web view you can set **DEBUG = True** in [smartparkswarm_worker/settings.py](./smartparkswarm_worker/settings.py) at line 26 to get a Swagger-like interface.
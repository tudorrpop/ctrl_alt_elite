#!/bin/bash

set -e

worker_ip=""
getSpots_flag=false
setup_flag=false
entry_flag=false
exit_flag=false
scanQr_flag=false
parkinglot_name=""
parkinglot_location=""
parkinglot_capacity=0
license_plate=""
qr_code=""
spot_uuid=""

print_help() {
    echo "Usage:"
    echo "  $0 --setup-parkinglot --worker-ip IP --parkinglot-name NAME --parkinglot-location LOCATION --parkinglot-capacity CAPACITY"
    echo "  $0 --get-spots --worker-ip IP"
    echo "  $0 --scan-qr --worker-ip IP --qr-code QR"
    echo "  $0 --entry --worker-ip IP --spot-uuid UUID --license-plate PLATE --qr-code QR"
    echo "  $0 --exit --worker-ip IP --qr-code QR"
    echo
    echo "Options:"
    echo "  --setup-parkinglot       Setup the parking lot info"
    echo "  --get-spots              Get parking spots ids"
    echo "  --scan-qr                Simulate scanning the QR code at the barrier"
    echo "  --entry                  Simulate a vehicle entering"
    echo "  --exit                   Simulate a vehicle exiting"
    echo "  --worker-ip IP           IP address of the app worker"
    echo "  --parkinglot-name NAME   Name of the parking lot"
    echo "  --parkinglot-location LOCATION Location of the parking lot"
    echo "  --parkinglot-capacity N  Capacity of the parking lot"
    echo "  --license-plate PLATE    Vehicle license plate"
    echo "  --qr-code QR             QR code associated with the vehicle"
    echo "  --spot-uuid UUID         Optional parking spot UUID"
    echo "  --help                   Show this help message"
}

# First setup of the parking lot
setup_parkinglot() {
    ip=$1
    name=$2
    location=$3
    capacity=$4

    response=$(curl -s -w "%{http_code}" --location "http://$ip:8000/api/setup" \
    --header 'Content-Type: application/json' \
    --data "{
        \"name\": \"$name\",
        \"location\": \"$location\",
        \"capacity\": $capacity
    }" -o /dev/null)

    if [ $response -eq 201 ]; then
        echo "Successfully initialized parking lot"
    else
        echo "Something went wrong, http request returned code $response"
    fi
}

# Get all the spots in the lot and info about those
get_parkingspots() {
    ip=$1

    curl -s --location "http://$ip:8000/api/ParkingSpot" | \
    jq -r '[.[] | [.uuid, .spot_number, .is_occupied]] | (["ID","Number","Occupied"], (.[])) | @tsv' | \
    column -t
}

# Simulate a vehicle entering the parking lot
vehicle_enter() {
    ip=$1
    license_plate=$2
    qr_code=$3
    spot_uuid=$4

    response=$(curl -s -w "%{http_code}" --location "http://$ip:8000/api/VehicleEntry" \
    --header 'Content-Type: application/json' \
    --data "{
        \"license_plate\": \"$license_plate\",
        \"qr_code\": \"$qr_code\",
        \"active\": true,
        \"spot_uuid\": \"$spot_uuid\"
    }" -o /dev/null)

    if [ $response -eq 201 ]; then
        echo "Vehicle $license_plate parked on spot $spot_uuid."
    else
        echo "Something went wrong, http request returned code $response"
    fi
}


# Simulate a vehicle exiting the parking lot
vehicle_exit() {
    ip=$1
    qr_code=$2

    response=$(curl -s -w "%{http_code}" --location --request PUT "http://$ip:8000/api/VehicleEntry/exit/$qr_code" -o /dev/null)

    if [ $response -eq 200 ]; then
        echo "User with QR $qr_code exited the parking lot."
    else
        echo "Something went wrong, http request returned code $response"
    fi
}

# Simulate scanning a qr code
qr_scan() {
    ip=$1
    qr_code=$2

    # TODO ADD QR scan endpoint
    echo "QR scanned successfully"
}

if [ "$#" -eq 0 ] || [[ "$1" == "--help" ]]; then
    print_help
    exit 0
fi

while [[ "$#" -gt 0 ]]; do
    case "$1" in
        --setup-parkinglot) # Setup the parking lot
            setup_flag=true
            shift
            ;;
        --get-spots)        # Get all the parking spots and info about those
            getSpots_flag=true
            shift
            ;;
        --entry)            # Entry flag to trigger the vehicle entry function
            entry_flag=true
            shift
            ;;
        --exit)            # Exit flag to trigger the vehicle exit function
            exit_flag=true
            shift
            ;;
        --scan-qr)         # Flag to trigger when scanning the qr code
            scanQr_flag=true
            shift
            ;;
        --worker-ip)        # IP address of the worker
            worker_ip="$2"
            shift 2
            ;;
        --parkinglot-name)  # Name for the parkinglot
            parkinglot_name="$2"
            shift 2
            ;;
        --parkinglot-location) # Location of the parkinglot
            parkinglot_location="$2"
            shift 2
            ;;
        --parkinglot-capacity) # Capacity of the parkinglot
            parkinglot_capacity="$2"
            shift 2
            ;;
        --license-plate)    # License plate
            license_plate="$2"
            shift 2
            ;;
        --qr-code)          # QR code
            qr_code="$2"
            shift 2
            ;;
        --spot-uuid)        # Spot UUID
            spot_uuid="$2"
            shift 2
            ;;
        *)                  # Catch any invalid flags
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done


if [ -z "$worker_ip" ]; then
    echo "Error: You need to provide a valid worker ip address when interacting with the script"
    exit 1
elif $setup_flag; then
    if [ -n "$parkinglot_name" ] && [ -n "$parkinglot_location" ] && [ $parkinglot_capacity -ne 0 ]; then
        setup_parkinglot "$worker_ip" "$parkinglot_name" "$parkinglot_location" $parkinglot_capacity
    else
        echo "Error: --parkinglot-name, --parkinglot-location and --parkinglot-capacity are required when --setup-parkinglot is passed."
        exit 1
    fi
elif $getSpots_flag; then
    get_parkingspots "$worker_ip"
elif $entry_flag; then
    if [ -n "$license_plate" ] && [ -n "$qr_code" ]; then
        vehicle_enter "$worker_ip" "$license_plate" "$qr_code" "$spot_uuid"
    else
        echo "Error: --license-plate, --qr-code, --spot-uuid are required when --entry is passed."
        exit 1
    fi
elif $exit_flag; then
    if [ -n "$qr_code" ]; then
        vehicle_exit "$worker_ip" "$qr_code"
    else
        echo "Error: --qr-code is required when --exit is passed."
        exit 1
    fi
elif $scanQr_flag; then
    if [ -n "$qr_code" ]; then
        qr_scan "$worker_ip" "$qr_code"
    else
        echo "Error: --qr-code is required when --scan-qr is passed."
        exit 1
    fi
fi
package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.ParkingSpotStatus;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingLot;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingSpot;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.UserService;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.WorkerService;

import java.util.List;

@RestController
public class WorkersController {

    @Autowired
    private UserService userService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private SseController sseController;

    @Autowired
    public WorkersController(UserService userService, WorkerService workerService, SseController sseController) {
        this.userService = userService;
        this.workerService = workerService;
        this.sseController = sseController;
    }

    @PostMapping("/worker/markParked")
    public ResponseEntity<Boolean> markParked(@RequestParam String uuid) {
        boolean markParked = userService.markParked(uuid);
        return ResponseEntity.ok(markParked);
    }

    @PostMapping("/worker/unmarkParked")
    public ResponseEntity<Boolean> unmarkParked(@RequestParam String uuid) {
        boolean unmarkParked = userService.unmarkParked(uuid);
        return ResponseEntity.ok(unmarkParked);
    }

    @PutMapping("/worker/update")
    public ResponseEntity<List<ParkingSpotStatus>> updateParkingLot() {
        List<ParkingSpot> list = workerService.fetchAllParkingSpots("127.0.0.1:8000");
        List<ParkingSpotStatus> test = list.stream().map(spot ->
                new ParkingSpotStatus(
                        "P" + spot.getSpot_number(),
                        spot.is_occupied())).toList();

        sseController.broadcast(test);
        return ResponseEntity.ok(test);
    }

    @PostMapping("/worker/initialize")
    public ResponseEntity< ParkingLot> initializeWorker(@RequestBody StoreRequest storeRequest) {
        ParkingLot parkingLot = workerService.setupWorker(storeRequest);
        return ResponseEntity.ok(parkingLot);
    }



}

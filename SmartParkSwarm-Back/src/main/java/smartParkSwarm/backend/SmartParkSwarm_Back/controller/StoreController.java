package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.ParkingSpotStatus;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreOverviewModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.worker.ParkingSpot;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.WorkerService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class StoreController {

    @Autowired
    private final SseController sseController;

    @Autowired
    private final WorkerService workerService;

    @Autowired
    public StoreController(SseController sseController, WorkerService workerService){
        this.sseController = sseController;
        this.workerService = workerService;
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreOverviewModel>> fetchStores() {
        List<StoreOverviewModel> stores = new ArrayList<>();

        stores.add(new StoreOverviewModel(1L, "Kaufland Timisoara Fabric", "Strada Mihail Kogălniceanu 11, Timișoara 300126"));
        stores.add(new StoreOverviewModel(2L, "Kaufland Timisoara-Dumbravita", "Strada Conac 51, Timișoara 307160"));
        stores.add(new StoreOverviewModel(3L, "KauflandTimisoara-Elisabetin", "Strada Damaschin Bojinca 4, Timișoara 300216"));
        stores.add(new StoreOverviewModel(5L, "Kaufland Timisoara Vidrighin", "Strada Chimiștilor, Calea Stan Vidrighin 5-9, Timișoara 300571"));

        return ResponseEntity.ok(stores);
    }

    @PostMapping("/stores")
    public ResponseEntity<StoreOverviewModel> createStore(@RequestBody StoreRequest storeRequest) {
        // Should return a StoreOverviewModel after save
        return ResponseEntity.ok(new StoreOverviewModel(2L, storeRequest.getStoreName(), storeRequest.getStoreAddress()));
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<StoreModel> fetchStore(@PathVariable Long storeId) throws IOException {
        // Should return a StoreModel
        StoreModel store = new StoreModel(
                3L,
                "Kaufland Timisoara Fabric",
                "Strada Mihail Kogălniceanu 11, Timișoara 300126",
                ""
        );
        return ResponseEntity.ok(store);
    }

//    @PostConstruct
//    private void startSendingMockData() {
//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
//            sseController.broadcast(generateRandomStatuses());
//        }, 0, 5, TimeUnit.SECONDS);
//    }

    /**
     *
     * !! TESTING METHOD !!
     */
    private List<ParkingSpotStatus> generateRandomStatuses() {
        List<String> ids = List.of(
                "restricted_1", "restricted_2",
                "electric_1", "electric_2", "electric_3", "electric_4", "electric_5", "electric_6", "electric_7",
                "top_1", "top_2", "top_3", "top_4", "top_5", "top_6", "top_7",
                "coffee_1", "coffee_2", "coffee_3",
                "bottom_1", "bottom_2", "bottom_3", "bottom_4", "bottom_5",
                "bottom_6", "bottom_7", "bottom_8", "bottom_9", "bottom_10"
        );

        Random random = new Random();
        List<ParkingSpotStatus> parkingSpotStatuses = new ArrayList<>();

        for (String id : ids) {
            boolean isOccupied = random.nextBoolean();
            parkingSpotStatuses.add(new ParkingSpotStatus(id, isOccupied));
        }

        return parkingSpotStatuses;
    }
}

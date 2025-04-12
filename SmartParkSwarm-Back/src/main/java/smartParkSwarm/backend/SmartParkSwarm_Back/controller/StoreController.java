package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.ParkingSpotStatus;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreOverviewModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.service.StoreService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
public class StoreController {

    private final SseController sseController;

    private final StoreService storeService;

    public StoreController(SseController sseController, StoreService storeService) {
        this.sseController = sseController;
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreOverviewModel>> fetchStores() {
        List<StoreOverviewModel> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }

    @PostMapping("/stores")
    public ResponseEntity<StoreOverviewModel> createStore(@RequestBody StoreRequest storeRequest) {
        StoreOverviewModel storeOverviewModel=  storeService.saveStore(storeRequest);
        return ResponseEntity.ok(storeOverviewModel);
    }

    @PutMapping("/stores/{id}")
    public ResponseEntity<StoreOverviewModel> edit(@PathVariable Long id, @RequestBody StoreRequest storeRequest) {
        StoreOverviewModel updatedStore = storeService.editStore(id, storeRequest);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<StoreOverviewModel> delete(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<StoreModel> fetchStore(@PathVariable Long id) {
        StoreModel store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }

    @PostConstruct
    private void startSendingMockData() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            sseController.broadcast(generateRandomStatuses());
        }, 0, 5, TimeUnit.SECONDS);
    }

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

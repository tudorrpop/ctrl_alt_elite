package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreOverviewModel;

import smartParkSwarm.backend.SmartParkSwarm_Back.service.StoreService;

import java.util.List;

@RestController
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreOverviewModel>> fetchStores() {
        List<StoreOverviewModel> stores = storeService.getStores();
        return ResponseEntity.ok(stores);
    }

    // Method for testing the creation of multiple stores (WITHOUT WORKER INITIALIZATION).
    // The worker initialization functionality is in WorkersController -> WorkerService
//    @PostMapping("/stores")
//    public ResponseEntity<StoreOverviewModel> createStore(@RequestBody StoreRequest storeRequest) {
//        StoreOverviewModel storeOverviewModel = storeService.saveStore(storeRequest);
//        return ResponseEntity.ok(storeOverviewModel);
//    }

    @PutMapping("/stores/{id}")
    public ResponseEntity<StoreModel> edit(@PathVariable Long id, @RequestBody StoreRequest storeRequest) {
        StoreModel updatedStore = storeService.editStore(id, storeRequest);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<StoreModel> fetchStore(@PathVariable Long id) {
        StoreModel store = storeService.getStoreById(id);
        return ResponseEntity.ok(store);
    }
}

package smartParkSwarm.backend.SmartParkSwarm_Back.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreOverviewModel;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StoreController {

    @GetMapping("/stores")
    public ResponseEntity<List<StoreOverviewModel>> fetchStores() {
        List<StoreOverviewModel> stores = new ArrayList<>();

        stores.add(new StoreOverviewModel(1L, "Kaufland Eroilor"));
        stores.add(new StoreOverviewModel(2L, "Lidl Miresei"));
        stores.add(new StoreOverviewModel(3L, "Kaufland Dambovita"));
        stores.add(new StoreOverviewModel(5L, "Carrefour Operei"));

        return ResponseEntity.ok(stores);
    }
}

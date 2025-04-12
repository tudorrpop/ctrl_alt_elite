package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import org.springframework.stereotype.Service;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.entity.Store;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.ParkingLayout;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.request.StoreRequest;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.response.StoreOverviewModel;
import smartParkSwarm.backend.SmartParkSwarm_Back.repository.StoreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public StoreOverviewModel saveStore(StoreRequest storeRequest) {

        if(storeRequest.getStoreName() == null
                || storeRequest.getStoreAddress() == null
                || storeRequest.getParkingLayout() == null
                || storeRequest.getStoreName().isBlank()
                || storeRequest.getStoreAddress().isBlank()
                || storeRequest.getParkingLayout().isBlank()
        ) {
            return null;
        }

        Optional<Store> foundStore  = storeRepository.findByStoreName(storeRequest.getStoreName());
        if (foundStore.isPresent()) {
            return null;
        }

        Store store = new Store(
                storeRequest.getStoreName(),
                storeRequest.getStoreAddress(),
                ParkingLayout.valueOf(storeRequest.getParkingLayout().toUpperCase())
        );
        storeRepository.save(store);
        Optional<Store> returnedStored = storeRepository.findByStoreName(store.getStoreName());
        return returnedStored.map(value -> new StoreOverviewModel(
                value.getId(),
                value.getStoreName(),
                value.getStoreAddress()
        )).orElse(null);
    }

    public List<StoreOverviewModel> getStores() {
        List<Store> stores = storeRepository.findAll();
        List<StoreOverviewModel> storeOverviewModels = new ArrayList<>();
        for (Store store : stores) {
            storeOverviewModels.add(new StoreOverviewModel(
                    store.getId(),
                    store.getStoreName(),
                    store.getStoreAddress()
            ));
        }
        return storeOverviewModels;
    }

    public StoreModel getStoreById(Long storeId) {

        Optional<Store> foundStore = storeRepository.findById(storeId);
        return foundStore.map(store -> new StoreModel(
                store.getId(),
                store.getStoreName(),
                store.getStoreAddress(),
                store.getParkingLayout().toString()
        )).orElse(null);
    }

    public void deleteStore(Long storeId) {
        Optional<Store> foundStore = storeRepository.findById(storeId);
        foundStore.ifPresent(storeRepository::delete);
    }

    public StoreOverviewModel editStore(Long id, StoreRequest storeRequest) {

        if(storeRequest.getStoreName() == null
                || storeRequest.getStoreAddress() == null
                || storeRequest.getParkingLayout() == null
                || storeRequest.getStoreName().isBlank()
                || storeRequest.getStoreAddress().isBlank()
                || storeRequest.getParkingLayout().isBlank()
        ) {
            return null;
        }

        Optional<Store> foundStore = storeRepository.findById(id);
        if (foundStore.isEmpty()) {
            return null;
        }

        Optional<Store> store = storeRepository.findByStoreName(storeRequest.getStoreName());
        if (store.isPresent() && !store.get().getId().equals(foundStore.get().getId())) {
            return null;
        }
        foundStore.get().setStoreName(storeRequest.getStoreName());
        foundStore.get().setStoreAddress(storeRequest.getStoreAddress());
        foundStore.get().setParkingLayout(ParkingLayout.valueOf(storeRequest.getParkingLayout().toUpperCase()));
        storeRepository.save(foundStore.get());

        return new StoreOverviewModel(
                foundStore.get().getId(),
                foundStore.get().getStoreName(),
                foundStore.get().getStoreAddress()
        );
    }
}

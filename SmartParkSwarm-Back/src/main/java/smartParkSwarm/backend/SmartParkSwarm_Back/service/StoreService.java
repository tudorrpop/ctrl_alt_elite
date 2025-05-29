package smartParkSwarm.backend.SmartParkSwarm_Back.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
                || storeRequest.getCapacity() == null
                || storeRequest.getIpAddress() == null
                || storeRequest.getStoreName().isBlank()
                || storeRequest.getStoreAddress().isBlank()
                || storeRequest.getParkingLayout().isBlank()
                || storeRequest.getIpAddress().isBlank()
        ) {
            throw new IllegalArgumentException("Invalid fields: null or empty");
        }

        Optional<Store> foundStore  = storeRepository.findByStoreName(storeRequest.getStoreName());
        if (foundStore.isPresent()) {
            throw new EntityExistsException("A store with this name exists");
        }

        Store store = new Store(
                storeRequest.getStoreName(),
                storeRequest.getStoreAddress(),
                storeRequest.getParkingLayout(),
                storeRequest.getCapacity(),
                storeRequest.getIpAddress()
        );
        storeRepository.save(store);
        Optional<Store> returnedStored = storeRepository.findByStoreName(store.getStoreName());
        return returnedStored.map(value -> new StoreOverviewModel(
                value.getId(),
                value.getStoreName(),
                value.getStoreAddress()
        )).orElseThrow(() -> new EntityNotFoundException("A store with this name does not exist"));
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
                store.getParkingLayout()
        )).orElseThrow(() -> new EntityNotFoundException("A store with this name does not exist"));
    }

    public void deleteStore(Long storeId) {
        Optional<Store> foundStore = storeRepository.findById(storeId);
        foundStore.ifPresent(storeRepository::delete);
        throw new EntityNotFoundException("A store with this name does not exist");
    }

    public StoreOverviewModel editStore(Long id, StoreRequest storeRequest) {

        if(storeRequest.getStoreName() == null
                || storeRequest.getStoreAddress() == null
                || storeRequest.getParkingLayout() == null
                || storeRequest.getStoreName().isBlank()
                || storeRequest.getStoreAddress().isBlank()
                || storeRequest.getParkingLayout().isBlank()
        ) {
            throw new IllegalArgumentException("Invalid fields: null or empty");
        }

        Optional<Store> foundStore = storeRepository.findById(id);
        if (foundStore.isEmpty()) {
            throw new EntityNotFoundException("A store with this id does not exist");
        }

        Optional<Store> store = storeRepository.findByStoreName(storeRequest.getStoreName());
        if (store.isPresent() && !store.get().getId().equals(foundStore.get().getId())) {
            throw new EntityExistsException("A store with this name exists");
        }
        foundStore.get().setStoreName(storeRequest.getStoreName());
        foundStore.get().setStoreAddress(storeRequest.getStoreAddress());
        foundStore.get().setParkingLayout(storeRequest.getParkingLayout());
        storeRepository.save(foundStore.get());

        return new StoreOverviewModel(
                foundStore.get().getId(),
                foundStore.get().getStoreName(),
                foundStore.get().getStoreAddress()
        );
    }
}

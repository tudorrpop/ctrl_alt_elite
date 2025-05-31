package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public record StoreModel(Long storeId,
                         String storeName,
                         String storeAddress,
                         String parkingLayoutPath) {
}

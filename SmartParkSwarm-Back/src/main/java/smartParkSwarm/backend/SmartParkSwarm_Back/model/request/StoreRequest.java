package smartParkSwarm.backend.SmartParkSwarm_Back.model.request;

import lombok.Getter;

public class StoreRequest {
    private String storeName;
    private String storeAddress;
    private String parkingLayout;

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getParkingLayout() {
        return parkingLayout;
    }
}

package smartParkSwarm.backend.SmartParkSwarm_Back.model.request;

import lombok.Getter;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.ParkingLayout;

public class StoreRequest {
    private String storeName;
    private String storeAddress;
    private String parkingLayout;

    public StoreRequest(String storeName, String storeAddress, String parkingLayout) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.parkingLayout = parkingLayout;
    }

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

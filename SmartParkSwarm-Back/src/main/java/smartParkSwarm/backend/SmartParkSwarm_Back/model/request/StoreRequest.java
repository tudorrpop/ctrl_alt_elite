package smartParkSwarm.backend.SmartParkSwarm_Back.model.request;

public class StoreRequest {
    private String storeName;
    private String storeAddress;
    private String parkingLayoutPath;

    public StoreRequest(String storeName,
                        String storeAddress,
                        String parkingLayout
                        //String IpAddress
                        ) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.parkingLayoutPath = parkingLayout;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public String getParkingLayoutPath() {
        return parkingLayoutPath;
    }
}

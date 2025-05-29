package smartParkSwarm.backend.SmartParkSwarm_Back.model.request;

public class StoreRequest {
    private String storeName;
    private String storeAddress;
    private Integer capacity;
    private String parkingLayout;
    private String IpAddress;

    public StoreRequest(String storeName,
                        String storeAddress,
                        Integer capacity,
                        String parkingLayout,
                        String IpAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.capacity = capacity;
        this.parkingLayout = parkingLayout;
        this.IpAddress = IpAddress;
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

    public Integer getCapacity() {
        return capacity;
    }

    public String getIpAddress() {
        return IpAddress;
    }
}

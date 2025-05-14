package smartParkSwarm.backend.SmartParkSwarm_Back.model.entity;

import jakarta.persistence.*;
import smartParkSwarm.backend.SmartParkSwarm_Back.model.enums.ParkingLayout;

@Entity
@Table
public class Store {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;

    private String storeAddress;

    private String parkingLayout;

    public Store() {
    }

    public Store(String storeName, String storeAddress, String parkingLayout) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.parkingLayout = parkingLayout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getParkingLayout() {
        return parkingLayout;
    }

    public void setParkingLayout(String parkingLayout) {
        this.parkingLayout = parkingLayout;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}

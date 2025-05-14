package smartParkSwarm.backend.SmartParkSwarm_Back.model.worker;

public class ParkingLot {
    private String uuid;
    private String name;
    private String location;
    private int capacity;
    private String created_at;

    public ParkingLot(String name, String location, int capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
    }
}

package smartParkSwarm.backend.SmartParkSwarm_Back.model.worker;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParkingSpot {
    private String uuid;
    private String spot_number;
    @JsonProperty("is_occupied")
    private boolean is_occupied;
    private String lot_uuid;

    public boolean is_occupied() {
        return is_occupied;
    }

    public String getSpot_number() {
        return spot_number;
    }
}

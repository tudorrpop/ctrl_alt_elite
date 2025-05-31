package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public class SpotInfo {
    private String hour;
    private int free_spots;

    public SpotInfo() {}

    public SpotInfo(String hour, int freeSpots) {
        this.hour = hour;
        this.free_spots = freeSpots;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getFree_spots() {
        return free_spots;
    }

    public void setFree_spots(int free_spots) {
        this.free_spots = free_spots;
    }
}


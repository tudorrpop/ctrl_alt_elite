package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public class WeekdayOcuppancy {

    private String weekday;
    private double occupancy_percent;

    public WeekdayOcuppancy(String weekday, double occupancy_percent) {
        this.weekday = weekday;
        this.occupancy_percent = occupancy_percent;
    }

    public WeekdayOcuppancy(){}

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public double getOccupancy_percent() {
        return occupancy_percent;
    }

    public void setOccupancy_percent(double occupancy_percent) {
        this.occupancy_percent = occupancy_percent;
    }
}

package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public class MonthDayOcuppancy {

    private String day;
    private double occupancy_percent;

    public MonthDayOcuppancy(String weekday, double occupancy_percent) {
        this.day = weekday;
        this.occupancy_percent = occupancy_percent;
    }

    public MonthDayOcuppancy(){}

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getOccupancy_percent() {
        return occupancy_percent;
    }

    public void setOccupancy_percent(double occupancy_percent) {
        this.occupancy_percent = occupancy_percent;
    }
}

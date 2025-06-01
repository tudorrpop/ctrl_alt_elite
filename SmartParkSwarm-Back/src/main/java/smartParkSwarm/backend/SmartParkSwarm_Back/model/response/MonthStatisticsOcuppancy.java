package smartParkSwarm.backend.SmartParkSwarm_Back.model.response;

public class MonthStatisticsOcuppancy {

    private String store;
    private String month_name;
    private double occupancy_percent;

    public MonthStatisticsOcuppancy(String store, String weekday,  double occupancy_percent) {
        this.store = store;
        this.month_name = weekday;
        this.occupancy_percent = occupancy_percent;
    }

    public MonthStatisticsOcuppancy(){}

    public String getMonth_name() {
        return month_name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public double getOccupancy_percent() {
        return occupancy_percent;
    }

    public void setOccupancy_percent(double occupancy_percent) {
        this.occupancy_percent = occupancy_percent;
    }

}

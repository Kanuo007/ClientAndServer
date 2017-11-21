package bsdsass2testdata;

public class StatValue {

    private int total_vertical;
    private int num_lift_rides;

    public StatValue(int total_vertical, int num_lift_rides) {

        this.total_vertical = total_vertical;
        this.num_lift_rides = num_lift_rides;
    }


    public int getTotal_vertical() {
        return total_vertical;
    }

    public int getNum_lift_rides() {
        return num_lift_rides;
    }

    public void setTotal_vertical(int total_vertical) {
        this.total_vertical = total_vertical;
    }

    public void setNum_lift_rides(int num_lift_rides) {
        this.num_lift_rides = num_lift_rides;
    }

    @Override
    public String toString() {
        return "StatValue{" +
                "total_vertical=" + total_vertical +
                ", num_lift_rides=" + num_lift_rides +
                '}';
    }
}

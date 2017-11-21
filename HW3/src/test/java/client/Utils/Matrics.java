package client.Utils;

/**
 * Created by iris on 11/19/17.
 */
public class Matrics {
    Long startTime;
    Long latency;

    public Matrics(Long startTime, Long latency) {
        this.startTime = startTime;
        this.latency = latency;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getLatency() {
        return latency;
    }

    public void setLatency(Long latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return startTime + "/" + latency;
    }
}

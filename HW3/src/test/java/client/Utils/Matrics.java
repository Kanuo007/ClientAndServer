package client.Utils;

/**
 * Created by iris on 11/19/17.
 */
public class Matrics {
    Long startTime;
    Long latency;
    int errorAmount;

    public Matrics(Long startTime, Long latency, int errorAmount) {
        this.startTime = startTime;
        this.latency = latency;
        this.errorAmount = errorAmount;
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

    public int getErrorAmount() {
        return errorAmount;
    }

    public void setErrorAmount(int errorAmount) {
        this.errorAmount = errorAmount;
    }
}

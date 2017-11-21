package bsdsass2testdata;

public class Latency {
    String hostName;
    int dayNum;
    LatencyType latencyType;
    String requestType;
    long startTime;
    long latency;

    public Latency(String hostName, int dayNum, LatencyType latencyType, String requestType, long startTime, long latency) {
        this.hostName = hostName;
        this.dayNum = dayNum;
        this.latencyType = latencyType;
        this.requestType = requestType;
        this.startTime = startTime;
        this.latency = latency;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public LatencyType getLatencyType() {
        return latencyType;
    }

    public void setLatencyType(LatencyType latencyType) {
        this.latencyType = latencyType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    @Override
    public String toString() {
        return this.hostName + "/" + this.dayNum + "/" + this.latencyType + "/" + this.requestType + "/" + this.startTime + "/" + this.latency;
    }
}

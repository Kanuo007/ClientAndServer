package bsdsass2testdata;

public class StatKey {
    private String skierId;
    private int dayNum;


    public StatKey(String skierId, int dayNum) {
        this.skierId = skierId;
        this.dayNum = dayNum;

    }

    public String getSkierId() {
        return skierId;
    }

    public void setSkierId(String skierId) {
        this.skierId = skierId;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatKey statKey = (StatKey) o;

        if (dayNum != statKey.dayNum) return false;
        return skierId.equals(statKey.skierId);
    }

    @Override
    public int hashCode() {
        int result = skierId.hashCode();
        result = 31 * result + dayNum;
        return result;
    }
}

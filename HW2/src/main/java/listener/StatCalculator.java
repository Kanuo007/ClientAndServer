package listener;

import bsdsass2testdata.RFIDLiftData;
import bsdsass2testdata.StatKey;
import bsdsass2testdata.StatValue;

public class StatCalculator extends Thread{

    public StatCalculator(){}

    public void run(){

        while(true) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Listener.queue_Stat.size() != 800000) continue;

            RFIDLiftData rfidLiftData = null;
            while (true) {
                if(Listener.queue_Stat.isEmpty()) break;
                try {
                    rfidLiftData = Listener.queue_Stat.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                 int total_vertical = getVericalByLiftId(rfidLiftData.getLiftID());
                int lifts = 1;
                StatKey statKey = new StatKey(String.valueOf(rfidLiftData.getSkierID()), rfidLiftData.getDayNum());
                StatValue statValue = Listener.cacheExpertToStat.get(statKey);
                if (statValue != null) {
                    total_vertical += statValue.getTotal_vertical();
                    lifts += statValue.getNum_lift_rides();
                    statValue.setTotal_vertical(total_vertical);
                    statValue.setNum_lift_rides(lifts);
                } else {
                    statValue = new StatValue(total_vertical, lifts);
                }
                Listener.cacheExpertToStat.put(statKey, statValue);
            }
        }
    }



    private synchronized int getVericalByLiftId(int liftID) {
        if (liftID > 0 && liftID < 11){
            return 200;
        } else if (liftID >=11 && liftID < 21){
            return 300;
        } else if (liftID >=21 && liftID < 22){
            return 400;
        } else {
            return 500;
        }
    }
}



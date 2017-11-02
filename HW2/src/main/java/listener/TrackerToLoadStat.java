package listener;

import bsdsass2testdata.StatKey;
import bsdsass2testdata.StatValue;
import dao.StatDataDao;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class TrackerToLoadStat extends Thread {
    StatDataDao statDataDao;


    public TrackerToLoadStat(){
        statDataDao = StatDataDao.getInstance();
    }

    public void run() {
        while (true) {
//            System.out.println("Cache size：" + Listener.cacheExpertToStat.size());
            if (Listener.cacheExpertToStat.size() == 40000 && Listener.queue_Stat.size() == 0) {
//                System.out.println("begin to insert all user data into stat table");
                try {
                    statDataDao.multiInsert(Listener.cacheExpertToStat.entrySet());
                    Listener.cache = new ConcurrentHashMap<StatKey, StatValue>(Listener.cacheExpertToStat);
//                    System.out.println("the size of the new cache:" + Listener.cache.size());
                    Listener.cacheExpertToStat.clear();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

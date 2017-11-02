package listener;

import bsdsass2testdata.RFIDLiftData;
import bsdsass2testdata.StatKey;
import bsdsass2testdata.StatValue;
import dao.GeneralDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.exit;

public class Listener implements ServletContextListener {

    public static final BlockingQueue<RFIDLiftData> queue_Stat = new LinkedBlockingQueue<RFIDLiftData>();
    public static final BlockingQueue<RFIDLiftData> queue_Total = new LinkedBlockingQueue<RFIDLiftData>();
    public static final ConcurrentHashMap<StatKey, StatValue> cacheExpertToStat = new ConcurrentHashMap();
    public static ConcurrentHashMap<StatKey, StatValue> cache = new ConcurrentHashMap();

    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("this is last destroyeed");
        Thread.currentThread().interrupt();
    }


    public void contextInitialized(ServletContextEvent event) {
        System.out.println("======listener is beginning=========");

        // create two tables, one for total data and another for statistic data
        GeneralDao generalDao = GeneralDao.getInstance();
        int status = 0;
        try {
            status = generalDao.createTwoTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (status == 200) {
            // check queueSize
            // once the size is more than a bulk, insert into DB one time
            TrackerToLoadRaw trackerToLoadRaw = new TrackerToLoadRaw();
            trackerToLoadRaw.start();

            // check queue_stat_siz
            // once there is data, update the stat map using these 20 threads
            for(int i = 0; i < 10; i++) {
                Thread thread = new StatCalculator();
                thread.start();
            }

            // once the cacheExpertToStat map has 40000 data, insert into DB stat table, clear cacheExpertToStat
            TrackerToLoadStat trackerToLoadStat = new TrackerToLoadStat();
            trackerToLoadStat.start();

        } else {
            exit(1);
        }
    }
}

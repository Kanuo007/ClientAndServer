package listener;

import bsdsass2testdata.Latency;
import bsdsass2testdata.RFIDLiftData;
import dao.GeneralDao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.exit;

public class Listener implements ServletContextListener {
    public static final BlockingQueue<RFIDLiftData> queue_Total = new LinkedBlockingQueue<RFIDLiftData>();
    public static final BlockingQueue<Latency> queue_latency = new LinkedBlockingQueue<Latency>();
    public static final AtomicInteger error = new AtomicInteger(0);

//    public static final BlockingQueue<String> queue_latencies = new LinkedBlockingQueue<String>();

    public static String hostName = "";

    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("this is last destroyeed");
        Thread.currentThread().interrupt();
    }


    public void contextInitialized(ServletContextEvent event) {
        System.out.println("======listener is beginning=========");

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostName = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // create three tables, one for raw data, one for latency data and another for server error counter
        GeneralDao generalDao = GeneralDao.getInstance();
        int status = 0;
        try {
            status = generalDao.createThreeTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (status == 200) {
            // check queueSize
            // once the size is more than a bulk, insert into DB one time
            TrackerToLoadRawDataDB trackerToLoadRaw = new TrackerToLoadRawDataDB();
            trackerToLoadRaw.start();
        } else {
            exit(1);
        }

        for(int i = 0; i < 5; i++){
            TrackerToLoadLatencyDB trackerToLoadLatencyDB = new TrackerToLoadLatencyDB();
            trackerToLoadLatencyDB.start();
        }

        TrackerToLoadErrorTracker trackerToLoadToErrorTracker = new TrackerToLoadErrorTracker();
        trackerToLoadToErrorTracker.start();
    }
}

package listener;

import dao.LiftDataDao;

import java.sql.SQLException;

public class TrackerToLoadRawDataDB extends Thread {
    private LiftDataDao liftDataDao;
    public TrackerToLoadRawDataDB(){
        liftDataDao = LiftDataDao.getInstance();
    }

    public void run(){
        while(true) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int size =  Listener.queue_Total.size() < 10000? Listener.queue_Total.size() : 10000;
            if (size == 0) continue;
            try {
                System.out.println(Listener.queue_Total.size());
                liftDataDao.multiInsert(size);
            } catch (SQLException e) {
                e.printStackTrace();
                Listener.error.incrementAndGet();
            }
        }
    }
}


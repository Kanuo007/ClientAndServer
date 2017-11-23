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

            int size =  Listener.QUEUE_TOTAL.size() < 10000? Listener.QUEUE_TOTAL.size() : 10000;
            if (size == 0) continue;
            try {
                System.out.println(Listener.QUEUE_TOTAL.size());
                liftDataDao.multiInsert(size);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


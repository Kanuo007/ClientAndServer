package listener;

import dao.ErrorDao;

import java.sql.SQLException;

public class TrackerToLoadErrorTracker extends Thread {
    private ErrorDao errorDao;

    public TrackerToLoadErrorTracker(){
        errorDao = ErrorDao.getInstance();
    }

    public void run(){
        // the logic is: wait for all data of latency and raw data having been inserted into DB, then update the error.
        // Thus it is better to use a scheduler to set a certai time after everyday data post.
        while(true) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (Listener.queue_Total.size() == 0 && Listener.queue_latency.size() == 0 && Listener.error.get() > 0) {
                try {
                    errorDao.update(Listener.error.get());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Listener.error.set(0);
            }
        }
    }
}


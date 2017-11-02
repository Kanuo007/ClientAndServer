package listener;

import dao.LiftDataDao;

import java.sql.SQLException;

public class TrackerToLoadRaw extends Thread {
    private LiftDataDao liftDataDao;
    static int INSERT_SIZE = 5000;

    public TrackerToLoadRaw(){
        liftDataDao = LiftDataDao.getInstance();
    }

    public void run(){
        while(true){
            try {
                liftDataDao.multiInsert(INSERT_SIZE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


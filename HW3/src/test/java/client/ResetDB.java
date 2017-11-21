package client;

import client.Utils.ErrorDaoForTest;
import client.Utils.LatencyDaoForTest;
import client.Utils.LiftDataDaoForTest;

import java.sql.SQLException;
import java.util.concurrent.BrokenBarrierException;

public class ResetDB {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, SQLException {
        LiftDataDaoForTest liftDataDaoForTest = LiftDataDaoForTest.getInstance();
        LatencyDaoForTest latencyDaoForTest = LatencyDaoForTest.getInstance();
        ErrorDaoForTest errorDaoForTest = ErrorDaoForTest.getInstance();

        liftDataDaoForTest.reset();
        latencyDaoForTest.reset();
        errorDaoForTest.update(0);
    }
}



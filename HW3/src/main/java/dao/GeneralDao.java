package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GeneralDao {
    private ConnectionPoolManager connectionPoolManager;
    private static GeneralDao instance = null;


    private GeneralDao() {
        connectionPoolManager = new ConnectionPoolManager();
    }

    public static GeneralDao getInstance() {
        if(instance == null) {
            instance = new GeneralDao();
        }
        return instance;
    }


    public int createThreeTables() throws SQLException {
        String createLatencyData = "CREATE TABLE IF NOT EXISTS Latency(" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "hostName VARCHAR(255) NOT NULL, " +
                "dayNum int NOT NULL, " +
                "requestType VARCHAR(255) NOT NULL, " +
                "latencyType VARCHAR(255) NOT NULL, " +
                "startTime LONG NOT NULL, " +
                "latency LONG NOT NULL, " +
                "INDEX(dayNum), " +
                "CONSTRAINT pk_Latency_id PRIMARY KEY (id));";

        String createErrorTable = "CREATE TABLE IF NOT EXISTS Error (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "error INT NOT NULL, " +
                "CONSTRAINT pk_Error_id PRIMARY KEY (id));";

        String insertInit = "INSERT INTO Error(error) VALUES(0);";

        String createLifeDataTable = "CREATE TABLE IF NOT EXISTS LiftData (" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "resortId VARCHAR(255) NOT NULL, " +
                "dayNum INT NOT NULL, " +
                "skierId VARCHAR(255) NOT NULL, " +
                "liftId VARCHAR(255) NOT NULL, " +
                "curTime VARCHAR(255) NOT NULL, " +
                "INDEX(skierId), " +
                "CONSTRAINT pk_LifeData_id PRIMARY KEY (id));";

        Connection connection = null;
        PreparedStatement preStmt = null;
        try {
            connection = connectionPoolManager.getConnection();
            preStmt = connection.prepareStatement(createLifeDataTable);
            preStmt.executeUpdate();
            preStmt = connection.prepareStatement(createLatencyData);
            preStmt.executeUpdate();
            preStmt = connection.prepareStatement(createErrorTable);
            preStmt.executeUpdate();
            preStmt = connection.prepareStatement(insertInit);
            preStmt.executeUpdate();

            return 200;

        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preStmt != null) {
                preStmt.close();
            }
        }

    }
}

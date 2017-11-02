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


    public int createTwoTables() throws SQLException {
        String createStatDataTable = "CREATE TABLE IF NOT EXISTS StatData(" +
                "id BIGINT NOT NULL AUTO_INCREMENT, " +
                "skierId VARCHAR(255) NOT NULL, " +
                "dayNum INT NOT NULL, " +
                "total_vertical INT NOT NULL, " +
                "num_lift_rides INT NOT NULL, " +
                "INDEX(skierId), " +
                "CONSTRAINT pk_StatData_id PRIMARY KEY (id));";

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
        PreparedStatement createStmt = null;
        try {
            connection = connectionPoolManager.getConnection();
            createStmt = connection.prepareStatement(createLifeDataTable);
            createStmt.executeUpdate();
            createStmt = connection.prepareStatement(createStatDataTable);
            createStmt.executeUpdate();
            return 200;

        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (createStmt != null) {
                createStmt.close();
            }
        }

    }
}

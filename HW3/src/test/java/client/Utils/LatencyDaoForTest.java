package client.Utils;

import dao.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LatencyDaoForTest {
    private dao.ConnectionPoolManager connectionPoolManager;
    private static LatencyDaoForTest instance = null;

    private LatencyDaoForTest() {
        connectionPoolManager = new ConnectionPoolManager();
    }

    public static LatencyDaoForTest getInstance() {
        if(instance == null) {
            instance = new LatencyDaoForTest();
        }
        return instance;
    }


    public synchronized List<Matrics> getLatencyByHostAndDayAndRequestTypeAndLatencyType(
            String hostName, int dayNum, String requestType, String LatencyType
    ) throws SQLException {
        Connection connection = null;
        PreparedStatement getStmt = null;
        List<Matrics> list = new ArrayList<Matrics>();
        String getStat = "SELECT startTime, latency, errorAmount " +
                "FROM Latency WHERE hostName=? AND dayNum=? AND requestType=? AND latencyType=?;";

        try {
            connection = this.connectionPoolManager.getConnection();
            getStmt = connection.prepareStatement(getStat);
            getStmt.setString(1, hostName);
            getStmt.setInt(2, dayNum);
            getStmt.setString(3, requestType);
            getStmt.setString(4, LatencyType);

            ResultSet resultSet = getStmt.executeQuery();

            while (resultSet.next()) {
                Long startTime = resultSet.getLong("startTime");
                Long latency = resultSet.getLong("latency");
                int errorAmount = resultSet.getInt("errorAmount");
                Matrics matrics = new Matrics(startTime, latency, errorAmount);
                list.add(matrics);
            }} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(getStmt != null) {
                getStmt.close();
            }
        }
        return list;
    }

    public synchronized List<Matrics> getLatencyByDayAndRequestTypeAndLatencyType(int dayNum, String requestType, String LatencyType) throws SQLException {
        Connection connection = null;
        PreparedStatement getStmt = null;
        List<Matrics> list = new ArrayList<Matrics>();
        String getStat = "SELECT startTime, latency, errorAmount FROM Latency WHERE dayNum=? AND requestType=? AND latencyType=?;";

        try {
            connection = this.connectionPoolManager.getConnection();
            getStmt = connection.prepareStatement(getStat);
            getStmt.setInt(1, dayNum);
            getStmt.setString(2, requestType);
            getStmt.setString(3, LatencyType);
            ResultSet resultSet = getStmt.executeQuery();
            while (resultSet.next()) {
                Long startTime = resultSet.getLong("startTime");
                Long latency = resultSet.getLong("latency");
                int errorAmount = resultSet.getInt("errorAmount");
                Matrics matrics = new Matrics(startTime, latency, errorAmount);
                list.add(matrics);
            }} catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(getStmt != null) {
                getStmt.close();
            }
        }
        return list;
    }

    // clean
    public void reset() throws SQLException {
        Connection connection = null;
        PreparedStatement preStmt = null;
        String cleanStat = "TRUNCATE Latency";

        try {
            connection = this.connectionPoolManager.getConnection();
            preStmt = connection.prepareStatement(cleanStat);
            preStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(preStmt != null) {
                preStmt.close();
            }
        }
    }
}

package dao;

import bsdsass2testdata.Performance;
import listener.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LatencyDao {
    private ConnectionPoolManager connectionPoolManager;
    private static LatencyDao instance = null;

    private LatencyDao() {
        connectionPoolManager = new ConnectionPoolManager();
    }

    public static LatencyDao getInstance() {
        if(instance == null) {
            instance = new LatencyDao();
        }
        return instance;
    }


    // multiInsert
    public synchronized void multiInsert(int size) throws SQLException{
        Connection connection = null;
        PreparedStatement insertStmt = null;
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO Latency(hostName, dayNum, requestType, " +
                "latencyType, startTime, latency, errorAmount) VALUES ");
        for (int i = 0; i < size; i++) {
            Performance performance = null;
            try{
                performance = Listener.QUEUE_PERFORMANCE.take();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            stringBuilder.append("(").append("\"" + performance.getHostName() + "\"").append(", ")
                    .append(performance.getDayNum()).append(", ")
                    .append("\"" + performance.getRequestType() + "\"").append(", ")
                    .append("\"" + performance.getLatencyType() + "\"").append(", ")
                    .append(performance.getStartTime()).append(", ")
                    .append(performance.getLatency()).append(", ")
                    .append(performance.getErrorAmount()).append("),");

        }
        String str = stringBuilder.substring(0, stringBuilder.length() - 1) + ";";
        System.out.println(str);
        try {
            connection = connectionPoolManager.getConnection();
            insertStmt = connection.prepareStatement(str);
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
        }
    }

}

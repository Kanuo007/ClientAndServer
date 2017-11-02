package dao;

import bsdsass2testdata.StatKey;
import bsdsass2testdata.StatValue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

public class StatDataDao {


    private ConnectionPoolManager connectionPoolManager;
    private static StatDataDao instance = null;
    private LiftDataDao liftDataDao;
    private static final String INSERT_STAT =
            "INSERT IGNORE INTO StatData(skierId, dayNum, total_vertical, num_lift_rides) VALUES(?,?,?,?);";
    private static final String UPDATE_STAT =
            "UPDATE StatData set total_vertical = ?,num_lift_rides = ? WHERE skierId=? AND dayNum=?;";
    private static final String GET_STAT =
            "SELECT total_vertical, num_lift_rides FROM StatData WHERE skierId=? AND dayNum=?;";


    protected StatDataDao() {

        connectionPoolManager = new ConnectionPoolManager();
        liftDataDao = LiftDataDao.getInstance();
    }

    public static StatDataDao getInstance() {
        if(instance == null) {
            instance = new StatDataDao();
        }
        return instance;
    }

    // Insert
    public synchronized void multiInsert(Set<Map.Entry<StatKey, StatValue>> set ) throws SQLException{
        Connection connection = null;
        PreparedStatement insertStmt = null;
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO StatData(skierId, dayNum, total_vertical, num_lift_rides) VALUES ");
        for (Map.Entry each : set) {
            StatKey statKey = (StatKey) each.getKey();
            StatValue statValue = (StatValue) each.getValue();
            stringBuilder.append("(")
                    .append(statKey.getSkierId()).append(", ")
                    .append(statKey.getDayNum()).append(", ")
                    .append(statValue.getTotal_vertical()).append(", ")
                    .append(statValue.getNum_lift_rides()).append("),");
        }
        String state = stringBuilder.substring(0, stringBuilder.length() - 1) + ";";
        try {
            connection = connectionPoolManager.getConnection();
            insertStmt = connection.prepareStatement(state);
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


    // getSkierDataByDay
    public synchronized StatValue getSkierDataByDay(String skierId, int dayNum) throws SQLException {
        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet results = null;
        try {
            connection = this.connectionPoolManager.getConnection();
            selectStmt = connection.prepareStatement(GET_STAT);
            selectStmt.setString(1, skierId);
            selectStmt.setInt(2, dayNum);
            results = selectStmt.executeQuery();
            if (results.next()) {
                int total_vertical = results.getInt("total_vertical");
                int num_lift_rides = results.getInt("num_lift_rides");
                StatValue statValue = new StatValue(total_vertical, num_lift_rides);
                return statValue;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (selectStmt != null) {
                selectStmt.close();
            }
            if (results != null) {
                results.close();
            }
        }
        return null;
    }

}


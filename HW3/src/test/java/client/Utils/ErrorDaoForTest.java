package client.Utils;

import dao.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorDaoForTest {
    private dao.ConnectionPoolManager connectionPoolManager;
    private static ErrorDaoForTest instance = null;

    private ErrorDaoForTest() {
        connectionPoolManager = new ConnectionPoolManager();
    }

    public static ErrorDaoForTest getInstance() {
        if(instance == null) {
            instance = new ErrorDaoForTest();
        }
        return instance;
    }

    public synchronized void update(int error) throws SQLException {
        Connection connection = null;
        PreparedStatement preStmt = null;
        String updateStat = null;
        int DbError = 0;
        String getStat = "SELECT * From Error";

        try {
            connection = this.connectionPoolManager.getConnection();
            preStmt = connection.prepareStatement(getStat);
            ResultSet resultSet = preStmt.executeQuery();
            if (resultSet.next()) {
                DbError = resultSet.getInt("error");
            }

            DbError += error;
            updateStat ="INSERT INTO Error(error) VALUES(" + DbError + ");";
            preStmt = connection.prepareStatement(updateStat);
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

    //
    public int get() throws SQLException {
        Connection connection = null;
        PreparedStatement preStmt = null;
        int error = 0;
        String getStat = "SELECT * From Error";

        try {
            connection = this.connectionPoolManager.getConnection();
            preStmt = connection.prepareStatement(getStat);
            ResultSet resultSet = preStmt.executeQuery();
            if (resultSet.next()) {
                error = resultSet.getInt("error");
            }
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
        return error;
    }



        // clean
    public void reset() throws SQLException {
        Connection connection = null;
        PreparedStatement preStmt = null;
        String cleanStat = "TRUNCATE Error";

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

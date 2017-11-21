package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorDao {
    private ConnectionPoolManager connectionPoolManager;
    private static ErrorDao instance = null;

    private ErrorDao() {
        connectionPoolManager = new ConnectionPoolManager();
    }

    public static ErrorDao getInstance() {
        if(instance == null) {
            instance = new ErrorDao();
        }
        return instance;
    }


    // update
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

}

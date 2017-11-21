package client.Utils;

import dao.ConnectionPoolManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class LiftDataDaoForTest {

  private dao.ConnectionPoolManager connectionPoolManager;
  private static LiftDataDaoForTest instance = null;

  private LiftDataDaoForTest() {
      connectionPoolManager = new ConnectionPoolManager();
  }

  public static LiftDataDaoForTest getInstance() {
      if(instance == null) {
          instance = new LiftDataDaoForTest();
      }
      return instance;
  }


    // clean
    public synchronized void reset() throws SQLException {
        Connection connection = null;
        PreparedStatement preStmt = null;
        String cleanStat = "TRUNCATE LiftData";

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

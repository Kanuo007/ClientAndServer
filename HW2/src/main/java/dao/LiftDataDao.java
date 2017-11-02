package dao;

import bsdsass2testdata.RFIDLiftData;
import listener.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class LiftDataDao {

  private ConnectionPoolManager connectionPoolManager;
  private static LiftDataDao instance = null;

  private LiftDataDao() {
      connectionPoolManager = new ConnectionPoolManager();
  }

  public static LiftDataDao getInstance() {
      if(instance == null) {
          instance = new LiftDataDao();
      }
      return instance;
  }


  // multiInsert
  public synchronized void multiInsert(int size) throws SQLException{
      Connection connection = null;
      PreparedStatement insertStmt = null;
      StringBuilder stringBuilder = new StringBuilder("INSERT INTO LiftData(resortId, dayNum, skierId, liftId, curTime) VALUES ");
      for (int i = 0; i < size; i++) {
          RFIDLiftData rfidLiftData = null;
          try{
              rfidLiftData = Listener.queue_Total.take();
          } catch(InterruptedException e) {
              e.printStackTrace();
          }
          stringBuilder.append("(").append(rfidLiftData.getResortID()).append(", ")
                  .append(rfidLiftData.getDayNum()).append(", ")
                  .append(rfidLiftData.getSkierID()).append(", ")
                  .append(rfidLiftData.getLiftID()).append(", ")
                  .append(rfidLiftData.getTime()).append("),");

      }
      String str = stringBuilder.substring(0, stringBuilder.length() - 1) + ";";

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

  // delete
  public synchronized RFIDLiftData deleteDataByDay(int dayNum) throws SQLException{
      String deleteTable = "DELETE FROM LiftData WHERE dayNum=?";
      Connection connection = null;
      PreparedStatement deleteStmt = null;
      try {
          connection = connectionPoolManager.getConnection();
          deleteStmt = connection.prepareStatement(deleteTable);
          deleteStmt.setInt(1, dayNum);
          deleteStmt.executeUpdate();
          return null;
      } catch (SQLException e) {
          e.printStackTrace();
          throw e;
      } finally {
          if(connection != null) {
              connection.close();
          }
          if(deleteStmt != null) {
              deleteStmt.close();
          }
      }
  }

}

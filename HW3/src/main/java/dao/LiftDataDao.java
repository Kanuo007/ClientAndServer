package dao;

import bsdsass2testdata.Performance;
import bsdsass2testdata.LatencyType;
import bsdsass2testdata.RFIDLiftData;
import listener.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
      int dayNum = 0;

      StringBuilder stringBuilder = new StringBuilder("INSERT INTO LiftData(resortId, dayNum, skierId, liftId, curTime) VALUES ");
      for (int i = 0; i < size; i++) {
          RFIDLiftData rfidLiftData = null;
          try{
              rfidLiftData = Listener.QUEUE_TOTAL.take();
              // for people will not insert two day data within oneday, queue will not take two days data within oneday
              dayNum = rfidLiftData.getDayNum();
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

      Long QuerySendTime = System.currentTimeMillis();

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

      Long QueryResponseTime = System.currentTimeMillis();
      long query_latency = QueryResponseTime - QuerySendTime;
      Performance performance = new Performance(
              Listener.hostName,
              dayNum,
              LatencyType.QueryDBTime,
              "POST",
              QuerySendTime,
              query_latency,
              0);
      try {
          Listener.QUEUE_PERFORMANCE.put(performance);
      } catch (InterruptedException e) {
          e.printStackTrace();
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

  // get
  public synchronized List<Integer> getLiftDataBySkierIdAndDayNum(String skierId, int dayNum) throws SQLException{
      List<Integer> list = new ArrayList<Integer>();
      PreparedStatement getStmt = null;
      Connection connection = null;
      String getStat = "SELECT liftId FROM LiftData WHERE skierId=? AND dayNum=?;";

      Long QuerySendTime = System.currentTimeMillis();

      try {
          connection = this.connectionPoolManager.getConnection();
          getStmt = connection.prepareStatement(getStat);
          getStmt.setString(1, skierId);
          getStmt.setInt(2, dayNum);
          ResultSet resultSet = getStmt.executeQuery();
          while (resultSet.next()) {
              int liftId = resultSet.getInt("liftID");
              list.add(liftId);
          }
      } catch (SQLException e) {
          e.printStackTrace();
      } finally {
          if(connection != null) {
              connection.close();
          }
          if(getStmt != null) {
              getStmt.close();
          }
      }

      Long QueryResponseTime = System.currentTimeMillis();
      long query_latency = QueryResponseTime - QuerySendTime;
      Performance performance = new Performance(
              Listener.hostName,
              dayNum,
              LatencyType.QueryDBTime,
              "GET",
              QuerySendTime,
              query_latency,
              0);
      try {
          Listener.QUEUE_PERFORMANCE.put(performance);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      return list;
  }

}

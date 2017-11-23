package client;

import client.Utils.General;
import client.Utils.LatencyDaoForTest;
import client.Utils.Matrics;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import static client.Utils.General.*;


public class GetServerMatrics {

    // get the collective and individual performance for each server
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, SQLException {
        LatencyDaoForTest latencyDaoForTest = LatencyDaoForTest.getInstance();

        // for individual server
        List<Matrics> list1 = latencyDaoForTest.getLatencyByHostAndDayAndRequestTypeAndLatencyType(
                DB_Server1_IP , dayNum, requestType, latencyType
        );
        processStat(list1,DB_Server1_IP);

        // for individual server
        List<Matrics> list2 = latencyDaoForTest.getLatencyByHostAndDayAndRequestTypeAndLatencyType(
                DB_Server2_IP, dayNum, requestType, latencyType
        );
        processStat(list2, DB_Server2_IP);

        // for individual server
        List<Matrics> list3 = latencyDaoForTest.getLatencyByHostAndDayAndRequestTypeAndLatencyType(
                DB_Server3_IP, dayNum, requestType, latencyType
        );
        processStat(list3, DB_Server3_IP);

        // for loadbalancer
        List<Matrics> LBlist = latencyDaoForTest.getLatencyByDayAndRequestTypeAndLatencyType(
                dayNum, requestType, latencyType
        );
        processStat(LBlist, "NA");
    }

    private static void processStat(List<Matrics> list, String hostName){

        List<Long> startTime = new ArrayList<Long>();
        List<Long> latencies = new ArrayList<Long>();
        int error = 0;
        for (Matrics matrics: list){
            startTime.add(matrics.getStartTime());
            latencies.add(matrics.getLatency());
            error += matrics.getErrorAmount();
        }

        // load to csv
        String fileName = hostName + "_" + dayNum + "_" + requestType + "_" + latencyType + "_ServerSide.csv";
        General.writeCSV(latencies, startTime, fileName);

        // calculate statistics
        System.out.println("ServerSide Matrics");
        String serverType = "Individual Server";
        if (hostName == "NA") {
            serverType = "Collective Server";
        }
        System.out.println("ServerType: " + serverType);
        System.out.println("HostName: " + hostName);
        System.out.println("LatencyType: " + latencyType);
        System.out.println("RequestType: " + requestType);
        System.out.println("Amount of Error causing requests fail: " + error);
        System.out.println("Mean latencies of all requests: " + General.meanLatencies(latencies) + "ms");
        System.out.println("Median latencies of all requests: " + General.medianLatencies(latencies) + "ms");
        System.out.println("95th percentile latency: " + General.percentileLatencise(latencies,95) + "ms");
        System.out.println("99th percentile latency: " + General.percentileLatencise(latencies,99) + "ms\n\n");
    }
}

package client;

import client.Utils.ErrorDaoForTest;
import client.Utils.General;
import client.Utils.LatencyDaoForTest;
import client.Utils.Matrics;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import static client.Utils.General.*;


// EveryTime send a request, we run this four times. With changing different parameter in General file, we process
// one for queryDBTime GET latency, one for queryDBTime POST latency,
// one for request_responseTime GET latency, one for request_responseTime POST latency
public class GetServerMatrics {

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException, SQLException {
        LatencyDaoForTest latencyDaoForTest = LatencyDaoForTest.getInstance();

        // for individual server
//        List<Matrics> list = latencyDaoForTest.getLatencyByHostAndDayAndRequestTypeAndLatencyType(
//                DB_store_server_ip, dayNum, requestType, latencyType
//        );

        // for loadbalancer
        List<Matrics> LBlist = latencyDaoForTest.getLatencyByDayAndRequestTypeAndLatencyType(
                dayNum, requestType, latencyType
        );
        processStat(LBlist);
    }

    private static void processStat(List<Matrics> list){

        List<Long> startTime = new ArrayList<Long>();
        List<Long> latencies = new ArrayList<Long>();
        for (Matrics matrics: list){
            startTime.add(matrics.getStartTime());
            latencies.add(matrics.getLatency());
        }

        // load to csv
        String fileName = hostName.substring(0, 17) + dayNum + "_" + requestType + "_" + latencyType + "_ServerSide.csv";
        General.writeCSV(latencies, startTime, fileName);

        // calculate statistics
        System.out.println("ServerSide Matrics");
        String serverType = "Individual Server";
        if (hostName != serve_1_hostName) {
            serverType = "Three Servers";
        }

        ErrorDaoForTest errorDaoForTest = ErrorDaoForTest.getInstance();
        int error = 0;
        try {
            error = errorDaoForTest.get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("ServerType:" + serverType);
        System.out.println("LatencyType:" + latencyType);
        System.out.println("RequestType:" + requestType);
        System.out.println("Amount of Error causing request fail: " + error);
        System.out.println("Mean latencies of all requests: " + General.meanLatencies(latencies) + "ms");
        System.out.println("Median latencies of all requests: " + General.medianLatencies(latencies) + "ms");
        System.out.println("95th percentile latency: " + General.percentileLatencise(latencies,95) + "ms");
        System.out.println("99th percentile latency: " + General.percentileLatencise(latencies,99) + "ms");
    }
}

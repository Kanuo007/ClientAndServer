package client;

import client.Utils.General;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static client.Utils.General.*;
import static java.lang.System.exit;

public class GetClientMultithreaded {

    static CyclicBarrier barrier;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        barrier = new CyclicBarrier(General.GETTHREADS + 1);
        int totalSucessfulRequest = 0;


        // create threads to handle the request
        long start = System.currentTimeMillis();
        int eachThreadRequests = General.userAmount / General.GETTHREADS;
        int lastThreadRequests = General.userAmount % General.GETTHREADS;
        int threads = lastThreadRequests == 0? General.GETTHREADS : General.GETTHREADS + 1;
        GetClientThread clients[] = new GetClientThread[threads];

        int begin = 0;
        int EachEnd = 0;
        for (int i = 0; i < threads; i++) {
            begin = i * eachThreadRequests;
            if (i == threads - 1) {
                EachEnd = General.userAmount - 1;
            } else {
                EachEnd = begin + eachThreadRequests - 1;
            }
            System.out.println("TRHEADS " + i);
            System.out.println(begin);
            System.out.println(EachEnd);
            clients[i] = new GetClientThread(General.hostName, General.port, General.dayNum, begin, EachEnd, barrier);
            clients[i].start();
        }
        barrier.await();
        long end = System.currentTimeMillis();
        long totalLatency = end - start;


        //calculate and output the stats
        List<Long> latencies = new ArrayList();
        List<Long> startTimestamp = new ArrayList<Long>();
        for(GetClientThread client : clients) {
            totalSucessfulRequest += client.getSuccessRequest();
            for(int x = 0; x < General.userAmount/threads - 1; x++) {
                Long requestLatency = client.getThreadlatencyList().get(x);
                Long requestBeginTimeStamp = client.getThreadrequestBeginList().get(x);
                latencies.add(requestLatency);
                startTimestamp.add(requestBeginTimeStamp);
            }
        }

        System.out.println("ClientSide GET Matrics");
        System.out.println("Thread Amount:" +  threads);
        String serverType = "Individual Server";
        if (hostName != serve_1_hostName) {
            serverType = "Three Servers";
        }
        System.out.println("ServerType:" + serverType);
        System.out.println("RequestType:" + requestType);
        System.out.println("Total run time (wall time) for all threads to complete: " + totalLatency + "ms");
        System.out.println("Total Number of requests sent: " + General.userAmount);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalLatency);
        System.out.println("Average Number of requests per second: " + General.userAmount/seconds);
        System.out.println("Total Number of successful requests: " + totalSucessfulRequest);
        System.out.println("Mean latencies of all requests: " + General.meanLatencies(latencies) + "ms");
        System.out.println("Median latencies of all requests: " + General.medianLatencies(latencies) + "ms");
        System.out.println("95th percentile latency: " + General.percentileLatencise(latencies,95) + "ms");
        System.out.println("99th percentile latency: " + General.percentileLatencise(latencies,99) + "ms");

        String fileName = hostName.substring(0, 17) + dayNum + "_" + requestType + "_" + latencyType + "_ClientSide.csv";
        General.writeCSV(latencies, startTimestamp, fileName);
        exit(1);
    }


}


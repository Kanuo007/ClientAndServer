package client;

import bsdsass2testdata.RFIDLiftData;
import client.Utils.General;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static client.Utils.General.*;
import static java.lang.System.exit;

public class PostClientMultithreaded {

    static CyclicBarrier barrier;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        int threads = General.POSTTHREADS;
        barrier = new CyclicBarrier(threads + 1);
        int totalSucessfulRequest = 0;

        if (args.length > 1) {
            threads = Integer.valueOf(args[1]);
        }

        // read data from file
        ArrayList <RFIDLiftData> RFIDDataIn = General.readData(General.dayFile);
        int dataSize = RFIDDataIn.size();
        // create threads to handle the request
        long start = System.currentTimeMillis();
        int eachThreadRequests = RFIDDataIn.size() / threads;
        int lastThreadRequests = RFIDDataIn.size() % threads;
        threads = lastThreadRequests == 0? threads : threads + 1;
        PostClientThread clients[] = new PostClientThread[threads];

        int begin = 0;
        int EachEnd = 0;
        for (int i = 0; i < threads; i++) {
            begin = i * eachThreadRequests;
            if (i == threads - 1) {
                EachEnd = RFIDDataIn.size() - 1;
            } else {
                EachEnd = begin + eachThreadRequests - 1;
            }
            clients[i] = new PostClientThread(General.hostName, General.port, RFIDDataIn, begin, EachEnd, barrier);
            clients[i].start();
        }
        barrier.await();
        long end = System.currentTimeMillis();
        long totalLatency = end - start;


        //calculate and output the stats
        List<Long> latencies = new ArrayList();
        List<Long> startTimestamp = new ArrayList<Long>();
        for(PostClientThread client : clients) {
            totalSucessfulRequest += client.getSuccessRequest();

            for(int x = 0; x < dataSize/threads; x++) {
                Long requestLatency = client.getThreadlatencyList().get(x);
                Long requestBeginTimeStamp = client.getThreadrequestBeginList().get(x);
                latencies.add(requestLatency);
                startTimestamp.add(requestBeginTimeStamp);
            }
        }

        System.out.println("ClientSide POST Matrics");
        System.out.println("Thread Amount:" +  threads);
        String serverType = "Individual Server";
        if (hostName == loadBalancer_hostName) {
            serverType = "Collective Server";
        }
        System.out.println("ServerType:" + serverType);
        System.out.println("Total run time (wall time) for all threads to complete: " + totalLatency + "ms");
        System.out.println("Total Number of requests sent: " + RFIDDataIn.size());
        long seconds = TimeUnit.MILLISECONDS.toSeconds(totalLatency);
        System.out.println("Average Number of requests per second: " + RFIDDataIn.size()/seconds);
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


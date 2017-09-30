package com.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import static java.lang.System.exit;

public class ClientMultithreaded {

    static CyclicBarrier barrier;

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        String hostName = "34.213.161.101";
        String port = "8080";
        int iterations = 100 ;
        int threads = 10;
        barrier = new CyclicBarrier(threads + 1);
        int totalSucessfulRequest = 0;

        if (args.length > 0) {
            hostName = args[0];
            port = args[1];
            iterations = Integer.valueOf(args[2]);
            threads = Integer.valueOf(args[3]);
        }

        ClientThread clients[] = new ClientThread[threads];
        long start = System.currentTimeMillis();
        for(int i = 0; i < threads; i++){
            clients[i] = new ClientThread(hostName, port, iterations, barrier);
            clients[i].start();
        }
        barrier.await();
        long end = System.currentTimeMillis();
        long totalLatency = end - start;

        //calculate and output the stats
        List<Long> latencies = new ArrayList();
        for(ClientThread client : clients) {
            totalSucessfulRequest += client.getSuccessRequest();
            for(Long each : client.getThreadlatencyList()) {
                latencies.add(each);
            }
        }

        System.out.println("Thread Amount:" +  threads);
        System.out.println("Iteratoin Amount: " +  iterations);
        System.out.println("Total run time (wall time) for all threads to complete: " + totalLatency + "ms");
        System.out.println("Total Number of requests sent: " + 2 * threads * iterations);
        System.out.println("Total Number of successful requests: " + totalSucessfulRequest);
        System.out.println("Mean latencies of all requests: " + meanLatencies(latencies) + "ms");
        System.out.println("Median latencies of all requests: " + medianLatencies(latencies) + "ms");
        System.out.println("95th percentile latency: " + percentileLatencise(latencies,95) + "ms");
        System.out.println("99th percentile latency: " + percentileLatencise(latencies,99) + "ms");
        exit(1);
    }

    private static Long meanLatencies(List<Long> latencies)
    {   if (latencies.size() == 0) return 0L;
        long sum = 0;
        for (Long mark : latencies) {
            sum += mark;
        }
        return latencies.isEmpty()? 0: sum/latencies.size();
    }

    private static Long medianLatencies(List<Long> latencies)
    {
        if (latencies.size() == 0) return 0L;
        Collections.sort(latencies);
        Long median = (latencies.get(latencies.size()/2 - 1) + latencies.get(latencies.size() / 2)) / 2;
        return median;
    }

    private static Long percentileLatencise(List<Long> latencies, double percentile)
    {
        if (latencies.size() == 0) return 0L;
        Collections.sort(latencies);
        int index = (int)Math.ceil(((percentile / (double)100) * (double)latencies.size()));
        return latencies.get(index - 1);
    }
}


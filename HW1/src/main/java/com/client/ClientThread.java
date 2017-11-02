package com.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ClientThread extends Thread {

    private static String getEndPoint = "/ClientAndServer_war/rest/myresourceGet";
    private static String postEndPoint = "/ClientAndServer_war/rest/myresourcePost";

    private int iteration;
    private String hostName;
    private String port;
    private CyclicBarrier synk;
    private Client client;
    private WebTarget webTarget;
    private int successRequest;
    private List<Long> threadlatencyList;

    ClientThread(String hostName, String port, int iteration, CyclicBarrier barrier) {
        this.hostName = hostName;
        this.port = port;
        this.synk = barrier;
        this.threadlatencyList = new ArrayList<Long>();
        this.client = ClientBuilder.newClient();
        this.successRequest = 0;
        this.iteration = iteration;
    }

    int getSuccessRequest() {
        return successRequest;
    }
    List<Long> getThreadlatencyList() {
        return threadlatencyList;
    }

    public void run() {
        // do the 100 iteration
        for (int i = 0; i < iteration; i++) {

            // track the get request and calculate the latency
            Long start = System.currentTimeMillis();
//            try {
//                String url = "http://"+ hostName + ":" + port + getEndPoint;
//                this.webTarget = client.target(url);
//                if (getStatus().equals("alive")) {
//                    this.successRequest++;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Error happens when call get request");
//            }
            Long end = System.currentTimeMillis();
            Long latency = end - start;
//            this.threadlatencyList.add(latency);
//            System.out.println(latency);

            // track the post request and calculate the latency
            start = System.currentTimeMillis();
            try {
                String url = "http://"+ hostName + ":" + port + postEndPoint;
                this.webTarget = client.target(url);
                if (this.postText("I'm the post information") == 200) {
                    this.successRequest++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error happens when call post request");
            }
            end = System.currentTimeMillis();
            latency = end - start;
            System.out.println(latency);
            threadlatencyList.add(latency);
        }

        // wait on the CyclicBarrier
        try {
            synk.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private int postText(String str) throws ClientErrorException {
        try{
            Response response = webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN)
                    .post(javax.ws.rs.client.Entity.entity(str, javax.ws.rs.core.MediaType.TEXT_PLAIN));
            int res = response.getStatus();
            response.close();
            return res;
        } catch (Exception e) {
            return -1;
        }
    }

    private String getStatus() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);

    }
}

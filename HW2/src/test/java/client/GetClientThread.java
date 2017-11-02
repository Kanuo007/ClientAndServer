package client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GetClientThread extends Thread {

    private String hostName;
    private String port;
    private int begin;
    private int end;
    private int dayNum;
    private Client client;
    private WebTarget webTarget;
    private CyclicBarrier sync;
    private int successRequest;
    private List<Long> threadlatencyList;
    private List<Long>threadrequestBeginList;


    public GetClientThread(String hostName, String port, int dayNum, int begin, int end, CyclicBarrier sync) {
        this.hostName = hostName;
        this.port = port;
        this.begin = begin;
        this.end = end;
        this.dayNum = dayNum;
        this.successRequest = 0;
        this.threadlatencyList = new ArrayList<Long>();
        this.threadrequestBeginList = new ArrayList<Long>();
        this.client = ClientBuilder.newClient();
        this.sync = sync;
    }

    int getSuccessRequest() { return this.successRequest; }
    List<Long> getThreadlatencyList() {
        return this.threadlatencyList;
    }
    List<Long> getThreadrequestBeginList(){ return this.threadrequestBeginList; }


    public void run() {
        for (int i = begin; i <= end; i++) {
            String url = "http://" + hostName + ":" + port + General.getEndPoint + "/" + i + "/" + this.dayNum;
            Long startTime = System.currentTimeMillis();
            try {
                System.out.println(url);
                this.webTarget = client.target(url);
                webTarget.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error happens when call get request");
            }
            this.successRequest++;
            Long endTime = System.currentTimeMillis();
            Long latency = endTime - startTime;
            this.threadlatencyList.add(latency);
            this.threadrequestBeginList.add(startTime);
        }

        // wait on the CyclicBarrier
        try {
            sync.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}

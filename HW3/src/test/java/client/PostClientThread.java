package client;

import bsdsass2testdata.RFIDLiftData;
import client.Utils.General;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;


public class PostClientThread extends Thread {

    private String hostName;
    private String port;
    private CyclicBarrier synk;
    private Client client;
    private WebTarget webTarget;
    private int successRequest;
    private List<Long> threadlatencyList;
    private List<Long>threadrequestBeginList;
    private int begin = 0;
    private int end = 0;
    List<RFIDLiftData>RFIDDataIn = new ArrayList<RFIDLiftData>();

    PostClientThread(String hostName, String port, List<RFIDLiftData> RFIDDataIn, int begin, int end, CyclicBarrier barrier) {
        this.hostName = hostName;
        this.port = port;
        this.synk = barrier;
        this.threadlatencyList = new ArrayList<Long>();
        this.threadrequestBeginList = new ArrayList<Long>();
        this.client = ClientBuilder.newClient();
        this.successRequest = 0;
        this.begin = begin;
        this.end = end;
        this.RFIDDataIn = RFIDDataIn;
    }

    int getSuccessRequest() {
        return this.successRequest;
    }
    List<Long> getThreadlatencyList() { return this.threadlatencyList; }
    List<Long> getThreadrequestBeginList() { return this.threadrequestBeginList; }

    public void run() {
        String url = "http://"+ hostName + ":" + port + General.postEndPoint;

        try {
            this.webTarget = client.target(url);
            for(int i = begin; i <= end; i++) {
                RFIDLiftData each = RFIDDataIn.get(i);

                Long start = System.currentTimeMillis();
                int status = this.postText(each);
                if (status!=200){
                    System.out.println(status);
                }
                if (status == 200) {
                    this.successRequest++;
                }
                Long end = System.currentTimeMillis();
                Long latency = end - start;
                System.out.println("each request latency: " + latency);
                this.threadlatencyList.add(latency);
                this.threadrequestBeginList.add(start);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error happens when call post request");
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

    private int postText(RFIDLiftData each) throws ClientErrorException {
        Form form = new Form();
        form.param("resortID", String.valueOf(each.getResortID()));
        form.param("dayNum", String.valueOf(each.getDayNum()));
        form.param("skierID", String.valueOf(each.getSkierID()));
        form.param("liftID", String.valueOf(each.getLiftID()));
        form.param("time", String.valueOf(each.getTime()));
        try{
            Response response = webTarget.request(MediaType.TEXT_PLAIN)
                    .post(javax.ws.rs.client.Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
            int res = response.getStatus();
            response.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}

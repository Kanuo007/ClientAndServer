package com.server;

import bsdsass2testdata.Performance;
import bsdsass2testdata.LatencyType;
import bsdsass2testdata.RFIDLiftData;
import listener.Listener;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;


@Path("load")
public class MyResourcePost {

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public synchronized int postText(@FormParam("resortID")int resortID,
                                     @FormParam("dayNum")int dayNum,
                                     @FormParam("skierID")int skierID,
                                     @FormParam("liftID")int liftID,
                                     @FormParam("time")int time)
    {
        Long reveiceRequestTime = System.currentTimeMillis();
        int error = 0;
        RFIDLiftData each = new RFIDLiftData(resortID, dayNum, skierID, liftID, time);
        Listener.QUEUE_TOTAL.add(each);

        Long sendResponseTime = System.currentTimeMillis();
        Long request_latency = sendResponseTime - reveiceRequestTime;
        Performance performance = new Performance(
                Listener.hostName, dayNum,
                LatencyType.ResponseTime,
                "POST",
                reveiceRequestTime,
                request_latency,
                error
        );
        try {
            Listener.QUEUE_PERFORMANCE.put(performance);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        return 200;
    }



}
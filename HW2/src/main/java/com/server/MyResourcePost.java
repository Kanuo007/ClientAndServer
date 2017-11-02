package com.server;

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
    public synchronized int postText(@FormParam("resortID")int resortID, @FormParam("dayNum")int dayNum, @FormParam("skierID")int skierID,
                                     @FormParam("liftID")int liftID, @FormParam("time")int time) {

        RFIDLiftData each = new RFIDLiftData(resortID, dayNum, skierID, liftID, time);
        Listener.queue_Total.add(each);
        Listener.queue_Stat.add(each);
        return 200;
    }



}
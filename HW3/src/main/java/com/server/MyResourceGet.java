package com.server;

import bsdsass2testdata.Latency;
import bsdsass2testdata.LatencyType;
import dao.LiftDataDao;
import listener.Listener;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Path("myvert/{skierID}/{dayNum}")
public class MyResourceGet {
    LiftDataDao liftDataDao = LiftDataDao.getInstance();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public synchronized String getMyvert(@PathParam("skierID") String skierID, @PathParam("dayNum")int dayNum) {
        Long reveiceRequestTime = System.currentTimeMillis();
        List<Integer> list = new ArrayList<Integer>();
        try {
            list = liftDataDao.getLiftDataBySkierIdAndDayNum(skierID, dayNum);
        } catch (SQLException e) {
            Listener.error.incrementAndGet();
            e.printStackTrace();
        }
        int total_vertical = 0;
        int num_lift_rides = 0;
        if (list == null) {
            return "list is null, check the dao";
        }
        for (int liftId : list){
            num_lift_rides++;
            total_vertical += getVericalByLiftId(liftId);
        }

        String res = "For user " + skierID + " in day " + dayNum + "\n"
                + "the vertical is " + total_vertical + "\n"
                + "the number of lift rides is " + num_lift_rides;

        Long sendResponseTime = System.currentTimeMillis();
        Long request_latency = sendResponseTime - reveiceRequestTime;
        Latency latency = new Latency(
                Listener.hostName, dayNum,
                LatencyType.ResponseTime,
                "GET",
                reveiceRequestTime,
                request_latency);
        try {
            Listener.queue_latency.put(latency);
        } catch(InterruptedException e) {
            Listener.error.incrementAndGet();
            e.printStackTrace();
        }
//        Listener.queue_latencies.put(latency.toString());
        return res;
    }

    private synchronized int getVericalByLiftId(int liftID) {
        if (liftID > 0 && liftID < 11){
            return 200;
        } else if (liftID >=11 && liftID < 21){
            return 300;
        } else if (liftID >=21 && liftID < 22){
            return 400;
        } else {
            return 500;
        }
    }

}
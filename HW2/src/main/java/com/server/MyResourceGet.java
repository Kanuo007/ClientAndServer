package com.server;

import bsdsass2testdata.StatKey;
import bsdsass2testdata.StatValue;
import dao.StatDataDao;
import listener.Listener;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


@Path("myvert/{skierID}/{dayNum}")
public class MyResourceGet {
    StatDataDao statDataDao = StatDataDao.getInstance();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public synchronized String getMyvert(
            @PathParam("skierID") String skierID,
            @PathParam("dayNum")int dayNum
    ) {

        int total_vertical = 0;
        int lifts = 0;
        StatValue statValue = Listener.cache.get(new StatKey(skierID, dayNum));
        if (statValue != null) {
            total_vertical = statValue.getTotal_vertical();
            lifts = statValue.getNum_lift_rides();
        } else {
            try {
                statValue = statDataDao.getSkierDataByDay(skierID, dayNum);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (statValue != null){
                total_vertical = statValue.getTotal_vertical();
                lifts = statValue.getNum_lift_rides();
            }
        }

        String res = "For user " + skierID + " in day " + dayNum + "\n"
                + "the vertical is " + total_vertical + "\n"
                + "the number of lift rides is " + lifts;
//        System.out.println(lifts);
        return res;
    }


}
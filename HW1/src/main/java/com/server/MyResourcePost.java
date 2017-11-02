package com.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("myresourcePost")
public class MyResourcePost {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public int postText(String content) {
        return (content.length());
    }
}
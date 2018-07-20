package com.proofpoint.featureflag.testapi;

import com.google.inject.Inject;
import org.ff4j.FF4j;

import javax.ws.rs.*;

@Path("/")
public class TestingResource
{
    @Inject
    FF4j ff4j;

    @GET
    public boolean get()
    {
        return ff4j.exist("TestingResource") && ff4j.check("TestingResource");
    }

}

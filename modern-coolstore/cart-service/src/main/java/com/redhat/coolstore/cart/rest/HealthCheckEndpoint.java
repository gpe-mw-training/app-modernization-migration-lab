package com.redhat.coolstore.cart.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
@Path("/")
public class HealthCheckEndpoint {

    @Autowired
    private HealthEndpoint health;

    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Health getHealth() {
        return health.invoke();
    }
}

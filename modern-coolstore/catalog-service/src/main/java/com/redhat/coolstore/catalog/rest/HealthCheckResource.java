package com.redhat.coolstore.catalog.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.wildfly.swarm.health.Health;
import org.wildfly.swarm.health.HealthStatus;

@Path("/")
public class HealthCheckResource {

    @GET
    @Health
    @Path("/status")
    public HealthStatus check() {
        return HealthStatus.named("server-state").up();
    }

}

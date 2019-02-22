package com.redhat.moviestore.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet();
        resources.add(MovieDetails.class);
        return resources;
    }

	
}

package com.redhat.coolstore.inventory.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.redhat.coolstore.inventory.rest.InventoryResource;


@ApplicationPath("/api")
public class RestApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet();

		resources.add(InventoryResource.class);

		resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
		resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

		return resources;
	}


}

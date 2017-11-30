package com.redhat.coolstore.rest;

import java.io.Serializable;



import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.coolstore.model.Inventory;
import com.redhat.coolstore.service.inventory.InventoryService;

import com.example.proprietary.customannotation.ProprietaryInitParam;
import com.example.proprietary.customannotation.ProprietaryServlet;

@ProprietaryServlet(name="FOO", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="one", value="1") }, mapping = {"/foo/*"})
@RequestScoped
@Path("/inventory")
public class InventoryResource implements Serializable {

	private static final long serialVersionUID = -7227732980791688773L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResource.class);

	@Inject
	private InventoryService inventoryService;

	@GET
	@Path("{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Inventory getAvailability(@PathParam("itemId") String itemId) {
		LOGGER.debug("Calling the inventory service");
		return inventoryService.getInventory(itemId);
	}

}
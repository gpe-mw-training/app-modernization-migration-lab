package com.redhat.coolstore.inventory.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.coolstore.inventory.model.InventoryEntity;
import com.redhat.coolstore.inventory.service.InventoryService;


@RequestScoped
@Path("/inventory")
public class InventoryResource  {

	@Inject
	private InventoryService inventoryService;

	@GET
	@Path("{itemId}")
	@Produces(MediaType.APPLICATION_JSON)
	public InventoryEntity getInventory(@PathParam("itemId") String itemId) {
		InventoryEntity inventory = inventoryService.getInventory(itemId);
		if (inventory == null) {
			throw new NotFoundException();
		} else {
			return inventory;
		}

	}

}

package com.redhat.coolstore.service.inventory;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.redhat.coolstore.model.Inventory;

@Stateless
public class InventoryService {

	@PersistenceContext(unitName="inventory")
	private EntityManager em;

	public InventoryService() {

	}
	
	public Inventory getInventory(String itemId) {
		Inventory inventory = em.find(Inventory.class,itemId);
		return inventory;
	}
}

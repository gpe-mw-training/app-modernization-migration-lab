package com.redhat.coolstore.service.inventory;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.redhat.coolstore.model.InventoryEntity;

@Stateless
public class InventoryService {

	@PersistenceContext(unitName="coolstore")
	private EntityManager em;

	public InventoryService() {

	}
	
	public InventoryEntity getInventory(String itemId) {
		InventoryEntity inventory = em.find(InventoryEntity.class,itemId);
		return inventory;
	}
}

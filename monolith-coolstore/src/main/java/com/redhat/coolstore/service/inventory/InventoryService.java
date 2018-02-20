package com.redhat.coolstore.service.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.redhat.coolstore.model.InventoryEntity;

@ApplicationScoped
public class InventoryService {

	@PersistenceContext(unitName="coolstore")
	private EntityManager em;

	public InventoryEntity getInventory(String itemId) {
		InventoryEntity inventory = em.find(InventoryEntity.class,itemId);
		return inventory;
	}
}

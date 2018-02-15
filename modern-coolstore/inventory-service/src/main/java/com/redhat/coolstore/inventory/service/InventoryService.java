package com.redhat.coolstore.inventory.service;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.redhat.coolstore.inventory.model.InventoryEntity;


@ApplicationScoped
public class InventoryService {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public InventoryEntity getInventory(String itemId) {
        InventoryEntity inventory = em.find(InventoryEntity.class, itemId);
        return inventory;
    }

}

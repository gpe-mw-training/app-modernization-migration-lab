package com.redhat.coolstore.inventory.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;


import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.model.InventoryEntity;
import com.redhat.coolstore.service.inventory.InventoryService;

@RunWith(Arquillian.class)
public class InventoryServiceTest {

    @Inject
    private InventoryService inventoryService;
    
    
    @Deployment
    public static Archive<?> createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addPackages(false, InventoryService.class.getPackage())
                    .addPackages(false, InventoryEntity.class.getPackage())
                    .addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
                    .addAsWebInfResource("test-ds.xml", "test-ds.xml")
                    .addAsWebInfResource("beans.xml","beans.xml")
                    .addAsResource("test-inventory.sql",  "test-coolstore.sql");
    }
    
    @Test
    public void getInventory() throws Exception {
   		assertThat(inventoryService, notNullValue());
        InventoryEntity inventory = inventoryService.getInventory("123456");
        assertThat(inventory, notNullValue());
        assertThat(inventory.getQuantity(), is(35));
    }

    @Test
    public void getNonExistingInventory() throws Exception {
        assertThat(inventoryService, notNullValue());
        InventoryEntity inventory = inventoryService.getInventory("notfound");
        assertThat(inventory, nullValue());
    }
}


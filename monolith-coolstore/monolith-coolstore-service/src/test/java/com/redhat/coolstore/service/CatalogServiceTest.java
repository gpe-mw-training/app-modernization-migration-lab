package com.redhat.coolstore.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.model.CatalogEntity;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.CatalogService;

@RunWith(Arquillian.class)
public class CatalogServiceTest {

    @Inject
    private CatalogService catalogService;
    
    
    @Deployment
    public static Archive<?> createDeployment() {
            return ShrinkWrap.create(WebArchive.class)
                    .addPackages(false, CatalogService.class.getPackage())
                    .addPackages(false, Product.class.getPackage())
                    .addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
                    .addAsWebInfResource("test-ds.xml", "test-ds.xml")
                    .addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
                    .addAsResource("test-catalog.sql",  "test-coolstore.sql");
    }
    
    @Test
    public void testGetProduct() throws Exception {
   		assertThat(catalogService, notNullValue());
        CatalogEntity product = catalogService.getCatalogItemById("123456");
        assertThat(product, notNullValue());
        assertThat(product.getName(), is("Redhat"));
    }

    @Test
    public void testGetNonExistingCatalog() throws Exception {
        assertThat(catalogService, notNullValue());
        CatalogEntity product = catalogService.getCatalogItemById("notfound");
        assertThat(product, nullValue());
    }
    
    @Test
    public void testAddProduct() {
    	assertThat(catalogService, notNullValue());
    	
    	CatalogEntity p = new CatalogEntity();
    	p.setDescription("productDescription1");
    	p.setItemId("999999");
    	p.setName("productName1");
    	p.setPrice(99.0);
    	catalogService.addCatalogItem(p);
    	assertThat(catalogService.getCatalogItemById("999999").getName(),is("productName1"));
    }
    
    @Test
    public void testAddProducts() {
    	assertThat(catalogService, notNullValue());
    	
    	String itemId1 = "999998";
    	CatalogEntity p1 = new CatalogEntity();
    	p1.setDescription("productDescription1");
    	p1.setItemId(itemId1);
    	p1.setName("productName1");
    	p1.setPrice(100.0);
    	catalogService.addCatalogItem(p1);
    	
    	String itemId2 = "111111";
    	CatalogEntity p2 = new CatalogEntity();
    	p2.setDescription("productDescription2");
    	p2.setItemId(itemId2);
    	p2.setName("productName2");
    	p2.setPrice(100.0);
    	
    	catalogService.addCatalogItem(p2);
    	
         Set<String> itemIds = catalogService.getCatalogItems().stream().filter(p -> p.getPrice() == 100.0).
        		 map(p -> p.getItemId()).
        		 collect(Collectors.toSet());
        assertThat(itemIds.size(),is(2));
        assertThat(itemIds, hasItems(itemId1,itemId2));
    }
}


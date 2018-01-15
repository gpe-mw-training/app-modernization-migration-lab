package com.redhat.coolstore.inventory.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.allOf;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.catalog.CatalogService;

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
                    .addAsWebInfResource("beans.xml","beans.xml")
                    .addAsResource("test-catalog.sql",  "test-coolstore.sql");
    }
    
    @Test
    public void testGetProduct() throws Exception {
   		assertThat(catalogService, notNullValue());
        Product product = catalogService.getProduct("123456");
        assertThat(product, notNullValue());
        assertThat(product.getName(), is("Redhat"));
    }

    @Test
    public void testGetNonExistingCatalog() throws Exception {
        assertThat(catalogService, notNullValue());
        Product product = catalogService.getProduct("notfound");
        assertThat(product, nullValue());
    }
    
    @Test
    public void testAddProduct() {
    	assertThat(catalogService, notNullValue());
    	
    	Product p = new Product();
    	p.setDescription("productDescription1");
    	p.setItemId("999999");
    	p.setName("productName1");
    	p.setPrice(100.0);
    	catalogService.addProduct(p);
    	assertThat(catalogService.getProduct("999999").getName(),is("productName1"));
    }
    
    @Test
    public void testAddProducts() {
    	assertThat(catalogService, notNullValue());
    	List<Product> listProducts = new ArrayList<Product>();
    	
    	String itemId1 = "999998";
    	Product p1 = new Product();
    	p1.setDescription("productDescription1");
    	p1.setItemId(itemId1);
    	p1.setName("productName1");
    	p1.setPrice(100.0);
    	listProducts.add(p1);
    	
    	String itemId2 = "111111";
    	Product p2 = new Product();
    	p2.setDescription("productDescription2");
    	p2.setItemId(itemId2);
    	p2.setName("productName2");
    	p2.setPrice(100.0);
    	listProducts.add(p2);
    	
    	catalogService.addProducts(listProducts);;
    	
         Set<String> itemIds = catalogService.getProducts().stream().filter(p -> p.getPrice() == 100.0).
        		 map(p -> p.getItemId()).
        		 collect(Collectors.toSet());
         
        assertThat(itemIds.size(),is(2));
        assertThat(itemIds,allOf(hasItem(itemId1),hasItem(itemId2)));
         
    	
    }
}


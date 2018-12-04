package com.redhat.coolstore.service.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import com.redhat.coolstore.model.CatalogEntity;
import com.redhat.coolstore.model.InventoryEntity;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.product.ProductService;

@Alternative
@Stateless
public class MockProductService extends ProductService{

	private Map<String, Product> mockProducts;

	@PostConstruct
	public void init() {
		mockProducts = new HashMap<>();
		
		
		InventoryEntity inventory1 = new InventoryEntity("123456",1,"India","http://maps.google.com/?q=India");
		CatalogEntity catalog1 = new CatalogEntity("123456","productName", "productDescription", 200.0,inventory1);
		mockProducts.put("123456", new Product(catalog1.getItemId(),catalog1.getName(), catalog1.getDescription(), catalog1.getPrice(),
												inventory1.getLocation(),inventory1.getQuantity(),inventory1.getLink()));
		
		InventoryEntity inventory2 = new InventoryEntity("pq",1,"India","http://maps.google.com/?q=delhi");
		CatalogEntity catalog2 = new CatalogEntity("p1", "productName1", "productDescription1", 100.0,inventory2);
		mockProducts.put("p1",new Product(catalog2.getItemId(),catalog2.getName(), catalog2.getDescription(), catalog2.getPrice(),
				inventory2.getLocation(),inventory2.getQuantity(),inventory2.getLink()));
		
		InventoryEntity inventory3 = new InventoryEntity("p2",1,"India","http://maps.google.com/?q=denver");
		CatalogEntity catalog3 =new CatalogEntity("p2", "productName3", "productDescription2", 200.0,inventory3);
		mockProducts.put("p2",new Product(catalog3.getItemId(),catalog3.getName(), catalog3.getDescription(), catalog3.getPrice(),
				inventory3.getLocation(),inventory3.getQuantity(),inventory3.getLink()));
		
		InventoryEntity inventory4 = new InventoryEntity("p3",1,"India","http://maps.google.com/?q=belgium");
		CatalogEntity catalog4 = new CatalogEntity("p3", "productName3", "productDescription3", 300.0,inventory4);
		mockProducts.put("p3",new Product(catalog4.getItemId(),catalog4.getName(), catalog4.getDescription(), catalog4.getPrice(),
				inventory4.getLocation(),inventory4.getQuantity(),inventory4.getLink()));
		
		
	}

	public List<Product> getProducts() {
		return (List<Product>) mockProducts.values();

	}


	public Product getProductByItemId(String itemId) {
		return mockProducts.get(itemId);
	}

}

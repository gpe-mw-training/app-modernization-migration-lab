package com.redhat.coolstore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import com.redhat.coolstore.model.Product;

@Alternative
@Stateless
public class MockProductService implements ProxyProductService{

	private Map<String, Product> mockProducts;

	@PostConstruct
	public void init() {
		mockProducts = new HashMap<>();
		
		mockProducts.put("123456", new Product("123456","productName", "productDescription", 200.0));
		mockProducts.put("p1",new Product("p1", "productName1", "productDescription1", 100.0));
		mockProducts.put("p2",new Product("p2", "productName3", "productDescription2", 200.0));
		mockProducts.put("p3",new Product("p3", "productName3", "productDescription3", 300.0));
		
	}

	public List<Product> getProducts() {
		return (List<Product>) mockProducts.values();

	}


	public Product getProductByItemId(String itemId) {
		return mockProducts.get(itemId);
	}

}

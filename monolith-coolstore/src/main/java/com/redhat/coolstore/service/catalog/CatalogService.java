package com.redhat.coolstore.service.catalog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import com.redhat.coolstore.model.Product;


@Stateless
public class CatalogService {


	@PersistenceContext(unitName="catalog")
	private EntityManager em;
	
	public CatalogService() {
	}

	public List<Product> getProducts() {
		return (List<Product>) em.createQuery("Select p from Product p").getResultList();
	}


	public Product getProduct(String itemId) {
		System.out.println(itemId);
		Product p = em.find(Product.class,itemId);

		System.out.println(p);
		return p;
	}

	public void add(Product product) {
		em.persist(product);
		
	}

	public void addAll(List<Product> products) {
		for (Product product : products) {
			add(product);
		}
	}




}

package com.redhat.coolstore.service.catalog;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.redhat.coolstore.model.Product;


@Stateless
public class CatalogService {


	@PersistenceContext(unitName="coolstore")
	private EntityManager em;
	
	public CatalogService() {
	}

	public List<Product> getProducts() {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
	        Root<Product> member = criteria.from(Product.class);
	        criteria.select(member);
	        return em.createQuery(criteria).getResultList();
	}


	public Product getProduct(String itemId) {
		return em.find(Product.class,itemId);
	}

	public void addProduct(Product product) {
		em.persist(product);
		
	}

	public void addProducts(List<Product> products) {
		for (Product product : products) {
			addProduct(product);
		}
	}




}

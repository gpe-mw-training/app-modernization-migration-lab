package com.redhat.coolstore.catalog.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.redhat.coolstore.catalog.model.Product;


@ApplicationScoped
@Transactional
public class CatalogService {


	@PersistenceContext(unitName="primary")
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

}

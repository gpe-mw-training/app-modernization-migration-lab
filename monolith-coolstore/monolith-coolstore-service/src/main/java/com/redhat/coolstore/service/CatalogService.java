package com.redhat.coolstore.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.redhat.coolstore.model.CatalogEntity;


@Stateless
public class CatalogService {


	@PersistenceContext(unitName="coolstore")
	private EntityManager em;
	
	public CatalogService() {
	}

	public List<CatalogEntity> getCatalogItems() {
	        CriteriaBuilder cb = em.getCriteriaBuilder();
	        CriteriaQuery<CatalogEntity> criteria = cb.createQuery(CatalogEntity.class);
	        Root<CatalogEntity> member = criteria.from(CatalogEntity.class);
	        criteria.select(member);
	        return em.createQuery(criteria).getResultList();
	}


	public CatalogEntity getCatalogItemById(String itemId) {
		return em.find(CatalogEntity.class,itemId);
	}
	
	public void addCatalogItem(CatalogEntity p) {
		em.persist(p);
	}

}

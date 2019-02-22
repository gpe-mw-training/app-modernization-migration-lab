package com.redhat.moviestore.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.redhat.moviestore.model.MovieDetailsEntity;

@Stateless
public class MovieService {

	@PersistenceContext(unitName="assignment")
	private EntityManager em;
    
    public MovieService() {
    }

    public List<MovieDetailsEntity> getMovies() {

    	CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MovieDetailsEntity> criteria = cb.createQuery(MovieDetailsEntity.class);
        Root<MovieDetailsEntity> member = criteria.from(MovieDetailsEntity.class);
        criteria.select(member);
        return em.createQuery(criteria).getResultList();
    }

    public MovieDetailsEntity getMovieByName(String name) {
    	TypedQuery<MovieDetailsEntity> query = em.createQuery("select m from MovieDetailsEntity m where m.name=:name",MovieDetailsEntity.class);
    	return query.setParameter("name", name).getSingleResult(); 
    }
}
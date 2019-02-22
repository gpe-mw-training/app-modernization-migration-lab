package com.redhat.moviestore.rest;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.moviestore.model.MovieDetailsEntity;
import com.redhat.moviestore.service.MovieService;

@RequestScoped
@Path("/moviedetails")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieDetails implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7877226781430373644L;

	@Inject
	private MovieService ms;


	@GET
	@Path("/")
	public List<MovieDetailsEntity> listAll() {
		return ms.getMovies();
	}

	@GET
	@Path("/{name}")
	public MovieDetailsEntity getMovieByName(@PathParam("name") String name) {
		MovieDetailsEntity entity = ms.getMovieByName(name);
		if (entity == null )
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		else 
			return entity;
	}

}

package com.redhat.coolstore.catalog.rest;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.coolstore.catalog.model.Product;
import com.redhat.coolstore.catalog.service.CatalogService;
import javax.ws.rs.NotFoundException;

@RequestScoped
@Path("/catalog")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CatalogResource implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7227732980791688773L;

	@Inject
	private CatalogService catalogService;

	@GET
	@Path("/products")
	public List<Product> listAll() {
		return catalogService.getProducts();
	}

	@GET
	@Path("/product/{itemId}")
	public Product getProduct(@PathParam("itemId") String itemId) {
		Product p = catalogService.getProduct(itemId);
		if(p == null)
			throw new NotFoundException();
		else
			return p;
	}

	@POST
	@Path("/product")
	public Response add(Product product) {
		catalogService.addProduct(product);
		return Response.ok().build();
	}

}

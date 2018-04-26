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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RequestScoped
@Path("/catalog")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(
		value = "The catalog service",
		description = "This API will list all catalog items",
		produces = MediaType.APPLICATION_JSON,
		basePath = "/catalog"
		)
public class CatalogResource implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7227732980791688773L;

	@Inject
	private CatalogService catalogService;

	@GET
	@Path("/products")
	@ApiOperation(
			value = "Retrieve all catalog items"	            
			)
	public List<Product> listAll() {
		return catalogService.getProducts();
	}

	@GET
	@Path("/product/{itemId}")
	@ApiOperation(
			value = "Retrieve catalog item based on Item ID."
	)
	public Product getProduct(@ApiParam(value = "Unique Item ID of the product", required = true, example = "329299")@PathParam("itemId") String itemId) {
		Product p = catalogService.getProduct(itemId);
		if(p == null)
			throw new NotFoundException();
		else
			return p;
	}

	@POST
	@Path("/product")
	@ApiOperation(
			value = "Add item to catalog db"
	)
	public Response add(@ApiParam(value = "Product that needs to be added", required = true)Product product) {
		catalogService.addProduct(product);
		return Response.ok().build();
	}

}

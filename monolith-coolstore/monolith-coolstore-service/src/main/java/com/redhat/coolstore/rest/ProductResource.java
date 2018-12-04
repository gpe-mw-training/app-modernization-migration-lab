package com.redhat.coolstore.rest;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.proprietary.customAnnotation.ProprietaryInitParam;
import com.example.proprietary.customAnnotation.ProprietaryServlet;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.ProductService;

@ProprietaryServlet(name="catalog", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="catalog", value="spring") }, mapping = {"/catalog/*"})
@RequestScoped
@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7877226781430373644L;
	
	@Inject
    private ProductService pm;


    @GET
    @Path("/")
    public List<Product> listAll() {
        return pm.getProducts();
    }

    @GET
    @Path("/{itemId}")
    public Product getCart(@PathParam("itemId") String itemId) {
        return pm.getProductByItemId(itemId);
    }

}

package com.redhat.coolstore.rest;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.proprietary.customAnnotation.ProprietaryInitParam;
import com.example.proprietary.customAnnotation.ProprietaryServlet;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.catalog.CatalogService;

@ProprietaryServlet(name="catalog", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="catalog", value="spring") }, mapping = {"/catalog/*"})
@SessionScoped
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
        return catalogService.getProduct(itemId);
    }

    @POST
    @Path("/")
    public Response add(Product product) {
    	System.out.println(product);
        catalogService.add(product);
        return Response.ok().build();
    }

}

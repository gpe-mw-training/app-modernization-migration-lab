package com.redhat.coolstore.rest;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.proprietary.customAnnotation.ProprietaryInitParam;
import com.example.proprietary.customAnnotation.ProprietaryServlet;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.service.ProxyProductServiceImpl;
import com.redhat.coolstore.service.ShoppingCartService;

@ProprietaryServlet(name="cart", runAs="SuperUser", initParams = { @ProprietaryInitParam (name="cart", value="spring") }, mapping = {"/cart/*"})
@RequestScoped
@Path("/cart")
public class CartResource implements Serializable {

	private static final long serialVersionUID = -7227732980791688773L;

	@Inject
	private ShoppingCartService shoppingCartService;

	@GET
	@Path("/{cartId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart getCart(@PathParam("cartId") String cartId) {
		return shoppingCartService.getShoppingCart(cartId);
	}
	

	@POST
	@Path("/{cartId}/{itemId}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart add(@PathParam("cartId") String cartId,
			@PathParam("itemId") String itemId,
			@PathParam("quantity") int quantity) throws Exception {
		try {
			return shoppingCartService.addToCart(cartId, itemId, quantity);
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.SERVICE_UNAVAILABLE);
		}

	}

	@DELETE
	@Path("/{cartId}/{itemId}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart delete(@PathParam("cartId") String cartId,
			@PathParam("itemId") String itemId,
			@PathParam("quantity") int quantity) throws Exception {
		return shoppingCartService.deleteItem(cartId, itemId, quantity);
	}

	@POST
	@Path("/checkout/{cartId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ShoppingCart checkout(@PathParam("cartId") String cartId) {
		ShoppingCart cart = shoppingCartService.checkout(cartId);
		return cart;
	}
	
}




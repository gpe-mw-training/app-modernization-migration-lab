package com.redhat.coolstore.cart.rest;

import java.io.Serializable;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redhat.coolstore.cart.model.ShoppingCart;
import com.redhat.coolstore.cart.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Component
@Path("/cart")
@Api(value = "Cart Service", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
public class CartResource implements Serializable {

	private static final long serialVersionUID = -7227732980791688773L;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@GET
	@Path("/{cartId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Gets a shoppingcart for cartid")
	  @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "shoppingcart found", response = ShoppingCart.class),
	    @ApiResponse(code = 404, message = "shoppingcart not found")
	  })
	public ShoppingCart getCart(@PathParam("cartId")  @ApiParam(value = "ShoppingCart cartID") String cartId) {

		return shoppingCartService.getShoppingCart(cartId);
	}

	@POST
	@Path("/{cartId}/{itemId}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds an item to cart")
	  @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "shoppingcart found", response = ShoppingCart.class),
	    @ApiResponse(code = 404, message = "shoppingcart not found")
	  })
	public ShoppingCart add(@PathParam("cartId") @ApiParam(value = "ShoppingCart cartID") String cartId,
			@PathParam("itemId") @ApiParam(value = "ShoppingCart itemID") String itemId,
			@PathParam("quantity") @ApiParam(value = "Item's quantity")int quantity) throws Exception {
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




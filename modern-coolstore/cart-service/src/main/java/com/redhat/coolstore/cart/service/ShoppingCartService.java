package com.redhat.coolstore.cart.service;

import com.redhat.coolstore.cart.model.Product;
import com.redhat.coolstore.cart.model.ShoppingCart;
import com.redhat.coolstore.cart.model.ShoppingCartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartService {

	@Autowired
	private CatalogService catalogService;

	@Autowired
	private PriceCalculationService priceCalculationService;

	Map<String, ShoppingCart> carts=new HashMap<String, ShoppingCart>();;

	Map<String, Product> productMap = new HashMap<String, Product>();

	public ShoppingCart getShoppingCart(String cartId) {
		ShoppingCart cart = carts.get(cartId);
		if (cart == null) {
			cart = new ShoppingCart(cartId);
			cart.setCartId(cartId);
			carts.put(cartId, cart);
		}
		return cart;
	}


	public Product getProduct(String itemId) {
		Product product = productMap.get(itemId);
		if (product == null) {
			product = catalogService.getProduct(itemId);
			productMap.put(itemId, product);
		}
		return product;
	}

	public ShoppingCart deleteItem(String cartId, String itemId, int quantity) {
		ShoppingCart sc = getShoppingCart(cartId); 
		if (quantity <= 0) {
			return sc;
		}

		Optional<ShoppingCartItem> cartItem = sc.getShoppingCartItemList().stream().filter(sci -> sci.getProduct().getItemId().equals(itemId)).findFirst();
		if (cartItem.isPresent()) {
			if (cartItem.get().getQuantity() <= quantity) {
				sc.removeShoppingCartItem(cartItem.get());
			} else {
				cartItem.get().setQuantity(cartItem.get().getQuantity() - quantity);
			}
		}
		calculateCartPrice(sc);
		carts.put(cartId, sc);
		return sc;    	
	}

	public ShoppingCart checkout(String cartId) {
		ShoppingCart sc = new ShoppingCart();
		sc.setCartId(cartId);
		carts.put(sc.getCartId(), sc);
		return sc;
	}


	public ShoppingCart addToCart(String cartId, String itemId, int quantity) {
		ShoppingCart sc = getShoppingCart(cartId);
		if (quantity <= 0) {
			return sc;
		}

		Product product = getProduct(itemId);
		if (product == null) {
			return sc;
		}

		Optional<ShoppingCartItem> cartItem = sc.getShoppingCartItemList().stream().filter(sci -> sci.getProduct().getItemId().equals(itemId)).findFirst();
		if (cartItem.isPresent()) {
			cartItem.get().setQuantity(cartItem.get().getQuantity() + quantity);
		} else {

			ShoppingCartItem sci = new ShoppingCartItem();
			sci.setProduct(product);
			sci.setQuantity(quantity);
			sci.setPrice(product.getPrice());
			sc.addShoppingCartItem(sci);
		}
		calculateCartPrice(sc);
		carts.put(cartId, sc);
		return sc;


	}

	public ShoppingCart calculateCartPrice(ShoppingCart sc) {
		priceCalculationService.priceShoppingCart(sc);
		carts.put(sc.getCartId(), sc);
		return sc;
	}
}
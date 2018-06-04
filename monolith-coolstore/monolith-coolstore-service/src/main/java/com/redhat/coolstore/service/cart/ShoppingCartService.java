package com.redhat.coolstore.service.cart;

import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.model.ShoppingCartItem;
import com.redhat.coolstore.service.catalog.CatalogService;
import com.redhat.coolstore.service.shipping.PriceCalculationService;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ShoppingCartService {

    @Inject
    private CatalogService catalogService;

    @Inject
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
    	List<ShoppingCartItem> listShoppingCartItems = sc.getShoppingCartItemList();

    	for (ShoppingCartItem cartItem : listShoppingCartItems) {
    		if(cartItem.getProduct().getItemId().equals(itemId)){
    			if(quantity >= cartItem.getQuantity()){
    				sc.removeShoppingCartItem(cartItem);
    			}else{
    				cartItem.setQuantity(cartItem.getQuantity() - quantity);
    			}
    			calculateCartPrice(sc);
    			carts.put(cartId, sc);
    		}
    	}
    	return sc;    	
    }

    public ShoppingCart checkout(String cartId) {
    	ShoppingCart sc = new ShoppingCart();
        sc.setCartId(cartId);
        carts.put(sc.getCartId(), sc);
        return sc;
    }

   
    public ShoppingCart addToCart(String cartId, String itemId, int quantity) {
        ShoppingCart cart = getShoppingCart(cartId);
        Product product = getProduct(itemId);

        if (product == null) {
            return cart;
        }

        ShoppingCartItem sci = new ShoppingCartItem();
        sci.setProduct(product);
        sci.setQuantity(quantity);
        sci.setPrice(product.getPrice());
        cart.addShoppingCartItem(sci);

        calculateCartPrice(cart);
        carts.put(cartId, cart);
        return cart;
        
        
    }

    public ShoppingCart calculateCartPrice(ShoppingCart sc) {
        priceCalculationService.priceShoppingCart(sc);
        carts.put(sc.getCartId(), sc);
        return sc;
    }
}
package com.redhat.coolstore.service.cart;

import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.model.ShoppingCartItem;
import com.redhat.coolstore.service.catalog.CatalogService;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.api.BasicCacheContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ShoppingCartService {

    private static final Logger LOG = LoggerFactory.getLogger(ShoppingCartService.class);

    @Inject
    ShippingService ss;

    @Inject
    CatalogService catalogServie;

    @Inject
    PromoService ps;

    Map<String, ShoppingCart> carts;

    Map<String, Product> productMap = new HashMap<String, Product>();

    @PostConstruct
    public void init() {
        String host = System.getenv("DATAGRID_HOST");
        String port = System.getenv("DATAGRID_PORT");

        if (host != null) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.addServer()
                        .host(host)
                        .port(Integer.parseInt(port));
                BasicCacheContainer manager = new RemoteCacheManager(builder.build());
                carts = manager.getCache("cart");

                LOG.info("Using remote JBoss Data Grid (Hot Rod) on {}:{} for cart data", host, port);
            } catch (Exception ex) {
                LOG.error("Failed to connect to remote JBoss Data Grid (Hot Rod) on {}:{}", host, port);
            }
        }

        if (carts == null) {
            LOG.info("Using local cache for cart data");
            carts = new HashMap<String, ShoppingCart>();
        }
    }

    public ShoppingCart getShoppingCart(String cartId) {
        if (!carts.containsKey(cartId)) {
            ShoppingCart cart = new ShoppingCart(cartId);
            carts.put(cartId, cart);
            return cart;
        }

        ShoppingCart cart = carts.get(cartId);
        priceShoppingCart(cart);
        carts.put(cartId, cart);
        return cart;
    }

    public void priceShoppingCart(ShoppingCart sc) {
        if (sc != null) {
            initShoppingCartForPricing(sc);

            if (sc.getShoppingCartItemList() != null && sc.getShoppingCartItemList().size() > 0) {
                ps.applyCartItemPromotions(sc);

                for (ShoppingCartItem sci : sc.getShoppingCartItemList()) {
                    sc.setCartItemPromoSavings(sc.getCartItemPromoSavings() + sci.getPromoSavings() * sci.getQuantity());
                    sc.setCartItemTotal(sc.getCartItemTotal() + sci.getPrice() * sci.getQuantity());
                }

                ss.calculateShipping(sc);
            }

            ps.applyShippingPromotions(sc);

            sc.setCartTotal(sc.getCartItemTotal() + sc.getShippingTotal());
        }
    }

    void initShoppingCartForPricing(ShoppingCart sc) {
        sc.setCartItemTotal(0);
        sc.setCartItemPromoSavings(0);
        sc.setShippingTotal(0);
        sc.setShippingPromoSavings(0);
        sc.setCartTotal(0);

        for (ShoppingCartItem sci : sc.getShoppingCartItemList()) {
            Product p = getProduct(sci.getProduct().getItemId());

            //if product exist, create new product to reset price
            if (p != null) {
                sci.setProduct(new Product(p.getItemId(), p.getName(), p.getDescription(), p.getPrice()));
                sci.setPrice(p.getPrice());
            }

            sci.setPromoSavings(0);
        }
    }

    public Product getProduct(String itemId) {
        if (!productMap.containsKey(itemId)) {
            // Fetch and cache products. TODO: Cache should expire at some point!
            List<Product> products = catalogServie.getProducts();
            for (Product product : products) {
            	productMap.put(product.getItemId(), product);
			}
//            productMap = products.stream().collect(Collectors.toMap(Product::getItemId, Function.identity()));
        }

        return productMap.get(itemId);
    }

    public ShoppingCart deleteItem(String cartId, String itemId, int quantity) {
        List<ShoppingCartItem> toRemoveList = new ArrayList<ShoppingCartItem>();

        ShoppingCart cart = getShoppingCart(cartId);
        
        List<ShoppingCartItem> listShoppingCartItems = cart.getShoppingCartItemList();
        
       /* cart.getShoppingCartItemList().stream()
                .filter(sci -> sci.getProduct().getItemId().equals(itemId))
                .forEach(sci -> {
                    if (quantity >= sci.getQuantity()) {
                        toRemoveList.add(sci);
                    } else {
                        sci.setQuantity(sci.getQuantity() - quantity);
                    }
                });*/
        for (ShoppingCartItem sci : listShoppingCartItems) {
        	if(sci.getProduct().getItemId().equals(itemId)){
        		if(quantity >= sci.getQuantity()){
        			toRemoveList.add(sci);
        		}else{
        			sci.setQuantity(sci.getQuantity() - quantity);
        		}
        		
        	}
			
		}
        
        
        //toRemoveList.forEach(cart::removeShoppingCartItem);
        for (ShoppingCartItem removeSci : toRemoveList) {
        	cart.removeShoppingCartItem(removeSci);
		}
       
        priceShoppingCart(cart);
        carts.put(cartId, cart);

        return cart;
    }

    public ShoppingCart checkout(String cartId) {
        ShoppingCart cart = getShoppingCart(cartId);
        cart.resetShoppingCartItemList();
        priceShoppingCart(cart);
        carts.put(cartId, cart);
        return cart;
    }

    public ShoppingCart addItem(String cartId, String itemId, int quantity) throws Exception {
        ShoppingCart cart = getShoppingCart(cartId);
        Product product = getProduct(itemId);

        if (product == null) {
            LOG.warn("Invalid product {} request to get added to the shopping cart. No product added", itemId);
            return cart;
        }

        ShoppingCartItem sci = new ShoppingCartItem();
        sci.setProduct(product);
        sci.setQuantity(quantity);
        sci.setPrice(product.getPrice());
        cart.addShoppingCartItem(sci);

        try {
            priceShoppingCart(cart);
            cart.setShoppingCartItemList(dedupeCartItems(cart));
        } catch (Exception ex) {
            cart.removeShoppingCartItem(sci);
            throw ex;
        }

        carts.put(cartId, cart);
        return cart;
    }

    public ShoppingCart set(String cartId, String tmpId) throws Exception {

        ShoppingCart cart = getShoppingCart(cartId);
        ShoppingCart tmpCart = getShoppingCart(tmpId);

        if (tmpCart != null) {
            cart.resetShoppingCartItemList();
            cart.setShoppingCartItemList(tmpCart.getShoppingCartItemList());
        }

        try {
            priceShoppingCart(cart);
            cart.setShoppingCartItemList(dedupeCartItems(cart));
        } catch (Exception ex) {
            throw ex;
        }

        carts.put(cartId, cart);
        return cart;
    }

    List<ShoppingCartItem> dedupeCartItems(ShoppingCart sc) {
        List<ShoppingCartItem> result = new ArrayList<ShoppingCartItem>();
        Map<String, Integer> quantityMap = new HashMap<String, Integer>();
        for (ShoppingCartItem sci : sc.getShoppingCartItemList()) {
            if (quantityMap.containsKey(sci.getProduct().getItemId())) {
                quantityMap.put(sci.getProduct().getItemId(), quantityMap.get(sci.getProduct().getItemId()) + sci.getQuantity());
            } else {
                quantityMap.put(sci.getProduct().getItemId(), sci.getQuantity());
            }
        }

        for (String itemId : quantityMap.keySet()) {
            Product p = getProduct(itemId);
            ShoppingCartItem newItem = new ShoppingCartItem();
            newItem.setQuantity(quantityMap.get(itemId));
            newItem.setPrice(p.getPrice());
            newItem.setProduct(p);
            result.add(newItem);
        }

        return result;
    }
}
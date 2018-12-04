package com.redhat.coolstore.service.shipping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.model.ShoppingCartItem;
import com.redhat.coolstore.service.PriceCalculationService;


public class PriceCalculationServiceTest {

    @Test
    public void priceCartForEmptyShoppingCart() throws Exception {
        ShoppingCart sc = new ShoppingCart();

        PriceCalculationService priceCalculationService = new PriceCalculationService();
        priceCalculationService.priceShoppingCart(sc);
        assertThat(sc.getShippingTotal(), equalTo(0.0));
        assertThat(sc.getCartItemTotal(), equalTo(0.0));
        assertThat(sc.getCartItemTotal(), equalTo(0.0));
    }

    @Test
    public void priceCartForShoppingCart() throws Exception {
        ShoppingCart sc = new ShoppingCart();
        ShoppingCartItem sci1 = new ShoppingCartItem();
        sci1.setPrice(10.0);
        sci1.setQuantity(1);
        sc.getShoppingCartItemList().add(sci1);
        ShoppingCartItem sci2 = new ShoppingCartItem();
        sci2.setPrice(20.0);
        sci2.setQuantity(2);
        sc.getShoppingCartItemList().add(sci2);

        PriceCalculationService priceCalculationService = new PriceCalculationService();
        priceCalculationService.priceShoppingCart(sc);
        assertThat(sc.getShippingTotal(), equalTo(6.99));
        assertThat(sc.getCartItemTotal(), equalTo(50.0));
        assertThat(sc.getCartTotal(), equalTo(56.99));
    }

    @Test
    public void priceCartForShoppingCartWithFreeShipping() throws Exception {
        ShoppingCart sc = new ShoppingCart();
        ShoppingCartItem sci1 = new ShoppingCartItem();
        sci1.setPrice(20.0);
        sci1.setQuantity(1);
        sc.getShoppingCartItemList().add(sci1);
        ShoppingCartItem sci2 = new ShoppingCartItem();
        sci2.setPrice(40.0);
        sci2.setQuantity(2);
        sc.getShoppingCartItemList().add(sci2);

        PriceCalculationService priceCalculationService = new PriceCalculationService();
        priceCalculationService.priceShoppingCart(sc);
        assertThat(sc.getShippingTotal(), equalTo(0.00));
        assertThat(sc.getCartItemTotal(), equalTo(100.0));
        assertThat(sc.getCartTotal(), equalTo(100.00));
    }

}

package com.redhat.coolstore.service;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

import com.redhat.coolstore.model.ShoppingCart;

@Alternative
@Stateless
public class MockPriceCalculationService{

    public void priceShoppingCart(ShoppingCart sc) {
       sc.setCartTotal(10.0);
    }


}

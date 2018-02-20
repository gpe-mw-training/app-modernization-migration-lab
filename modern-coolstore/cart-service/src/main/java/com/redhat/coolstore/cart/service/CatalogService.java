package com.redhat.coolstore.cart.service;

import com.redhat.coolstore.cart.model.Product;

public interface CatalogService {

    Product getProduct(String itemId);

}

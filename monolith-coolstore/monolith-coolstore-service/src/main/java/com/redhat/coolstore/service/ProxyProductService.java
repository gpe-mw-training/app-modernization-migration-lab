package com.redhat.coolstore.service;

import com.redhat.coolstore.model.Product;

public interface ProxyProductService {

	public Product getProductByItemId(String itemId);

}

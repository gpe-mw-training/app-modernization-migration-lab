package com.redhat.coolstore.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.redhat.coolstore.model.CatalogEntity;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.CatalogService;

@Stateless
public class ProductService {

    @Inject
    CatalogService cm;
    
    public ProductService() {
    }

    public List<Product> getProducts() {
        return cm.getCatalogItems().stream().map(entity -> toProduct(entity)).collect(Collectors.toList());
    }

    public Product getProductByItemId(String itemId) {
        CatalogEntity entity = cm.getCatalogItemById(itemId);
        if (entity == null)
            return null;
        return toProduct(entity);
    }
    
    private Product toProduct(CatalogEntity entity) {
    	 Product prod = new Product();
         prod.setItemId(entity.getItemId());
         prod.setName(entity.getName());
         prod.setDesc(entity.getDescription());
         prod.setPrice(entity.getPrice());
         return prod;
    }

}
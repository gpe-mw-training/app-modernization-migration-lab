package com.redhat.coolstore.service;

import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.redhat.coolstore.model.Product;


public class ProxyProductServiceImpl implements ProxyProductService {
	
	private ClientRequest request; 
	
	private String CATALOG_URL=System.getenv("CATALOG_SERVICE_URL");

	@Override
	public Product getProductByItemId(String itemId) {
		if(CATALOG_URL == null)
			throw new RuntimeErrorException(null, "Please define enviornment variable CATALOG_SERVICE_URL with catalog service url");
		
		String url = CATALOG_URL+ itemId;

		request = new ClientRequest(url);
		request.accept(MediaType.APPLICATION_JSON);
		ClientResponse<Product> response=null;
		try {
			response = request.get(Product.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.clear();
		return response.getEntity();
	}

}

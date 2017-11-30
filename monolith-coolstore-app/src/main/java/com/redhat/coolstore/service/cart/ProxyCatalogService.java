/*package com.redhat.coolstore.service.cart;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.redhat.coolstore.model.Product;


public class ProxyCatalogService{

	private static final String urlcatalogservice ="http://127.0.0.1:8080/monolith-coolstore";//System.getenv("CATALOG_ENDPOINT");

	public ProxyCatalogService(){

	}

	public List<Product> getProducts(){
		Client client = ClientBuilder.newBuilder().build();

		WebTarget target = client.target(urlcatalogservice).path("/rest/catalog/products");
		Response response = target.request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();

		
		String jsonString = response.readEntity(String.class);
		
		Gson gson = new Gson();
		List<Product> products = new ArrayList<Product>();
		
		return gson.fromJson(jsonString,products.getClass());

	}

}*/
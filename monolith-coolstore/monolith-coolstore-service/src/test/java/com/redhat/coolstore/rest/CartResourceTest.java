package com.redhat.coolstore.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.ShoppingCartService;
import com.redhat.coolstore.service.CatalogService;
import com.redhat.coolstore.service.ProductService;
import com.redhat.coolstore.service.PriceCalculationService;



@RunWith(Arquillian.class)
public class CartResourceTest {

	@ArquillianResource
	URL baseURL;

	private Client client;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class,"ROOT.war")
				.addPackages(false, CatalogService.class.getPackage())
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ProductService.class.getPackage())
				.addPackages(true, Product.class.getPackage())
				.addPackages(true, PriceCalculationService.class.getPackage())
				.addClass(CartResource.class)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
				.addAsWebInfResource(new FileAsset(new File("src/test/resources/test-web.xml")),"web.xml")
				.addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
				.addAsResource("test-catalog.sql",  "test-coolstore.sql");

	}

	@Before
	public void beforeTest() throws Exception {
		client = ClientBuilder.newClient();
	}

	@After
	public void afterTest() throws Exception {
		client.close();
	}

	@Test
	@RunAsClient
	public void testGetCart() throws Exception {
		WebTarget target = client.target(URI.create(new URL(baseURL, "/api/cart/123456").toExternalForm()));
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("cartId", null), equalTo("123456"));
		assertThat(value.getDouble("cartTotal", 0.0), equalTo(new Double(0.0)));
	}

	@Test
	@RunAsClient
	public void testCheckout() throws Exception {
		WebTarget target = client.target(URI.create(new URL(baseURL, "/api/cart/checkout/123456").toExternalForm()));
		Response response = target.
				request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).method(HttpMethod.POST);

		assertThat(response.getStatus(), equalTo(new Integer(200)));

		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("cartId", null), equalTo("123456"));
		assertThat(value.getDouble("cartItemTotal", 0.0), equalTo(new Double(0.0)));
	}

	@Test
	@RunAsClient
	public void testAddtoCart() throws Exception {
		WebTarget target = client.target(URI.create(new URL(baseURL, "/api/cart/mycart/123456/1").toExternalForm()));
		Response response = target.
				request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).method(HttpMethod.POST);

		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("cartId", null), equalTo("mycart"));
		assertThat(value.getDouble("cartItemTotal", 0.0), equalTo(new Double(34.99)));
		assertThat(value.getDouble("shippingTotal", 0.0), equalTo(new Double(4.99)));
	}
	
	@Test
	@RunAsClient
	public void testDeleteFromCart() throws Exception {
		WebTarget target = client.target(URI.create(new URL(baseURL, "/api/cart/mycart/123456/1").toExternalForm()));
		Response response = target.
				request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).method(HttpMethod.DELETE);
		
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("cartId", null), equalTo("mycart"));
		assertThat(value.getDouble("cartItemTotal", 0.0), equalTo(new Double(0.0)));
	}

}
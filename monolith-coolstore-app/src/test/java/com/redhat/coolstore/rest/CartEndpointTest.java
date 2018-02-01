package com.redhat.coolstore.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.service.cart.ShoppingCartService;
import com.redhat.coolstore.service.catalog.CatalogService;

public class CartEndpointTest {

	private static String port = System.getProperty("arquillian.http.port", "18080");

	private Client client;
	
	@ArquillianResource
	URL baseURL;
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ShoppingCart.class.getPackage())
				.addPackages(false, CatalogService.class.getPackage())
				.addClass(Product.class)
				.addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource("test-beans.xml","beans.xml")
				.addAsResource("test-catalog.sql",  "test-coolstore.sql");
	}


	@Before
	public void before() throws Exception {
		client = ClientBuilder.newClient();
	}

	@After
	public void after() throws Exception {
		client.close();
	}


	@Test
	public void testRetrieveCartById() throws Exception {
		try {
			System.out.println("============================================================"+baseURL);
			WebTarget target = client.target(baseURL.toString()).path("/cart").path("/123456");
			Response response = target.request(MediaType.APPLICATION_JSON).get();

			assertThat(response.getStatus(), equalTo(new Integer(200)));

			JsonObject value = (JsonObject) Json.parse((String) response.getEntity());
			System.out.println(value);
			//assertThat(value.getString("itemId", null), equalTo("123456"));
			//assertThat(value.getString("location", null), equalTo("location [MOCK]"));
			//assertThat(value.getInt("quantity", 0), equalTo(new Integer(99)));
			//assertThat(value.getString("link", null), equalTo("link"));
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
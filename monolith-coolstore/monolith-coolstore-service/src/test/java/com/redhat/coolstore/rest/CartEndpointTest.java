package com.redhat.coolstore.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.service.cart.ShoppingCartService;
import com.redhat.coolstore.service.catalog.CatalogService;
import com.redhat.coolstore.service.shipping.PriceCalculationService;

@RunWith(Arquillian.class)
@RunAsClient
public class CartEndpointTest {
	
	@ArquillianResource
	URL baseURL;
	
	private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	
	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ShoppingCart.class.getPackage())
				.addPackages(false, CatalogService.class.getPackage())
				.addPackages(false, PriceCalculationService.class.getPackage())
				.addClass(Product.class)
				.addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource("test-beans.xml","beans.xml")
				.addAsResource("test-catalog.sql",  "test-coolstore.sql");
	}

	@Test
	public void testRetrieveCartById(@ArquillianResource URL contextPath) throws Exception {
		try {
			
			HttpResponse response = httpClient.execute(new HttpGet(contextPath.toString() + "rest/cart/123456"));
			System.out.println(response.getStatusLine().getStatusCode());
			

			//JsonObject value = (JsonObject) Json.parse((String) response.getEntity());
			//
			//assertThat(value.getString("itemId", null), equalTo("123456"));
			//assertThat(value.getString("location", null), equalTo("location [MOCK]"));
			//assertThat(value.getInt("quantity", 0), equalTo(new Integer(99)));
			//assertThat(value.getString("link", null), equalTo("link"));
		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}
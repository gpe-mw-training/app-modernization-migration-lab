package com.redhat.coolstore.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.HttpMethod;
/*import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;*/
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;
import com.redhat.coolstore.service.ShoppingCartService;



@RunWith(Arquillian.class)
public class CartResourceTest {

	@ArquillianResource
	URL baseURL;

	private ClientRequest request;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class,"ROOT.war")
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ShoppingCart.class.getPackage())
				.addClass(CartResource.class)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource("test-beans.xml","beans.xml")
				.addAsWebInfResource(new FileAsset(new File("src/test/resources/test-web.xml")),"web.xml");

	}

	@After
	public void afterTest() throws Exception {
		request.clear();
	}

	@Test
	@RunAsClient
	public void testGetCart() throws Exception {
		request = new ClientRequest(new URL(baseURL,"/api/cart/123456").toExternalForm());
		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<ShoppingCart> response=null;
		try {
			response = request.get(ShoppingCart.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ShoppingCart s = response.getEntity();

		assertThat(response.getStatus(), equalTo(new Integer(200)));
		assertThat(s.getCartId(), equalTo("123456"));
		assertThat(s.getCartTotal(), equalTo(new Double(0.0)));
	}
	
	@Test
	@RunAsClient
	public void testCheckout() throws Exception {
		request = new ClientRequest(new URL(baseURL,"/api/cart/checkout/123456").toExternalForm());
		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<ShoppingCart> response=null;
		try {
			response = request.post();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ShoppingCart s = response.getEntity(ShoppingCart.class);
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		assertThat(s.getCartId(), equalTo("123456"));
		assertThat(s.getCartItemTotal(), equalTo(new Double(0.0)));
	}

    @Test
	@RunAsClient
	public void testAddtoCart() throws Exception {
    	request = new ClientRequest(new URL(baseURL,"/api/cart/mycart/123456/1").toExternalForm());
		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<ShoppingCart> response=null;
		try {
			response = request.post();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ShoppingCart s = response.getEntity(ShoppingCart.class);
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		assertThat(s.getCartId(), equalTo("mycart"));
		assertThat(s.getCartItemTotal(), equalTo(new Double(0)));
		assertThat(s.getCartTotal(), equalTo(new Double(10)));
	}
		
	@Test
	@RunAsClient
	public void testDeleteFromCart() throws Exception {
		request = new ClientRequest(new URL(baseURL,"/api/cart/mycart/123456/1").toExternalForm());
		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<ShoppingCart> response=null;
		try {
			response = request.post();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ShoppingCart s = response.getEntity(ShoppingCart.class);
		
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		assertThat(s.getCartId(), equalTo("mycart"));
		assertThat(s.getCartItemTotal(), equalTo(new Double(0.0)));
	}
	 
}
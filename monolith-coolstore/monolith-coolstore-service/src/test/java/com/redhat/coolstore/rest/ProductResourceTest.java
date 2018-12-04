package com.redhat.coolstore.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.net.URI;
import java.net.URL;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.service.CatalogService;
import com.redhat.coolstore.service.ProductService;


@RunWith(Arquillian.class)
public class ProductResourceTest {

	@ArquillianResource
	URL baseURL;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class,"ROOT.war")
				.addPackages(false, CatalogService.class.getPackage())
				.addPackages(false, ProductService.class.getPackage())
				.addPackages(true, Product.class.getPackage())
				.addClass(ProductResource.class)
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
				.addAsWebInfResource(new FileAsset(new File("src/test/resources/test-web.xml")),"web.xml")
				.addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
				.addAsResource("test-catalog.sql",  "test-coolstore.sql");

	}

	@Test
	@RunAsClient
	public void testGetProduct() throws Exception {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URI.create(new URL(baseURL, "/api/product/123456").toExternalForm()));
		Response response = target.request(MediaType.APPLICATION_JSON).get();
		assertThat(response.getStatus(), equalTo(new Integer(200)));
		JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
		assertThat(value.getString("itemId", null), equalTo("123456"));
		assertThat(value.getString("name", null), equalTo("Redhat"));
		assertThat(value.getString("desc",null), equalTo("Fedora"));
		assertThat(value.getDouble("price", 0.0), equalTo(new Double(34.99)));
	}
	
}
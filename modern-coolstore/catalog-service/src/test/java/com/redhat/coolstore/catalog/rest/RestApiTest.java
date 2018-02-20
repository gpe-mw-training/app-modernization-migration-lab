package com.redhat.coolstore.catalog.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.redhat.coolstore.catalog.model.Product;
import com.redhat.coolstore.catalog.service.CatalogService;

@RunWith(Arquillian.class)
public class RestApiTest {

    private static String port = System.getProperty("arquillian.swarm.http.port", "18080");
    
    private Client client;

    @CreateSwarm
    public static Swarm newContainer() throws Exception {
        Properties properties = new Properties();
        properties.put("swarm.http.port", port);
        return new Swarm(properties).withProfile("local");
    }

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
        		.addPackages(true,CatalogResource.class.getPackage())
        		.addPackage(CatalogService.class.getPackage())
        		.addPackage(Product.class.getPackage())
                .addAsResource("project-local.yml", "project-local.yml")
                .addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
                .addAsResource("META-INF/test-load.sql",  "META-INF/test-load.sql");
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
    @RunAsClient
    public void testGetProduct() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/catalog/product").path("/123456");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(new Integer(200)));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("itemId", null), equalTo("123456"));
        assertThat(value.getString("name", null), equalTo("catalog"));
        assertThat(value.getString("description",null), equalTo("CatalogServiceTest"));
        assertThat(value.getDouble("price", 0.0), equalTo(new Double(34)));
    }
    
    @Test
    @RunAsClient
    public void testGetProductWhenItemIdDoesNotExist() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/catalog/product").path("/doesnotexist");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(new Integer(404)));
    }
    
    @Test
    @RunAsClient
    public void testHealthCheckCombined() throws Exception {
        WebTarget target = client.target("http://localhost:" + port).path("/health");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(new Integer(200)));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("outcome", ""), equalTo("UP"));
        JsonArray checks = value.get("checks").asArray();
        assertThat(checks.size(), equalTo(new Integer(1)));
        JsonObject state = checks.get(0).asObject();
        assertThat(state.getString("id", ""), equalTo("server-state"));
        assertThat(state.getString("result", ""), equalTo("UP"));
    }
    
    @Test
    @RunAsClient
    public void testHealthCheckStatus() throws Exception {
        WebTarget target = client.target("http://localhost:"+ port).path("/status");
        Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(new Integer(200)));
        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
        assertThat(value.getString("id", ""), equalTo("server-state"));
        assertThat(value.getString("result", ""), equalTo("UP"));
    }
}

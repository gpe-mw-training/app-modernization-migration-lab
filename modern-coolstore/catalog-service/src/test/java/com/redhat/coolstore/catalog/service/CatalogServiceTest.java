package com.redhat.coolstore.catalog.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Properties;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.arquillian.CreateSwarm;

import com.redhat.coolstore.catalog.model.Product;


@RunWith(Arquillian.class)
public class CatalogServiceTest {
    
    private static String port="18081";

    @CreateSwarm
    public static Swarm newContainer() throws Exception {
        Properties properties = new Properties();
        properties.put("swarm.http.port", port);
        return new Swarm(properties).withProfile("local");
    }

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, CatalogService.class.getPackage())
                .addPackages(true, Product.class.getPackage())
                .addAsResource("project-local.yml", "project-local.yml")
                .addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
                .addAsResource("META-INF/test-load.sql",  "META-INF/test-load.sql");
    }

    @Inject
    private CatalogService catalogService;

    @Test
    public void getProduct() throws Exception {
        assertThat(catalogService, notNullValue());
        Product product = catalogService.getProduct("123456");
        assertThat(product, notNullValue());
        assertThat(product.getPrice(), is(34.0));
    }
    
    @Test
    public void getNonExistingProduct() throws Exception {
        assertThat(catalogService, notNullValue());
        Product product = catalogService.getProduct("notfound");
        assertThat(product, nullValue());
    }
}


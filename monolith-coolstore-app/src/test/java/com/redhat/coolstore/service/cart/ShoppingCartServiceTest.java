package com.redhat.coolstore.service.cart;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.redhat.coolstore.service.catalog.CatalogService;
import com.redhat.coolstore.model.ShoppingCartItem;
import com.redhat.coolstore.model.Product;
import com.redhat.coolstore.model.ShoppingCart;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(Arquillian.class)
public class ShoppingCartServiceTest {

	private ShoppingCartService shoppingCartService;
	
	@Mock
	private CatalogService catalogService ;
	
	@Deployment
	public static Archive<?> createDeployment() {
		 MavenResolverSystem resolver = Maven.resolver();
		 
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ShoppingCart.class.getPackage())
				.addPackages(false, CatalogService.class.getPackage())
				.addClass(Product.class)
				.addAsResource("META-INF/test-persistence.xml",  "META-INF/persistence.xml")
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml")
				.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.mockito:mockito-all:1.10.8").withTransitivity().asSingleFile())
				.addAsResource("test-catalog.sql",  "test-coolstore.sql");
	}
	
	
	@Before
    public void setup() {
		catalogService = Mockito.mock(CatalogService.class);
		shoppingCartService = new ShoppingCartService(catalogService);

		when(catalogService.getProduct("123456")).thenReturn(new Product("123456", "productName", "productDescription", 200.0));
        when(catalogService.getProduct("p1")).thenReturn(new Product("p1", "productName1", "productDescription", 100.0));
		
	}

	@Test
    public void testGetProduct() {
		assertThat(shoppingCartService, notNullValue());
        assertThat(shoppingCartService.getProduct("123456").getPrice(), is(200.0));
    }
	
	/*@Test
	public void testAddtoCart() {
		ShoppingCart sc = shoppingCartService.addToCart("123456", "p1", 1);
      
		System.out.println("++++++++++++++++++++++++++++++++++++++++"+sc);
		
		assertThat(sc.getShoppingCartItemList().size(), equalTo(1));
        assertThat(sc.getCartItemTotal(), equalTo(100.0));
       
        ShoppingCartItem sci = sc.getShoppingCartItemList().get(0);
        
        assertThat(sci.getProduct(), notNullValue());
        assertThat(sci.getProduct().getItemId(), equalTo("p1"));
        assertThat(sci.getProduct().getPrice(), equalTo(100.0));
        assertThat(sci.getPrice(), equalTo(100.0));
        assertThat(sci.getQuantity(), equalTo(1));
       
        verify(catalogService).getProduct("p1");
	}*/


}


package com.redhat.coolstore.service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.redhat.coolstore.service.ShoppingCartService;
import com.redhat.coolstore.model.ShoppingCartItem;
import com.redhat.coolstore.model.ShoppingCart;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;



@RunWith(Arquillian.class)
public class ShoppingCartServiceTest {

	@Inject
	private ShoppingCartService shoppingCartService;

	@Deployment
	public static Archive<?> createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addPackages(false, ShoppingCartService.class.getPackage())
				.addPackages(false, ShoppingCart.class.getPackage())
				.addAsWebInfResource("test-ds.xml", "test-ds.xml")
				.addAsWebInfResource("test-beans.xml","beans.xml");
	}


	@Test
	public void testGetProduct() {
		assertThat(shoppingCartService, notNullValue());
		assertThat(shoppingCartService.getProduct("123456").getPrice(), is(200.0));
	}

	@Test
	public void testAddtoCart() {
		ShoppingCart sc = shoppingCartService.addToCart("999", "p1", 1);
		assertThat(sc.getShoppingCartItemList().size(), equalTo(1));
		assertThat(sc.getCartItemTotal(), equalTo(0.0));
		ShoppingCartItem sci = sc.getShoppingCartItemList().get(0);
		assertThat(sci.getProduct(), notNullValue());
		assertThat(sci.getProduct().getItemId(), equalTo("p1"));
		assertThat(sci.getProduct().getPrice(), equalTo(100.0));
		assertThat(sci.getPrice(), equalTo(100.0));
		assertThat(sci.getQuantity(), equalTo(1));
	}

	@Test
	public void testDeleteItemFromCart() {
		ShoppingCart scAddition = shoppingCartService.addToCart("998", "p2", 3);
		ShoppingCart sc = shoppingCartService.deleteItem("998", "p2", 2);
		assertThat(sc.getShoppingCartItemList().size(), equalTo(1));
		ShoppingCartItem sci = sc.getShoppingCartItemList().get(0);
		assertThat(sci.getProduct(), notNullValue());
		assertThat(sci.getProduct().getItemId(), equalTo("p2"));
		assertThat(sci.getProduct().getPrice(), equalTo(200.0));
		assertThat(sci.getPrice(), equalTo(200.0));
		assertThat(sci.getQuantity(), equalTo(1));
	}

	@Test
    public void testCheckout() {
        shoppingCartService.addToCart("997", "p3", 3);
        ShoppingCart sc = shoppingCartService.checkout("997");
        assertThat(sc, notNullValue());
        assertThat(sc.getCartId(), equalTo("997"));
        assertThat(sc.getCartItemTotal(), equalTo(0.0));
        assertThat(sc.getShippingTotal(), equalTo(0.0));
        assertThat(sc.getCartTotal(), equalTo(0.0));
        assertThat(sc.getShoppingCartItemList().size(), equalTo(0));
    }
	
}


package com.redhat.coolstore.cart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart implements Serializable {

	private static final long serialVersionUID = -1108043957592113528L;

	private double cartItemTotal;

	private double shippingTotal;
	
	private double cartTotal;

	private String cartId;
			
	private List<ShoppingCartItem> shoppingCartItemList = new ArrayList<ShoppingCartItem>();

	public ShoppingCart() {
	}

	public ShoppingCart(String cartId) {
		this.cartId = cartId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public List<ShoppingCartItem> getShoppingCartItemList() {
		return shoppingCartItemList;
	}

	public void setShoppingCartItemList(List<ShoppingCartItem> shoppingCartItemList) {
		this.shoppingCartItemList = shoppingCartItemList;
	}

	public void resetShoppingCartItemList() {
		shoppingCartItemList = new ArrayList<ShoppingCartItem>();
	}

	public void addShoppingCartItem(ShoppingCartItem sci) {
		
		if ( sci != null ) {
			
			shoppingCartItemList.add(sci);
			
		}
		
	}
	
	public boolean removeShoppingCartItem(ShoppingCartItem sci) {
		
		boolean removed = false;
		
		if ( sci != null ) {
			
			removed = shoppingCartItemList.remove(sci);
			
		}
		
		return removed;
		
	}

	public double getCartItemTotal() {
		return cartItemTotal;
	}

	public void setCartItemTotal(double cartItemTotal) {
		this.cartItemTotal = cartItemTotal;
	}

	public double getShippingTotal() {
		return shippingTotal;
	}

	public void setShippingTotal(double shippingTotal) {
		this.shippingTotal = shippingTotal;
	}

	public double getCartTotal() {
		return cartTotal;
	}

	public void setCartTotal(double cartTotal) {
		this.cartTotal = cartTotal;
	}

	@Override
	public String toString() {
		return "ShoppingCart [cartId=" + cartId
				+ ", cartItemTotal=" + cartItemTotal
				+ ", shippingTotal=" + shippingTotal
				+ ", cartTotal=" + cartTotal + ", shoppingCartItemList="
				+ shoppingCartItemList + "]";
	}
	
}

package com.redhat.coolstore.model;

import java.io.Serializable;

public class Product implements Serializable {

    @Override
	public String toString() {
		return "Product [itemId=" + itemId + ", name=" + name + ", desc=" + desc + ", price=" + price + ", location="
				+ location + ", quantity=" + quantity + ", link=" + link + "]";
	}

	private String itemId;

	private String name;

    private String desc;

    private double price;

    private String location;

    private int quantity;

    private String link;


    public Product() {
    }
    
    public Product(String itemId, String name, String desc, double price, String location, int quantity, String link) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.location = location;
		this.quantity = quantity;
		this.link = link;
	}

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}

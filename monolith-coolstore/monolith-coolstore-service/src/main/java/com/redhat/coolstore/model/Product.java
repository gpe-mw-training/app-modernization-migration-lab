package com.redhat.coolstore.model;

import java.io.Serializable;

public class Product implements Serializable {

    @Override
	public String toString() {
		return "Product [itemId=" + itemId + ", name=" + name + ", description=" + description + ", price=" + price + ", location="
				+ "]";
	}

	private String itemId;

	private String name;

    private String description;

    private double price;


    public Product() {
    }
    
    public Product(String itemId, String name, String desc, double price) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.description = desc;
		this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

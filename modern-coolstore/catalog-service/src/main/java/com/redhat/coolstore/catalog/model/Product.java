package com.redhat.coolstore.catalog.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "PRODUCT_CATALOG", uniqueConstraints = @UniqueConstraint(columnNames = "itemId"))
public class Product implements Serializable {

	private static final long serialVersionUID = -7304814269819778382L;
	@XmlElement
    @Id
	private String itemId;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private double price;
	
	public Product() {
		
	}
	
	public Product(String itemId, String name, String description, double price) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.description = description;
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
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [itemId=" + itemId + ", name=" + name + ", description="
				+ description + ", price=" + price + "]";
	}
}

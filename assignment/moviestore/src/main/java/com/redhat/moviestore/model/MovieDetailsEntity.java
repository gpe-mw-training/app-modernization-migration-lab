package com.redhat.moviestore.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.omg.CORBA.portable.BoxedValueHelper;

@Entity
@XmlRootElement
@Table(name = "moviecatalog", uniqueConstraints = @UniqueConstraint(columnNames = "itemId"))
public class MovieDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 529983552581533762L;
	@XmlElement
	@Id
	private String itemId;
	@XmlElement
	private String name;
	@XmlElement
	private String genre;
	@XmlElement
	private int release_year;
	
	@XmlElement
	private double rating;

	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private BoxOfficeEntity boxOfficeDetails;


	public BoxOfficeEntity getBoxOfficeDetails() {
		return boxOfficeDetails;
	}

	public void setBoxOfficeDetails(BoxOfficeEntity boxOfficeDetails) {
		this.boxOfficeDetails = boxOfficeDetails;
	}

	public MovieDetailsEntity() {

	}

	public MovieDetailsEntity(String itemId, String name, String description, double price,BoxOfficeEntity entity) {
		super();
		this.boxOfficeDetails = entity;
		this.itemId = itemId;
		this.name = name;
	}
	
	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public int getRelease_year() {
		return release_year;
	}

	public void setRelease_year(int release_year) {
		this.release_year = release_year;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
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

	@Override
	public String toString() {
		return "MovieDetailsEntity [itemId=" + itemId + ", name=" + name + ", genre=" + genre + ", release_year="
				+ release_year + ", rating=" + rating + ", boxofficedetails=" + boxOfficeDetails + "]";
	}
}

package com.redhat.moviestore.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name = "BOX_OFFICE", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class BoxOfficeEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3323707520047638678L;


    @XmlElement
    @Id
    private String id;


    @XmlElement
    private String budget;


    @XmlElement
    private int opening;


    @XmlElement
    private String gross;

    public BoxOfficeEntity() {

    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public int getOpening() {
		return opening;
	}

	public void setOpening(int opening) {
		this.opening = opening;
	}

	public String getGross() {
		return gross;
	}

	public void setGross(String gross) {
		this.gross = gross;
	}

	
	@Override
	public String toString() {
		return "BoxOfficeEntity [id=" + id + ", budget=" + budget + ", opening=" + opening + ", gross=" + gross + "]";
	}

   
}

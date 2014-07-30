package org.googlecode.jef.spring.entity;

import javax.persistence.Table;
import jef.database.DataObject;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import javax.persistence.Entity;
import jef.codegen.support.NotModified;
/**
 * This class was generated by JEF according to the table in database.
 * You need to modify the type of primary key field, to the strategy your own.
 */
@NotModified
@Entity
@Table(name="product")
public class Product extends DataObject{


	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	@Id
	@Column(name="id",precision=10,columnDefinition="NUMBER",nullable=false)
	private int id;

	@Column(name="price",scale=4,precision=11,columnDefinition="NUMBER",nullable=false)
	private Float price;

	public void setId(int obj){
		this.id = obj;
	}

	public int getId(){
		return id;
	}

	public void setPrice(Float obj){
		this.price = obj;
	}

	public Float getPrice(){
		return price;
	}

	public Product(){
	}

	public Product(int id){
		this.id = id;
	}


public enum Field implements jef.database.Field{id,price}
}
package com.lebel.restsample.data;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "ProductID", "Name", "ProductNumber", "Color", "ListPrice", 
	    "DaysToManufacture", "SellStartDate", "SellEndDate"})
public class AdventureProduct {
	private int productID;
	private String name;
	private String productNumber;
	private String Color;
	private double listPrice;
	private int daysToManufacture;
	private Date sellStartDate;
	private Date sellEndDate;
	public AdventureProduct() {
	}
	public AdventureProduct(int productID, String name, String productNumber,
			String color, double listPrice, int daysToManufacture,
			Date sellStartDate, Date sellEndDate) {
		this.productID = productID;
		this.name = name;
		this.productNumber = productNumber;
		Color = color;
		this.listPrice = listPrice;
		this.daysToManufacture = daysToManufacture;
		this.sellStartDate = sellStartDate;
		this.sellEndDate = sellEndDate;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public double getListPrice() {
		return listPrice;
	}
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	public int getDaysToManufacture() {
		return daysToManufacture;
	}
	public void setDaysToManufacture(int daysToManufacture) {
		this.daysToManufacture = daysToManufacture;
	}
	public Date getSellStartDate() {
		return sellStartDate;
	}
	public void setSellStartDate(Date sellStartDate) {
		this.sellStartDate = sellStartDate;
	}
	public Date getSellEndDate() {
		return sellEndDate;
	}
	public void setSellEndDate(Date sellEndDate) {
		this.sellEndDate = sellEndDate;
	}
	
}

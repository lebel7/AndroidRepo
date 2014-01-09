package com.lebel.restsample.data;

public class Product {
	private int ProductId;
	private String SupCode;
	private String SuplierCat;
	private String Format;
	private String Artist;
	private String Title;
	private String ShortDescription;
	private String Barcode;
	private int OnHand;
	private String BinNo;
	private float Price1;
	private int OutOfStock;
	
	public Product() {
	}

	public Product(int productId, String supCode, String suplierCat,
			String format, String artist, String title,
			String shortDescription, String barcode, int onHand, String binNo,
			float price1, int outOfStock) {
		ProductId = productId;
		SupCode = supCode;
		SuplierCat = suplierCat;
		Format = format;
		Artist = artist;
		Title = title;
		ShortDescription = shortDescription;
		Barcode = barcode;
		OnHand = onHand;
		BinNo = binNo;
		Price1 = price1;
		OutOfStock = outOfStock;
	}

	public int getProductId() {
		return ProductId;
	}

	public void setProductId(int productId) {
		ProductId = productId;
	}

	public String getSupCode() {
		return SupCode;
	}

	public void setSupCode(String supCode) {
		SupCode = supCode;
	}

	public String getSuplierCat() {
		return SuplierCat;
	}

	public void setSuplierCat(String suplierCat) {
		SuplierCat = suplierCat;
	}

	public String getFormat() {
		return Format;
	}

	public void setFormat(String format) {
		Format = format;
	}

	public String getArtist() {
		return Artist;
	}

	public void setArtist(String artist) {
		Artist = artist;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getShortDescription() {
		return ShortDescription;
	}

	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}

	public String getBarcode() {
		return Barcode;
	}

	public void setBarcode(String barcode) {
		Barcode = barcode;
	}

	public int getOnHand() {
		return OnHand;
	}

	public void setOnHand(int onHand) {
		OnHand = onHand;
	}

	public String getBinNo() {
		return BinNo;
	}

	public void setBinNo(String binNo) {
		BinNo = binNo;
	}

	public float getPrice1() {
		return Price1;
	}

	public void setPrice1(float price1) {
		Price1 = price1;
	}

	public int getOutOfStock() {
		return OutOfStock;
	}

	public void setOutOfStock(int outOfStock) {
		OutOfStock = outOfStock;
	}
	
}

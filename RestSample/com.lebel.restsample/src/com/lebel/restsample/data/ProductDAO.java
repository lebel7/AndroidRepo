package com.lebel.restsample.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sybase.jdbc4.jdbc.SybDataSource;

public class ProductDAO {
	
	protected Connection getConnection() {
		Connection con = null;
		SybDataSource ds = new SybDataSource();
		ds.setUser("dba");
		ds.setPassword("sql");
		//ds.setServerName("pmdtestserver");
		//ds.setPortNumber(2639);
		//ds.setServerName("192.168.10.14");
		ds.setServerName("localhost");
		ds.setPortNumber(2638);
		ds.setDatabaseName("proper3");
		ds.setBE_AS_JDBC_COMPLIANT_AS_POSSIBLE("true");
		ds.setFAKE_METADATA("true");
		
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public Product GetProductByBarcode(String barcode) {
		final String qry = new String("SELECT p.ProductId, p.SuppCode, p.SupplierCat, p.Format, " +
			"p.Artist, p.Title, p.ShortDesc, p.Barcode, p.OnHand, p.BinNo, " +
			"p.Price1, p.OutOfStock FROM Product p WHERE p.Barcode = " + barcode);
		Product prod = new Product();
		Connection dbcon = getConnection();
		
		if (dbcon != null) {
			try {
				Statement stmt = dbcon.createStatement();
				stmt.setCursorName("Cur_Product");
				ResultSet rs = stmt.executeQuery(qry);
				while (rs.next()) {
					prod.setProductId(rs.getInt("ProductId"));
					prod.setSuppCode(rs.getString("SuppCode"));
					prod.setSupplierCat(rs.getString("SupplierCat"));
					prod.setFormat(rs.getString("Format"));
					prod.setArtist(rs.getString("Artist"));
					prod.setTitle(rs.getString("Title"));
					prod.setShortDescription(rs.getString("ShortDesc"));
					prod.setBarcode(rs.getString("Barcode"));
					prod.setOnHand(rs.getInt("OnHand"));
					prod.setBinNo(rs.getString("BinNo"));
					prod.setPrice1(rs.getInt("Price1"));
					prod.setOutOfStock(rs.getInt("OutOfStock"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (!dbcon.isClosed()) {
						dbcon.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prod;
	}
	
	public List<Product> GetTop20Products() {
		final String qry = new String("SELECT TOP 20 p.ProductId, p.SuppCode, p.SupplierCat, p.Format, " +
				"p.Artist, p.Title, p.ShortDesc, p.Barcode, p.OnHand, p.BinNo, " +
				"p.Price1, p.OutOfStock FROM Product p WHERE p.SuppCode = 'PROP' AND " +
				"p.Artist <> '' AND p.Title <> '' AND p.Barcode <> '' AND p.Price1 > 0 " +
				"AND p.OnHand > 0 ORDER BY p.ProductId ASC");
		List<Product> prodList = new ArrayList<Product>();
		Connection dbcon = getConnection();
			
		if (dbcon != null) {
			try {
				Statement stmt = dbcon.createStatement();
				stmt.setCursorName("Cur_Product");
				ResultSet rs = stmt.executeQuery(qry);
				while (rs.next()) {
					Product prod = new Product();
					prod.setProductId(rs.getInt("ProductId"));
					prod.setSuppCode(rs.getString("SuppCode"));
					prod.setSupplierCat(rs.getString("SupplierCat"));
					prod.setFormat(rs.getString("Format"));
					prod.setArtist(rs.getString("Artist"));
					prod.setTitle(rs.getString("Title"));
					prod.setShortDescription(rs.getString("ShortDesc"));
					prod.setBarcode(rs.getString("Barcode"));
					prod.setOnHand(rs.getInt("OnHand"));
					prod.setBinNo(rs.getString("BinNo"));
					prod.setPrice1(rs.getInt("Price1"));
					prod.setOutOfStock(rs.getInt("OutOfStock"));
					prodList.add(prod);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (!dbcon.isClosed()) {
						dbcon.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return prodList;
	}
}

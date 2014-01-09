package com.lebel.restsample.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sybase.jdbc4.jdbc.SybDataSource;

public class AdventureProductDAO {
	protected Connection getConnection() {
		Connection con = null;
		SybDataSource ds = new SybDataSource();
		ds.setUser("dba");
		ds.setPassword("sql");
		ds.setServerName("localhost");
		ds.setPortNumber(2639);
		ds.setDatabaseName("AdventureWorks2008");
		ds.setBE_AS_JDBC_COMPLIANT_AS_POSSIBLE("true");
		ds.setFAKE_METADATA("true");
		
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public AdventureProduct GetProductById(int id) {
		final String qry = new String("SELECT p.ProductID, p.Name, p.ProductNumber, p.Color, " +
			"p.ListPrice, p.DaysToManufacture, p.SellStartDate, p.SellEndDate " +
			"FROM Production.Product p WHERE p.ProductID = " + id);
		AdventureProduct prod = new AdventureProduct();
		Connection dbcon = getConnection();
		
		if (dbcon != null) {
			try {
				Statement stmt = dbcon.createStatement();
				stmt.setCursorName("Cur_Product");
				ResultSet rs = stmt.executeQuery(qry);
				while (rs.next()) {
					prod.setProductID(rs.getInt("ProductID"));
					prod.setName(rs.getString("Name"));
					prod.setProductNumber(rs.getString("ProductNumber"));
					prod.setColor(rs.getString("Color"));
					prod.setListPrice(rs.getDouble("ListPrice"));
					prod.setDaysToManufacture(rs.getInt("DaysToManufacture"));
					prod.setSellStartDate(rs.getDate("SellStartDate"));
					prod.setSellEndDate(rs.getDate("SellEndDate"));
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
	
	public List<AdventureProduct> GetTop20Products() {
		final String qry = new String("SELECT TOP 20 p.ProductID, p.Name, p.ProductNumber, p.Color, " +
			"p.ListPrice, p.DaysToManufacture, p.SellStartDate, p.SellEndDate " +
			"FROM PRoduction.Product p ORDER BY p.ProductID ASC");
		List<AdventureProduct> prodList = new ArrayList<AdventureProduct>();
		Connection dbcon = getConnection();
			
		if (dbcon != null) {
			try {
				Statement stmt = dbcon.createStatement();
				stmt.setCursorName("Cur_Product");
				ResultSet rs = stmt.executeQuery(qry);
				while (rs.next()) {
					AdventureProduct prod = new AdventureProduct();
					prod.setProductID(rs.getInt("ProductID"));
					prod.setName(rs.getString("Name"));
					prod.setProductNumber(rs.getString("ProductNumber"));
					prod.setColor(rs.getString("Color"));
					prod.setListPrice(rs.getDouble("ListPrice"));
					prod.setDaysToManufacture(rs.getInt("DaysToManufacture"));
					prod.setSellStartDate(rs.getDate("SellStartDate"));
					prod.setSellEndDate(rs.getDate("SellEndDate"));
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

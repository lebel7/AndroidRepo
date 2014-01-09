package com.lebel.restsample.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
//import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jamesmurty.utils.XMLBuilder;
import com.lebel.restsample.data.Product;
import com.lebel.restsample.data.ProductDAO;

@Path("v1/product")
public class ProductService {
	ProductDAO proddao = new ProductDAO();
	
	@GET
	@Path("/getProductByIdInJSON/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductByIdInJSON(@PathParam("id") int id) {
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetProductById(id));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/getProductByIdInXML/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String getProductByIdInXML(@PathParam("id") int id) {
		String retvalue = "";
		try {
			Product prod = proddao.GetProductById(id);
			XMLBuilder builder = XMLBuilder.create("Products");
			XMLBuilder productItem = builder.elem("Product");
			productItem.e("ProductId").t(String.format("%s", prod.getProductId()));
			productItem.e("SupCode").t(String.format("%s", prod.getSupCode()));
			productItem.e("SuplierCat").t(String.format("%s", prod.getSuplierCat()));
			productItem.e("Format").t(String.format("%s", prod.getFormat()));
			productItem.e("Artist").t(String.format("%s", prod.getArtist()));
			productItem.e("Title").t(String.format("%s", prod.getTitle()));
			productItem.e("ShortDescription").t(String.format("%s", prod.getShortDescription()));
			productItem.e("Barcode").t(String.format("%s", prod.getBarcode()));
			productItem.e("OnHand").t(String.format("%s", prod.getOnHand()));
			productItem.e("BinNo").t(String.format("%s", prod.getBinNo()));
			productItem.e("Price1").t(String.format("%s", prod.getPrice1()));
			productItem.e("OutOfStock").t(String.format("%s", prod.getOutOfStock()));
			
			Properties outputProperties = new Properties();

			// Explicitly identify the output as an XML document
			outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
			// Pretty-print the XML output (doesn't work in all cases)
			outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
			// Get 2-space indenting when using the Apache transformer
			outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
			// Omit the XML declaration header
			outputProperties.put(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
			retvalue = builder.asString(outputProperties);
			
		} catch (ParserConfigurationException | FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/gettop20InJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTop20ProductInJSON() {
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetTop20Products());
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/gettop20InXML")
	@Produces(MediaType.APPLICATION_XML)
	public String getTop20ProductInXML() {
		String retvalue = "";
		try {
			XMLBuilder builder = XMLBuilder.create("Products");
			List<Product> gotProductList = proddao.GetTop20Products();
			
			for (Product prod : gotProductList) {
				XMLBuilder productItem = builder.elem("Product");
				productItem.e("ProductId").t(String.format("%s", prod.getProductId()));
				productItem.e("SupCode").t(String.format("%s", prod.getSupCode()));
				productItem.e("SuplierCat").t(String.format("%s", prod.getSuplierCat()));
				productItem.e("Format").t(String.format("%s", prod.getFormat()));
				productItem.e("Artist").t(String.format("%s", prod.getArtist()));
				productItem.e("Title").t(String.format("%s", prod.getTitle()));
				productItem.e("ShortDescription").t(String.format("%s", prod.getShortDescription()));
				productItem.e("Barcode").t(String.format("%s", prod.getBarcode()));
				productItem.e("OnHand").t(String.format("%s", prod.getOnHand()));
				productItem.e("BinNo").t(String.format("%s", prod.getBinNo()));
				productItem.e("Price1").t(String.format("%s", prod.getPrice1()));
				productItem.e("OutOfStock").t(String.format("%s", prod.getOutOfStock()));
			}
			Properties outputProperties = new Properties();

			// Explicitly identify the output as an XML document
			outputProperties.put(javax.xml.transform.OutputKeys.METHOD, "xml");
			// Pretty-print the XML output (doesn't work in all cases)
			outputProperties.put(javax.xml.transform.OutputKeys.INDENT, "yes");
			// Get 2-space indenting when using the Apache transformer
			outputProperties.put("{http://xml.apache.org/xslt}indent-amount", "2");
			// Omit the XML declaration header
			outputProperties.put(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
			retvalue = builder.asString(outputProperties);
			 
		} catch (ParserConfigurationException | FactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return retvalue;
	}
}

package com.lebel.restsample.service;

//import java.beans.XMLEncoder;
//import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.jamesmurty.utils.XMLBuilder;
import com.lebel.restsample.data.AdventureProduct;
import com.lebel.restsample.data.AdventureProductDAO;


@Path("v1/adventure")
public class AdventureService {
AdventureProductDAO proddao = new AdventureProductDAO();
	
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
		/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(stream);
		encoder.writeObject(proddao.GetProductById(id));
		encoder.close();
		String xml = stream.toString();
		retvalue = xml;*/
		try {
			AdventureProduct prod = proddao.GetProductById(id);
			XMLBuilder builder = XMLBuilder.create("Products");
			XMLBuilder productItem = builder.elem("Product");
			productItem.e("ProductId").t(String.format("%s", prod.getProductID()));
			productItem.e("Name").t(String.format("%s", prod.getName()));
			productItem.e("ProductNumber").t(String.format("%s", prod.getProductNumber()));
			productItem.e("Color").t(String.format("%s", prod.getColor()));
			productItem.e("ListPrice").t(String.format("%s", prod.getListPrice()));
			productItem.e("DaysToManufacture").t(String.format("%s", prod.getDaysToManufacture()));
			productItem.e("SellStartDate").t(String.format("%s", prod.getSellStartDate()));
			productItem.e("SellEndDate").t(String.format("%s", prod.getSellEndDate()));
			
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
			List<AdventureProduct> gotProductList = proddao.GetTop20Products();
			
			for (AdventureProduct prod : gotProductList) {
				XMLBuilder productItem = builder.elem("Product");
				productItem.e("ProductId").t(String.format("%s", prod.getProductID()));
				productItem.e("Name").t(String.format("%s", prod.getName()));
				productItem.e("ProductNumber").t(String.format("%s", prod.getProductNumber()));
				productItem.e("Color").t(String.format("%s", prod.getColor()));
				productItem.e("ListPrice").t(String.format("%s", prod.getListPrice()));
				productItem.e("DaysToManufacture").t(String.format("%s", prod.getDaysToManufacture()));
				productItem.e("SellStartDate").t(String.format("%s", prod.getSellStartDate()));
				productItem.e("SellEndDate").t(String.format("%s", prod.getSellEndDate()));
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

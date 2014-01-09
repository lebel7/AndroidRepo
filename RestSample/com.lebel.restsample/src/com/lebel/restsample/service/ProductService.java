package com.lebel.restsample.service;

import java.io.IOException;
//import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

//import com.lebel.restsample.data.Product;
import com.lebel.restsample.data.ProductDAO;

@Path("v1/product")
public class ProductService {
	ProductDAO proddao = new ProductDAO();
	
	@GET
	@Path("/getProductByIdInJSON/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProductByIdInJSON(@PathParam("id") int id) {
		//return proddao.GetProductById(id);
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetProductById(id));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/getProductByIdInXML/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public String getProductByIdInXML(@PathParam("id") int id) {
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetProductById(id));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/gettop20InJSON")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTop20ProductInJSON() {
		//return proddao.GetTop20Products();
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetTop20Products());
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retvalue;
	}
	
	@GET
	@Path("/gettop20InXML")
	@Produces(MediaType.APPLICATION_XML)
	public String getTop20ProductInXML() {
		//return proddao.GetTop20Products();
		String retvalue = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			retvalue = mapper.writeValueAsString(proddao.GetTop20Products());
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retvalue;
	}
}

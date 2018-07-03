package com.waveSocks.tests;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.Response;
import com.qmetry.qaf.automation.ws.rest.RestWSTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.waveSocks.bean.UserRegisterBean;

public class ApiTest extends RestWSTestCase{

	@Test(priority=0)
	public void registerUser(){
		UserRegisterBean userRegisterBean = new UserRegisterBean();
		userRegisterBean.fillRandomData();
		
		getWebResource("/register").header("Content-Type", "application/json").post(new Gson().toJson(userRegisterBean) );
		
		Response response = getResponse();
		System.out.println(response);
		
		JSONObject jsonObject = new JSONObject(response.getMessageBody());
		ConfigurationManager.getBundle().setProperty("user.id", jsonObject.getString("id"));
		
		Validator.verifyThat(jsonObject.getString("id").length(), Matchers.greaterThan(10));
	}
	
	@Test(priority=1)
	public void addItemToCart(){
		System.out.println("user id:  "+ConfigurationManager.getBundle().getProperty("user.id").toString());
		Response response = getResponse();
		System.out.println("add Item To Cart res: "+response.getMessageBody());
		
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		//object.addProperty("id", "3395a43e-2d88-40de-b95f-e00e1502085b");
		
		getWebResource("/cart").type(MediaType.APPLICATION_JSON).post(object.toString());
		
		Response response1 = getResponse();
		System.out.println("addItemToCart res: "+response1.getStatus().getStatusCode());
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
		
	}
	
	@Test(priority=2)
	public void verifyCart(){
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response3 = getResponse();
		System.out.println("verifyCart res status code: "+response3.getStatus().getStatusCode());
		System.out.println("verifyCart res body: "+response3.getMessageBody());
		
		JsonArray responseArray = new JsonParser().parse(response3.getMessageBody()).getAsJsonArray();
		System.out.println("verifyCart items in cart: "+responseArray.get(0).getAsJsonObject().get("quantity"));
		int quantity = responseArray.get(0).getAsJsonObject().get("quantity").getAsInt();
		Validator.verifyThat(quantity, Matchers.greaterThan(0));
	}
	
	@Test(priority=3)
	public void updateCart(){
		System.out.println("updateCart user id:  "+ConfigurationManager.getBundle().getProperty("user.id").toString());
		
		Map<String, String> map = new HashMap<String, String>();
		String userId = ConfigurationManager.getBundle().getProperty("user.id").toString();
		map.put("userID", userId );
		map.put("quantity", "5");
		map.put("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		
		getWebResource("/cart/update").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, new Gson().toJson(map));
		
		Response response1 = getResponse();
		System.out.println("updateCart res status code : "+response1.getStatus().getStatusCode());
		
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
		
	}
	
	@Test(priority=4)
	public void verifyUpdatedCart(){
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		
		Response response3 = getResponse();
		
		System.out.println("verifyUpdatedCart res status code: "+response3.getStatus().getStatusCode());
		System.out.println("verifyUpdatedCart res body: "+response3.getMessageBody());
		
		JsonArray responseArray = new JsonParser().parse(response3.getMessageBody()).getAsJsonArray();
		System.out.println("verifyUpdatedCart items in cart: "+responseArray.get(0).getAsJsonObject().get("quantity"));
		
		int quantity = responseArray.get(0).getAsJsonObject().get("quantity").getAsInt();
		
		Validator.verifyThat(quantity, Matchers.greaterThan(0));
	}
	
	@Test(priority=5)
	public void addSecondItemToCart(){
		System.out.println("user id:  "+ConfigurationManager.getBundle().getProperty("user.id").toString());
		Response response = getResponse();
		System.out.println("add Item To Cart res: "+response.getMessageBody());
		
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		//object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		object.addProperty("id", "3395a43e-2d88-40de-b95f-e00e1502085b");
		
		getWebResource("/cart").type(MediaType.APPLICATION_JSON).post(object.toString());
		
		Response response1 = getResponse();
		System.out.println("addItemToCart res: "+response1.getStatus().getStatusCode());
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
		
	}
	
	@Test(priority=6)
	public void verifyItemsInCart(){
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response3 = getResponse();
		System.out.println("verifyCart res status code: "+response3.getStatus().getStatusCode());
		System.out.println("verifyCart res body: "+response3.getMessageBody());
		
		JsonArray responseArray = new JsonParser().parse(response3.getMessageBody()).getAsJsonArray();
		System.out.println("verifyCart items in cart: "+responseArray.size());
		int items = responseArray.size();
		Validator.verifyThat(items, Matchers.greaterThan(0));
	}

	@Test(priority=7)
	public void deleteItemFromCart(){
		System.out.println("user id:  "+ConfigurationManager.getBundle().getProperty("user.id").toString());
		
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		//object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		object.addProperty("id", "3395a43e-2d88-40de-b95f-e00e1502085b");
		
		getWebResource("/cart").type(MediaType.APPLICATION_JSON).delete(object.toString());
		
		Response response1 = getResponse();
		System.out.println("deleteItemFromCart res: "+response1.getStatus().getStatusCode());
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
		
	}
	
	@Test(priority=8)
	public void verifyItemsInCartAfterDelete(){
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response3 = getResponse();
		System.out.println("verifyCart res status code: "+response3.getStatus().getStatusCode());
		System.out.println("verifyCart res body: "+response3.getMessageBody());
		
		
	}
	
	@Test(priority=9)
	public void addItemToCartAfterDelete(){
		System.out.println("user id:  "+ConfigurationManager.getBundle().getProperty("user.id").toString());
		
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		//object.addProperty("id", "3395a43e-2d88-40de-b95f-e00e1502085b");
		
		getWebResource("/cart").type(MediaType.APPLICATION_JSON).post(object.toString());
		
		Response response1 = getResponse();
		System.out.println("addItemToCartAfterDelete res: "+response1.getStatus().getStatusCode());
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
		
	}
	
	@Test(priority=10)
	public void verifyCartAfterDelete(){
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response3 = getResponse();
		System.out.println("verifyCartAfterDelete res status code: "+response3.getStatus().getStatusCode());
		System.out.println("verifyCartAfterDelete res body: "+response3.getMessageBody());
		
		JsonArray responseArray = new JsonParser().parse(response3.getMessageBody()).getAsJsonArray();
		System.out.println("verifyCartAfterDelete items in cart: "+responseArray.get(0).getAsJsonObject().get("quantity"));
		int quantity = responseArray.get(0).getAsJsonObject().get("quantity").getAsInt();
		Validator.verifyThat(quantity, Matchers.greaterThan(0));
	}
}

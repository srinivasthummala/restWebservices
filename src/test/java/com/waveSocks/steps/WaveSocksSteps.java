package com.waveSocks.steps;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.StatusType;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.openqa.selenium.internal.Base64Encoder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.Response;
import com.qmetry.qaf.automation.ws.rest.RestWSTestCase;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.waveSocks.bean.AddressBean;
import com.waveSocks.bean.PaymentBean;
import com.waveSocks.bean.UserRegisterBean;

public class WaveSocksSteps extends RestWSTestCase {

	@QAFTestStep(description = "customer register as new user")
	public void customerRegisterAsNewUser() {
		UserRegisterBean userRegisterBean = new UserRegisterBean();
		userRegisterBean.fillRandomData();
		getWebResource("/register").header("Content-Type", "application/json")
				.post(new Gson().toJson(userRegisterBean));
		Response response = getResponse();
		Validator.verifyThat("Verifying succes status code 200: ", response.getStatus().getStatusCode(),
				Matchers.equalTo(200));

		JSONObject jsonObject = new JSONObject(response.getMessageBody());
		ConfigurationManager.getBundle().setProperty("user.id", jsonObject.getString("id"));
		Validator.verifyThat("verifying ID presence : ", jsonObject.getString("id").length(), Matchers.greaterThan(10));
	}

	@QAFTestStep(description = "adds an item into cart")
	public void addsAnItemIntoCart() {
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");

		getWebResource("/cart").type(MediaType.APPLICATION_JSON).post(object.toString());
		Response response = getResponse();
		Validator.verifyThat("Verifying creation status code 201 : ", response.getStatus().getStatusCode(),
				Matchers.equalTo(201));
	}

	@QAFTestStep(description = "verify item presence in cart")
	public void verifyItemPresenceInCart() {
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat("Verifying succes status code 200: ", response.getStatus().getStatusCode(),
				Matchers.equalTo(200));
		JsonArray responseArray = new JsonParser().parse(response.getMessageBody()).getAsJsonArray();
		Validator.verifyThat(responseArray.size(), Matchers.notNullValue());
		Validator.verifyThat(responseArray.size(), Matchers.greaterThan(0));
		int quantity = responseArray.get(0).getAsJsonObject().get("quantity").getAsInt();
		Validator.verifyThat(quantity, Matchers.greaterThan(0));
	}

	@QAFTestStep(description = "update the cart quantity by {0} for existing product")
	public void updateTheCartQuantityByForExistingProduct(String quantity) {
		ConfigurationManager.getBundle().setProperty("item.quantity", quantity);
		Map<String, String> map = new HashMap<String, String>();
		String userId = ConfigurationManager.getBundle().getProperty("user.id").toString();
		map.put("userID", userId);
		map.put("quantity", quantity);
		map.put("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");
		getWebResource("/cart/update").type(MediaType.APPLICATION_JSON).post(ClientResponse.class,
				new Gson().toJson(map));
		Response response = getResponse();
		Validator.verifyThat("Verifying accept status code 202: ", response.getStatus().getStatusCode(),
				Matchers.equalTo(Status.ACCEPTED.getStatusCode()));
	}

	@QAFTestStep(description = "verify quantity of an item in cart")
	public void verifyQuantityOfAnItemInCart() {
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat("Verifying succes status code 200: ", response.getStatus().getStatusCode(),
				Matchers.equalTo(200));
		JsonArray responseArray = new JsonParser().parse(response.getMessageBody()).getAsJsonArray();
		Validator.verifyThat(responseArray.size(), Matchers.greaterThan(0));
		String expectedQuantity = ConfigurationManager.getBundle().getProperty("item.quantity").toString();
		String actualQuantity = responseArray.get(0).getAsJsonObject().get("quantity").getAsString();
		Validator.verifyThat(actualQuantity, Matchers.equalTo(expectedQuantity));
	}

	@QAFTestStep(description = "delete product from cart")
	public void deleteProductFromCart() {
		JsonObject object = new JsonObject();
		object.addProperty("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		object.addProperty("id", "510a0d7e-8e83-4193-b483-e27e09ddc34d");

		getWebResource("/cart").type(MediaType.APPLICATION_JSON).delete(object.toString());

		Response response1 = getResponse();
		Validator.verifyThat(response1.getStatus().getStatusCode(), Matchers.greaterThan(200));
	}

	@QAFTestStep(description = "cart should be empty")
	public void verifyCartEmty() {
		getWebResource("/cart").header("Content-Type", "application/json").get(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(200));
		JsonArray responseArray = new JsonParser().parse(response.getMessageBody()).getAsJsonArray();
		responseArray.isJsonArray();
		Validator.verifyThat(responseArray.size(), Matchers.equalTo(0));
	}

	@QAFTestStep(description = "add shipping address")
	public void addShippingAddress() {
		AddressBean addressBean = new AddressBean();
		addressBean.fillRandomData();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		jsonObject.put("number", "12345678");
		jsonObject.put("country", "india");
		jsonObject.put("city", "pune");
		jsonObject.put("street", "hinjawadi");
		jsonObject.put("postcode", "12324");
		getWebResource("/addresses").type(MediaType.APPLICATION_JSON).post(jsonObject.toString());
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(200));

	}

	@QAFTestStep(description = "verify addres added")
	public void verifyAddresAdded() {
		getWebResource("/addresses").type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Response response = getResponse();
		JsonObject responseBody = new JsonParser().parse(response.getMessageBody()).getAsJsonObject();
		JsonObject embeddedObject = responseBody.get("_embedded").getAsJsonObject();
		JsonArray addressArray = embeddedObject.get("address").getAsJsonArray();
		if (addressArray.size() > 0) {
			Validator.verifyThat(addressArray.get(0).getAsJsonObject().get("street").getAsString(),
					Matchers.notNullValue());
			Validator.verifyThat(addressArray.get(0).getAsJsonObject().get("country").getAsString(),
					Matchers.notNullValue());
			Validator.verifyThat(addressArray.get(0).getAsJsonObject().get("city").getAsString(),
					Matchers.notNullValue());
			Validator.verifyThat(addressArray.get(0).getAsJsonObject().get("postcode").getAsString(),
					Matchers.notNullValue());
		}

	}

	@QAFTestStep(description = "add payment details")
	public void addPaymentDetails() {
		Reporter.log(ConfigurationManager.getBundle().getProperty("user.id").toString());
		PaymentBean paymentBean = new PaymentBean();
		paymentBean.fillRandomData();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userID", ConfigurationManager.getBundle().getProperty("user.id").toString());
		jsonObject.put("longNum", "1234567890987654");
		jsonObject.put("expiries", "12/27");
		jsonObject.put("ccv", "123");

		getWebResource("/cards").type(MediaType.APPLICATION_JSON).post(jsonObject.toString());
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(200));

		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(200));
		JsonObject responseBody = new JsonParser().parse(response.getMessageBody()).getAsJsonObject();
		Reporter.log(responseBody.toString());
		String id = responseBody.get("id").getAsString();
		Validator.verifyThat(id.length(), Matchers.greaterThan(10));

	}

	@QAFTestStep(description = "place order")
	public void placeOrder() {
		getWebResource("/orders").type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(201));
		JsonObject jsonObject = new JsonParser().parse(response.getMessageBody()).getAsJsonObject();
		Reporter.log(jsonObject.toString());
	}

	@QAFTestStep(description = "verifies ordered placed")
	public void verifiesOrderedPlaced() {
		getWebResource("/orders").type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(201));
		JsonArray jsonArray = new JsonParser().parse(response.getMessageBody()).getAsJsonArray();
		Reporter.log(jsonArray.toString());
	}

	@QAFTestStep(description = "customer login with valid credentials")
	public void customerLoginWithValidCredentials() {
		String username = "srinu";
		String password = "123abc";
		String authString  = username + ":" + password;
		
		String auth = "{\"username\":\"srinu\",\"password\":\"123abc\"}";
		String authStringEnc = new Base64Encoder().encode(authString.getBytes());
		getWebResource("/login").header("Authorization", "Basic " + authStringEnc).type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		//getWebResource("/login").accept(MediaType.APPLICATION_JSON).entity(auth).post(ClientResponse.class);
		Response response = getResponse();
		Validator.verifyThat(response.getStatus().getStatusCode(), Matchers.equalTo(Status.OK.getStatusCode()));
		Reporter.log(response.getMessageBody().toString());
	}
}

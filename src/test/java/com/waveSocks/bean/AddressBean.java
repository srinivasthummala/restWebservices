package com.waveSocks.bean;

import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;
import com.qmetry.qaf.automation.util.Randomizer;

public class AddressBean extends BaseDataBean{
	@Randomizer
	private String number;
	
	@Randomizer
	private String country;
	
	@Randomizer
	private String city;
	
	@Randomizer(length=6, type=RandomizerTypes.DIGITS_ONLY)
	private String postcode;
	
	@Randomizer
	private String userID;
	
	@Randomizer
	private String street;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getUserid() {
		return userID;
	}

	public void setUserid(String userid) {
		this.userID = userid;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}

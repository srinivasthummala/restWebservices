package com.waveSocks.bean;

import com.qmetry.qaf.automation.data.BaseDataBean;
import com.qmetry.qaf.automation.util.RandomStringGenerator.RandomizerTypes;
import com.qmetry.qaf.automation.util.Randomizer;

public class PaymentBean extends BaseDataBean{
	@Randomizer(type=RandomizerTypes.DIGITS_ONLY)
	private String longNum;
	
	@Randomizer(type=RandomizerTypes.DIGITS_ONLY, format="mm/yy")
	private String expiries;
	
	@Randomizer(type=RandomizerTypes.DIGITS_ONLY, length=3)
	private String ccv;
	
	private String userID;

	public String getLongNumber() {
		return longNum;
	}

	public void setLongNumber(String longNumber) {
		this.longNum = longNumber;
	}

	public String getExpiries() {
		return expiries;
	}

	public void setExpiries(String expiry) {
		this.expiries = expiry;
	}

	public String getCcv() {
		return ccv;
	}

	public void setCcv(String ccv) {
		this.ccv = ccv;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
}

package com.waveSocks.tests;

import com.qmetry.qaf.automation.ws.rest.DefaultRestClient;
import com.sun.jersey.api.client.Client;

public class CookieEnabledClient extends DefaultRestClient{
	@Override
	protected Client createClient() {
		Client client=super.createClient();
		client.getProperties().put("jersey.config.client.followRedirects", true);
		client.addFilter(new SessionManagementFilter());
		return client;
	}
}
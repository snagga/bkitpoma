package com.bkitmobile.poma.openid.client.provider;

public class GoogleProvider extends Provider {
	public GoogleProvider() {
		setLogo("images/openid/googleW.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Google";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "https://www.google.com/accounts/o8/id";
	}

}

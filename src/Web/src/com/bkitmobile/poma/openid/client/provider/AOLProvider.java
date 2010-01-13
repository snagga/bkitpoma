package com.bkitmobile.poma.openid.client.provider;

public class AOLProvider extends Provider{

	public AOLProvider() {
		setLogo("images/openid/aolW.png");
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AOL";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://openid.aol.com/"+USER_NAME;
	}
}

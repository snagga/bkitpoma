package com.bkitmobile.poma.openid.client.provider;

public class WordpressProvider extends Provider{

	public WordpressProvider() {
		setLogo("images/openid/wordpress.png");
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Wordpress";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://"+USER_NAME+".wordpress.com";
	}

}

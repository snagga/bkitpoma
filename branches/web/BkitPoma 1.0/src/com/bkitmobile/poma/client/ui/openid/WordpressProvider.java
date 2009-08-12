package com.bkitmobile.poma.client.ui.openid;

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
		return "http://username.wordpress.com";
	}

}

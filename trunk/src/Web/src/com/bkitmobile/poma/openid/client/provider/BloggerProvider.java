package com.bkitmobile.poma.openid.client.provider;

public class BloggerProvider extends Provider{

	public BloggerProvider() {
		// TODO Auto-generated constructor stub
		setLogo("images/openid/blogger.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Blogger";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://"+USER_NAME+".blogspot.com";
	}

}

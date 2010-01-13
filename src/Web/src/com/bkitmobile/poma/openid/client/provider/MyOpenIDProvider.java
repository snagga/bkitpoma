package com.bkitmobile.poma.openid.client.provider;

public class MyOpenIDProvider extends Provider{

	public MyOpenIDProvider() {
		setLogo("images/openid/myopenidW.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "MyOpenID";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://"+USER_NAME+".myopenid.com/";
	}

}

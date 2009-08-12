package com.bkitmobile.poma.client.ui.openid;

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
		return "http://username.myopenid.com/";
	}

}

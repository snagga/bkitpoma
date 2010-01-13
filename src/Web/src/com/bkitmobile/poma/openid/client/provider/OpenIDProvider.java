package com.bkitmobile.poma.openid.client.provider;

public class OpenIDProvider extends Provider {
	
	public OpenIDProvider() {
		setLogo("images/openid/openidico.gif");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "OpenID";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return USER_NAME;
	}
}

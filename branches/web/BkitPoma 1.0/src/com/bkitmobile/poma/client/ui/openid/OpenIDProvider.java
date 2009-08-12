package com.bkitmobile.poma.client.ui.openid;

public class OpenIDProvider extends Provider {
	
	public OpenIDProvider() {
		setLogo("images/openid/openidico.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "OpenID";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "username";
	}
}

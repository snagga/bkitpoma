package com.bkitmobile.poma.openid.client.provider;

public class VidoopProvider extends Provider{

	public VidoopProvider() {
		setLogo("images/openid/vidoop.png");
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Vidoop";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://"+USER_NAME+".myvidoop.com/";
	}

}

package com.bkitmobile.poma.client.ui.openid;

public class VerisignProvider extends Provider{

	public VerisignProvider() {
		setLogo("images/openid/verisign.png");
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Verisign";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://username.pip.verisignlabs.com/";
	}

}

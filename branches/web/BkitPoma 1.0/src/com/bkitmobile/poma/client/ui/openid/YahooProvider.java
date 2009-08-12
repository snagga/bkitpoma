package com.bkitmobile.poma.client.ui.openid;

public class YahooProvider extends Provider{

	public YahooProvider() {
		setLogo("images/openid/yahooW.png");
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Yahoo";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "https://me.yahoo.com/username";
	}

}

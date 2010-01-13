package com.bkitmobile.poma.openid.client.provider;

public class FlickrProvider extends Provider {

	public FlickrProvider() {
		setLogo("images/openid/flickrW.png");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Flickr";
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return "http://flickr.com/" + USER_NAME;
	}

}

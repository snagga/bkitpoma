package com.bkitmobile.poma.client.ui.openid;

import com.bkitmobile.poma.client.StringUtils;
import com.google.gwt.user.client.ui.Image;

public abstract class Provider {
	private Image img;
	
	public Image getLogo() {
		return img;
	}
	public void setLogo(Image img) {
		this.img = img;
	}
	public void setLogo(String uri) {
		this.img = new Image(uri);
	}
	
	public static final String USER_NAME = "username";
	public abstract String getName();
	public abstract String getURL();
	
	public String getURL(String username) {
		return StringUtils.strReplace(getURL(), USER_NAME, username);
	}
	
	public String getFirstURL() {
		return getURL().substring(0, getURL().indexOf(USER_NAME));
	}
	
	public String getLastURL() {
		return getURL().substring(getURL().indexOf(USER_NAME)+USER_NAME.length());
	}
}

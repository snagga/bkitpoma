package com.bkitmobile.poma.openid.client.provider;

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
	
	public static final String USER_NAME = "__USERNAME__}";
	public abstract String getName();
	public abstract String getURL();
	
	public boolean requireUsernameInUrl() {
		return getURL().indexOf(USER_NAME) != -1;
	}
	
	public String getURL(String username) {
		return getURL().replaceAll(USER_NAME, username);
	}
	
	public String getFirstURL() {
		if (!requireUsernameInUrl()) return getURL();
		return getURL().substring(0, getURL().indexOf(USER_NAME));
	}
	
	public String getLastURL() {
		if (!requireUsernameInUrl()) return "";
		return getURL().substring(getURL().indexOf(USER_NAME)+USER_NAME.length());
	}
}

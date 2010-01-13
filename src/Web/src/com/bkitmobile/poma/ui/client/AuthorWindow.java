package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.localization.client.AuthorWindowConstants;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class AuthorWindow extends Window {
	
	private static AuthorWindow authorWindow = null;
	private AuthorWindowConstants constants = GWT.create(AuthorWindowConstants.class);
	
	private int windowWidth = 350;
	public static AuthorWindow getInstance() {
		if (authorWindow != null) 
			return authorWindow;
		else
			return new AuthorWindow();
	}
	
	private AuthorWindow() {
		super();
		authorWindow = this;
		
		setTitle(constants.author());
		setWidth(windowWidth);
		setAutoHeight(true);
		setLayout(new VerticalLayout(0));
		setCloseAction(Window.HIDE);
		setResizable(false);
		setHtml("<img alt='BkitPoma logo' title='BkitPoma logo' src='images/poma/logo-medium.png' />" +
				"<img src='images/poma/couples.gif' height=90 width=152 alt='Couples'><br /><br /><br />" +
				"Under Apache 2.0 license <br />" +
				"<a href = 'http://bkitclub.net/bkitmobile' style='text-decoration: none'>&copy by BkitMobile group </a><br />" +
				"Project homepage: <a href='http://code.google.com/p/bkitpoma' target='_blank' >http://code.google.com/p/bkitpoma</a><br />" +
				"Powered by:<br /> " +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - <a style='text-decoration: none; color:green' href='http://code.google.com'>Google Code </a><br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - <a style='text-decoration: none; color:green' href='http://code.google.com/appengine/'>Google App Engine </a><br/>" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - <a style='text-decoration: none; color:green' href='http://code.google.com/webtoolkit/'>Google Web Toolkit </a>");
	}
}

package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.localization.client.IntroductionWindowConstants;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.form.TextArea;

public class IntroductionWindow extends Window {
	
	private static IntroductionWindow window = null;
	
	public static IntroductionWindow getInstance() {
		return window == null ? new IntroductionWindow() : window;
	}
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 200;
	private IntroductionWindowConstants constants = GWT.create(IntroductionWindowConstants.class);
	
	private IntroductionWindow() {
		setAutoScroll(true);
		setClosable(true);
		setCloseAction(Window.HIDE);
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setTitle(constants.title());
		
		setHtml("<div style='font-weight: bold; color: blue'>" + constants.content() + "</div>");
	}
}

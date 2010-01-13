package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.captcha.client.RecaptchaService;
import com.bkitmobile.poma.captcha.client.RecaptchaWidget;
import com.bkitmobile.poma.localization.client.CaptchaWindowConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class CaptchaWindow extends Window {

	private RecaptchaWidget wgCaptcha;
	private static boolean firstShow = true;
	private static CaptchaWindow captchaWindow = null;
	private CaptchaWindowConstants constants = GWT.create(CaptchaWindowConstants.class);

	// localization

	/**
	 * @return static instance of this captcha window
	 */
	public static CaptchaWindow getInstance() {
		if (captchaWindow != null)
			return captchaWindow;
		return new CaptchaWindow();
	}

	/**
	 * Constructor
	 */
	private CaptchaWindow() {
		captchaWindow = this;
		/*
		 * init
		 */
		setWidth(350);
		setTitle(constants.captchaTitle());
		setButtonAlign(Position.CENTER);
		setCloseAction(Window.HIDE);
		setDraggable(false);
		setResizable(false);

		/*
		 * Captcha
		 */
		wgCaptcha = new RecaptchaWidget(
				"6LdakQcAAAAAALX2JUFtsjbPTV0TcAkMhQY8iMkS");
		add(wgCaptcha);
		
		/*
		 * Validate button
		 */
		addButton(new Button(constants.captchaButton(),
				new ButtonListenerAdapter() {

					@Override
					public void onClick(Button button, EventObject e) {
						RecaptchaService.Util.getInstance().verifyChallenge(
								wgCaptcha.getChallenge(),
								wgCaptcha.getResponse(),
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
										MessageBox.alert(constants.captchaFailure(), caught
												.toString());
										wgCaptcha.reload();
									}

									@Override
									public void onSuccess(Boolean result) {
										if (result) {
											hide();
											if (function != null)
												function.execute();
										} else {
											MessageBox.alert(constants.captchaError());
											wgCaptcha.reload();
										}
									}
								});
					}
				}));
		
	}

	/**
	 * override function show
	 */
	public void show(Function function) {
		this.function = function;
		
		if (firstShow) {
			CaptchaWindow.super.show();
			firstShow = false;
		} else {
			wgCaptcha.reload();
			CaptchaWindow.super.show();
		}
	}

	private Function function;
}

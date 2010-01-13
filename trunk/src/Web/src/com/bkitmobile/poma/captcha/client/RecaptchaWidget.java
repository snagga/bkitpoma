package com.bkitmobile.poma.captcha.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;

public class RecaptchaWidget extends Composite {
	protected static final String RECAPTCHA_DIV_HTML = "<div id=\"recaptcha_div\"/>";
	protected static final String RECAPTCHA_DIV_ID = "recaptcha_div";

	private HTML html;
	private String key;

	public RecaptchaWidget(String key) {
		this.key = key;
		html = new HTML(RECAPTCHA_DIV_HTML);
		initWidget(html);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		create();
	}

	@Override
	protected void onDetach() {
		destroy();
		super.onDetach();
	}

	protected void create() {
		Recaptcha.create(key, RECAPTCHA_DIV_ID);
	}

	protected void destroy() {
		Recaptcha.destroy();
	}

	public void reload() {
		Recaptcha.reload();
	}

	public String getChallenge() {
		return Recaptcha.getChallenge();
	}

	public String getResponse() {
		return Recaptcha.getResponse();
	}

	public void focusResponseField() {
		Recaptcha.focusResponseField();
	}

	public void showHelp() {
		Recaptcha.showHelp();
	}

	public void switchType(String newType) {
		Recaptcha.switchType(newType);
	}
}

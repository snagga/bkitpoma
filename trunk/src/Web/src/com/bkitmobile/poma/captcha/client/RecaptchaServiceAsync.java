package com.bkitmobile.poma.captcha.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RecaptchaServiceAsync {
	
	public void verifyChallenge(String challenge, String response, AsyncCallback<Boolean> callback);

}

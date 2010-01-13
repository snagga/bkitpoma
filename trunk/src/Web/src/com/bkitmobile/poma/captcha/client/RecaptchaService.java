package com.bkitmobile.poma.captcha.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../bkitpoma/recaptcha")
public interface RecaptchaService extends RemoteService {
	
	public boolean verifyChallenge(String challenge, String response);

	public static class Util {

		public static RecaptchaServiceAsync getInstance() {

			return GWT.create(RecaptchaService.class);
		}
	}
}

package com.poma.bkitpoma.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("recaptcha")
public interface RecaptchaService extends RemoteService {
	
	public boolean verifyChallenge(String challenge, String response);

	public static class Util {

		public static RecaptchaServiceAsync getInstance() {

			return GWT.create(RecaptchaService.class);
		}
	}
}

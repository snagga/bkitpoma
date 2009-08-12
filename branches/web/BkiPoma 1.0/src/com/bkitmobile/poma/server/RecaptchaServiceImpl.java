package com.bkitmobile.poma.server;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RecaptchaServiceImpl extends RemoteServiceServlet implements
		com.bkitmobile.poma.client.captcha.RecaptchaService {

	private static final long serialVersionUID = -3280477565969758716L;

	public boolean verifyChallenge(String challenge, String response) {
		ReCaptcha r = ReCaptchaFactory.newReCaptcha("6LdakQcAAAAAALX2JUFtsjbPTV0TcAkMhQY8iMkS",
				"6LdakQcAAAAAAECnOGDzDEzIhuxrmx18t-NFgYBv", true);
		return r.checkAnswer(
				getThreadLocalRequest().getRemoteAddr().toString(), challenge,
				response).isValid();
	}

}

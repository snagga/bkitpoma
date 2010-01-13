package com.bkitmobile.poma.mail.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MailServiceAsync {
//	public void sendEmailToAdmin(String sender, String title, String content,
//			AsyncCallback<Boolean> callBack);
//
//	void sendEmail(String from, String to, String title, String content,
//			AsyncCallback<Boolean> callback);

	void sendEmailToAdmin1(String sender, String title, String content,
			AsyncCallback<Boolean> callback);

	void sendEmail1(String from, String to, String title, String content,
			AsyncCallback<Boolean> callback);

}

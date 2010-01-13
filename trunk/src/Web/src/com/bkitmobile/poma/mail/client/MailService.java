package com.bkitmobile.poma.mail.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../bkitpoma/mail")
public interface MailService extends RemoteService{

	Boolean sendEmailToAdmin1(String sender, String title, String content);
	Boolean sendEmail1(String from, String to, String title,
			String content);
}

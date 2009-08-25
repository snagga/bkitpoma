package com.bkitmobile.poma.client.mail;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("mailtoadmin")
public interface MailService extends RemoteService{

	Boolean sendEmailToAdmin(String sender, String title, String content);
	Boolean sendEmail(String from, String to, String title, String content);
	
}

package com.bkitmobile.poma.server.mail;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MailServiceImpl extends RemoteServiceServlet implements
		com.bkitmobile.poma.client.mail.MailService {

	@Override
	public Boolean sendEmailToAdmin(String sender, String title, String content) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"admins"));
			msg.setSubject(title);
			msg.setText(content);
			Transport.send(msg);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	@Override
	public Boolean sendEmail(String from, String to, String title,
			String content) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			msg.setSubject(title);
			msg.setText(content);
			Transport.send(msg);
		} catch (Exception ex) {
			return false;
		}

		return true;
	}
}

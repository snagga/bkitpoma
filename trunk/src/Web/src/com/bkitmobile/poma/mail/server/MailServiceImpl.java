package com.bkitmobile.poma.mail.server;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CAdminConfig;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MailServiceImpl extends RemoteServiceServlet implements
		com.bkitmobile.poma.mail.client.MailService {

	private static final Logger logger = Logger.getLogger(MailServiceImpl.class
			.getName());

	private DatabaseServiceImpl db = new DatabaseServiceImpl();

	@Override
	public Boolean sendEmailToAdmin1(String sender, String title, String content) {
		ServiceResult<CAdminConfig> tmp = db.getRecord("mail");
		String adminMail = tmp.getResult().getValue() == null ? "admins" : tmp.getResult().getValue();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		
		content = "Sender: " + sender + "\n\n" + content;

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("vo.mita.ov@gmail.com")); //This is terrible thing
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					adminMail));
			
			msg.setSubject(title, "UTF-8");
			msg.setText(content, "UTF-8");
			msg.setHeader("Content-Type", "text/plain; charset=UTF-8");

//			Window.open(content, "_blank", null); // test
			Transport.send(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(Level.SEVERE, "Java Mail to admin fail", ex);
			return false;
		}
		logger.log(Level.INFO, "Send Mail admin success");

		return true;
	}

	@Override
	public Boolean sendEmail1(String from, String to, String title,
			String content) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			msg.setSubject(title, "UTF-8");
			msg.setText(content, "UTF-8");
			msg.setHeader("Content-Type", "text/plain; charset=UTF-8");

//			Window.open(content, "_blank", null); // test
			Transport.send(msg);
		} catch (Exception ex) {
			ex.printStackTrace();

			logger.log(Level.SEVERE, "Java Mail to admin fail", ex);
			return false;
		}
		logger.log(Level.INFO, "Send Mail success");
		return true;
	}

}

package com.bkitmobile.poma.server;

import org.mortbay.log.Log;

import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.mail.TrackerService;
import com.bkitmobile.poma.server.database.DatabaseServiceImpl;
import com.bkitmobile.poma.server.localization.EmailLocalizationMessages;
import com.bkitmobile.poma.server.mail.MailServiceImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.client.Window.Location;

public class TrackerServiceImpl extends RemoteServiceServlet implements
		TrackerService {

	private EmailLocalizationMessages messages = GWT
			.create(EmailLocalizationMessages.class);
	private MailServiceImpl mailServiceImpl = new MailServiceImpl();
	private DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl();
	@Override
	public Boolean validateEmailForgotPassword(String trackerUN) {
		
		ServiceResult<CTracker> result = databaseServiceImpl
				.getTracker(trackerUN);
		CTracker cTracker = result.getResult();
		// String hostName = "http://bkitpoma.appspot.com/";
		String hostName = Location.getHost();
		System.out.println("host: " + hostName);

		String url= 
				hostName
				+ "?trackerUN="
				+ trackerUN
				+ "&md53Pass="
				+ DatabaseServiceImpl.md5Cool(DatabaseServiceImpl
						.md5Cool(cTracker.getPassword()));
		System.out.println("URL: " + url);

		boolean b = mailServiceImpl.sendEmail(
				"bkitmobile@googlegroups.com", result.getResult().getEmail(),
				messages.title_verify_email_forgot_password(), messages.content_verify_email_forgot_password(trackerUN, url));
		if (b) {
			Log.info("Send email to " + trackerUN + " with email "
					+ cTracker.getEmail() + "successful !");
		} else {
			Log.info("Can not send email to " + trackerUN + " with email "
					+ cTracker.getEmail() + "!");
		}
		return b;
	}

	@Override
	public Boolean validateEmailNewTracker(CTracker cTracker) {
		String name = (cTracker.getName().equals("")) ? cTracker
				.getUsername() : cTracker.getName();
		String url = "Link to Servlet";
		boolean b = mailServiceImpl.sendEmail(
				"bkitmobile@googlegroups.com", cTracker.getEmail(),
				messages.title_verify_email_new_tracker(), messages.content_verify_email_new_tracker(name, cTracker.getUsername(), url));
		if (b) {
			Log.info("Send email to " + cTracker.getUsername() + " with email "
					+ cTracker.getEmail() + "successful !");
		} else {
			Log.info("Can not send email to " + cTracker.getUsername()+ " with email "
					+ cTracker.getEmail() + "!");
		}
		return b;
	}
}

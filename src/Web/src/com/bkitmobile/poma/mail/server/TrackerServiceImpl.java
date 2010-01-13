package com.bkitmobile.poma.mail.server;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;
import com.bkitmobile.poma.mail.client.TrackerService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TrackerServiceImpl extends RemoteServiceServlet implements
		TrackerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MailServiceImpl mailServiceImpl = new MailServiceImpl();
	private DatabaseServiceImpl databaseServiceImpl = new DatabaseServiceImpl();

	private ResourceBundle messages;
	private Locale currentLocale;

	@Override
	public Boolean validateEmailForgotPassword(String trackerUN) {

		ServiceResult<CTracker> result = databaseServiceImpl
				.getTracker(trackerUN);
		boolean b = false;
		if (result.isOK()) {

			CTracker cTracker = result.getResult();
			String language = cTracker.getLang();
			if (language == null)
				language = "en";

			currentLocale = new Locale(language);
			messages = ResourceBundle
					.getBundle(
							"com/bkitmobile/poma/localization/server/EmailLocalizationMessages",
							currentLocale);

			// String hostName = "http://bkitpoma.appspot.com/";

			String url = IConstants.HOST
					+ "bkitpoma/serverservlet"
					+ "?mode=1&trackerUN="
					+ trackerUN
					+ "&md53="
					+ DatabaseServiceImpl.md5Cool(DatabaseServiceImpl
							.md5Cool(cTracker.getPassword()));
			

			String s = String.format(messages
					.getString("content_verify_email_forgot_password"),
					trackerUN, url);
			
			b = mailServiceImpl.sendEmail1("vo.mita.ov@gmail.com", result
					.getResult().getEmail(), messages
					.getString("title_verify_email_forgot_password"), s);

			if (b) {
				log.info("Send email to " + trackerUN + " with email "
						+ cTracker.getEmail() + "successful !");
			} else {
				log.info("Can not send email to " + trackerUN + " with email "
						+ cTracker.getEmail() + "!");
			}
		} else {
			b = false;
		}
		return b;
	}

	@Override
	public Boolean validateEmailNewTracker(CTracker cTracker) {
		String name = (cTracker.getName().equals("")) ? cTracker.getUsername()
				: cTracker.getName();
		
		String url = IConstants.HOST
				+ "bkitpoma/serverservlet?mode=0&trackerUN="
				+ cTracker.getUsername()
				+ "&md53="
				+ DatabaseServiceImpl.md5Cool(DatabaseServiceImpl
						.md5Cool(DatabaseServiceImpl.md5Cool(cTracker
								.getPassword())));//
		
		log.info("url in validateEmailNewTracker = " + url);

		String language = cTracker.getLang();
		if (language == null)
			language = "en";

		currentLocale = new Locale(language);

		messages = ResourceBundle
				.getBundle(
						"com/bkitmobile/poma/localization/server/EmailLocalizationMessages",
						currentLocale);

		String s = String.format(messages
				.getString("content_verify_email_new_tracker"), cTracker
				.getUsername(), url);
		
		boolean b = mailServiceImpl.sendEmail1("vo.mita.ov@gmail.com", cTracker
				.getEmail(), messages
				.getString("title_verify_email_new_tracker"), s);
		if (b) {
			log.info("Send email to " + cTracker.getUsername() + " with email "
					+ cTracker.getEmail() + "successful !");
		} else {
			log.info("Can not send email to " + cTracker.getUsername()
					+ " with email " + cTracker.getEmail() + "!");
		}
		return b;
	}

	private final static Logger log = Logger
			.getLogger(DatabaseServiceImpl.class.getName());
}

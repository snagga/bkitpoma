package com.bkitmobile.poma.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.server.database.DatabaseServiceImpl;
import com.bkitmobile.poma.server.localization.EmailLocalizationMessages;
import com.bkitmobile.poma.server.mail.MailServiceImpl;
import com.google.appengine.repackaged.com.google.common.base.Log;
import com.google.gwt.core.client.GWT;

/**
 * This Servlet serves responsee from email of tracker user
 * @author Tam Vo Minh
 */
@SuppressWarnings("deprecation")
public class ServerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private EmailLocalizationMessages messages = GWT
			.create(EmailLocalizationMessages.class);

	private DatabaseServiceImpl dbServiceImpl = new DatabaseServiceImpl();
	private MailServiceImpl emailToAdminServiceImpl = new MailServiceImpl();
	private int mode = 0;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		mode = Integer.parseInt(req.getParameter("mode"));
		switch (mode) {
		case 1:
			verifyEmailNewTracker(req);
			break;
		case 2:
			gotNewPassword(req);
			break;
		default:
			Log.error("Server do not support this mode");
			break;
		}
	}

	/**
	 * Verify email of tracker successfully and reset password in database
	 * @param req
	 */
	private void gotNewPassword(HttpServletRequest req) {
		String trackerUN = req.getParameter("trackerUN");
		String md53Pass = req.getParameter("md53Pass");

		ServiceResult<CTracker> result = dbServiceImpl.getTracker(trackerUN);
		CTracker cTracker = result.getResult();

		if (result.getResult() == null) {
			Log.error(trackerUN + " not found in database");
		} else {
			if (md53Pass.equals(DatabaseServiceImpl.md5Cool(DatabaseServiceImpl
					.md5Cool(cTracker.getPassword())))) {
				// Reset Password
				cTracker = dbServiceImpl.resetPasswordTracker(trackerUN)
						.getResult();
				String name = (cTracker.getName().equals("")) ? cTracker
						.getUsername() : cTracker.getName();
				boolean b = emailToAdminServiceImpl.sendEmail(
						"bkitmobile@googlegroups.com", cTracker.getEmail(),
						messages.title_get_new_password(), messages
								.content_get_new_password(name, trackerUN,
										cTracker.getPassword()));
				if (b) {
					Log.info("Reset password for " + trackerUN
							+ " successfully!");
				} else {
					Log.info("Can not send email to " + trackerUN
							+ " with email " + cTracker.getEmail() + "!");
				}
			} else {
				Log.error(trackerUN
						+ " md53pass not match with password in database");
			}
		}
	}
	
	/**
	 * Verify email of tracker successfully and update field active of this tracker in database  
	 * @param req
	 */
	private void verifyEmailNewTracker(HttpServletRequest req) {
		String trackerUN = req.getParameter("trackerUN");
		CTracker cTracker = new CTracker(trackerUN);
		cTracker.setActived(true);
		dbServiceImpl.updateTracker(cTracker);
	}
}

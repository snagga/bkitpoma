package com.bkitmobile.poma.mail.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;

/**
 * This Servlet serves responsee from email of tracker user
 * 
 * @author Tam Vo Minh
 */
@SuppressWarnings("deprecation")
public class ServerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ResourceBundle messages;
	private Locale currentLocale;

	private DatabaseServiceImpl dbServiceImpl = new DatabaseServiceImpl();
	private MailServiceImpl emailToAdminServiceImpl = new MailServiceImpl();
	private int mode = IConstants.MODE_VERIFYEMAILNEWTRACKER;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		mode = Integer.parseInt(req.getParameter("mode"));
		
		switch (mode) {
		case IConstants.MODE_VERIFYEMAILNEWTRACKER:
			verifyEmailNewTracker(req, out);
			break;
		case IConstants.MODE_GOTNEWPASSWORD:
			gotNewPassword(req, out);
			break;
		default:
			log.severe("Server do not support this mode");
			break;
		}
	}

	/**
	 * Verify email of tracker successfully and reset password in database
	 * 
	 * @param req
	 */
	private void gotNewPassword(HttpServletRequest req, PrintWriter out) {
		String trackerUN = req.getParameter("trackerUN");
		String md53 = req.getParameter("md53");

		ServiceResult<CTracker> result = dbServiceImpl.getTracker(trackerUN);
		CTracker cTracker = result.getResult();

		String language = cTracker.getLang();
		if (language == null)
			language = "en";
		currentLocale = new Locale(language);
		messages = ResourceBundle.getBundle(
				"com/bkitmobile/poma/localization/server/EmailLocalizationMessages",
				currentLocale);

		if (result.getResult() == null) {
			log.severe(trackerUN + " not found in database");
		} else {
			if (md53.equals(DatabaseServiceImpl.md5Cool(DatabaseServiceImpl
					.md5Cool(cTracker.getPassword())))) {
				
				// Reset Password
				cTracker = new CTracker(cTracker.getUsername());
				String newPass = com.bkitmobile.poma.util.client.Utils.getUpperAlphaNumeric(6);
				cTracker.setPassword(newPass);
				cTracker = dbServiceImpl.updateTracker(cTracker)
						.getResult();
				cTracker.setPassword(newPass);
				String name = (cTracker.getName().equals("")) ? cTracker
						.getUsername() : cTracker.getName();

				String s = String.format(messages
						.getString("content_get_new_password"), name,
						trackerUN, cTracker.getPassword());
				
				boolean b = emailToAdminServiceImpl.sendEmail1(
						"vo.mita.ov@gmail.com", cTracker.getEmail(), messages
								.getString("title_get_new_password"), s);
				if (b) {
					log.info("Reset password for " + trackerUN
							+ " successfully!");

					// Prompt to client successfully
					out.println("<html><body>");
					out.println("<font size = '5' >Verify email successfully. Please check your email to get new Password</ font>");
					out.println("</body></html>");

				} else {
					log.info("Can not send email to " + trackerUN
							+ " with email " + cTracker.getEmail() + "!");
				}
			} else {
				log.severe(trackerUN
						+ " md53pass not match with password in database");
			}
		}
	}

	/**
	 * Verify email of tracker successfully and update field active of this
	 * tracker in database
	 * 
	 * @param req
	 * @throws IOException
	 */
	private void verifyEmailNewTracker(HttpServletRequest req, PrintWriter out) {
		
		String trackerUN = req.getParameter("trackerUN");
		String md53 = req.getParameter("md53");
		ServiceResult<CTracker> result = dbServiceImpl.getTracker(trackerUN);
		
		if (DatabaseServiceImpl.md5Cool(
				DatabaseServiceImpl.md5Cool(result.getResult().getPassword()))
				.equals(md53)) {
			
			CTracker cTracker = new CTracker(trackerUN);
			cTracker.setActived(true);
			dbServiceImpl.updateTracker(cTracker);

			// Prompt to client successfully
			out.println("<html><body>");
			out.println("<font size = '5' >Verify email successfully</ font>");
			out.println("</body></html>");
		} else {
			out.println("<html><body>");
			out.println("<font size = '5' >Verify email unsuccessfully</ font>");
			out.println("</body></html>");
			log.info("Password does not match");
		}

	}

	private final static Logger log = Logger
			.getLogger(DatabaseServiceImpl.class.getName());
}

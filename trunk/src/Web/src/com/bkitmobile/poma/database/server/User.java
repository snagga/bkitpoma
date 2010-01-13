package com.bkitmobile.poma.database.server;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.verisign.joid.consumer.OpenIdFilter;

import com.bkitmobile.poma.database.client.entity.CTracker;

public class User {
	private static User user;

	public static User getInstance() {
		return user;
	}

	String fb_session_key = null;
	String fb_user = null;
	String fb_locale = null;

	private HttpServletRequest request;
	private HttpSession session;
	private HashMap<String, String> cookie;

	private final static Logger log = Logger.getLogger(User.class
			.getSimpleName());

	private String tracker_language = null;
	private String tracker_username = null;
	private int tracker_type = -1;

	public static final String API_KEY = "a8d694517496758f43e57022b77b01c0";
	public static final String SECRET_KEY = "bd106866600cedff2b91829ad481543e";
	public String facebook_session_key;
	public boolean isFacebook = false;
	public boolean in_facebook_frame = false;

	DatabaseServiceImpl db = new DatabaseServiceImpl();

	public User(HttpServletRequest request) {
		user = this;
		this.request = request;
		parseCookie();
	}

	public boolean determine() {
		if (facebook()) {
			return true;
		}

		tracker_language = cookie.get("tracker_language");

		try {
			tracker_username = cookie.get("tracker_username");
			tracker_type = Integer.parseInt(cookie.get("tracker_type"));
			if (tracker_username != null)
				return true;
		} catch (Exception ex) {
		}

		if (openid()) {
			return true;
		}

		if (poma()) {
			return true;
		}

		return false;
	}

	private void parseCookie() {
		setCookie(new HashMap());
		if (request.getCookies() != null) {
			for (Cookie c : request.getCookies()) {
				if (c != null && c.getName() != null) {
					
					getCookie().put(c.getName(), c.getValue());
				}
			}
		}
	}

	private boolean facebook() {
		if (request.getParameter("fb_sig_canvas_user") != null) {
			in_facebook_frame = true;
			fb_user = request.getParameter("fb_sig_canvas_user");
			fb_locale = request.getParameter("fb_sig_locale");
			setTrackerUsername(fb_user);
			String fb_lang = fb_locale == null ? "vi" : fb_locale.substring(0,
					2);
			setTrackerLanguage(fb_lang);
			setTrackerType(2);
			getSession().setAttribute("tracker_username", fb_user);
			getSession().setAttribute("tracker_type", 2);
			getSession().setAttribute("tracker_language", fb_lang);
			return true;
		}

		String prefix = request.getParameter("fb_sig_session_key") == null ? API_KEY
				: "fb_sig";
		final String _SESSION_KEY = prefix + "_session_key";
		final String _USER = prefix + "_user";
		final String _LOCALE = prefix + "_locale";

		if (prefix.equals(API_KEY)) {
			fb_session_key = getCookie().get(_SESSION_KEY);
			fb_user = getCookie().get(_USER);
			fb_locale = getCookie().get(_LOCALE);
			in_facebook_frame = false;
		} else {
			fb_session_key = request.getParameter(_SESSION_KEY);
			fb_user = request.getParameter(_USER);
			fb_locale = request.getParameter(_LOCALE);
			in_facebook_frame = true;
		}

		if (fb_session_key != null) {
			isFacebook = true;
			if (fb_user != null) {
				setTrackerUsername(fb_user);
				setTrackerType(2);
				String fb_lang = fb_locale == null ? "vi" : fb_locale
						.substring(0, 2);
				setTrackerLanguage(fb_lang);
				getSession().setAttribute("tracker_username", fb_user);
				getSession().setAttribute("tracker_type", 2);
				getSession().setAttribute("tracker_language", fb_lang);
				log.warning("Facebook ID: " + fb_user);
				return true;
			}
			facebook_session_key = fb_session_key;
		}
		return false;
	}

	private boolean poma() {
		String session_username = null;
		try {
			session_username = (String) getSession().getAttribute(
					"tracker_username");
			CTracker ctracker = db.getTracker(session_username).getResult();
			if (tracker_language == null) {
				if (ctracker != null) {
					setTrackerLanguage(ctracker.getLang() == null ? "vi"
							: ctracker.getLang());
				} else {
					setTrackerLanguage("vi");
				}
			}
			tracker_type = ctracker.getType() == null ? 0 : ctracker.getType();
			getSession().setAttribute("tracker_username", session_username);
			getSession().setAttribute("tracker_type", tracker_type);
			getSession().setAttribute("tracker_language", getTrackerLanguage());
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

	private boolean openid() {
		String loggedInAs = null;

		try {
			loggedInAs = OpenIdFilter.getCurrentUser(getSession());
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}

		// loggedInAs = "https://me.yahoo.com/mr_rkh";

		if (loggedInAs != null) {
			setTrackerUsername(loggedInAs);
			setTrackerType(1);
			CTracker ctracker = db.getTracker(loggedInAs).getResult();
			if (tracker_language == null) {
				if (ctracker != null) {
					setTrackerLanguage(ctracker.getLang() == null ? "vi"
							: ctracker.getLang());
				} else {
					setTrackerLanguage("vi");
				}
			}
			getSession().setAttribute("tracker_username", loggedInAs);
			getSession().setAttribute("tracker_type", 1);
			getSession().setAttribute("tracker_language", getTrackerLanguage());
			log.warning("OpenID: " + loggedInAs);
			return true;
		}
		return false;
	}

	/**
	 * @param tracker_language
	 *            the tracker_language to set
	 */
	public void setTrackerLanguage(String tracker_language) {
		this.tracker_language = tracker_language;
	}

	/**
	 * @return the tracker_language
	 */
	public String getTrackerLanguage() {
		return tracker_language;
	}

	/**
	 * @param tracker_username
	 *            the tracker_username to set
	 */
	public void setTrackerUsername(String tracker_username) {
		this.tracker_username = tracker_username;
	}

	/**
	 * @return the tracker_username
	 */
	public String getTrackerUsername() {
		return tracker_username;
	}

	/**
	 * @param tracker_type
	 *            the tracker_type to set
	 */
	public void setTrackerType(int tracker_type) {
		this.tracker_type = tracker_type;
	}

	/**
	 * @return the tracker_type
	 */
	public int getTrackerType() {
		return tracker_type;
	}

	/**
	 * @param cookie
	 *            the cookie to set
	 */
	public void setCookie(HashMap<String, String> cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the cookie
	 */
	public HashMap<String, String> getCookie() {
		return cookie;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
		for (String valueNames : session.getValueNames()) {
			
		}
	}

	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}
}

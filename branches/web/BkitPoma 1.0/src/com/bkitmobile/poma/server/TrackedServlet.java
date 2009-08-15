package com.bkitmobile.poma.server;

import java.io.*;
import java.util.Date;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.WayPoint;

public class TrackedServlet extends HttpServlet {

	/**
	 * TrackedServlet : test session
	 */
	private static final long serialVersionUID = -3542079201849893482L;

	DatabaseServiceImpl db = new DatabaseServiceImpl();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		if (session.getAttribute("certificated") != null) {
			if (request.getParameter("newtrack") != null) {
				out.println(db.insertTrack(request.getSession().getAttribute(
						"name").toString()));
			} else {
				String lat = request.getParameter("lat");
				String lng = request.getParameter("lng");
				String trackid = request.getParameter("trackid");
				if (db.insertWayPoint(lat, lng, "0", trackid) == 0) {
					out.println("success! " + lat + "," + lng + "," + trackid);
				}
			}
		} else {
			response.setStatus(404);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("name");
		String password = request.getParameter("pass");

		if (request.getParameter("login") != null && username != null) {
			if (db.loginTracker(username, password)) {
				session.setAttribute("certificated", "true");
				UserSettings.tracker.setUsername(username);
				session.setAttribute("name", username);
			}
		}

		if (session.getAttribute("certificated") != null) {
			out.println("You are " + session.getAttribute("name") + "!");
		} else {
			out.println("You bastard!");
		}
	}
}

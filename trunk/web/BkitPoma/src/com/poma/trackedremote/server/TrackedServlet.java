package com.poma.trackedremote.server;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class TrackedServlet extends HttpServlet {

	/**
	 * TrackedServlet : test session
	 */
	private static final long serialVersionUID = -3542079201849893482L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();

		if (session.getAttribute("certificated") != null) {
			out.println(request.getParameter("lat") + ","
					+ request.getParameter("lng"));
			out.println("You are " + session.getAttribute("name") + "!");
		} else {
			response.setStatus(404);
			//out.println("BAD REQUEST");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		if (request.getParameter("login") != null
				&& request.getParameter("name") != null) {
			session.setAttribute("certificated", "true");
			// verify id & password
			session.setAttribute("name", request.getParameter("name"));
		}

		if (session.getAttribute("certificated") != null) {
			out.println("You are " + session.getAttribute("name") + "!");
		}
	}
}

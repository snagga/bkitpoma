package com.bkitmobile.poma.server.mobile;

import java.io.*;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bkitmobile.poma.client.database.entity.CTrack;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.bkitmobile.poma.server.database.DatabaseServiceImpl;

public class MobileApi extends HttpServlet {

	/**
	 * TrackedServlet : test session
	 */
	private static final long serialVersionUID = -3542079201849893482L;
	private static final Logger log = Logger.getLogger(MobileApi.class.getName());

	DatabaseServiceImpl db = new DatabaseServiceImpl();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Object obj = session.getAttribute("ctracked");
		if (!(obj instanceof CTracked)) {
			response.setStatus(404);
			return;
		}
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
	
		CTracked ctracked = (CTracked)obj;
		
		String op = request.getParameter("op");
		if (op == null)
			op = "";

		CTrack ctrack = null;
		obj = session.getAttribute("ctrack");
		if (obj instanceof CTrack) {
			ctrack = (CTrack) obj;
		}

		if (ctrack == null) {
			ctrack = db.newTrack(ctracked.getUsername()).getResult();
		}

		if (op.equals("logout")) {
			session.removeAttribute("ctracked");
			session.removeAttribute("ctrack");
			out.println("OK");
		} else if (op.equals("latlng")) {
			double lat = Double.parseDouble(request.getParameter("lat"));
			double lng = Double.parseDouble(request.getParameter("lng"));
			if (db.insertWaypoint(ctrack.getTrackID(), lat, lng, 0L).isOK()) {
				out.println("OK");
			}
		} else if (op.equals("cellid")) {
			out.println("OK");
		} else if (op.equals("newtrack")) {
			ctrack = db.newTrack(ctracked.getUsername()).getResult();
			session.setAttribute("ctrack", ctrack);
			out.println(ctrack.getTrackID());
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("id");
		String password = request.getParameter("pwd");
		
		Long id = 0L;
		try {
			id = Long.parseLong(username);
		} catch (Exception ex) {
		}

		if (username != null && password != null) {
			CTracked ctracked = db.loginTrackedFromMobile(id, password)
					.getResult();
			if (ctracked != null) {
				out.println("OK");
				session.setAttribute("ctracked", ctracked);
				session.setAttribute("ctrack", db.newTrack(
						ctracked.getUsername()).getResult());
				log.info(ctracked + " : logined!");
			}
		}
	}
}

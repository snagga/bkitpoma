package com.bkitmobile.poma.mobile.server;

import java.io.*;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.*;

import com.bkitmobile.poma.database.client.*;
import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;
import com.bkitmobile.poma.util.server.geometry.Geocode;
import com.bkitmobile.poma.util.server.geometry.Utils;

/**
 * Mobile use this servlet to verify and post location data into system
 * @author Hieu Rocker
 */
public class MobileApi extends HttpServlet {

	private static final long serialVersionUID = -3542079201849893482L;
	private static final Logger log = Logger.getLogger(MobileApi.class
			.getName());

	DatabaseServiceImpl db = new DatabaseServiceImpl();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		// All request in GET method must be certificated
		Object obj = session.getAttribute("device_id");

		if (!(obj instanceof Long)) {
			// Client must be login first, then use Mobile service
			response.setStatus(404);
			return;
		}

		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		// Here is tracked's id
		Long device_id = (Long) obj;

		// Determine operation type
		String op = request.getParameter("op");
		if (op == null)
			op = "";

		// Get current track
		Long track_id = null;
		obj = session.getAttribute("track_id");
		if (obj instanceof Long) {
			track_id = (Long) obj;
		}

		if (op.equals("logout")) {
			// Client request a logout operation
			session.removeAttribute("device_id");
			session.removeAttribute("track_id");
			out.print("OK," + device_id);
		} else if (op.equals("latlng")) {
			// Client insert update it's location in latitude/longitude

			// If it's a first waypoint, create a new track
			if (track_id == null) {
				track_id = db.newTrack(device_id).getResult().getTrackID();
				session.setAttribute("track_id", track_id);
			}

			// Parse latitude, longitude from request
			double lat = Double.parseDouble(request.getParameter("lat"));
			double lng = Double.parseDouble(request.getParameter("lng"));

			long speed = -1L;
			try {
				// Try to get speed from request
				speed = Long.parseLong(request.getParameter("spd"));
			} catch (Exception ex) {
			}

			if (speed < 0) {
				// Client don't send speed to server
				try {
					// Calculate speed manually
					double lastLat = (Double) session.getAttribute("latitude");
					double lastLng = (Double) session.getAttribute("longitude");
					long time = (Long) session.getAttribute("time");
					long distance = Utils.getDistance(lastLat, lastLng, lat,
							lng);
					speed = distance * 1000
							/ Math.abs(time - System.currentTimeMillis());
				} catch (Exception ex) {
					speed = 0L;
				}
			}

			// Insert new point into server
			ServiceResult<CWaypoint> result = db.insertWaypoint(track_id, lat,
					lng, speed);
			CWaypoint cwaypoint = result.getResult();
			if (result.isOK()) {
				// OK,latitude,longitude,speed(m/s),time,trackid
				session.setAttribute("latitude", lat);
				session.setAttribute("longitude", lng);
				session.setAttribute("time", cwaypoint.getTime().getTime());
				out.print("OK," + cwaypoint.getLat() + "," + cwaypoint.getLng()
						+ "," + cwaypoint.getSpeed() + ","
						+ cwaypoint.getTime().getTime() + ","
						+ cwaypoint.getTrackID());
			}
		} else if (op.equals("cellid")) {
			// Client send it's location by cellular technique
			if (track_id == null) {
				track_id = db.newTrack(device_id).getResult().getTrackID();
				session.setAttribute("track_id", track_id);
			}

			try {
				int cell = Integer.parseInt(request.getParameter("cell"));
				int lac = Integer.parseInt(request.getParameter("lac"));
				Geocode geocode = Utils.getLocation(cell, lac);
				out.println(geocode.getLatitude() + ","
						+ geocode.getLongitude());
			} catch (Exception ex) {
			}

			// TODO Implements cellular method to calculate location of a mobile
			out.println("Not implement");
		} else if (op.equals("newtrack")) {
			// Client request to create a new track
			track_id = db.newTrack(device_id).getResult().getTrackID();
			session.setAttribute("track_id", track_id);
			out.print("OK," + track_id);
		} else if (op.equals("changepass")) {
			String newpass = request.getParameter("newpass");
			if (newpass != null) {
				CTracked ctracked = new CTracked();
				ctracked.setUsername(device_id);
				ctracked.setPassword(newpass);
				if (db.updateTracked(ctracked).isOK()) {
					out.println("OK," + device_id);
				}
			}
		} else if (op.equals("config")) {
			CTracked ctracked = db.getTracked(device_id).getResult();
			Integer interval = ctracked.getIntervalGps();
			if (interval == null) interval = 10;
			out.print("OK," + interval + ",");
			byte[] b = ctracked.getSchedule();
			if (b == null) {
				for (int i = 0; i < 23; i++) {
					out.print("1.");
				}
				out.println(1);
			} else {
				for (int i = 0; i < 23; i++) {
					out.print(b[i] + ".");
				}
				out.println(b[23]);
			}
		} else if (op.equals("amilogin")) {
			out.println("OK");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// POST method only used for tracked login operation
		HttpSession session = request.getSession();
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();

		// Get the username and password from request
		String username = request.getParameter("id");
		String password = request.getParameter("pwd");

		Long id = 0L;
		try {
			id = Long.parseLong(username);
		} catch (Exception ex) {
		}

		if (username != null && password != null) {
			// Login into tracked system
			CTracked ctracked = db.loginTrackedFromMobile(id, password)
					.getResult();

			if (ctracked != null) {
				// Login successful
				out.print("OK," + ctracked.getUsername());
				session.setAttribute("device_id", ctracked.getUsername());
				log.info(ctracked + " : logined!");
			}
		}
	}
}

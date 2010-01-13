package com.bkitmobile.poma.flexmap.server;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.*;
import javax.servlet.http.*;

import org.w3c.dom.Element;

import com.bkitmobile.poma.database.client.*;
import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;

/**
 * Mobile use this servlet to verify and post location data into system
 * 
 * @author Hieu Rocker
 */
public class TrackerApi extends HttpServlet {

	private static final long serialVersionUID = -3542079201849893482L;
	private static final Logger log = Logger.getLogger(TrackerApi.class
			.getName());

	DatabaseServiceImpl db = new DatabaseServiceImpl();

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain");

		// Get the username and api from request
		String username = request.getParameter("uid");
		String password = request.getParameter("api");

		if (username != null && password != null) {
			// Login into tracked system
			ServiceResult<CTracker> result = db.loginTrackerFromApi(username,
					password);

			// if (result == null || !result.isOK() || result.getResult() ==
			// null) {
			// return;
			// }

			if (result.getResult() != null) {
				// Login successful
				getTrackedsInfoByTracker(result.getResult()
						.getUsername(), response.getOutputStream());
				log.info(result.getResult() + " : logined using api");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean getTrackedsInfoByTracker(final String trackerUN,
			OutputStream out) {
		ServiceResult<ArrayList<CTracked>> result = db
				.getTrackedsByTracker(trackerUN);

		if (result == null || !result.isOK() || result.getResult() == null) {
			return false;
		}

		XMLCreator xce = new XMLCreator<CTracked>() {
			@Override
			public Element createElement(CTracked obj) {
				Element trackedEle = dom.createElement("tracked");
				
				Element idEle = dom.createElement("id");
				idEle.appendChild(dom.createTextNode(obj.getUsername().toString()));

				Element nameEle = dom.createElement("name");
				nameEle.appendChild(dom.createTextNode(obj.getName()));
				
				Element iconEle = dom.createElement("icon");
				iconEle.appendChild(dom.createTextNode(obj.getIconPath()));

				trackedEle.appendChild(idEle);
				trackedEle.appendChild(nameEle);
				trackedEle.appendChild(iconEle);

				if (obj.getLastestWaypointPK() != null) {
					ServiceResult<CWaypoint> wp = db
							.getLastestWaypointTracked(obj.getUsername());

					if (wp != null && wp.isOK()) {
						CWaypoint waypoint = wp.getResult();

						Element latitude = dom.createElement("latitude");
						latitude.appendChild(dom.createTextNode(waypoint
								.getLat().toString()));

						Element longitude = dom.createElement("longitude");
						longitude.appendChild(dom.createTextNode(waypoint
								.getLng().toString()));

						Element time = dom.createElement("time");
						time.appendChild(dom.createTextNode(waypoint.getTime()
								.getTime()
								+ ""));

						Element speed = dom.createElement("speed");
						speed.appendChild(dom.createTextNode(waypoint
								.getSpeed().toString()));

						trackedEle.appendChild(time);
						trackedEle.appendChild(latitude);
						trackedEle.appendChild(longitude);
						trackedEle.appendChild(speed);
					}
				}

				return trackedEle;
			}

			@Override
			public Element getRootElement() {
				// TODO Auto-generated method stub
				Element ele = dom.createElement("trackeds");
				ele.setAttribute("owner", trackerUN);
				return ele;
			}
		};

		xce.setData(result.getResult());

		// run the example
		xce.print(out);
//		xce.print(System.out);

		return true;
	}
}

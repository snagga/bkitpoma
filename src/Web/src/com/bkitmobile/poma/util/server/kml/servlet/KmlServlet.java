package com.bkitmobile.poma.util.server.kml.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTrack;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.database.client.entity.CWaypoint;
import com.bkitmobile.poma.database.server.DatabaseServiceImpl;
import com.bkitmobile.poma.util.server.kml.Document;
import com.bkitmobile.poma.util.server.kml.Kml;
import com.bkitmobile.poma.util.server.kml.LineString;
import com.bkitmobile.poma.util.server.kml.Point;

/**
 * Tracker use this servlet to collects tracked's kml
 * 
 * @author Tam Vo Minh
 */
public class KmlServlet extends HttpServlet {

	private static final long serialVersionUID = -3542079201849893482L;
	private static final Logger log = Logger.getLogger(KmlServlet.class
			.getName());

	private Kml kml;

	private Long trackedID;
	private Long track;
	private String apitracked = "";

	private PrintWriter writer;

	DatabaseServiceImpl db = new DatabaseServiceImpl();
	private Document document;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("application/xml");

		initKML();

		writer = response.getWriter();

		// Get parameters
		try {
			trackedID = Long.parseLong(request.getParameter("tracked"));

			track = request.getParameter("track") == null ? 0 : Long
					.parseLong(request.getParameter("track"));
			apitracked = request.getParameter("apitracked");
			// from = new Date(Long.parseLong(request.getParameter("from")));
			// to = new Date(Long.parseLong(request.getParameter("to")));
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(400); // Bad request
			log.severe(e.getMessage());
			return;
		}

		// Validate api of tracked
		if (!apitracked.equals("null")) {
			CTracked cTracked = db.loginTrackedFromApi(trackedID, apitracked)
					.getResult();
			if (cTracked == null) {
				response.setStatus(401); // Bad request
				return;
			}
		}

		try {
			if (track == 0) {
				outKMLByTracked();
			} else {
				ServiceResult<ArrayList<CTrack>> result = db
						.getTracksByTracked(trackedID);
				if (result.isOK()) {
					boolean checkTrackInTracked = false;
					for (CTrack cTrack : result.getResult()) {
						System.out.print(cTrack.getTrackID() + "?");
						if (cTrack.getTrackID().equals(track)) {
							checkTrackInTracked = true;
							break;
						}
					}

					if (checkTrackInTracked == false)
						throw new Exception("Track is not owned by tracked");
				} else {
					throw new Exception(result.getMessage());
				}

				outKMLByTrack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.severe(e.getMessage());
			response.setStatus(500);// Server Error
		}

	}

	private void initKML() {
		kml = new Kml();
		document = new Document();
		kml.setFeature(document);
	}

	// param: /bkitpoma/kml?tracked=<id>&track=<id>&apitracked=<api>
	private void outKMLByTrack() throws Exception {
		ServiceResult<ArrayList<CWaypoint>> result = db
				.getWaypointsByTrack(track);

		if (result.isOK()) {

			List<Point> list = new ArrayList<Point>();
			for (CWaypoint cWaypoint : result.getResult()) {
				Point point = new Point(cWaypoint.getLat(), cWaypoint.getLng());
				point.setId(String.valueOf(cWaypoint.getTrackID()));

				list.add(point);

				// Placemark placemark = new Placemark();
				// placemark.setLocation(cWaypoint.getLat(),
				// cWaypoint.getLng());
				// placemark.setId(String.valueOf(cWaypoint.getTrackID()));
				// placemark.setDescription("<b>Speed:</b> "
				// + cWaypoint.getSpeed() + "\n<b>Time:</b> "
				// + cWaypoint.getTime().toString());
				//
				// document.addFeature(placemark);
			}

			LineString lineString = new LineString();
			lineString.setId(String.valueOf(track));
			lineString.setCoordinates(list);

			document.addFeature(lineString);
			kml.createKml(writer);
		} else {
			throw new Exception(result.getMessage());
		}
	}

	// param: /bkitpoma/kml?tracked=<id>&apitracked=<api>
	private void outKMLByTracked() throws Exception {
		System.out.println("tracked running");
		ServiceResult<ArrayList<CTrack>> result = db
				.getTracksByTracked(trackedID);
		if (result.isOK()) {
			for (CTrack cTrack : result.getResult()) {
				ServiceResult<ArrayList<CWaypoint>> result2 = db
						.getWaypointsByTrack(cTrack.getTrackID());

				// Polyline
				if (result2.isOK()) {
					List<Point> list = new ArrayList<Point>();

					for (CWaypoint cWaypoint : result2.getResult()) {
						// Placemark placemark = new Placemark();
						// placemark.setLocation(cWaypoint.getLat(), cWaypoint
						// .getLng());
						//placemark.setId(String.valueOf(cWaypoint.getTrackID())
						// );
						// placemark.setDescription("<b>Speed:</b> "
						// + cWaypoint.getSpeed() + "\n<b>Time:</b> "
						// + cWaypoint.getTime().toString());
						//
						// document.addFeature(placemark);

						Point point = new Point(cWaypoint.getLat(), cWaypoint
								.getLng());
						point.setId(String.valueOf(cWaypoint.getTrackID()));

						list.add(point);

					}

					LineString lineString = new LineString();
					lineString.setId(String.valueOf(cTrack.getTrackID()));
					lineString.setCoordinates(list);

					document.addFeature(lineString);

				} else {
					throw new Exception(result2.getMessage());
				}

			}
			kml.createKml(writer);
		} else {
			throw new Exception(result.getMessage());
		}
	}

}

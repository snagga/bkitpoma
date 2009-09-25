package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.geometry.Waypoint;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class WaypointOperation extends Operation {
	public static String WAYPOINT_LATLNG = Operation.SERVICE_URL
			+ "/waypoint/latlng/__lat__/__lng__/__spd__";

	public Geocode geocode;

        public long speed = -1;

	public WaypointOperation(HttpSessionConnection connection, double lat,
			double lng) {
		this(connection, lat, lng, -1);
	}

        public WaypointOperation(HttpSessionConnection connection, double lat,
			double lng, long spd) {
		super(connection);
		geocode = new Geocode(lat, lng);
                this.speed = spd;
		parse(execute());
	}

	protected String execute() {
		try {
			String url = StringUtils.strReplace(WAYPOINT_LATLNG, "__lat__", geocode.latitude);
			url = StringUtils.strReplace(url, "__lng__", geocode.longitude);
                        url = StringUtils.strReplace(url, "__spd__", speed);
			return connection.httpGet(url);
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	protected void parseResult() {
		// OK,lat,lng,speed,time,trackID
		try {
			String[] s = StringUtils.split(rawMessage, ',');
			ok = s[0].equals("OK");

			double lat = Double.parseDouble(s[1]);
			double lng = Double.parseDouble(s[2]);

			long speed = Long.parseLong(s[3]);
			long time = Long.parseLong(s[4]);

			long trackID = Long.parseLong(s[5]);

			// Result a waypoint that inserted into Server Database
			result = new Waypoint(new Geocode(lat, lng), speed, time, trackID);
		} catch (Exception ex) {
			ok = false;
		}
                message = !ok ? "Waypoint:failed" : result.toString();
	}
}

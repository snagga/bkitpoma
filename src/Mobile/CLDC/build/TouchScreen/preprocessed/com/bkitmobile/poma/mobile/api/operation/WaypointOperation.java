package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.geometry.Waypoint;
import com.bkitmobile.poma.mobile.api.util.StringUtil;

/**
 * Represents an operation that send location to server using GPS/A-GPS
 * @author Hieu Rocker
 */
public class WaypointOperation extends Operation {
    /**
     * URL used to send location to server
     */
    public static String WAYPOINT_LATLNG = Operation.SERVICE_URL + "/waypoint/latlng/__lat__/__lng__/__spd__";

    /**
     * Location's info
     */
    public Geocode geocode;

    /**
     * Speed
     */
    public long speed = -1;

    /**
     * Create an operation used to send location to POMA server
     * @param connection
     * @param lat
     * @param lng
     */
    public WaypointOperation(HttpSessionConnection connection, double lat,
            double lng) {
        this(connection, lat, lng, -1);
    }

    /**
     * Create an operation used to send location (with speed) to POMA server
     * @param connection
     * @param lat
     * @param lng
     * @param spd
     */
    public WaypointOperation(HttpSessionConnection connection, double lat,
            double lng, long spd) {
        super(connection);
        geocode = new Geocode(lat, lng);
        this.speed = spd;
        parse(execute());
    }

    protected String execute() {
        try {
            String url = StringUtil.strReplace(WAYPOINT_LATLNG, "__lat__", geocode.latitude);
            url = StringUtil.strReplace(url, "__lng__", geocode.longitude);
            url = StringUtil.strReplace(url, "__spd__", speed);
            return connection.httpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    protected void parseResult() {
        // OK,lat,lng,speed,time,trackID
        try {
            String[] s = StringUtil.split(rawMessage, ',');
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

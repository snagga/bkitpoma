package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.geometry.Waypoint;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class CellIDOperation extends Operation {

    public static String WAYPOINT_CELLID = Operation.SERVICE_URL + "/waypoint/cellid/__cellid__/__lac__/__mnc__/__mcc__";
    public int cellid;
    public int locationAreCode;
    public int mobileNetworkCode;
    public int mobileCountryCode;

    public CellIDOperation(HttpSessionConnection connection, int cellid, int lac) {
        this(connection, cellid, lac, 0, 0);
    }

    public CellIDOperation(HttpSessionConnection connection, int cellid, int lac, int mnc, int mcc) {
        super(connection);
        this.cellid = cellid;
        this.locationAreCode = lac;
        this.mobileNetworkCode = mnc;
        this.mobileCountryCode = mcc;
    }

    protected String execute() {
        try {
            String url = StringUtils.strReplace(WAYPOINT_CELLID, "__cellid__", cellid);
            url = StringUtils.strReplace(url, "__lac__", locationAreCode);
            url = StringUtils.strReplace(url, "__mnc__", mobileNetworkCode);
            url = StringUtils.strReplace(url, "__mcc__", mobileCountryCode);
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
        message = !ok ? "CellID:failed" : "CellID:successed";
    }
}

package com.bkitmobile.poma.mobile.api.connection;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.operation.CellIDOperation;
import com.bkitmobile.poma.mobile.api.operation.ChangePassOperation;
import com.bkitmobile.poma.mobile.api.operation.LoginOperation;
import com.bkitmobile.poma.mobile.api.operation.LogoutOperation;
import com.bkitmobile.poma.mobile.api.operation.NewTrackOperation;
import com.bkitmobile.poma.mobile.api.operation.WaypointOperation;

/**
 *
 * @author hieu.hua
 */
public class TrackedSession {

    public long trackedID;
    public String password;
    public HttpSessionConnection connection;

    public TrackedSession(long id, String password) {
        this.trackedID = id;
        this.password = password;
        connection = new HttpSessionConnection();
    }

    public TrackedSession() {
        this.trackedID = 0L;
        this.password = "";
        connection = new HttpSessionConnection();
    }

    public LoginOperation login() {
        return new LoginOperation(connection, trackedID, password);
    }

    public LogoutOperation logout() {
        return new LogoutOperation(connection);
    }

    public ChangePassOperation changePass(String newPass) {
        return new ChangePassOperation(connection, newPass);
    }

    public NewTrackOperation newTrack() {
        return new NewTrackOperation(connection);
    }

    public WaypointOperation waypoint(double lat, double lng) {
        return waypoint(lat, lng, -1);
    }

    public WaypointOperation waypoint(double lat, double lng, long speed) {
        return new WaypointOperation(connection, lat, lng, speed);
    }

    public WaypointOperation waypoint(Geocode geocode) {
        return waypoint(geocode, -1);
    }

    public WaypointOperation waypoint(Geocode geocode, long speed) {
        return waypoint(geocode.latitude, geocode.longitude, speed);
    }

    public CellIDOperation waypoint(int cellid, int lac, int mnc, int mcc) {
        return new CellIDOperation(connection, cellid, lac);
    }
}

package com.bkitmobile.poma.mobile.api.connection;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.operation.CellIDOperation;
import com.bkitmobile.poma.mobile.api.operation.ChangePassOperation;
import com.bkitmobile.poma.mobile.api.operation.ConfigOperation;
import com.bkitmobile.poma.mobile.api.operation.LoginOperation;
import com.bkitmobile.poma.mobile.api.operation.LogoutOperation;
import com.bkitmobile.poma.mobile.api.operation.NewTrackOperation;
import com.bkitmobile.poma.mobile.api.operation.WaypointOperation;

/**
 * A working session of device
 * @author Hieu Rocker
 */
public class DeviceSession {

    /**
     * Device's ID
     */
    public long deviceID;

    /**
     * Device's password
     */
    public String password;

    /**
     * Device's interval
     */
    public long interval;

    /**
     * Device's schedule
     */
    public boolean[] schedule;

    /**
     * HttpConnection used to connect to POMA
     */
    public HttpSessionConnection connection;

    /**
     * Create a device's session with specified id, password
     * @param id
     * @param password
     */
    public DeviceSession(long id, String password) {
        this.deviceID = id;
        this.password = password;
        connection = new HttpSessionConnection();
    }

    public DeviceSession() {
        this.deviceID = 0L;
        this.password = "";
        connection = new HttpSessionConnection();
    }

    public DeviceSession(long deviceID, String password, HttpSessionConnection connection) {
        this.deviceID = deviceID;
        this.password = password;
        this.connection = connection;
    }

    /**
     * Login into POMA
     * @return
     */
    public LoginOperation login() {
        return new LoginOperation(connection, deviceID, password);
    }

    /**
     * Logout from POMA
     * @return
     */
    public LogoutOperation logout() {
        return new LogoutOperation(connection);
    }

    /**
     * Change password
     * @param newPass
     * @return
     */
    public ChangePassOperation changePass(String newPass) {
        return new ChangePassOperation(connection, newPass);
    }

    /**
     * Begin a new track
     * @return
     */
    public NewTrackOperation newTrack() {
        return new NewTrackOperation(connection);
    }

    /**
     * Send waypoint to POMA
     * @param lat
     * @param lng
     * @return
     */
    public WaypointOperation waypoint(double lat, double lng) {
        return waypoint(lat, lng, -1);
    }

    /**
     * Send waypoint to POMA
     * @param lat
     * @param lng
     * @param speed
     * @return
     */
    public WaypointOperation waypoint(double lat, double lng, long speed) {
        return new WaypointOperation(connection, lat, lng, speed);
    }

    /**
     * Send waypoint to POMA
     * @param geocode
     * @return
     */
    public WaypointOperation waypoint(Geocode geocode) {
        return waypoint(geocode, -1);
    }

    /**
     * Send waypoint to POMA
     * @param geocode
     * @param speed
     * @return
     */
    public WaypointOperation waypoint(Geocode geocode, long speed) {
        return waypoint(geocode.latitude, geocode.longitude, speed);
    }

    /**
     * Send waypoint to POMA by cellular method
     * @param cellid
     * @param lac
     * @param mnc
     * @param mcc
     * @return
     */
    public CellIDOperation waypoint(int cellid, int lac, int mnc, int mcc) {
        return new CellIDOperation(connection, cellid, lac);
    }

    /**
     * Get config of device
     * @return
     */
    public ConfigOperation config() {
        return new ConfigOperation(connection);
    }
}

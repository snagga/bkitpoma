package com.bkitmobile.poma.mobile.location;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import javax.microedition.location.Coordinates;
import javax.microedition.location.LocationException;

/**
 *
 * @author hieu.hua
 */
public class LocationProvider implements Runnable {

    public Geocode curGeocode = new Geocode();
    public Geocode lastGeocode = new Geocode();

    public Geocode getLocation() {
        return curGeocode;
    }
    private javax.microedition.location.LocationProvider lp = null;
    public long interval = 10000;

    private LocationChangedListener locationChangedListener;
    public void setLocationChangedListener(LocationChangedListener locationChangedListener) {
        this.locationChangedListener = locationChangedListener;
    }

    public LocationProvider(long interval) {
        this.interval = interval;
        getLocationProvider();
    }

    private void getLocationProvider() {
        try {
            lp = javax.microedition.location.LocationProvider.getInstance(null);
        } catch (LocationException ex) {
            ex.printStackTrace();
        }
    }

    private Geocode checkLocation() throws Exception {
        if (lp == null) getLocationProvider();

        Coordinates coordinates = lp.getLocation(60).getQualifiedCoordinates();

        if (coordinates != null) {
            // Use coordinate information
            curGeocode.latitude = coordinates.getLatitude();
            curGeocode.longitude = coordinates.getLongitude();
        }

        if (locationChangedListener != null && !curGeocode.equals(lastGeocode)) {
            locationChangedListener.onLocationChanged(lastGeocode, curGeocode);
        }

        lastGeocode.latitude = curGeocode.latitude;
        lastGeocode.longitude = curGeocode.longitude;

        return curGeocode;
    }

    boolean bRun = false;
    private Thread t = null;

    public void start() {
        if (!bRun) {
            bRun = true;
            t = new Thread(this);
            t.start();
        }
    }

    public void stop() {
        bRun = false;
    }

    public void run() {
        while (bRun) {
            try {
                long l1 = System.currentTimeMillis();
                checkLocation();
                long l2 = System.currentTimeMillis();
                long delta = interval-(l2-l1);
                if (delta>0) {
                    Thread.sleep(delta);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
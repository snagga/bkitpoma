package com.bkitmobile.poma.mobile.location;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import javax.microedition.location.Coordinates;
import javax.microedition.location.LocationException;

/**
 * This class help POMA receive Geocode from internal/external GPS receiver
 * @author Hieu Rocker
 */
public class LocationProvider implements Runnable {

    public Geocode curGeocode = new Geocode();
    public Geocode lastGeocode = new Geocode();

    /**
     * Get current geocode given by Location Provider
     * @return
     */
    public Geocode getLocation() {
        return curGeocode;
    }
    private javax.microedition.location.LocationProvider lp = null;

    public boolean[] schedule;
    /**
     * LocationProvider will check location every interval (ms)
     */
    public long interval = 10000;
    private LocationChangedListener locationChangedListener;

    /**
     * Set location changed listener for location changed event
     * @param locationChangedListener
     */
    public void setLocationChangedListener(LocationChangedListener locationChangedListener) {
        this.locationChangedListener = locationChangedListener;
    }

    /**
     * Create an instance of LocationProvider, which provide mechanism to receive GPS's geocode and raise event when location changed
     * @param interval
     */
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

    /**
     * Get geocode from GPS
     * @return
     * @throws Exception
     */
    private Geocode checkLocation() throws Exception {
        if (lp == null) {
            getLocationProvider();
        }

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

    /**
     * Start receive GPS satellite
     */
    public void start() {
        if (!bRun) {
            bRun = true;
            t = new Thread(this);
            t.start();
        }
    }

    /**
     * Stop receive GPS satellite
     */
    public void stop() {
        bRun = false;
    }

    /**
     * Period receive GPS satellite
     */
    public void run() {
        while (bRun) {
            if (schedule != null && !schedule[(int) ((System.currentTimeMillis() / 36000000) % 24)]) {
                continue;
            }
            
            try {
                long l1 = System.currentTimeMillis();
                checkLocation();
                long l2 = System.currentTimeMillis();
                long delta = interval - (l2 - l1);
                if (delta > 0) {
                    Thread.sleep(delta);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

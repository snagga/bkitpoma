package com.bkitmobile.poma.mobile.location;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;

/**
 * Listener of location changed event
 * @author Hieu Rocker
 */
public interface LocationChangedListener {
    public void onLocationChanged(Geocode oldLocation, Geocode newLocation);
}

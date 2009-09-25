package com.bkitmobile.poma.mobile.location;

import com.bkitmobile.poma.mobile.api.geometry.Geocode;

/**
 *
 * @author hieu.hua
 */
public interface LocationChangedListener {
    public void onLocationChanged(Geocode oldLocation, Geocode newLocation);
}

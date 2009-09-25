package com.bkitmobile.poma.mobile.api.geometry;

import com.bkitmobile.poma.mobile.api.util.StringUtil;

/**
 * Contains coordinate of locaton
 * @author Hieu Rocker
 */
public class Geocode {
	/**
	 * Latitude
	 */
	public double latitude;
	
	/**
	 * Longitude
	 */
	public double longitude;

	public Geocode() {
	}

	public Geocode(double lat, double lng) {
		this.latitude = lat;
		this.longitude = lng;
	}

	public Geocode(String value) {
		String[] s = StringUtil.split(value, ',');
		latitude = Double.parseDouble(s[0]);
		longitude = Double.parseDouble(s[1]);
	}

	public String toString() {
		return latitude + "," + longitude;
	}

	public int hashCode() {
		return (int) (latitude * 10000 + longitude * 10000);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Geocode)) {
			return false;
		}
		Geocode that = (Geocode) obj;
		return this.latitude == that.latitude
				&& this.longitude == that.longitude;
	}
}

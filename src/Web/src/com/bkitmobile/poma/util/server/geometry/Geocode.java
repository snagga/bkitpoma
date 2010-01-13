package com.bkitmobile.poma.util.server.geometry;

public class Geocode {
	private double latitude;
	private double longitude;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Geocode(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Geocode)) return false;
		Geocode that = (Geocode)obj;
		return that.latitude == this.latitude && that.longitude == this.longitude;
	}
	
	@Override
	protected Geocode clone() {
		return new Geocode(latitude, longitude);
	}
}

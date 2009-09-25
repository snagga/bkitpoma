package com.bkitmobile.poma.mobile.api.geometry;

import java.util.Date;

public class Waypoint {
	public Geocode geocode;
	public long speed = -1L;
	public long time = 0;
	public long trackID = 0;

	public Waypoint(Geocode geocode, long speed, long time, long trackID) {
		this.geocode = geocode;
		this.speed = speed;
		this.time = time;
		this.trackID = trackID;
	}

	public Date getTime() {
		return new Date(time);
	}

	public void setTime(Date time) {
		this.time = time.getTime();
	}

	public String toString() {
		return "geo[" + geocode + "],spd[" + speed + "],time[" + time
				+ "],track[" + trackID + "]";
	}

	public int hashCode() {
		return (int) (geocode.hashCode() + speed + time + trackID);
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Waypoint)) {
			return false;
		}
		Waypoint that = (Waypoint) obj;
		return this.geocode.equals(that.geocode) && this.speed == that.speed
				&& this.time == that.time && this.trackID == that.trackID;
	}
}

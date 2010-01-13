package com.bkitmobile.poma.database.client.entity;

import java.util.Date;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CWaypoint implements IsSerializable {
	private String waypointPK;
	private Long trackID;
	private Double lat;
	private Double lng;
	private Long speed;
	private Date time;

	public CWaypoint() {
		time = new Date();
	}

	public CWaypoint(String waypointPK) {
		this.waypointPK = waypointPK;
		String[] s = waypointPK.split("#");
		time = new Date(Long.parseLong(s[1]));
		trackID = Long.parseLong(s[2]);
	}

	public CWaypoint(Long trackID) {
		this.time = new Date();
		this.waypointPK = "#" + this.time.getTime() + "#" + trackID;
		this.trackID = trackID;
	}

	public CWaypoint(Double lat, Double lng, Long speed,
			Long trackID) {
		this.time = new Date();
		this.trackID = trackID;
		this.waypointPK = "#" + this.time.getTime() + "#" + trackID;
		this.lat = lat;
		this.lng = lng;
		this.speed = speed;
	}

	public void setTrackID(Long trackID) {
		this.trackID = trackID;
	}

	public Long getTrackID() {
		return trackID;
	}

	public String getWaypointPK() {
		return waypointPK;
	}

	public void setWaypointPK(String waypointPK) {
		this.waypointPK = waypointPK;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Long getSpeed() {
		return speed;
	}

	public void setSpeed(Long speed) {
		this.speed = speed;
	}

	public LatLng getLatLng() {
		return LatLng.newInstance(getLat(), getLng());
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (waypointPK != null ? waypointPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CWaypoint)) {
			return false;
		}
		CWaypoint other = (CWaypoint) object;
		if ((this.waypointPK == null && other.waypointPK != null)
				|| (this.waypointPK != null && !this.waypointPK
						.equals(other.waypointPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Waypoint[waypointPK=" + waypointPK + "]";
	}
}

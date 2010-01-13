package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.bkitmobile.poma.database.client.entity.CWaypoint;
import com.google.gwt.maps.client.geom.LatLng;
/**
 * 
 * @author Hieu Rocker
 */
@Entity
@Table(name="waypoint")
public class Waypoint implements Serializable {
	private static final long serialVersionUID = -1727317053947090383L;

	@Id
	@Basic(optional = false)
	@Column(name = "WAYPOINTPK")
	protected String waypointPK;
	
	@Basic(optional = false)
	@Column(name = "TRACKID")
	private Long trackID;
	
	@Basic(optional = false)
	@Column(name = "LAT")
	private Double lat;
	
	@Basic(optional = false)
	@Column(name = "LNG")
	private Double lng;
	
	@Basic(optional = true)
	@Column(name = "SPEED")
	private Long speed;
	
	@Basic(optional = false)
	@Column(name = "TIME")
	private Date time;

	public Waypoint() {
		time = new Date();
	}

	public Waypoint(String waypointPK) {
		this.waypointPK = waypointPK;
		String[] s = waypointPK.split("#");
		time = new Date(Long.parseLong(s[1]));
		trackID = Long.parseLong(s[2]);
	}

	public Waypoint(Long trackID) {
		this.time = new Date();
		this.waypointPK = "#" + this.time.getTime() + "#" + trackID;
		this.trackID = trackID;
	}

	public Waypoint(Double lat, Double lng, Long speed,
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
		if (!(object instanceof Waypoint)) {
			return false;
		}
		Waypoint other = (Waypoint) object;
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
	
	/**
	 * Copy from a CWaypoint instance
	 * @param obj <code>obj</code> is an instance which is copied
	 */
	public void copy(CWaypoint obj) {
		this.time = obj.getTime();
		this.trackID = obj.getTrackID();
		this.waypointPK = obj.getWaypointPK();
		this.lat = obj.getLat();
		this.lng = obj.getLng();
		this.speed = obj.getSpeed();
	}
	
	/**
	 * Copy from a Waypoint instance
	 * @param obj <code>obj</code> is an instance which is copied
	 * @return a new CWaypoint instance
	 */
	public static CWaypoint copy(Waypoint obj) {
		CWaypoint cwaypoint = new CWaypoint();
		cwaypoint.setTime(obj.getTime());
		cwaypoint.setTrackID(obj.getTrackID());
		cwaypoint.setWaypointPK(obj.getWaypointPK());
		cwaypoint.setLat(obj.getLat());
		cwaypoint.setLng(obj.getLng());
		cwaypoint.setSpeed(obj.getSpeed());
		return cwaypoint;
	}
}

package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.bkitmobile.poma.database.client.entity.CTrack;

/**
 * 
 * @author Hieu Rocker
 */
@Entity
@Table(name = "track")
public class Track implements Serializable {
	private static final long serialVersionUID = -6930413608483920963L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "TRACKID")
	private Long trackID;
	
	@Basic(optional = false)
	@Column(name = "TRACKEDID")
	private Long trackedID;
	
	@Basic(optional = true)
	@Column(name = "BEGINTIME")
	private Date beginTime;
	
	@Basic(optional = true)
	@Column(name = "ENDTIME")
	private Date endTime;

	public Track() {
	}

	public Track(Long trackedID) {
		this.trackedID = trackedID;
	}
	
	public Long getTrackID() {
		return trackID;
	}

	public Long getTrackedID() {
		return trackedID;
	}

	public void setTrackedID(Long trackedID) {
		this.trackedID = trackedID;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (trackID != null ? trackID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Track)) {
			return false;
		}
		Track other = (Track) object;
		if ((this.trackID == null && other.trackID != null)
				|| (this.trackID != null && !this.trackID.equals(other.trackID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Track[trackid=" + trackID + "]";
	}
	
	
	/**
	 * Copy from a CTrack instance
	 * @param obj <code>obj</code> is an instance which is copied
	 */
	public void copy(CTrack obj) {
		this.trackID = obj.getTrackID();
		this.trackedID = obj.getTrackedID();
		this.beginTime = obj.getBeginTime();
		this.endTime = obj.getEndTime();
	}
	
	/**
	 * Copy from a Track instance
	 * @param obj <code>obj</code> is an instance which is copied
	 * @return a new CTrack instance
	 */
	public static CTrack copy(Track obj) {
		CTrack ctrack = new CTrack();
		ctrack.setTrackID(obj.getTrackID());
		ctrack.setTrackedID(obj.getTrackedID());
		ctrack.setBeginTime(obj.getBeginTime());
		ctrack.setEndTime(obj.getEndTime());
		return ctrack;
	}
}

package com.bkitmobile.poma.database.client.entity;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CTrack implements IsSerializable {
	private Long trackID;
	private Long trackedID;
	private Date beginTime;
	private Date endTime;
	
	public CTrack() {
	}

	public CTrack(Long trackedID) {
		this.trackedID = trackedID;
	}
	
	public void setTrackID(Long trackID) {
		this.trackID = trackID;
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
		if (!(object instanceof CTrack)) {
			return false;
		}
		CTrack other = (CTrack) object;
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
}

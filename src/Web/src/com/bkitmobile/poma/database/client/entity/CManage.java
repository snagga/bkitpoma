package com.bkitmobile.poma.database.client.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CManage implements IsSerializable {
	private String managePK;
	private String trackerUN;
	private Long trackedID;

	public CManage() {
	}

	public CManage(String key) {
		this.managePK = key;
		String[] s = key.split("#");
		this.trackerUN = s[0];
		this.trackedID = Long.parseLong(s[1]);
	}

	public CManage(String trackerUN, Long trackedID) {
		this.managePK = trackerUN + "#" + trackedID;
		this.trackerUN = trackerUN;
		this.trackedID = trackedID;
	}

	public void setManagePK(String key) {
		this.managePK = key;
	}

	public String getManagePK() {
		return this.managePK;
	}

	public String getTrackerUN() {
		return trackerUN;
	}

	public void setTrackerUN(String trackerUN) {
		this.trackerUN = trackerUN;
	}

	public Long getTrackedID() {
		return trackedID;
	}

	public void setTrackedID(Long trackedID) {
		this.trackedID = trackedID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (managePK != null ? managePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CManage)) {
			return false;
		}
		CManage other = (CManage) object;
		if ((this.managePK == null && other.managePK != null)
				|| (this.managePK != null && !this.managePK
						.equals(other.managePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Manage[key=" + managePK + "]";
	}

}

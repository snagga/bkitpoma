package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;
import javax.persistence.*;
import com.bkitmobile.poma.database.client.entity.CManage;

/**
 * 
 * @author Hieu Rocker
 */
@Entity
@Table(name = "manage")
public class Manage implements Serializable {
	private static final long serialVersionUID = 7496018160986937525L;

	@Id
	@Basic(optional = false)
	@Column(name = "MANAGEPK")
	private String managePK;
	
	@Basic(optional = false)
	@Column(name = "TRACKERUN")
	private String trackerUN;
	
	@Basic(optional = false)
	@Column(name = "TRACKEDID")
	private Long trackedID;

	public Manage() {
	}

	public Manage(String key) {
		this.managePK = key;
		String[] s = key.split("#");
		this.trackerUN = s[0];
		this.trackedID = Long.parseLong(s[1]);
	}

	public Manage(String trackerUN, Long trackedID) {
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
		if (!(object instanceof Manage)) {
			return false;
		}
		Manage other = (Manage) object;
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

	

	/**
	 * Copy from a CManage instance
	 * @param obj <code>obj</code> is an instance which is copied
	 */
	public void copy(CManage obj) {
		this.managePK = obj.getManagePK();
		this.trackedID = obj.getTrackedID();
		this.trackerUN = obj.getTrackerUN();
	}
	
	/**
	 * Copy from a Manage instance
	 * @param obj <code>obj</code> is an instance which is copied
	 * @return a new Manage instance
	 */
	public static CManage copy(Manage obj) {
		CManage cmanage = new CManage();
		cmanage.setManagePK(obj.getManagePK());
		cmanage.setTrackedID(obj.getTrackedID());
		cmanage.setTrackerUN(obj.getTrackerUN());
		return cmanage;
	}
}
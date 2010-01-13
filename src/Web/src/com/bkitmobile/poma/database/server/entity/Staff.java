package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;

import javax.persistence.*;

import com.bkitmobile.poma.database.client.entity.CStaff;

/**
 * 
 * @author Hieu Rocker
 */
@Entity
@Table(name = "staff")
public class Staff implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "STAFFPK")
	private String staffPK;

	@Basic(optional = false)
	@Column(name = "TRACKERUN")
	private String trackerUN;

	@Basic(optional = false)
	@Column(name = "TRACKEDID")
	private Long trackedID;

	 public Staff() {
	    }

	    public Staff(String staffPK) {
	        this.staffPK = staffPK;
			String[] s = staffPK.split("#");
			this.trackerUN = s[0];
			this.trackedID = Long.parseLong(s[1]);
	    }

	    public Staff(String trackerUN, Long trackedID) {
	        this.staffPK = trackerUN + "#" + trackedID;
	        this.trackerUN = trackerUN;
	        this.trackedID = trackedID;
	    }
	    
	    public String getStaffPK() {
	        return staffPK;
	    }

	    public void setStaffPK(String staffPK) {
	        this.staffPK = staffPK;
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
	        hash += (staffPK != null ? staffPK.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
	        if (!(object instanceof Staff)) {
	            return false;
	        }
	        Staff other = (Staff) object;
	        if ((this.staffPK == null && other.staffPK != null) || (this.staffPK != null && !this.staffPK.equals(other.staffPK))) {
	            return false;
	        }
	        return true;
	    }

	    @Override
	    public String toString() {
	        return "entity.Staff[staffPK=" + staffPK + "]";
	    }
	
	/**
	 * Copy from a CStaff instance
	 * @param obj <code>obj</code> is an instance which is copied
	 */
    public void copy(CStaff obj) {
    	this.staffPK = obj.getStaffPK();
    	this.trackedID = obj.getTrackedID();
    	this.trackerUN = obj.getTrackerUN();
    }
    
    /**
	 * Copy from a Staff instance
	 * @param obj <code>obj</code> is an instance which is copied
	 * @return a new CStaff instance
	 */
    public static CStaff copy(Staff obj) {
    	CStaff cstaff = new CStaff();
    	cstaff.setStaffPK(obj.getStaffPK());
    	cstaff.setTrackedID(obj.getTrackedID());
    	cstaff.setTrackerUN(obj.getTrackerUN());
    	return cstaff;
    }
}

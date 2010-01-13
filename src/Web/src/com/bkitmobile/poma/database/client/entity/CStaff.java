package com.bkitmobile.poma.database.client.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author Hieu Rocker
 */
public class CStaff implements IsSerializable{
    private String staffPK;
    private String trackerUN;
    private Long trackedID;

    public CStaff() {
    }

    public CStaff(String staffPK) {
        this.staffPK = staffPK;
		String[] s = staffPK.split("#");
		this.trackerUN = s[0];
		this.trackedID = Long.parseLong(s[1]);
    }

    public CStaff(String trackerUN, Long trackedID) {
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
        if (!(object instanceof CStaff)) {
            return false;
        }
        CStaff other = (CStaff) object;
        if ((this.staffPK == null && other.staffPK != null) || (this.staffPK != null && !this.staffPK.equals(other.staffPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Staff[staffPK=" + staffPK + "]";
    }

}

package com.bkitmobile.poma.database.client.entity;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CTrackedRule implements IsSerializable {
	
	private Long ruleID;
	
	private Boolean isPermit;
	
	private Long minSpeed;
	
	private Long maxSpeed;
	
	private List<Double> latLngList;
	
	private Long trackedID;

	public Long getRuleID() {
		return ruleID;
	}

	public void setRuleID(Long ruleID) {
		this.ruleID = ruleID;
	}

	public Boolean isPermit() {
		return isPermit;
	}

	public void setPermit(Boolean isPermit) {
		this.isPermit = isPermit;
	}

	public Long getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(Long minSpeed) {
		this.minSpeed = minSpeed;
	}

	public Long getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Long maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public List<Double> getLatLngList() {
		return latLngList;
	}

	public void setLatLngList(List<Double> latLngList) {
		this.latLngList = latLngList;
	}

	public void setTrackedID(Long trackedID) {
		this.trackedID = trackedID;
	}

	public Long getTrackedID() {
		return trackedID;
	}
	
	public CTrackedRule()
	{
	}
	
	public CTrackedRule(Boolean isPermit, List<Double> latLngList, Long trackedID) {
		this.isPermit = isPermit;
		this.latLngList = latLngList;
		this.trackedID = trackedID;
	}

	public CTrackedRule(Boolean isPermit, List<Double> latLngList,
			Long maxSpeed, Long minSpeed, Long trackedID) {
		this.isPermit = isPermit;
		this.latLngList = latLngList;
		this.maxSpeed = maxSpeed;
		this.minSpeed = minSpeed;
		this.trackedID = trackedID;
	}
	
	public boolean isValid() {
		if (this.latLngList == null || this.latLngList.size() < 3) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ruleID != null ? ruleID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CTrackedRule)) {
			return false;
		}
		CTrackedRule other = (CTrackedRule) object;
		if ((this.ruleID == null && other.ruleID != null)
				|| (this.ruleID != null && !this.ruleID.equals(other.ruleID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.TrackedRule[ruleID=" + ruleID + "]";
	}

	public static CTrackedRule defaultCTrackedRule(CTrackedRule ctrackedRule) {
		if (ctrackedRule.isPermit() == null) ctrackedRule.isPermit = false;
		return ctrackedRule;
	}
}

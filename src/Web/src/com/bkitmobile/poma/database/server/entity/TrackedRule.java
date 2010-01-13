package com.bkitmobile.poma.database.server.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bkitmobile.poma.database.client.entity.CTrackedRule;

@Entity
@Table(name = "TrackedRule")
public class TrackedRule {

	@Id
	@Basic(optional = false)
	@Column(name = "RULEID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ruleID;

	@Basic(optional = false)
	@Column(name = "isPermit")
	private Boolean isPermit;

	@Basic(optional = true)
	@Column(name = "minSpeed")
	private Long minSpeed;

	@Basic(optional = true)
	@Column(name = "maxSpeed")
	private Long maxSpeed;

	@Basic(optional = false)
	@Column(name = "latLngList")
	private List<Double> latLngList;

	@Basic(optional = false)
	@Column(name = "trackedID")
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
	
	public TrackedRule()
	{
	}

	public TrackedRule(Boolean isPermit, List<Double> latLngList, Long trackedID) {
		this.isPermit = isPermit;
		this.latLngList = latLngList;
		this.trackedID = trackedID;
	}

	public TrackedRule(Boolean isPermit, List<Double> latLngList,
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
		if (!(object instanceof TrackedRule)) {
			return false;
		}
		TrackedRule other = (TrackedRule) object;
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

	/**
	 * Copy from a CTrackedRule instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 */
	public void copy(CTrackedRule obj) {
		this.isPermit = obj.isPermit();
		this.latLngList = obj.getLatLngList();
		this.maxSpeed = obj.getMaxSpeed();
		this.minSpeed = obj.getMinSpeed();
		this.ruleID = obj.getRuleID();
		this.trackedID = obj.getTrackedID();
	}
	
	/**
	 * Copy from a TrackedRule instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 * @return a new CTrackedRule instance
	 */
	public static CTrackedRule copy(TrackedRule obj) {
		CTrackedRule ctrackedRule = new CTrackedRule();
		ctrackedRule.setPermit(obj.isPermit());
		
		if (obj.getLatLngList() != null) {
			ArrayList<Double> arr = new ArrayList<Double>();
			for (Object o:obj.getLatLngList()){
				if (o instanceof Double) {
					arr.add((Double)o);
				}
			}
			ctrackedRule.setLatLngList(arr);
		}
		
		ctrackedRule.setTrackedID(obj.getTrackedID());
		ctrackedRule.setMaxSpeed(obj.getMaxSpeed());
		ctrackedRule.setMinSpeed(obj.getMinSpeed());
		ctrackedRule.setRuleID(obj.getRuleID());
		return ctrackedRule;
	}
}

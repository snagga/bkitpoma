package com.bkitmobile.poma.database.client.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CAdminConfig implements IsSerializable {
	private String name;
	private String value;

	public CAdminConfig() {
	}

	public CAdminConfig(String name) {
		this.name = name;
	}

	public CAdminConfig(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (name != null ? name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CAdminConfig)) {
			return false;
		}
		CAdminConfig other = (CAdminConfig) object;
		if ((this.name == null && other.name != null)
				|| (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.AdminConfig[key=" + name + ", value=" + value + "]";
	}

}

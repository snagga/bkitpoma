package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;

import javax.jdo.annotations.Column;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bkitmobile.poma.database.client.entity.CAdminConfig;
import com.google.appengine.api.datastore.Text;

@Entity
@Table(name = "adminconfig")
public class AdminConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "NAME")
	private String name;

	@Basic(optional = false)
	@Column(name = "VALUE")
	private Text value;

	public AdminConfig() {
	}

	public AdminConfig(String name, Text value) {
		this.setName(name);
		this.setValue(value.getValue());
	}
	
	public AdminConfig(String name, String value) {
		this.setName(name);
		this.setValue(value);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = new Text(value);
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value.getValue();
	}

	@Override
	public String toString() {
		return "entity.AdminConfig[key=" + name + ", value=" + value + "]";
	}

	/**
	 * Copy from a CAdminConfig instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 */
	public void copy(CAdminConfig obj) {
		this.name = obj.getName();
		this.value = new Text(obj.getValue());
	}

	/**
	 * Copy from a AdminConfig instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 * @return a new AdminConfig instance
	 */
	public static CAdminConfig copy(AdminConfig obj) {
		return new CAdminConfig(obj.getName(),obj.getValue());
	}
}

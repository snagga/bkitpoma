package com.bkitmobile.poma.database.client.entity;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CTracker implements IsSerializable {
	protected String username;
	protected String password;
	protected String name;
	protected Date birthday;
	protected String tel;
	protected String addr;
	protected String email;
	protected Integer type;
	protected Integer gmt;
	protected String lang;
	protected String country;
	protected Boolean active;
	protected Boolean enable;
	protected String apiKey;

	public CTracker() {
	}
	
	public CTracker(String username) {
		this.username = username;
	}

	public CTracker(String username, String password, String name, Integer type) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.type = type;
	}

	public CTracker(String username, String password, String name,
			Date birthday, String tel, String addr, String email,
			Integer type, Integer gmt, String lang,
			String country, Boolean active, Boolean enable, String apiKey) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		this.tel = tel;
		this.addr = addr;
		this.email = email;
		this.type = type;
		this.gmt = gmt;
		this.lang = lang;
		this.country = country;
		this.active = active;
		this.enable = enable;
	}

	/**
	 * Get tracker's username
	 * @return tracker's username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Change tracker's username
	 * @param username <code>username</code> is a new username of tracker
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get password of tracker which already encoded by <b>MD5 algorithm</b> 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Change tracker's password
	 * @param password the <code>password</code> encoded by <b>MD5 algorithm</b>
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get tracker's name
	 * @return tracker's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Change tracker's name
	 * @param name <code>name</code> is a new name of tracker
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get tracker's birthday
	 * @return tracker's birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * Change tracker's birthday
	 * @param birthday <code>birthday</code> is a new birthday of tracker
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * Get tracker's telephone number
	 * @return tracker's telephone number
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * Change tracker's telephone number
	 * @param tel <code>tel</code> is a new telephone number of tracker
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * Get tracker's address
	 * @return tracker's address
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * Change tracker's address
	 * @param addr <code>addr</code> is a new address of tracker
	 */
	public void setAddr(String addr) {
		this.addr = addr;
	}

	/**
	 * Get tracker's email
	 * @return tracker's email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Change tracker's email
	 * @param email <code>email</code> is a new email of tracker
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get tracker's membership type (non-commercial, commercial...) 
	 * @return tracker's membership type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * Change tracker's membership type
	 * @param type <code>type</code> is a new membership type of tracker
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * Get tracker's timezome
	 * @return tracker's timezone
	 */
	public Integer getGmt() {
		return gmt;
	}

	/**
	 * Change tracker's timezone
	 * @param gmt <code>gmt</code> is a new tracker's timezone
	 */
	public void setGmt(Integer gmt) {
		this.gmt = gmt;
	}

	/**
	 * Get tracker's language 
	 * @return tracker's language
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Change tracker's language
	 * @param lang <code>lang</code> is a new tracker's language
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Get tracker's country
	 * @return tracker's country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Change tracker's country
	 * @param country <code>country</code> is a new tracker's country
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Determine this tracker actived or not
	 * @return <code>true</code> if this tracker already actived
	 * <br /><code>false</code> if otherwise
	 */
	public Boolean isActived() {
		return active;
	}

	/**
	 * Active or deactive this tracker
	 * @param active - set <code>active</code> to <code>false</code> if you want to <b>deactive</b>, otherwise set it to <code>true</code>
	 */
	public void setActived(Boolean active) {
		this.active = active;
	}

	/**
	 * Determine this tracker enabled or not
	 * @return <code>true</code> if this tracker is enabled
	 * <br /><code>false</code> if this tracker is disabled
	 */
	public Boolean isEnabled() {
		return enable;
	}

	/**
	 * Enable or disable this tracker
	 * @param enable - set <code>enable</code> to <code>false</code> if you want to <b>disable</b>, otherwise set it to <code>true</code>
	 */
	public void setEnabled(Boolean enable) {
		this.enable = enable;
	}
	
	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (username != null ? username.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CTracker)) {
			return false;
		}
		CTracker other = (CTracker) object;
		if ((this.username == null && other.username != null)
				|| (this.username != null && !this.username
						.equals(other.username))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Tracker[username=" + username + "]";
	}

	public static CTracker defaultCTracker(CTracker ctracker) {
		if (ctracker == null)
			return null;
		if (ctracker.getCountry() == null)
			ctracker.setCountry("VN");
		if (ctracker.getLang() == null)
			ctracker.setLang("vi");
		if (ctracker.getType() == null)
			ctracker.setType(0);
		if (ctracker.getGmt() == null)
			ctracker.setGmt(7);
		if (ctracker.isActived() == null)
			ctracker.setActived(false);
		if (ctracker.isEnabled() == null)
			ctracker.setEnabled(true);
		return ctracker;
	}
	
	public CTracker clone()  {
		return new CTracker(this.username, this.password, this.name,
				this.birthday, this.tel, this.addr, this.email,
				this.type, this.gmt, this.lang,
				this.country, this.active, this.enable, this.apiKey);
	}
}

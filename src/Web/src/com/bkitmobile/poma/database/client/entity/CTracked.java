package com.bkitmobile.poma.database.client.entity;

import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Hieu Rocker
 */
public class CTracked implements IsSerializable {
	private Long username;
	private String password;
	private String name;
	private Date birthday;
	private String tel;
	private String email;
	private Boolean active;
	private Integer gmt;
	private String lang;
	private String country;
	private String iconpath;
	private Boolean showinmap;
	private Boolean embedded;
	private byte[] schedule;
	private Integer intervalgps;
	private String apikey;
	private String lastestWaypointPK;
	private Boolean rulePermit;

	// //// Not persisted
	private boolean underManage;

	public void setUnderManage(boolean underManage) {
		this.underManage = underManage;
	}

	public boolean isUnderManage() {
		return underManage;
	}

	// ////Not persisted

	public CTracked() {
	}

	public CTracked(String password, String name, String tel) {
		this.password = password;
		this.name = name;
		this.tel = tel;
	}

	public CTracked(String password, String name, Date birthday, String tel,
			String email, Boolean active, Integer gmt, String lang,
			String country, String iconpath, Boolean showinmap,
			Boolean embedded, byte[] schedule, Integer intervalgps,
			String apikey) {
		this.password = password;
		this.name = name;
		this.birthday = birthday;
		this.tel = tel;
		this.email = email;
		this.active = active;
		this.gmt = gmt;
		this.lang = lang;
		this.country = country;
		this.iconpath = iconpath;
		this.showinmap = showinmap;
		this.embedded = embedded;
		this.schedule = schedule;
		this.intervalgps = intervalgps;
		this.apikey = apikey;
	}

	public Long getUsername() {
		return this.username;
	}

	public void setUsername(Long username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Integer getGmt() {
		return gmt;
	}

	public void setGmt(Integer gmt) {
		this.gmt = gmt;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIconPath() {
		return iconpath;
	}

	public void setIconPath(String iconpath) {
		this.iconpath = iconpath;
	}

	public Boolean getShowInMap() {
		return showinmap;
	}

	public void setShowInMap(Boolean showinmap) {
		this.showinmap = showinmap;
	}

	public Boolean getEmbedded() {
		return embedded;
	}

	public void setEmbedded(Boolean embedded) {
		this.embedded = embedded;
	}

	public byte[] getSchedule() {
		return schedule;
	}

	public void setSchedule(byte[] schedule) {
		this.schedule = schedule;
	}

	public Integer getIntervalGps() {
		return intervalgps;
	}

	public void setIntervalGps(Integer intervalgps) {
		this.intervalgps = intervalgps;
	}

	public void setApiKey(String apiKey) {
		this.apikey = apiKey;
	}

	public String getApiKey() {
		return apikey;
	}

	/**
	 * @param lastestWaypointPK
	 *            the lastestWaypointPK to set
	 */
	public void setLastestWaypointPK(String lastestWaypointPK) {
		this.lastestWaypointPK = lastestWaypointPK;
	}

	/**
	 * @return the lastestWaypointPK
	 */
	public String getLastestWaypointPK() {
		return lastestWaypointPK;
	}

	/**
	 * @param rulePermit
	 *            the rulePermit to set
	 */
	public void setRulePermit(Boolean rulePermit) {
		this.rulePermit = rulePermit;
	}

	/**
	 * @return the rulePermit
	 */
	public Boolean getRulePermit() {
		return rulePermit;
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
		if (!(object instanceof CTracked)) {
			return false;
		}
		CTracked other = (CTracked) object;
		if ((this.username == null && other.username != null)
				|| (this.username != null && !this.username
						.equals(other.username))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "entity.Tracked[username=" + username + "]";
	}

	public static CTracked defaultCTracked(CTracked ctracked) {
		if (ctracked == null)
			return null;
		if (ctracked.getCountry() == null)
			ctracked.setCountry("VN");
		if (ctracked.getEmbedded() == null)
			ctracked.setEmbedded(false);
		if (ctracked.getGmt() == null)
			ctracked.setGmt(7);
		if (ctracked.getIconPath() == null)
			ctracked
					.setIconPath("http://maps.google.com/mapfiles/ms/micons/red-dot.png");
		if (ctracked.getIntervalGps() == null)
			ctracked.setIntervalGps(10);
		if (ctracked.getLang() == null)
			ctracked.setLang("vi");
		if (ctracked.getSchedule() == null)
			ctracked.setSchedule(new byte[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
					1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
		if (ctracked.getShowInMap() == null) {
			ctracked.setShowInMap(false);
		}
		if (ctracked.isActive() == null) {
			ctracked.setActive(true);
		}
		if (ctracked.rulePermit == null) {
			ctracked.setRulePermit(false);
		}
		return ctracked;
	}
}

package com.bkitmobile.poma.database.server.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.bkitmobile.poma.database.client.entity.CTracked;
import com.google.appengine.api.datastore.ShortBlob;

/**
 * 
 * @author Hieu Rocker
 */
@Entity
@Table(name = "tracked")
public class Tracked implements Serializable {
	private static final long serialVersionUID = -5188068218913985964L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "USERNAME")
	private Long username;

	@Basic(optional = false)
	@Column(name = "PASSWORD")
	private String password;

	@Basic(optional = true)
	@Column(name = "NAME")
	private String name;

	@Basic(optional = true)
	@Column(name = "BIRTHDAY")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Basic(optional = false)
	@Column(name = "TEL")
	private String tel;

	@Basic(optional = true)
	@Column(name = "EMAIL")
	private String email;

	@Basic(optional = true)
	@Column(name = "ACTIVE")
	private Boolean active;

	@Basic(optional = true)
	@Column(name = "GMT")
	private Integer gmt;

	@Basic(optional = true)
	@Column(name = "LANG")
	private String lang;

	@Basic(optional = true)
	@Column(name = "COUNTRY")
	private String country;

	@Basic(optional = true)
	@Column(name = "ICONPATH")
	private String iconpath;

	@Basic(optional = true)
	@Column(name = "SHOWINMAP")
	private Boolean showinmap;

	@Basic(optional = true)
	@Column(name = "EMBEDDED")
	private Boolean embedded;

	@Basic(optional = true)
	@Column(name = "SCHEDULE")
	private ShortBlob schedule;

	@Basic(optional = true)
	@Column(name = "INTERVALGPS")
	private Integer intervalgps;

	@Basic(optional = true)
	@Column(name = "APIKEY")
	private String apikey;
	
	@Basic(optional = true)
	private String lastestWaypointPK;

	private Boolean rulePermit;
	
	public Tracked() {
	}

	public Tracked(String password, String name, String tel) {
		this.password = password;
		this.name = name;
		this.tel = tel;
	}

	public Tracked(String password, String name, Date birthday, String tel,
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
		if (schedule != null)
			this.schedule = new ShortBlob(schedule);
		this.intervalgps = intervalgps;
		this.apikey = apikey;
	}

	public Long getUsername() {
		return this.username;
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
		if (this.schedule == null)
			return null;
		return this.schedule.getBytes();
	}

	public void setSchedule(byte[] schedule) {
		if (schedule == null) {
			this.schedule = null;
			return;
		}
		this.schedule = new ShortBlob(schedule);
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
	 * @param lastestWaypointPK the lastestWaypointPK to set
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
	 * @param rulePermit the rulePermit to set
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
		if (!(object instanceof Tracked)) {
			return false;
		}
		Tracked other = (Tracked) object;
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

	/**
	 * Copy from a CTracked instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 */
	public void copy(CTracked obj) {
		this.username = obj.getUsername();
		this.password = obj.getPassword();
		this.name = obj.getName();
		this.birthday = obj.getBirthday();
		this.tel = obj.getTel();
		this.email = obj.getEmail();
		this.active = obj.isActive();
		this.gmt = obj.getGmt();
		this.lang = obj.getLang();
		this.country = obj.getCountry();
		this.iconpath = obj.getIconPath();
		this.showinmap = obj.getShowInMap();
		this.embedded = obj.getEmbedded();
		if (obj.getSchedule() == null)
			this.schedule = null;
		else
			this.schedule = new ShortBlob(obj.getSchedule());
		this.intervalgps = obj.getIntervalGps();
		this.apikey = obj.getApiKey();
		this.lastestWaypointPK = obj.getLastestWaypointPK();
		this.rulePermit = obj.getRulePermit();
	}

	/**
	 * Copy from a Tracked instance
	 * 
	 * @param obj
	 *            <code>obj</code> is an instance which is copied
	 * @return a new CTracked instance
	 */
	public static CTracked copy(Tracked obj) {
		CTracked ctracked = new CTracked();
		ctracked.setUsername(obj.getUsername());
		ctracked.setPassword(obj.getPassword());
		ctracked.setName(obj.getName());
		ctracked.setBirthday(obj.getBirthday());
		ctracked.setTel(obj.getTel());
		ctracked.setEmail(obj.getEmail());
		ctracked.setActive(obj.isActive());
		ctracked.setGmt(obj.getGmt());
		ctracked.setLang(obj.getLang());
		ctracked.setCountry(obj.getCountry());
		ctracked.setIconPath(obj.getIconPath());
		ctracked.setShowInMap(obj.getShowInMap());
		ctracked.setEmbedded(obj.getEmbedded());
		ctracked.setSchedule(obj.getSchedule());
		ctracked.setIntervalGps(obj.getIntervalGps());
		ctracked.setApiKey(obj.getApiKey());
		ctracked.setLastestWaypointPK(obj.getLastestWaypointPK());
		ctracked.setRulePermit(obj.getRulePermit());
		return ctracked;
	}

}

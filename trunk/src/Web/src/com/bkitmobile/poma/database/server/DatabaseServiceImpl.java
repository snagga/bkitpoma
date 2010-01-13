package com.bkitmobile.poma.database.server;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.verisign.joid.consumer.OpenIdFilter;

import com.bkitmobile.poma.util.client.Utils;
import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.database.server.entity.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	// //////////////////////////////////////////Tracker
	@Override
	public ServiceResult<CTracker> insertTracker(CTracker ctracker) {
		// Prevent SQL Injection
		ctracker = preventSQLInjCTracker(ctracker);
		ctracker = CTracker.defaultCTracker(ctracker);
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);

		if (ctracker == null || ctracker.getUsername() == null) {
			result.setMessage("insertTracker:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, ctracker.getUsername());
			if (tracker != null) {
				result.setMessage("insertTracker:failed:duplicate:" + ctracker);
			} else {
				tracker = new Tracker();
				ctracker.setPassword(md5Cool(ctracker.getPassword()));
				tracker.copy(ctracker);
				em.persist(tracker);
				result.setOK(true);
				result.setMessage("insertTracker:successed:" + ctracker);
				result.setResult(ctracker);
			}
		} catch (Exception ex) {
			String s = "insertTracker:failed:" + ex.getMessage() + ":"
					+ ctracker;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> updateTrackerPassword(String username,
			String oldPassword, String newPassword) {
		username = preventSQLInj(username);
		oldPassword = preventSQLInj(oldPassword);
		newPassword = preventSQLInj(newPassword);

		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);

		if (username == null || oldPassword == null || newPassword == null
				|| newPassword.equals("")) {
			result.setMessage("updateTrackerPassword:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null) {
				result
						.setMessage("updateTrackerPassword:failed:notFoundTracker");
			} else if (tracker.getPassword() == null
					|| !tracker.getPassword().equals(md5Cool(oldPassword))) {
				result.setMessage("updateTrackerPassword:failed:notMatch");
			} else {
				tracker.setPassword(md5Cool(newPassword));
				em.merge(tracker);
				result.setOK(true);
				result
						.setMessage("updateTrackerPassword:successed:"
								+ username);
				result.setResult(Tracker.copy(tracker));
			}
		} catch (Exception ex) {
			String s = "updateTrackerPassword:failed:" + ex.getMessage() + ":"
					+ username + ":" + oldPassword + ":" + newPassword;
			log.log(Level.SEVERE, s, ex);
			result.setMessage(s);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	@Override
	public ServiceResult<CTracker> removeTracker(String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);

		if (trackerUN == null) {
			result.setMessage("removeTracker:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("removeTracker:failed:notfound:" + trackerUN);
			} else {
				// String sql =
				// "DELETE T FROM Tracker T WHERE T.username = :username";
				// Query query = em.createQuery(sql);
				// query.setParameter("username", trackerUN);
				// query.executeUpdate();

				ServiceResult<Boolean> res1 = removeManagesByTracker(trackerUN);
				ServiceResult<Boolean> res2 = removeStaffsByTracker(trackerUN);
				if (res1.isOK() && res2.isOK()) {
					em.remove(tracker);
					result.setOK(true);
					result.setMessage("removeTracker:successed:" + trackerUN);
					result.setResult(Tracker.copy(tracker));
				} else {
					result.setMessage("removeTracker:failed:"
							+ res1.getMessage() + "&" + res2.getMessage());
				}
			}
		} catch (Exception ex) {
			String s = "removeTracker:failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> updateTracker(CTracker ctracker) {
		ctracker = preventSQLInjCTracker(ctracker);
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		if (ctracker == null || ctracker.getUsername() == null) {
			result.setMessage("updateTrackerFailed:notfound:null");
			return result;
		}
		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, ctracker.getUsername());
			if (tracker == null) {
				result.setMessage("updateTrackerFailed:notfound:" + ctracker);
			} else {
				// Update only non-null field
				if (ctracker.getAddr() != null)
					tracker.setAddr(ctracker.getAddr());
				if (ctracker.getBirthday() != null)
					tracker.setBirthday(ctracker.getBirthday());
				if (ctracker.getCountry() != null)
					tracker.setCountry(ctracker.getCountry());
				if (ctracker.getEmail() != null)
					tracker.setEmail(ctracker.getEmail());
				if (ctracker.getGmt() != null)
					tracker.setGmt(ctracker.getGmt());
				if (ctracker.getLang() != null)
					tracker.setLang(ctracker.getLang());
				if (ctracker.getPassword() != null)
					tracker.setPassword(md5Cool(ctracker.getPassword()));
				if (ctracker.getName() != null)
					tracker.setName(ctracker.getName());
				if (ctracker.getTel() != null)
					tracker.setTel(ctracker.getTel());
				if (ctracker.getType() != null)
					tracker.setType(ctracker.getType());

				// CANNOT update username: it's an ID
				// if (ctracker.getUsername() != null)
				// tracker.setUsername(ctracker.getUsername());

				if (ctracker.isActived() != null)
					tracker.setActived(ctracker.isActived());
				if (ctracker.isEnabled() != null)
					tracker.setEnabled(ctracker.isEnabled());
				if (ctracker.getApiKey() != null)
					tracker.setApiKey(ctracker.getApiKey());

				em.merge(tracker);
				result.setOK(true);
				result.setMessage("updateTracker:successed:" + ctracker);
				result.setResult(Tracker.copy(tracker));
			}
		} catch (Exception ex) {
			String s = "updateTracker:failed:" + ex.getMessage() + ":"
					+ ctracker;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> loginTracker(String username, String password) {
		return loginTracker(username, password, false);
	}

	@Override
	public ServiceResult<CTracker> loginTracker(String username,
			String password, boolean remember) {
		HttpSession session = this.getThreadLocalRequest().getSession();
		username = preventSQLInj(username);
		password = md5Cool(preventSQLInj(password));

		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);

		if (username == null) {
			result.setMessage("loginTracker:failed:notFoundOrWrongPass:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null || (!tracker.getPassword().equals(password))) {
				result.setMessage("loginTracker:failed:notFoundOrWrongPass:"
						+ username);
			} else {
				if (!tracker.isActived()) {
					result.setMessage("loginTracker:failed:notActived");
				} else if (!tracker.isEnabled()) {
					result.setMessage("loginTracker:failed:notEnabled");
				} else {
					result.setOK(true);
					result.setMessage("loginTracker:successed:" + tracker);
				}
				CTracker ctracker = Tracker.copy(tracker);
				result.setResult(ctracker);
				session
						.setAttribute("tracker_username", ctracker
								.getUsername());
				session.setAttribute("tracker_type",
						ctracker.getType() == null ? 0 : ctracker.getType());
				session.setAttribute("tracker_language",
						ctracker.getLang() == null ? "vi" : ctracker.getLang());

				// if (remember) {
				// this.getThreadLocalResponse().addCookie(
				// new Cookie("tracker_username", ctracker
				// .getUsername()));
				// }
			}
		} catch (Exception ex) {
			String s = "loginTracker:failed:" + ex.getMessage() + ":"
					+ username;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> loginTrackerFromApi(String username,
			String api) {
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);

		username = preventSQLInj(username);
		api = preventSQLInj(api);

		if (username == null || api == null) {
			result.setMessage("loginTrackerFromApi:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null || tracker.getApiKey() == null
					|| (!tracker.getApiKey().equals(api))) {
				result
						.setMessage("loginTrackerFromApi:failed:notFoundOrWrongApi:"
								+ username);
			} else {
				CTracker ctracker = Tracker.copy(tracker);
				result.setResult(ctracker);
				if (!tracker.isActived()) {
					result.setMessage("loginTrackerFromApi:failed:notActived");
				} else if (!tracker.isEnabled()) {
					result.setMessage("loginTracker:failed:notEnabled");
				} else {
					result.setOK(true);
					result.setMessage("loginTrackerFromApi:successed:"
							+ tracker);
				}
			}
		} catch (Exception ex) {
			String s = "loginTrackerFromApi:failed:" + username + ":" + api
					+ ":" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	@Override
	public ServiceResult<CTracker> logoutTracker() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		try {
			String tracker_username = (String) session
					.getAttribute("tracker_username");
			session.removeAttribute("tracker_username");
			session.removeAttribute("tracker_type");
			session.removeAttribute("tracker_language");
			session.removeAttribute("tracker_name");
			OpenIdFilter.logout(session);
			CTracker ctracker = getTracker(tracker_username).getResult();
			result.setOK(true);
			result.setMessage("logoutTracker:successed:" + ctracker);
			result.setResult(ctracker);
		} catch (Exception ex) {
			String s = "logoutTracker:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> getTracker(String username) {
		username = preventSQLInj(username);

		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);

		if (username == null) {
			result.setMessage("getTracker:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null) {
				result.setMessage("getTracker:failed:notfound:" + username);
			} else {
				result.setOK(true);
				result.setMessage("getTracker:successed:" + username);
				result.setResult(Tracker.copy(tracker));
			}
		} catch (Exception ex) {
			String s = "getTracker:failed:" + ex.getMessage() + ":" + username;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTracker>> getAllTrackers() {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTracker>> result = new ServiceResult<ArrayList<CTracker>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			String sql = "SELECT T FROM Tracker T";
			Query query = em.createQuery(sql);

			List list = query.getResultList();
			if (list == null) {
				result.setResult(new ArrayList<CTracker>());
			} else {
				ArrayList<Tracker> trackerList = new ArrayList<Tracker>(list);
				ArrayList<CTracker> ctrackerList = new ArrayList<CTracker>();
				for (Tracker tracker : trackerList) {
					ctrackerList.add(Tracker.copy(tracker));
				}
				result.setResult(ctrackerList);
			}
			result.setOK(true);
			result.setMessage("getAllTracker:successed:"
					+ result.getResult().size() + " trackers");
		} catch (Exception ex) {
			String s = "getAllTracker:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> isLogined() {
		HttpSession session = this.getThreadLocalRequest().getSession();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setMessage("isLogined:false");
		result.setResult(null);
		String tracker_username = null;
		Integer tracker_type = null;
		try {
			tracker_username = (String) session
					.getAttribute("tracker_username");
			tracker_type = (Integer) session.getAttribute("tracker_type");
		} catch (Exception ex) {
			String s = "isLogined:false:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
			return result;
		}

		CTracker ctracker = null;
		if (tracker_username != null) {
			try {
				ctracker = getTracker(tracker_username).getResult();
				result.setOK(true);
				result.setMessage("isLogined:true:" + ctracker);
				result.setResult(ctracker);
			} catch (Exception ex) {
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}

			if (ctracker == null) {
				if (tracker_type == 1) {
					ctracker = new CTracker(tracker_username, null, null,
							tracker_type);
				} else if (tracker_type == 2) {
					String tracker_name = session.getAttribute("tracker_name") instanceof String ? session
							.getAttribute("tracker_name").toString()
							: "Facebook " + tracker_username;
					ctracker = new CTracker(tracker_username, md5Cool(System
							.currentTimeMillis()
							+ ""), tracker_name, 2);
					ctracker.setActived(true);
					ctracker.setEnabled(true);
					if (insertTracker(ctracker).isOK()) {
						log.warning(ctracker.getUsername()
								+ " inserted (facebook)");
					}
				}
			}

			result.setOK(true);
			result.setMessage("isLogined:true:" + ctracker);
			result.setResult(ctracker);
		}
		return result;
	}

	// ////////////////////////////////////////// Tracked
	@Override
	public ServiceResult<CTracked> insertTracked(CTracked ctracked,
			String trackerUN, boolean isManage) {
		ctracked = preventSQLInjCTracked(ctracked);
		ctracked = CTracked.defaultCTracked(ctracked);
		trackerUN = preventSQLInj(trackerUN);

		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (ctracked == null || trackerUN == null) {
			result.setMessage("insertTracked:failed:trackedOrTrackerNull");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			boolean isOK = true;
			Tracker tracker = null;

			// Find tracker if necessary
			if (trackerUN != null) {
				tracker = em.find(Tracker.class, trackerUN);
				if (tracker == null) {
					result.setMessage("insertTracked:failed:trackerUNnotFound:"
							+ trackerUN);
					isOK = false;
				}
			}
			if (isOK) {
				ctracked.setPassword(md5Cool(ctracked.getPassword()));
				Tracked tracked = new Tracked();
				tracked.copy(ctracked);

				em.persist(tracked);
				em.clear();

				if (tracker != null) {
					// Insert into Manage or Staff table
					em.persist(isManage ? new Manage(trackerUN, tracked
							.getUsername()) : new Staff(trackerUN, tracked
							.getUsername()));
				}

				ctracked = Tracked.copy(tracked);

				result.setOK(true);
				result.setMessage("insertTracked:successed:"
						+ ctracked.getUsername() + ":" + tracker + ":"
						+ isManage);
				result.setResult(ctracked);
			}
		} catch (Exception ex) {
			String s = "insertTracked:failed:" + ex.getMessage() + ":"
					+ ctracked.getUsername();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracked> removeTracked(Long trackedID) {
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("removeTracked:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("removeTracked:failed:notfound:" + trackedID);
			} else {
				ServiceResult<Boolean> res = removeTracksByTracked(trackedID);
				if (res.isOK()) {
					em.remove(tracked);
					result.setOK(true);
					result.setMessage("removeTracked:successed:" + trackedID);
					result.setResult(Tracked.copy(tracked));
				} else {
					result.setMessage("removeTracked:failed:"
							+ res.getMessage());
				}
			}
		} catch (Exception ex) {
			String s = "removeTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracked> updateTracked(CTracked ctracked) {
		ctracked = preventSQLInjCTracked(ctracked);
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (ctracked == null || ctracked.getUsername() == null
				|| ctracked.getUsername() <= 0L) {
			result.setMessage("updateTracker:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, ctracked.getUsername());
			if (tracked == null) {
				result.setMessage("updateTracker:failed:notfound:" + ctracked);
			} else {
				// Update only non-null field

				if (ctracked.getApiKey() != null)
					tracked.setApiKey(ctracked.getApiKey());
				if (ctracked.getBirthday() != null)
					tracked.setBirthday(ctracked.getBirthday());
				if (ctracked.getCountry() != null)
					tracked.setCountry(ctracked.getCountry());
				if (ctracked.getEmail() != null)
					tracked.setEmail(ctracked.getEmail());
				if (ctracked.getEmbedded() != null)
					tracked.setEmbedded(ctracked.getEmbedded());
				if (ctracked.getGmt() != null)
					tracked.setGmt(ctracked.getGmt());
				if (ctracked.getIconPath() != null)
					tracked.setIconPath(ctracked.getIconPath());
				if (ctracked.getIntervalGps() != null)
					tracked.setIntervalGps(ctracked.getIntervalGps());
				if (ctracked.getLang() != null)
					tracked.setLang(ctracked.getLang());
				if (ctracked.getName() != null)
					tracked.setName(ctracked.getName());
				if (ctracked.getPassword() != null)
					tracked.setPassword(md5Cool(ctracked.getPassword()));
				if (ctracked.getSchedule() != null)
					tracked.setSchedule(ctracked.getSchedule());
				if (ctracked.getShowInMap() != null)
					tracked.setShowInMap(ctracked.getShowInMap());
				if (ctracked.isActive() != null)
					tracked.setActive(ctracked.isActive());
				if (ctracked.getTel() != null)
					tracked.setTel(ctracked.getTel());
				if (ctracked.getRulePermit() != null) {
					tracked.setRulePermit(ctracked.getRulePermit());
				}
				// USERNAME IS AN ID
				// if (ctracked.getUsername() != null)
				// tracked.setUsername(ctracked.getUsername());

				em.merge(tracked);
				result.setOK(true);
				result.setMessage("updateTracker:successed" + ctracked);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "updateTracker:failed:" + ex.getMessage() + ":"
					+ ctracked;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CWaypoint> updateLastestWaypointTracked(
			Long trackedID, String waypointPK) {
		waypointPK = preventSQLInj(waypointPK);
		ServiceResult<CWaypoint> result = new ServiceResult<CWaypoint>();
		result.setOK(false);

		if (trackedID == null || trackedID <= 0L || waypointPK == null) {
			result.setMessage("updateLastestWaypointTracked:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Waypoint waypoint = em.find(Waypoint.class, waypointPK);
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (waypoint == null) {
				result
						.setMessage("updateLastestWaypointTracked:failed:waypointNotFound:"
								+ waypointPK);
			} else if (tracked == null) {
				result
						.setMessage("updateLastestWaypointTracked:failed:trackedNotFound:"
								+ trackedID);
			} else {
				tracked.setLastestWaypointPK(waypointPK);
				em.merge(tracked);
				result.setOK(true);
				result.setMessage("updateLastestWaypointTracked:successed:"
						+ trackedID + ":" + waypointPK);
				result.setResult(Waypoint.copy(waypoint));
			}
		} catch (Exception ex) {
			String s = "updateLastestWaypointTracked:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	@Override
	public ServiceResult<CWaypoint> getLastestWaypointTracked(Long trackedID) {
		ServiceResult<CWaypoint> result = new ServiceResult<CWaypoint>();
		result.setOK(false);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getLastestWaypointTracked:failed:null");
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getLastestWaypointTracked:failed:notFound:"
						+ trackedID);
			} else if (tracked.getLastestWaypointPK() == null) {
				result
						.setMessage("getLastestWaypointTracked:failed:trackedHaveNoWaypoint:"
								+ trackedID);
			} else {
				Waypoint waypoint = em.find(Waypoint.class, tracked
						.getLastestWaypointPK());
				if (waypoint == null) {
					result
							.setMessage("getLastestWaypointTracked:failed:notFoundWaypoint:"
									+ tracked.getLastestWaypointPK());
				} else {
					result.setOK(true);
					result.setMessage("getLastestWaypointTracked:successed:"
							+ waypoint);
					result.setResult(Waypoint.copy(waypoint));
				}
			}
		} catch (Exception ex) {
			String s = "getLastestWaypointTracked:failed:" + trackedID + ":"
					+ ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	@Override
	public ServiceResult<CTracked> loginTrackedFromMobile(Long username,
			String password) {
		password = md5Cool(preventSQLInj(password));

		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (username == null || username <= 0L) {
			result
					.setMessage("loginTrackedFromMobile:failed:notFoundOrWrongPass:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null || (!tracked.getPassword().equals(password))) {
				result
						.setMessage("loginTrackedFromMobile:failed:notFoundOrWrongPass:"
								+ username);
			} else {
				result.setOK(true);
				result.setMessage("loginTrackedFromMobile:successed:"
						+ username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "loginTrackedFromMobile:failed:" + ex.getMessage() + ":"
					+ username;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracked> loginTrackedFromApi(Long username, String api) {
		api = preventSQLInj(api);

		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (username == null || username <= 0L) {
			result
					.setMessage("loginTrackedFromApi:failed:notFoundOrWrongApi:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null || tracked.getApiKey() == null
					|| (!tracked.getApiKey().equals(api))) {
				result
						.setMessage("loginTrackedFromApi:failed:notFoundOrWrongApi:"
								+ username);
			} else {
				result.setOK(true);
				result.setMessage("loginTrackedFromApi:successed:" + username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "loginTrackedFromApi:failed:" + ex.getMessage() + ":"
					+ username;
			log.log(Level.SEVERE, s, ex);
			result.setMessage(s);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTracked> getTracked(Long username) {
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);

		if (username == null || username <= 0L) {
			result.setMessage("getTracked:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null) {
				result.setMessage("getTracked:failed:notfound:" + username);
			} else {
				result.setOK(true);
				result.setMessage("getTracked:successed:" + username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "getTracked:failed:" + ex.getMessage() + ":" + username;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTracked>> getAllTrackeds() {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			String sql = "SELECT T FROM Tracked T";
			Query query = em.createQuery(sql);
			List list = query.getResultList();
			if (list == null) {
				result.setResult(new ArrayList<CTracked>());
			} else {
				ArrayList<Tracked> trackedList = new ArrayList<Tracked>(list);
				ArrayList<CTracked> ctrackedList = new ArrayList<CTracked>();
				for (Tracked waypoint : trackedList) {
					ctrackedList.add(Tracked.copy(waypoint));
				}
				result.setResult(ctrackedList);
			}
			result.setOK(true);
			result.setMessage("getAllTrackeds:successed:"
					+ result.getResult().size() + " trackeds");
		} catch (Exception ex) {
			String s = "getAllTrackeds:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<ArrayList<CTracked>> getTrackedsByTracker(
			String trackerUN) {
		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);
		if (trackerUN == null) {
			result.setMessage("getTrackedsByTracker:failed:null");
			return result;
		}
		try {
			ServiceResult<ArrayList<CTracked>> resManage = getTrackedsByTrackerManage(trackerUN);
			ServiceResult<ArrayList<CTracked>> resStaff = getTrackedsByTrackerStaff(trackerUN);
			ArrayList<CTracked> ctrackedList = resManage.getResult();
			if (ctrackedList != null)
				ctrackedList.addAll(resStaff.getResult());
			else
				ctrackedList = resStaff.getResult();
			result.setResult(ctrackedList);
			result.setOK(true);
			result.setMessage(resManage.getMessage() + "&"
					+ resStaff.getMessage());
		} catch (Exception ex) {
			String s = "getTrackedsByTracker:failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTracked>> getTrackedsByTrackerManage(
			String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);

		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);

		if (trackerUN == null) {
			result.setMessage("getTrackedsByTracker:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("getTrackedsByTrackerManage:failed:notfound:"
						+ trackerUN);
			} else {
				String sql = "SELECT M FROM Manage M WHERE M.trackerUN = :trackerUN";
				Query query = em.createQuery(sql);
				query.setParameter("trackerUN", trackerUN);
				List list = query.getResultList();
				ArrayList<Manage> manageList = null;
				ArrayList<CTracked> ctrackedList = new ArrayList<CTracked>();
				if (list != null) {
					manageList = new ArrayList<Manage>(list);
					for (Manage manage : manageList) {
						sql = "SELECT T FROM Tracked T WHERE T.username = :username";
						query = em.createQuery(sql);
						query.setParameter("username", manage.getTrackedID());
						list = query.getResultList();
						if (list != null) {
							for (Object tracked : list) {
								CTracked ctracked = Tracked
										.copy((Tracked) tracked);
								ctracked.setUnderManage(true);
								ctrackedList.add(ctracked);
							}
						}
					}
				}
				result.setResult(ctrackedList);
				result.setOK(true);
				result.setMessage("getTrackedsByTrackerManage:successed:"
						+ ctrackedList.size() + " trackeds");
			}
		} catch (Exception ex) {
			String s = "getTrackedsByTrackerManage:failed:" + ex.getMessage()
					+ ":" + trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTracked>> getTrackedsByTrackerStaff(
			String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);

		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);

		if (trackerUN == null) {
			result.setMessage("getTrackedsByTracker:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("getTrackedsByTrackerStaff:failed:notfound:"
						+ trackerUN);
			} else {
				String sql = "SELECT S FROM Staff S WHERE S.trackerUN = :trackerUN";
				Query query = em.createQuery(sql);
				query.setParameter("trackerUN", trackerUN);
				List list = query.getResultList();
				ArrayList<Staff> staffList = null;
				ArrayList<CTracked> ctrackedList = new ArrayList<CTracked>();
				if (list != null) {
					staffList = new ArrayList<Staff>(list);
					for (Staff staff : staffList) {
						sql = "SELECT T FROM Tracked T WHERE T.username = :username";
						query = em.createQuery(sql);
						query.setParameter("username", staff.getTrackedID());
						list = query.getResultList();
						if (list != null) {
							for (Object tracked : list) {
								CTracked ctracked = Tracked
										.copy((Tracked) tracked);
								ctracked.setUnderManage(false);
								ctrackedList.add(ctracked);
							}
						}
					}
				}
				result.setResult(ctrackedList);
				result.setOK(true);
				result.setMessage("getTrackedsByTrackerStaff:successed:"
						+ ctrackedList.size() + " trackeds");
			}
		} catch (Exception ex) {
			String s = "getTrackedsByTrackerStaff:failed:" + ex.getMessage()
					+ ":" + trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// ////////////////////////////////////////// Track
	@Override
	public ServiceResult<CTrack> newTrack(Long trackedID) {
		ServiceResult<CTrack> result = new ServiceResult<CTrack>();
		result.setOK(false);
		result.setResult(null);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("newTrackFailed:notFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("newTrackFailed:notFound:" + trackedID);
			} else {
				CTrack ctrack = new CTrack(trackedID);
				Track track = new Track();
				track.copy(ctrack);
				em.persist(track);
				em.clear();

				result.setOK(true);
				result.setMessage("newTrack:successed:" + track);
				result.setResult(Track.copy(track));
			}
		} catch (Exception ex) {
			String s = "newTrack:failed:" + ex.getMessage() + ":" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTrack> removeTrack(Long trackID) {
		ServiceResult<CTrack> result = new ServiceResult<CTrack>();
		result.setOK(false);
		result.setResult(null);

		if (trackID == null || trackID <= 0L) {
			result.setMessage("removeTrack:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setMessage("removeTrack:failed:notfound:" + trackID);
			} else {
				em.remove(track);
				result.setOK(true);
				result.setMessage("removeTrack:successed:" + track);
				result.setResult(Track.copy(track));
			}
		} catch (Exception ex) {
			result.setMessage("removeTrack:failed:" + ex.getMessage() + ":"
					+ trackID);
			ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeTracksByTracked(Long trackedID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		result.setResult(false);

		if (trackedID == null || trackedID < 0L) {
			result
					.setMessage("removeTracksByTracked:failed:trackedNotFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setOK(false);
				result
						.setMessage("removeTracksByTracked:failed:trackedNotFound:"
								+ trackedID);
			} else {
				// em.getTransaction().begin();
				// String sql =
				// "DELETE T FROM Track T WHERE T.trackedID = :trackedID";
				// Query query = em.createQuery(sql);
				// query.setParameter("trackedID", trackedID);
				// query.executeUpdate();
				// result.setOK(true);
				// result.setMessage("removeTracksByTracked:success:" +
				// trackedID);
				// em.getTransaction().commit();

				String sql = "SELECT T FROM Track T WHERE T.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				if (list != null) {
					for (Object track : list) {
						em.remove((Track) track);
					}
				}
				result.setOK(true);
				result.setMessage("removeTracksByTracked:successed:"
						+ trackedID);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeTracksByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTrack>> getAllTracks() {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTrack>> result = new ServiceResult<ArrayList<CTrack>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			String sql = "SELECT T FROM Track T";
			Query query = em.createQuery(sql);
			List list = query.getResultList();
			if (list == null) {
				result.setResult(new ArrayList<CTrack>());
			} else {
				ArrayList<Track> trackList = new ArrayList<Track>(list);
				ArrayList<CTrack> ctrackList = new ArrayList<CTrack>();
				for (Track track : trackList) {
					ctrackList.add(Track.copy(track));
				}
				result.setResult(ctrackList);
			}
			result.setOK(true);
			result.setMessage("getAllTracks:successed:"
					+ result.getResult().size() + " tracks");
		} catch (Exception ex) {
			String s = "getAllTracks:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID) {
		return getLastTracksByTracked(trackedID, -1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTrack>> getLastTracksByTracked(
			Long trackedID, int count) {
		ServiceResult<ArrayList<CTrack>> result = new ServiceResult<ArrayList<CTrack>>();
		result.setOK(false);
		result.setMessage(null);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getTracksByTracked:failed:trackedNotFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getTracksByTracked:failed:trackedNotFound:"
						+ trackedID);
			} else {
				String sql = "SELECT T FROM Track T WHERE (T.trackedID = :trackedID) AND (T.beginTime IS NOT NULL) ORDER BY T.beginTime DESC";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				if (count > 0) {
					query.setFirstResult(0);
					query.setMaxResults(count);
				}
				List list = query.getResultList();
				if (list == null) {
					result.setResult(new ArrayList<CTrack>());
				} else {
					ArrayList<Track> trackList = new ArrayList<Track>(list);
					ArrayList<CTrack> ctrackList = new ArrayList<CTrack>();
					for (Track track : trackList) {
						ctrackList.add(Track.copy(track));
					}
					result.setResult(ctrackList);
				}
				result.setOK(true);
				result.setMessage("getTracksByTracked:successed:"
						+ result.getResult().size() + " tracks");
			}
		} catch (Exception ex) {
			String s = "getTracksByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID,
			Date fromTime) {
		return getTracksByTracked(trackedID, fromTime, new Date());
	}

	@SuppressWarnings("unchecked")
	public ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID,
			Date fromTime, Date toTime) {
		ServiceResult<ArrayList<CTrack>> result = new ServiceResult<ArrayList<CTrack>>();
		result.setOK(false);
		result.setMessage(null);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getTracksByTracked:failed:trackedNotFound:null");
			return result;
		}

		if (fromTime == null || toTime == null) {
			result.setMessage("getTracksByTracked:failed:dateError:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getTracksByTracked:failed:trackedNotFound:"
						+ trackedID);
			} else {
				String sql = "SELECT T FROM Track T WHERE (T.trackedID = :trackedID) "
						+ "AND (T.beginTime >= :beginTime) AND (T.beginTime <= :endTime) ORDER BY T.beginTime ASC";

				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				query.setParameter("beginTime", fromTime);
				query.setParameter("endTime", toTime);

				List list = query.getResultList();
				if (list == null) {
					result.setResult(new ArrayList<CTrack>());
				} else {
					ArrayList<Track> trackList = new ArrayList<Track>(list);
					ArrayList<CTrack> ctrackList = new ArrayList<CTrack>();
					for (Track track : trackList) {
						ctrackList.add(Track.copy(track));
					}
					result.setResult(ctrackList);
				}

				result.setOK(true);
				result.setMessage("getTracksByTracked:successed:"
						+ result.getResult().size() + " tracks");
			}
		} catch (Exception ex) {
			String s = "getTracksByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// e.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// ////////////////////////////////////////// Waypoint
	@Override
	public ServiceResult<CWaypoint> insertWaypoint(Long trackID, double lat,
			double lng, long speed) {
		ServiceResult<CWaypoint> result = new ServiceResult<CWaypoint>();
		result.setOK(false);
		result.setResult(null);

		if (trackID == null || trackID <= 0L) {
			result.setMessage("insertWaypoint:failed:trackNotFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setMessage("insertWaypoint:failed:trackNotFound:"
						+ trackID);
			} else {
				Waypoint waypoint = new Waypoint(lat, lng, speed, trackID);
				em.persist(waypoint);

				// Change the begin time and end time of track
				if (track.getBeginTime() == null) {
					// This track is new, beginTime need to be initialized
					track.setBeginTime(waypoint.getTime());
				}

				track.setEndTime(waypoint.getTime());
				em.merge(track);
				try {
					Tracked tracked = em.find(Tracked.class, track
							.getTrackedID());
					tracked.setLastestWaypointPK(waypoint.getWaypointPK());
					em.merge(tracked);
					String s = "updateLastestWaypointTracked:successed:"
							+ track.getTrackedID() + ":"
							+ waypoint.getWaypointPK();
					log.info(s);
				} catch (Exception ex) {
					String s = "updateLastestWaypointTracked:failed:"
							+ track.getTrackedID() + ":"
							+ waypoint.getWaypointPK();
					log.log(Level.SEVERE, s, ex);
				}
				result.setOK(true);
				result.setMessage("insertWaypoint:successed:" + waypoint);
				result.setResult(Waypoint.copy(waypoint));
			}
		} catch (Exception ex) {
			String s = "insertWaypoint:failed:" + ex.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeWaypointsByTrack(Long trackID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		result.setResult(false);

		if (trackID == null || trackID <= 0L) {
			result
					.setMessage("removeWaypointsByTrack:failed:trackNotFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result
						.setMessage("removeWaypointsByTrack:failed:trackNotFound:"
								+ trackID);
			} else {
				String sql = "SELECT W FROM Waypoint W WHERE W.trackID = :trackID";
				Query query = em.createQuery(sql);
				query.setParameter("trackID", trackID);
				List list = query.getResultList();
				if (list != null) {
					for (Object waypoint : list) {
						em.remove((Waypoint) waypoint);
					}
				}
				result.setOK(true);
				result
						.setMessage("removeWaypointsByTrack:successed:"
								+ trackID);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeWaypointsByTrack:failed:" + ex.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CWaypoint>> getWaypointsByTrack(Long trackID) {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CWaypoint>> result = new ServiceResult<ArrayList<CWaypoint>>();
		result.setOK(false);
		result.setMessage(null);
		try {

			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setOK(false);
				result.setMessage("getWaypointByTrack:failed:trackNotFound:"
						+ trackID);
			} else {
				String sql = "SELECT W FROM Waypoint W WHERE W.trackID = "
						+ trackID + " ORDER BY W.time DESC";
				Query query = em.createQuery(sql);
				List list = query.getResultList();
				if (list == null) {
					result.setResult(new ArrayList<CWaypoint>());
				} else {
					ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>(
							list);
					ArrayList<CWaypoint> cwaypointList = new ArrayList<CWaypoint>();
					for (Waypoint waypoint : waypointList) {
						cwaypointList.add(Waypoint.copy(waypoint));
					}
					result.setResult(cwaypointList);
				}
				result.setOK(true);
				result.setMessage("getWaypointsByTrack:successed:"
						+ result.getResult().size() + " waypoints");
			}
		} catch (Exception ex) {
			String s = "getWaypointsByTrack:failed" + ex.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<ArrayList<CWaypoint>> getWaypointByTrack(Long trackID,
			Date fromTime) {
		return getWaypointsByTrack(trackID, fromTime, new Date());
	}

	@SuppressWarnings("unchecked")
	public ServiceResult<ArrayList<CWaypoint>> getWaypointsByTrack(
			Long trackID, Date fromTime, Date toTime) {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CWaypoint>> result = new ServiceResult<ArrayList<CWaypoint>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setOK(false);
				result.setMessage("getNewWaypointByTrack:failed:trackNotFound:"
						+ trackID);
			} else {
				String sql = "SELECT W FROM Waypoint W WHERE "
						+ "(W.trackID = :trackID) AND (W.time >= :fromTime) AND (W.time <= :toTime) ORDER BY W.time ASC";
				Query query = em.createQuery(sql);
				query.setParameter("trackID", trackID);
				query.setParameter("fromTime", fromTime);
				query.setParameter("toTime", toTime);

				List list = query.getResultList();
				if (list == null) {
					result.setResult(new ArrayList<CWaypoint>());
				} else {
					ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>(
							list);
					ArrayList<CWaypoint> cwaypointList = new ArrayList<CWaypoint>();
					for (Waypoint waypoint : waypointList) {
						cwaypointList.add(Waypoint.copy(waypoint));
					}
					result.setResult(cwaypointList);
				}
				result.setOK(true);
				result.setMessage("getNewWaypointByTrack:successed:"
						+ result.getResult().size() + " waypoints");
			}
		} catch (Exception ex) {
			String s = "getNewWaypointByTrack:failed" + ex.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// ////////////////////////////////////////// Manage
	@Override
	public ServiceResult<CManage> insertManage(String trackerUN, Long trackedID) {
		// Prevent SQL Injection
		trackerUN = preventSQLInj(trackerUN);

		ServiceResult<CManage> result = new ServiceResult<CManage>();
		result.setOK(false);
		result.setMessage(null);

		if (trackerUN == null || trackedID == null || trackedID <= 0L) {
			result.setMessage("insertManage:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			Tracked tracked = em.find(Tracked.class, trackedID);
			String managePK = trackerUN + "#" + trackedID;
			Manage manage = em.find(Manage.class, managePK);
			Staff staff = em.find(Staff.class, managePK);
			if (tracker == null) {
				result.setMessage("insertManage:failed:trackerNotFound:"
						+ trackerUN);
			} else if (tracked == null) {
				result.setMessage("insertManageFailed:trackedNotFound:"
						+ trackedID);
			} else if (manage != null) {
				result.setMessage("insertManageFailed:duplicate:manage:"
						+ manage);
			} else if (staff != null) {
				result.setMessage("insertManageFailed:duplicate:staff:"
						+ manage);
			} else {
				manage = new Manage(managePK);
				em.persist(manage);
				result.setOK(true);
				result.setMessage("insertManage:successed:" + manage);
				result.setResult(Manage.copy(manage));
			}
		} catch (Exception ex) {
			String s = "insertManage:failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CManage>> getManagesByTracked(Long trackedID) {
		ServiceResult<ArrayList<CManage>> result = new ServiceResult<ArrayList<CManage>>();
		result.setOK(false);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getManagesByTracked:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getManagesByTracked:failed:notfound:"
						+ trackedID);
			} else {
				String sql = "SELECT M FROM Manage M WHERE M.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				ArrayList<CManage> cmanageList = new ArrayList<CManage>();
				if (list != null) {
					for (Object obj : list) {
						cmanageList.add(Manage.copy((Manage) obj));
					}
				}
				result.setResult(cmanageList);
				result.setOK(true);
				result.setMessage("getManagesByTracked:successed:"
						+ cmanageList.size() + " manages");
			}
		} catch (Exception ex) {
			String s = "getManagesByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CManage> removeManage(String trackerUN, Long trackedID) {
		trackerUN = preventSQLInj(trackerUN);
		ServiceResult<CManage> result = new ServiceResult<CManage>();
		result.setOK(false);
		result.setMessage(null);

		if (trackerUN == null || trackedID == null || trackedID <= 0L) {
			result.setMessage("removeManage:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			String managePK = trackerUN + "#" + trackedID;
			Manage manage = em.find(Manage.class, managePK);
			if (manage == null) {
				result.setMessage("removeManage:failed:notFound" + manage);
			} else {
				em.remove(manage);
				result.setMessage("removeManage:successed:" + manage);
				result.setOK(true);
				result.setResult(Manage.copy(manage));
			}
		} catch (Exception ex) {
			String s = "removeManage:failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeManagesByTracker(String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);

		if (trackerUN == null) {
			result.setMessage("removeManagesByTracker:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracker tracker = (Tracker) em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setOK(false);
				result
						.setMessage("removeManagesByTracker:failed:trackerNotFound:"
								+ trackerUN);
			} else {
				String sql = "SELECT M FROM Manage M WHERE M.trackerUN = :trackerUN";
				Query query = em.createQuery(sql);
				query.setParameter("trackerUN", trackerUN);
				List list = query.getResultList();
				if (list != null) {
					for (Object manage : list) {
						em.remove((Manage) manage);
					}
				}
				result.setOK(true);
				result
						.setMessage("removeManagesByTracker:successed:"
								+ tracker);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeManagesByTracker:failed:" + ex.getMessage()
					+ trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeManagesByTracked(Long trackedID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		result.setResult(false);
		if (trackedID == null || trackedID <= 0L) {
			result
					.setMessage("removeManagesByTrackedFailed:trackedNotFound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracked tracked = (Tracked) em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result
						.setMessage("removeManagesByTrackedFailed:trackedNotFound:"
								+ trackedID);
			} else {
				String sql = "SELECT M FROM Manage M WHERE M.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				if (list != null) {
					for (Object manage : list) {
						em.remove((Manage) manage);
					}
				}
				result.setOK(true);
				result
						.setMessage("removeManagesByTracked:successed:"
								+ tracked);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeManagesByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<Boolean> insertManages(Long trackedID,
			ArrayList<String> arrManage) {
		// Prevent SQL Injection
		for (int i = 0; i < arrManage.size(); i++) {
			arrManage.set(i, preventSQLInj(arrManage.get(i)));
		}

		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);

		if (trackedID <= 0L) {
			result.setMessage("insertManage:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("insertManageFailed:trackedNotFound:"
						+ trackedID);
			} else {
				for (int i = 0; i < arrManage.size(); i++) {
					Tracker tracker = em.find(Tracker.class, arrManage.get(i));
					if (tracker == null) {
						result
								.setMessage("insertManage:failed:trackerNotFound:"
										+ tracker.getUsername());
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					String managePK = tracker.getUsername() + "#" + trackedID;
					Manage manage = em.find(Manage.class, managePK);
					Staff staff = em.find(Staff.class, managePK);

					if (manage != null) {
						result
								.setMessage("insertManageFailed:duplicate:manage:"
										+ manage);
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					if (staff != null) {
						result.setMessage("insertManageFailed:duplicate:staff:"
								+ manage);
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					manage = new Manage(managePK);
					em.persist(manage);
				}// end for

				result.setOK(true);
				result.setResult(true);
				result.setMessage("insertManage:successed:" + arrManage);
			}
		} catch (Exception ex) {
			String s = "insertManage:failed:" + ex.getMessage() + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// ////////////////////////////////////////// Staff
	@Override
	public ServiceResult<CStaff> insertStaff(String trackerUN, Long trackedID) {
		// Prevent SQL Injection
		trackerUN = preventSQLInj(trackerUN);

		ServiceResult<CStaff> result = new ServiceResult<CStaff>();
		result.setOK(false);
		result.setResult(null);

		if (trackerUN == null || trackedID == null || trackedID <= 0L) {
			result.setMessage("insertStaff:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			Tracked tracked = em.find(Tracked.class, trackedID);
			String staffPK = trackerUN + "#" + trackedID;
			Staff staff = em.find(Staff.class, staffPK);
			Manage manage = em.find(Manage.class, staffPK);
			if (tracker == null) {
				result.setMessage("insertStaff:failed:trackerNotFound:"
						+ trackerUN);
			} else if (tracked == null) {
				result.setMessage("insertStaff:failed:trackedNotFound:"
						+ trackedID);
			} else if (staff != null) {
				result
						.setMessage("insertStaff:failed:duplicate:staff:"
								+ staff);
			} else if (manage != null) {
				result.setMessage("insertStaff:failed:duplicate:manage:"
						+ staff);
			} else {
				staff = new Staff(trackerUN, trackedID);
				em.persist(staff);
				// em.flush();
				result.setOK(true);
				result.setMessage("insertStaff:successed:" + staff);
				result.setResult(Staff.copy(staff));
			}
		} catch (Exception ex) {
			String s = "insertStaff:failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CStaff>> getStaffsByTracked(Long trackedID) {
		ServiceResult<ArrayList<CStaff>> result = new ServiceResult<ArrayList<CStaff>>();
		result.setOK(false);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getStaffsByTracked:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getStaffsByTracked:failed:notfound:"
						+ trackedID);
			} else {
				String sql = "SELECT S FROM Staff S WHERE S.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				ArrayList<CStaff> cstaffList = new ArrayList<CStaff>();
				if (list != null) {
					for (Object obj : list) {
						cstaffList.add(Staff.copy((Staff) obj));
					}
				}
				result.setResult(cstaffList);
				result.setOK(true);
				result.setMessage("getStaffsByTracked:successed:"
						+ cstaffList.size() + " staffs");
			}
		} catch (Exception ex) {
			String s = "getStaffsByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CStaff> removeStaff(String trackerUN, Long trackedID) {
		trackerUN = preventSQLInj(trackerUN);
		ServiceResult<CStaff> result = new ServiceResult<CStaff>();
		result.setOK(false);
		result.setMessage(null);

		if (trackerUN == null || trackedID == null || trackedID <= 0L) {
			result.setMessage("removeStaff:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			String staffPK = trackerUN + "#" + trackedID;
			Staff staff = em.find(Staff.class, staffPK);
			if (staff == null) {
				result.setMessage("removeStaff:failed:notFound:" + staffPK);
			} else {
				em.remove(staff);
				result.setOK(true);
				result.setMessage("removeStaff:successed:" + staff);
				result.setResult(Staff.copy(staff));
			}
		} catch (Exception ex) {
			String s = "removeStaff:failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeStaffsByTracker(String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		result.setResult(false);

		if (trackerUN == null) {
			result.setMessage("removeStaffsByTracker:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracker tracker = (Tracker) em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result
						.setMessage("removeStaffsByTracker:failed:trackerNotFound:"
								+ trackerUN);
			} else {
				String sql = "Select S FROM Staff S WHERE S.trackerUN = :trackerUN";
				Query query = em.createQuery(sql);
				query.setParameter("trackerUN", trackerUN);
				List list = query.getResultList();
				if (list != null) {
					for (Object staff : list) {
						em.remove((Staff) staff);
					}
				}
				result.setOK(true);
				result.setMessage("removeStaffsByTracker:successed:" + tracker);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeStaffsByTracker:failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<Boolean> removeStaffsByTracked(Long trackedID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		result.setResult(false);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("removeStaffsByTracker:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result
						.setMessage("removeStaffsByTracked:failed:trackedNotFound:"
								+ trackedID);
			} else {
				String sql = "SELECT S FROM Staff S WHERE S.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				if (list != null) {
					for (Object staff : list) {
						em.remove((Staff) staff);
					}
				}
				result.setOK(true);
				result.setMessage("removeStaffsByTracked:successed:"
						+ trackedID);
				result.setResult(true);
			}
		} catch (Exception ex) {
			String s = "removeStaffsByTracked:failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<Boolean> insertStaffs(Long trackedID,
			ArrayList<String> arrStaff) {
		// Prevent SQL Injection
		for (int i = 0; i < arrStaff.size(); i++) {
			arrStaff.set(i, preventSQLInj(arrStaff.get(i)));
		}

		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);

		if (trackedID <= 0L) {
			result.setMessage("insertStaff:failed:null:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("insertStaffFailed:trackedNotFound:"
						+ trackedID);
			} else {
				for (int i = 0; i < arrStaff.size(); i++) {
					Tracker tracker = em.find(Tracker.class, arrStaff.get(i));
					if (tracker == null) {
						result.setMessage("insertStaff:failed:trackerNotFound:"
								+ tracker.getUsername());
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					String staffPK = tracker.getUsername() + "#" + trackedID;
					Staff staff = em.find(Staff.class, staffPK);
					Manage manage = em.find(Manage.class, staffPK);

					if (staff != null) {
						result.setMessage("insertStaffFailed:duplicate:staff:"
								+ staff);
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					if (manage != null) {
						result.setMessage("insertStaffFailed:duplicate:manage:"
								+ manage);
						result.setOK(false);
						result.setResult(false);
						return result;
					}

					staff = new Staff(staffPK);
					em.persist(staff);
				}// end for

				result.setOK(true);
				result.setResult(true);
				result.setMessage("insertStaff:successed:" + arrStaff);
			}
		} catch (Exception ex) {
			String s = "insertStaff:failed:" + ex.getMessage() + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}
	
	// //////////////////////////////////////////// TrackedRule
	@Override
	public ServiceResult<CTrackedRule> insertTrackedRule(
			CTrackedRule ctrackedRule) {
		ServiceResult<CTrackedRule> result = new ServiceResult<CTrackedRule>();
		result.setOK(false);

		ctrackedRule = CTrackedRule.defaultCTrackedRule(ctrackedRule);

		if (ctrackedRule == null) {
			result.setMessage("insertTrackedRule:failed:null");
			return result;
		}

		if (!ctrackedRule.isValid()) {
			result.setMessage("insertTrackedRule:failed:notValid");
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			TrackedRule trackedRule = new TrackedRule();
			trackedRule.copy(ctrackedRule);
			em.persist(trackedRule);
			em.clear();
			ctrackedRule = TrackedRule.copy(trackedRule);
			result.setOK(true);
			result.setMessage("insertTrackedRule:successed:" + ctrackedRule);
			result.setResult(ctrackedRule);
		} catch (Exception ex) {
			String s = "insertTrackedRule:failed:" + ex.getMessage() + ":"
					+ ctrackedRule;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTrackedRule> removeTrackedRule(Long trackedRuleID) {
		ServiceResult<CTrackedRule> result = new ServiceResult<CTrackedRule>();
		result.setOK(false);

		if (trackedRuleID == 0 || trackedRuleID <= 0) {
			result.setMessage("removeTrackedRule:failed:rule");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			TrackedRule trackedRule = em.find(TrackedRule.class, trackedRuleID);
			if (trackedRule == null) {
				result.setMessage("removeTrackedRule:failed:notFound:"
						+ trackedRuleID);
			} else {
				em.remove(trackedRule);
				result.setOK(true);
				result.setMessage("removeTrackedRule:successed:" + trackedRule);
				result.setResult(TrackedRule.copy(trackedRule));
			}
		} catch (Exception ex) {
			String s = "removeTrackedRule:failed:" + ex.getMessage() + ":"
					+ trackedRuleID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<ArrayList<CTrackedRule>> getTrackedRuleByTracked(
			Long trackedID) {
		ServiceResult<ArrayList<CTrackedRule>> result = new ServiceResult<ArrayList<CTrackedRule>>();
		result.setOK(false);

		if (trackedID == null || trackedID <= 0L) {
			result.setMessage("getTrackedRuleByTracked:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getTrackedRuleByTracked:failed:notfound:"
						+ trackedID);
			} else {
				String sql = "SELECT T FROM TrackedRule T WHERE T.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				List list = query.getResultList();
				ArrayList<CTrackedRule> ctrackedRuleList = new ArrayList<CTrackedRule>();
				if (list != null) {
					for (Object obj : list) {
						ctrackedRuleList.add(TrackedRule
								.copy((TrackedRule) obj));
					}
				}
				result.setResult(ctrackedRuleList);
				result.setOK(true);
				result.setMessage("getTrackedRuleByTracked:successed:"
						+ ctrackedRuleList.size() + " manages");
			}
		} catch (Exception ex) {
			String s = "getTrackedRuleByTracked:failed:" + ex.getMessage()
					+ ":" + trackedID;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CTrackedRule> updateTrackedRule(
			CTrackedRule ctrackedRule) {
		ServiceResult<CTrackedRule> result = new ServiceResult<CTrackedRule>();
		result.setOK(false);

		if (ctrackedRule == null) {

		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			TrackedRule trackedRule = em.find(TrackedRule.class, ctrackedRule
					.getRuleID());
		} catch (Exception ex) {
			String s = "updateTrackedRule:failed:" + ex.getMessage() + ":"
					+ ctrackedRule;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	// //////////////////////////////////////// Stuff
	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList> executeQuery(String sql) {
		ServiceResult<ArrayList> result = new ServiceResult<ArrayList>();
		result.setOK(false);

		if (sql == null || sql.trim().equals("")) {
			result.setMessage("executeQuery:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			Query query = em.createQuery(sql);
			ArrayList arr = new ArrayList();
			if (sql.trim().toUpperCase().startsWith("UPDATE")
					|| sql.trim().toUpperCase().startsWith("DELETE")) {
				Integer count = query.executeUpdate();
				arr.add(count);
			} else {
				List list = query.getResultList();
				if (list != null) {
					for (Object o : list) {
						if (o instanceof Tracker) {
							arr.add(Tracker.copy((Tracker) o));
						} else if (o instanceof Tracked) {
							arr.add(Tracked.copy((Tracked) o));
						} else if (o instanceof Track) {
							arr.add(Track.copy((Track) o));
						} else if (o instanceof Manage) {
							arr.add(Manage.copy((Manage) o));
						} else if (o instanceof Staff) {
							arr.add(Staff.copy((Staff) o));
						} else if (o instanceof Waypoint) {
							arr.add(Waypoint.copy((Waypoint) o));
						} else if (o instanceof TrackedRule) {
							arr.add(TrackedRule.copy((TrackedRule) o));
						}
					}
				}
			}

			result.setOK(true);
			result.setResult(arr);
			result.setMessage("executeQuery:successed:" + sql + ":"
					+ arr.size() + " entry");
		} catch (Exception ex) {
			String s = "executeQuery:failed:" + ex.getMessage() + ":" + sql;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	@Override
	public ServiceResult<Boolean> resetDatabase(String secretKey) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();

		if (secretKey == null || !secretKey.equals("blablabla")) {
			result.setMessage("resetDatabase:failed:wrongKey");
			result.setResult(false);
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();

		try {
			Query query = em.createQuery("DELETE FROM Waypoint W");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM Manage M");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM Staff S");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM TrackedRule TR");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM Track T");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM Tracked T");
			query.executeUpdate();
			query = em.createQuery("DELETE FROM Tracker T");
			query.executeUpdate();
			result.setOK(true);
			result.setMessage("resetDatabase:successed");
			result.setResult(true);
		} catch (Exception ex) {
			String s = "resetDatabase:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}

		return result;
	}

	// /////////////////////////////////////////////// AdminConfig
	@Override
	public ServiceResult<CAdminConfig> addRecord(String name, String value) {
		name = preventSQLInj(name);
		value = preventSQLInj(value);
		ServiceResult<CAdminConfig> result = new ServiceResult<CAdminConfig>();
		result.setOK(false);
		result.setResult(null);
		CAdminConfig cAdminConfig;
		AdminConfig adminConfig = null;

		if (name == null) {
			result.setMessage("addRecord:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			adminConfig = em.find(AdminConfig.class, name);
			if (adminConfig != null) {
				adminConfig.setValue(value);
				em.merge(adminConfig);
			} else {
				adminConfig = new AdminConfig(name, value);
				em.persist(adminConfig);
			}
			cAdminConfig = AdminConfig.copy(adminConfig);
			result.setResult(cAdminConfig);
			result.setOK(true);
			result.setMessage("addRecord:successed:" + adminConfig);
			result.setResult(cAdminConfig);
		} catch (Exception ex) {
			String s = "addRecord:failed:" + ex.getMessage() + ":"
					+ adminConfig;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CAdminConfig> removeRecord(String name) {
		name = preventSQLInj(name);
		ServiceResult<CAdminConfig> result = new ServiceResult<CAdminConfig>();
		result.setOK(false);
		result.setMessage(null);

		if (name == null || name.equals("")) {
			result.setMessage("removeRecord:failed:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		AdminConfig adminConfig = null;
		try {
			adminConfig = em.find(AdminConfig.class, name);
			if (adminConfig == null) {
				result.setMessage("removeRecord:failed:notFound:" + name);
			} else {
				em.remove(adminConfig);
				result.setOK(true);
				result.setMessage("removeRecord:successed:" + adminConfig);
				result.setResult(AdminConfig.copy(adminConfig));
			}
		} catch (Exception ex) {
			String s = "removeRecord:failed:" + adminConfig + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public ServiceResult<CAdminConfig> getRecord(String name) {
		name = preventSQLInj(name);

		ServiceResult<CAdminConfig> result = new ServiceResult<CAdminConfig>();
		result.setOK(false);
		result.setResult(null);

		if (name == null || name.equals("")) {
			result.setMessage("getRecord:failed:notfound:null");
			return result;
		}

		EntityManager em = EMF.get().createEntityManager();
		try {
			AdminConfig admnConfig = em.find(AdminConfig.class, name);
			if (admnConfig == null) {
				result.setMessage("getRecord:failed:notfound:" + name);
			} else {
				result.setOK(true);
				result.setMessage("getRecord:successed:" + name);
				result.setResult(AdminConfig.copy(admnConfig));
			}
		} catch (Exception ex) {
			String s = "getRecord:failed:" + ex.getMessage() + ":" + name;
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public ServiceResult<HashMap<String, String>> getAllRecords() {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<HashMap<String, String>> result = new ServiceResult<HashMap<String, String>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			String sql = "SELECT A FROM AdminConfig A";
			Query query = em.createQuery(sql);

			List list = query.getResultList();
			if (list == null) {
				result.setResult(new HashMap<String, String>());
			} else {
				ArrayList<AdminConfig> adminConfigList = new ArrayList<AdminConfig>(
						list);
				HashMap<String, String> hashMapAdminConfig = new HashMap<String, String>();
				for (AdminConfig adminConfig : adminConfigList) {
					hashMapAdminConfig.put(adminConfig.getName(), adminConfig
							.getValue());
				}
				result.setResult(hashMapAdminConfig);
			}
			result.setOK(true);
			result.setMessage("getAllAdminConfig:successed:"
					+ result.getResult().size() + " records");
		} catch (Exception ex) {
			String s = "getAllAdminConfig:failed:" + ex.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public ServiceResult<Boolean> setAllRecords(HashMap<String, String> hashMap) {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);

		Set<String> setKey = hashMap.keySet();
		Iterator<String> iterator = setKey.iterator();
		try {
			while (iterator.hasNext()) {
				String key = iterator.next();
				AdminConfig adminConfig = em.find(AdminConfig.class, key);
				if (adminConfig != null) {
					adminConfig.setValue(hashMap.get(key));
					em.merge(adminConfig);
				} else {
					adminConfig = new AdminConfig(key, hashMap.get(key));
					em.persist(adminConfig);
				}
				result.setMessage("setAllRecords:successed:" + hashMap.size());
				result.setOK(true);
				result.setResult(true);
			}
		} catch (Exception e) {
			String s = "setAllRecords:failed:" + e.getMessage();
			result.setMessage(s);
			log.log(Level.SEVERE, s, e);
			e.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
		
	private static Policy policy = null;

	private static Policy getAntiSamyPolicy() {
		Policy policy = null;
		try {
			File file = new File("otherconfigs/antisamy-anythinggoes-1.3.xml");
			policy = Policy.getInstance(file);
		} catch (PolicyException pe) {
			log.log(Level.SEVERE, "getAntiSamyPolicy:" + pe.getMessage(), pe);
		}
		return policy;
	}

	public static String preventSQLInj(String dirtyInput) {
		if (dirtyInput == null)
			return null;
		CleanResults cr = null;
		try {
			if (policy == null)
				policy = getAntiSamyPolicy();
			AntiSamy as = new AntiSamy();
			cr = as.scan(dirtyInput, policy);
		} catch (Exception ex) {
			log.log(Level.SEVERE, "preventSQLInj:" + ex.getMessage(), ex);
		}
		return cr.getCleanHTML(); // some custom function
	}

	// //////////////////////////////////////////////// SQL Injection
	public static CTracker preventSQLInjCTracker(CTracker ctracker) {
		if (ctracker == null)
			return null;
		ctracker.setAddr(preventSQLInj(ctracker.getAddr()));
		ctracker.setCountry(preventSQLInj(ctracker.getCountry()));
		ctracker.setEmail(preventSQLInj(ctracker.getEmail()));
		ctracker.setLang(preventSQLInj(ctracker.getLang()));
		ctracker.setPassword(preventSQLInj(ctracker.getPassword()));
		ctracker.setName(preventSQLInj(ctracker.getName()));
		ctracker.setTel(preventSQLInj(ctracker.getTel()));
		ctracker.setUsername(preventSQLInj(ctracker.getUsername()));
		ctracker.setApiKey(preventSQLInj(ctracker.getApiKey()));
		return ctracker;
	}

	public static CTracked preventSQLInjCTracked(CTracked ctracked) {
		if (ctracked == null)
			return null;
		ctracked.setApiKey(preventSQLInj(ctracked.getApiKey()));
		ctracked.setCountry(preventSQLInj(ctracked.getCountry()));
		ctracked.setEmail(preventSQLInj(ctracked.getEmail()));
		ctracked.setIconPath(preventSQLInj(ctracked.getIconPath()));
		ctracked.setLang(preventSQLInj(ctracked.getLang()));
		ctracked.setName(preventSQLInj(ctracked.getName()));
		ctracked.setPassword(preventSQLInj(ctracked.getPassword()));
		ctracked.setTel(preventSQLInj(ctracked.getTel()));
		return ctracked;
	}

	public static CManage preventSQLInjCManage(CManage cmanage) {
		if (cmanage == null)
			return null;
		cmanage.setManagePK(preventSQLInj(cmanage.getManagePK()));
		cmanage.setTrackerUN(preventSQLInj(cmanage.getTrackerUN()));
		return cmanage;
	}

	public static CStaff preventSQLInjCStaff(CStaff cstaff) {
		if (cstaff == null)
			return null;
		cstaff.setStaffPK(preventSQLInj(cstaff.getStaffPK()));
		cstaff.setTrackerUN(preventSQLInj(cstaff.getTrackerUN()));
		return cstaff;
	}

	public static CTrack preventSQLInjCTrack(CTrack ctrack) {
		return ctrack;
	}

	public static CWaypoint preventSQLInjCWaypoint(CWaypoint cwaypoint) {
		if (cwaypoint == null)
			return null;
		cwaypoint.setWaypointPK(preventSQLInj(cwaypoint.getWaypointPK()));
		return cwaypoint;
	}

	public static String md5Cool(String value) {
		MessageDigest algorithm = null;

		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			log.severe("[MD5]Cannot find digest algorithm");
			return null;
		}

		byte[] defaultBytes = value.getBytes();
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private final static Logger log = Logger
			.getLogger(DatabaseServiceImpl.class.getName());

}

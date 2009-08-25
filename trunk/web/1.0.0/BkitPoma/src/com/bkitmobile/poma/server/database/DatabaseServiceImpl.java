package com.bkitmobile.poma.server.database;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import com.bkitmobile.poma.client.Utils;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CManage;
import com.bkitmobile.poma.client.database.entity.CStaff;
import com.bkitmobile.poma.client.database.entity.CTrack;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.database.entity.CWaypoint;
import com.bkitmobile.poma.server.database.entity.Manage;
import com.bkitmobile.poma.server.database.entity.Staff;
import com.bkitmobile.poma.server.database.entity.Track;
import com.bkitmobile.poma.server.database.entity.Tracked;
import com.bkitmobile.poma.server.database.entity.Tracker;
import com.bkitmobile.poma.server.database.entity.Waypoint;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements
		DatabaseService {

	// //////////////////////////////////////////Tracker
	@Override
	public ServiceResult<CTracker> insertTracker(CTracker ctracker) {
		// Prevent SQL Injection
		ctracker = preventSQLInjCTracker(ctracker);

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, ctracker.getUsername());
			if (tracker != null) {
				result.setMessage("insertTracker:Failed:duplicate:" + ctracker);
			} else {
				tracker = new Tracker();
				ctracker.setPassword(md5Cool(ctracker.getPassword()));
				tracker.copy(ctracker);
				em.persist(tracker);
				result.setOK(true);
				result.setMessage("insertTracker:Successed:" + ctracker);
				result.setResult(ctracker);
			}
		} catch (Exception ex) {
			String s = "insertTracker:Failed:" + ex.getMessage() + ":"
					+ ctracker;
			result.setMessage(s);
			log.severe(s);
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

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("removeTracker:Failed:notfound:" + trackerUN);
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
					result.setMessage("removeTracker:Successed:" + trackerUN);
					result.setResult(Tracker.copy(tracker));
				} else {
					result.setMessage("removeTracker:Failed:"
							+ res1.getMessage() + "&" + res2.getMessage());
				}
			}
		} catch (Exception ex) {
			String s = "removeTracker:Failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
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
					tracker.setPassword(ctracker.getPassword());
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

				em.merge(tracker);
				result.setOK(true);
				result.setMessage("updateTracker:Successed:" + ctracker);
				result.setResult(Tracker.copy(tracker));
			}
		} catch (Exception ex) {
			String s = "updateTracker:Failed:" + ex.getMessage() + ":"
					+ ctracker;
			result.setMessage(s);
			log.severe(s);
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
		username = preventSQLInj(username);
		password = md5Cool(preventSQLInj(password));

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null || (!tracker.getPassword().equals(password))) {
				result.setMessage("loginTracker:Failed:notFoundOrWrongPass:"
						+ username);
			} else {
				if (!tracker.isActived()) {
					result.setMessage("loginTracker:Failed:notActived");
				} else if (!tracker.isEnabled()) {
					result.setMessage("loginTracker:Failed:notEnabled");
				} else {
					result.setOK(true);
					result.setMessage("loginTracker:Successed:" + tracker);
					CTracker ctracker = Tracker.copy(tracker);
					this.getThreadLocalRequest().getSession().setAttribute(
							"ctracker", ctracker);
					result.setResult(ctracker);
				}
			}
		} catch (Exception ex) {
			String s = "loginTracker:Failed:" + ex.getMessage() + ":"
					+ username;
			result.setMessage(s);
			log.severe(s);
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
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		try {
			CTracker ctracker = (CTracker) this.getThreadLocalRequest()
					.getSession().getAttribute("ctracker");
			this.getThreadLocalRequest().getSession().removeAttribute(
					"ctracker");
			result.setOK(true);
			result.setMessage("logoutTracker:Successed:" + ctracker);
		} catch (Exception ex) {
			String s = "logoutTracker:Failed:" + ex.getMessage();
			result.setMessage(s);
			log.severe(s);
			// ex.printStackTrace();
		}
		return result;
	}

	@Override
	public ServiceResult<CTracker> getTracker(String username) {
		username = preventSQLInj(username);

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, username);
			if (tracker == null) {
				result.setMessage("getTracker:Failed:notfound:" + username);
			} else {
				result.setOK(true);
				result.setMessage("getTracker:Successed:" + username);
				result.setResult(Tracker.copy(tracker));
			}
		} catch (Exception ex) {
			String s = "getTracker:Failed:" + ex.getMessage() + ":" + username;
			result.setMessage(s);
			log.severe(s);
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
			result.setMessage("getAllTracker:Successed:"
					+ result.getResult().size() + " trackers");
		} catch (Exception e) {
			String s = "getAllTracker:Failed:" + e.getMessage();
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		result.setResult(null);
		Object obj = session.getAttribute("ctracker");
		if (obj == null || !(obj instanceof CTracker)) {
			result.setMessage("isLogined:false");
			return result;
		}
		result.setOK(true);
		CTracker ctracker = (CTracker) obj;
		result.setMessage("isLogined:true:" + ctracker);
		result.setResult(ctracker);
		return result;
	}

	// ////////////////////////////////////////// Tracked
	@Override
	public ServiceResult<CTracked> insertTracked(CTracked ctracked,
			String trackerUN, boolean isManage) {
		ctracked = preventSQLInjCTracked(ctracked);
		trackerUN = preventSQLInj(trackerUN);

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			boolean isOK = true;
			Tracker tracker = null;

			// Find tracker if necessary
			if (trackerUN != null) {
				tracker = em.find(Tracker.class, trackerUN);
				if (tracker == null) {
					result.setMessage("insertTracked:Failed:trackerUNnotFound:"
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
				result.setMessage("insertTracked:Successed:"
						+ ctracked.getUsername() + ":" + tracker + ":"
						+ isManage);
				result.setResult(ctracked);
			}
		} catch (Exception ex) {
			String s = "insertTracked:Failed:" + ex.getMessage() + ":"
					+ ctracked.getUsername();
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("removeTracked:Failed:notfound:" + trackedID);
			} else {
				ServiceResult<Boolean> res = removeTracksByTracked(trackedID);
				if (res.isOK()) {
					em.remove(tracked);
					result.setOK(true);
					result.setMessage("removeTracked:Successed:" + trackedID);
					result.setResult(Tracked.copy(tracked));
				} else {
					result.setMessage("removeTracked:Failed:"
							+ res.getMessage());
				}
			}
		} catch (Exception ex) {
			String s = "removeTracked:Failed:" + ex.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracked tracked = em.find(Tracked.class, ctracked.getUsername());
			if (tracked == null) {
				result.setMessage("updateTracker:Failed:notfound:" + ctracked);
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
				// USERNAME IS AN ID
				// if (ctracked.getUsername() != null)
				// tracked.setUsername(ctracked.getUsername());

				em.merge(tracked);
				result.setOK(true);
				result.setMessage("updateTracker:Successed" + ctracked);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "updateTracker:Failed:" + ex.getMessage() + ":"
					+ ctracked;
			result.setMessage(s);
			log.severe(s);
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

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null || (!tracked.getPassword().equals(password))) {
				result
						.setMessage("loginTrackedFromMobile:Failed:notFoundOrWrongPass:"
								+ username);
			} else {
				result.setOK(true);
				result.setMessage("loginTrackedFromMobile:Successed:"
						+ username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "loginTrackedFromMobile:Failed:" + ex.getMessage() + ":"
					+ username;
			result.setMessage(s);
			log.severe(s);
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

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null || (!tracked.getApiKey().equals(api))) {
				result
						.setMessage("loginTrackedFromApi:Failed:notFoundOrWrongApi:"
								+ username);
			} else {
				result.setOK(true);
				result.setMessage("loginTrackedFromApi:Successed:" + username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "loginTrackedFromApi:Failed:" + ex.getMessage() + ":"
					+ username;
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracked> result = new ServiceResult<CTracked>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracked tracked = em.find(Tracked.class, username);
			if (tracked == null) {
				result.setMessage("getTracked:Failed:notfound:" + username);
			} else {
				result.setOK(true);
				result.setMessage("getTracked:Successed:" + username);
				result.setResult(Tracked.copy(tracked));
			}
		} catch (Exception ex) {
			String s = "getTracked:Failed:" + ex.getMessage() + ":" + username;
			result.setMessage(s);
			log.severe(s);
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
			result.setMessage("getAllTrackeds:Successed:"
					+ result.getResult().size() + " trackeds");
		} catch (Exception e) {
			String s = "getAllTrackeds:Failed:" + e.getMessage();
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
			String s = "getTrackedsByTracker:Failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.severe(s);
			// ex.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ServiceResult<ArrayList<CTracked>> getTrackedsByTrackerManage(
			String trackerUN) {
		trackerUN = preventSQLInj(trackerUN);
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("getTrackedsByTrackerManage:Failed:notfound:"
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
				result.setMessage("getTrackedsByTrackerManage:Successed:"
						+ ctrackedList.size() + " trackeds");
			}
		} catch (Exception e) {
			String s = "getTrackedsByTrackerManage:Failed:" + e.getMessage()
					+ ":" + trackerUN;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTracked>> result = new ServiceResult<ArrayList<CTracked>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("getTrackedsByTrackerStaff:Failed:notfound:"
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
								ctracked.setUnderManage(true);
								ctrackedList.add(ctracked);
							}
						}
					}
				}
				result.setResult(ctrackedList);
				result.setOK(true);
				result.setMessage("getTrackedsByTrackerStaff:Successed:"
						+ ctrackedList.size() + " trackeds");
			}
		} catch (Exception e) {
			String s = "getTrackedsByTrackerStaff:Failed:" + e.getMessage()
					+ ":" + trackerUN;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTrack> result = new ServiceResult<CTrack>();
		result.setOK(false);
		result.setResult(null);
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
				result.setMessage("newTrack:Successed:" + track);
				result.setResult(Track.copy(track));
			}
		} catch (Exception ex) {
			String s = "newTrack:Failed:" + ex.getMessage() + ":" + trackedID;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTrack> result = new ServiceResult<CTrack>();
		result.setOK(false);
		result.setResult(null);
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setMessage("removeTrack:Failed:notfound");
			} else {
				em.remove(track);
				result.setOK(true);
				result.setMessage("removeTrack:Successed:" + track);
				result.setResult(Track.copy(track));
			}
		} catch (Exception ex) {
			result.setMessage("removeTrack:Failed:" + ex.getMessage() + ":"
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

	@Override
	public ServiceResult<Boolean> removeTracksByTracked(Long trackedID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setOK(false);
				result
						.setMessage("removeTracksByTracked:Failed:trackedNotFound:"
								+ trackedID);
			} else {
				em.getTransaction().begin();
				String sql = "DELETE T FROM Track T WHERE T.trackedID = :trackedID";
				Query query = em.createQuery(sql);
				query.setParameter("trackedID", trackedID);
				query.executeUpdate();
				result.setOK(true);
				result.setMessage("removeTracksByTracked:Success:" + trackedID);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			String s = "removeTracksByTracked:Failed:" + e.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
			result.setMessage("getAllTracks:Successed:"
					+ result.getResult().size() + " tracks");
		} catch (Exception e) {
			String s = "getAllTracks:Failed:" + e.getMessage();
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTrack>> result = new ServiceResult<ArrayList<CTrack>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getTracksByTracked:Failed:trackedNotFound:"
						+ trackedID);
			} else {
				String sql = "SELECT T FROM Track T WHERE T.trackedID = :trackedID ORDER BY T.trackID DESC";
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
				result.setMessage("getTracksByTracked:Successed:"
						+ result.getResult().size() + " tracks");
			}
		} catch (Exception e) {
			String s = "getTracksByTracked:Failed:" + e.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<ArrayList<CTrack>> result = new ServiceResult<ArrayList<CTrack>>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result.setMessage("getTracksByTracked:Failed:trackedNotFound:"
						+ trackedID);
			} else {
				String sql = "SELECT T FROM Track T WHERE (T.trackedID = :trackedID) "
						+ "AND (T.beginTime >= :beginTime) AND (T.endTime <= :endTime) ORDER BY T.trackID DESC";

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
				result.setMessage("getTracksByTracked:Successed:"
						+ result.getResult().size() + " tracks");
			}
		} catch (Exception e) {
			String s = "getTracksByTracked:Failed:" + e.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CWaypoint> result = new ServiceResult<CWaypoint>();
		result.setOK(false);
		result.setResult(null);
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result.setMessage("insertWaypoint:Failed:trackNotFound:"
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

				result.setOK(true);
				result.setMessage("insertWaypoint:Successed:" + waypoint);
				result.setResult(Waypoint.copy(waypoint));
			}
		} catch (Exception ex) {
			String s = "insertWaypoint:Failed:" + ex.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.severe(s);
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
	public ServiceResult<Boolean> removeWaypointsByTrack(Long trackID) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		result.setOK(false);
		result.setMessage(null);
		EntityManager em = EMF.get().createEntityManager();
		try {
			Track track = em.find(Track.class, trackID);
			if (track == null) {
				result
						.setMessage("removeWaypointsByTrack:Failed:trackNotFound:"
								+ trackID);
			} else {
				em.getTransaction().begin();
				String sql = "DELETE W FROM Waypoint W WHERE W.trackID = :trackID";
				Query query = em.createQuery(sql);
				query.setParameter("trackID", trackID);
				query.executeUpdate();
				result.setOK(true);
				result
						.setMessage("removeWaypointsByTrack:Successed:"
								+ trackID);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			String s = "removeWaypointsByTrack:Failed:" + e.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
				result.setMessage("getWaypointByTrack:Failed:trackNotFound:"
						+ trackID);
			} else {
				String sql = "SELECT W FROM Waypoint W WHERE W.trackID = "
						+ trackID;
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
				result.setMessage("getWaypointsByTrack:Successed:"
						+ result.getResult().size() + " waypoints");
			}
		} catch (Exception e) {
			String s = "getWaypointsByTrack:Failed" + e.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
				result.setMessage("getNewWaypointByTrack:Failed:trackNotFound:"
						+ trackID);
			} else {
				String sql = "SELECT T FROM Waypoint WHERE " +
						"(T.trackID = :trackID) AND (T.time >= :fromTime) AND (T.time <= :toTime) ORDER BY T.time DESC";
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
				result.setMessage("getNewWaypointByTrack:Successed:"
						+ result.getResult().size() + " waypoints");
			}
		} catch (Exception e) {
			String s = "getNewWaypointByTrack:Failed" + e.getMessage() + ":"
					+ trackID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CManage> result = new ServiceResult<CManage>();
		result.setOK(false);
		result.setMessage(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			Tracked tracked = em.find(Tracked.class, trackedID);
			String managePK = trackerUN + "#" + trackedID;
			Manage manage = em.find(Manage.class, managePK);
			if (tracker == null) {
				result.setMessage("insertManage:Failed:trackerNotFound:"
						+ trackerUN);
			} else if (tracked == null) {
				result.setMessage("insertManageFailed:trackedNotFound:"
						+ trackedID);
			} else if (manage != null) {
				result.setMessage("insertManageFailed:duplicate:" + manage);
			} else {
				manage = new Manage(managePK);
				em.persist(manage);
				result.setOK(true);
				result.setMessage("insertManage:Successed:" + manage);
				result.setResult(Manage.copy(manage));
			}
		} catch (Exception ex) {
			String s = "insertManage:Failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();

		try {
			String managePK = trackerUN + "#" + trackedID;
			Manage manage = em.find(Manage.class, managePK);
			if (manage == null) {
				result.setMessage("removeManage:Failed:notFound" + manage);
			} else {
				String sql = "DELETE M FROM Manage M WHERE M.staffpk = :staffPK";
				Query query = em.createQuery(sql);
				query.setParameter("staffPK", managePK);
				query.executeUpdate();
				result.setOK(true);
				result.setMessage("removeManage:Successed:" + manage);
			}
		} catch (Exception e) {
			String s = "removeManage:Failed:" + e.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracker tracker = (Tracker) em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setOK(false);
				result
						.setMessage("removeManagesByTracker:Failed:trackerNotFound:"
								+ trackerUN);
			} else {
				// String sql =
				// "DELETE M FROM Manage M WHERE M.trackerUN = :trackerUN";
				// Query query = em.createQuery(sql);
				// query.setParameter("trackerUN", trackerUN);
				// query.executeUpdate();
				// result.setOK(true);
				// result.setMessage("removeManagesByTracker:Successed:" +
				// tracker);
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
						.setMessage("removeManagesByTracker:Successed:"
								+ tracker);
			}
		} catch (Exception e) {
			String s = "removeManagesByTracker:Failed:" + e.getMessage()
					+ trackerUN;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracked tracked = (Tracked) em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result
						.setMessage("removeManagesByTrackedFailed:trackedNotFound:"
								+ trackedID);
			} else {
				// String sql =
				// "DELETE M FROM Manage M WHERE M.trackedID = :trackedID";
				// Query query = em.createQuery(sql);
				// query.setParameter("trackedID", trackedID);
				// query.executeUpdate();
				// result.setOK(true);
				// result.setMessage("removeManagesByTracked:Successed:" +
				// tracked);
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
						.setMessage("removeManagesByTracked:Successed:"
								+ tracked);
			}
		} catch (Exception e) {
			String s = "removeManagesByTracked:Failed:" + e.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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

		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CStaff> result = new ServiceResult<CStaff>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			Tracked tracked = em.find(Tracked.class, trackedID);
			String staffPK = trackerUN + "#" + trackedID;
			Staff staff = em.find(Staff.class, staffPK);
			if (tracker == null) {
				result.setMessage("insertStaff:Failed:trackerNotFound:"
						+ trackerUN);
			} else if (tracked == null) {
				result.setMessage("insertStaff:Failed:trackedNotFound:"
						+ trackedID);
			} else if (staff != null) {
				result.setMessage("insertStaff:Failed:duplicate:" + staff);
			} else {
				staff = new Staff(trackerUN, trackedID);
				em.persist(staff);
				// em.flush();
				result.setOK(true);
				result.setMessage("insertStaff:Successed:" + staff);
				result.setResult(Staff.copy(staff));
			}
		} catch (Exception ex) {
			String s = "insertStaff:Failed:" + ex.getMessage() + trackerUN
					+ "#" + trackedID;
			result.setMessage(s);
			log.severe(s);
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
		EntityManager em = EMF.get().createEntityManager();

		try {
			String staffPK = trackerUN + "#" + trackedID;
			Staff staff = em.find(Staff.class, staffPK);
			if (staff == null) {
				result.setMessage("removeStaff:Failed:notFound:" + staffPK);
			} else {
				String sql = "DELETE S FROM Staff S WHERE S.staffpk = :staffPK";
				Query query = em.createQuery(sql);
				query.setParameter("staffPK", staffPK);
				query.executeUpdate();
				result.setOK(true);
				result.setMessage("removeStaff:Successed:" + staff);
			}
		} catch (Exception e) {
			String s = "removeStaff:Failed:" + e.getMessage() + trackerUN + "#"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
			e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();

		try {
			Tracker tracker = (Tracker) em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result
						.setMessage("removeStaffsByTracker:Failed:trackerNotFound:"
								+ trackerUN);
			} else {
				// String sql =
				// "DELETE S FROM Staff S WHERE S.trackerUN = :trackerUN";
				// Query query = em.createQuery(sql);
				// query.setParameter("trackerUN", trackerUN);
				// query.executeUpdate();
				// result.setOK(true);
				// result.setMessage("removeStaffsByTracker:Successed:" +
				// tracker);
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
				result.setMessage("removeStaffsByTracker:Successed:" + tracker);
			}
		} catch (Exception e) {
			String s = "removeStaffsByTracker:Failed:" + e.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
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
		EntityManager em = EMF.get().createEntityManager();
		try {
			Tracked tracked = em.find(Tracked.class, trackedID);
			if (tracked == null) {
				result
						.setMessage("removeStaffsByTracked:Failed:trackedNotFound:"
								+ trackedID);
			} else {
				// String sql =
				// "DELETE S FROM Staff S WHERE S.trackedID = :trackedID";
				// Query query = em.createQuery(sql);
				// query.setParameter("trackedID", trackedID);
				// query.executeUpdate();
				// result.setOK(true);
				// result.setMessage("removeStaffsByTracked:Successed:" +
				// trackedID);
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
				result.setMessage("removeStaffsByTracked:Successed:"
						+ trackedID);
			}
		} catch (Exception e) {
			String s = "removeStaffsByTracked:Failed:" + e.getMessage() + ":"
					+ trackedID;
			result.setMessage(s);
			log.severe(s);
			// e.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	// //////////////////////////////////////////// STUFF

	// ////////////////////////////////////////// ResetPassword
	public ServiceResult<CTracker> resetPasswordTracker(String trackerUN) {
		EntityManager em = EMF.get().createEntityManager();
		ServiceResult<CTracker> result = new ServiceResult<CTracker>();
		result.setOK(false);
		result.setResult(null);
		try {
			Tracker tracker = em.find(Tracker.class, trackerUN);
			if (tracker == null) {
				result.setMessage("resetPassword:Failed:trackerNotFound:"
						+ trackerUN);
			} else {
				String newPass = Utils.getAlphaNumeric(6);
				tracker.setPassword(md5Cool(newPass));
				// TODO Email new password
				em.persist(tracker);
				result.setOK(true);
				result.setMessage("resetPassword:Successed:" + tracker);
				CTracker cTracker = Tracker.copy(tracker);
				cTracker.setPassword(newPass);
				result.setResult(cTracker);
			}
		} catch (Exception ex) {
			String s = "resetPassword:Failed:" + ex.getMessage() + ":"
					+ trackerUN;
			result.setMessage(s);
			log.severe(s);
			// ex.printStackTrace();
		} finally {
			try {
				em.close();
			} catch (Exception ex) {
			}
		}
		return result;
	}

	private static Policy policy = null;

	private static Policy getAntiSamyPolicy() {
		Policy policy = null;
		try {

			File file = new File("otherconfigs/antisamy-anythinggoes-1.3.xml");
			System.out.println(file.getAbsolutePath());
			policy = Policy.getInstance(file);
		} catch (PolicyException pe) {
			pe.printStackTrace();
		}
		return policy;
	}

	public static String preventSQLInj(String dirtyInput) {
		if (dirtyInput == null)
			return "";
		CleanResults cr = null;
		try {
			if (policy == null)
				policy = getAntiSamyPolicy();
			AntiSamy as = new AntiSamy();
			cr = as.scan(dirtyInput, policy);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cr.getCleanHTML(); // some custom function
	}

	public static CTracker preventSQLInjCTracker(CTracker ctracker) {
		ctracker.setAddr(preventSQLInj(ctracker.getAddr()));
		ctracker.setCountry(preventSQLInj(ctracker.getCountry()));
		ctracker.setEmail(preventSQLInj(ctracker.getEmail()));
		ctracker.setLang(preventSQLInj(ctracker.getLang()));
		ctracker.setPassword(preventSQLInj(ctracker.getPassword()));
		ctracker.setName(preventSQLInj(ctracker.getName()));
		ctracker.setTel(preventSQLInj(ctracker.getTel()));
		ctracker.setUsername(preventSQLInj(ctracker.getUsername()));
		return ctracker;
	}

	public static CTracked preventSQLInjCTracked(CTracked ctracked) {
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
		cmanage.setManagePK(preventSQLInj(cmanage.getManagePK()));
		cmanage.setTrackerUN(preventSQLInj(cmanage.getTrackerUN()));
		return cmanage;
	}

	public static CStaff preventSQLInjCStaff(CStaff cstaff) {
		cstaff.setStaffPK(preventSQLInj(cstaff.getStaffPK()));
		cstaff.setTrackerUN(preventSQLInj(cstaff.getTrackerUN()));
		return cstaff;
	}

	public static CTrack preventSQLInjCTrack(CTrack ctrack) {
		return ctrack;
	}

	public static CWaypoint preventSQLInjCWaypoint(CWaypoint cwaypoint) {
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

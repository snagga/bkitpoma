package com.bkitmobile.poma.database.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.bkitmobile.poma.database.client.entity.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../bkitpoma/db")
public interface DatabaseService extends RemoteService {
	
	//////////////////////////////////////////// Tracker
	ServiceResult<CTracker> insertTracker(CTracker ctracker);
	ServiceResult<CTracker> updateTrackerPassword(String username, String oldPassword, String newPassword);
	ServiceResult<CTracker> removeTracker(String trackerUN);
	ServiceResult<CTracker> updateTracker(CTracker ctracker);
	ServiceResult<CTracker> loginTracker(String username, String password);
	ServiceResult<CTracker> loginTracker(String username, String password, boolean remember);
	ServiceResult<CTracker> loginTrackerFromApi(String username, String api);
	ServiceResult<CTracker> logoutTracker();
	ServiceResult<CTracker> getTracker(String username);
	ServiceResult<ArrayList<CTracker>> getAllTrackers();
	ServiceResult<CTracker> isLogined();

	//////////////////////////////////////////// Tracked
	ServiceResult<CTracked> insertTracked(CTracked ctracked, String trackerUN, boolean isManage);
	ServiceResult<CTracked> removeTracked(Long trackedID);
	ServiceResult<CTracked> updateTracked(CTracked ctracked);
	ServiceResult<CWaypoint> updateLastestWaypointTracked(Long trackedID, String waypointPK);
	ServiceResult<CWaypoint> getLastestWaypointTracked(Long trackedID);
	ServiceResult<CTracked> loginTrackedFromMobile(Long username, String password);
	ServiceResult<CTracked> loginTrackedFromApi(Long username, String api);
	ServiceResult<CTracked> getTracked(Long username);
	ServiceResult<ArrayList<CTracked>> getAllTrackeds();
	ServiceResult<ArrayList<CTracked>> getTrackedsByTracker(String trackerUN);
	ServiceResult<ArrayList<CTracked>> getTrackedsByTrackerManage(String trackerUN);
	ServiceResult<ArrayList<CTracked>> getTrackedsByTrackerStaff(String trackerUN);
	
	//////////////////////////////////////////// Track
	ServiceResult<CTrack> newTrack(Long trackedID);
	ServiceResult<CTrack> removeTrack(Long trackID);
	ServiceResult<Boolean> removeTracksByTracked(Long trackedID);
	ServiceResult<ArrayList<CTrack>> getAllTracks();
	ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID);
	ServiceResult<ArrayList<CTrack>> getLastTracksByTracked(Long trackedID, int count);
	ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID,
			Date fromTime);
	ServiceResult<ArrayList<CTrack>> getTracksByTracked(Long trackedID,
			Date fromTime, Date toTime);
	
	//////////////////////////////////////////// Waypoint
	ServiceResult<CWaypoint> insertWaypoint(Long trackID, double lat, double lng, long speed);
	ServiceResult<Boolean> removeWaypointsByTrack(Long trackID);
	ServiceResult<ArrayList<CWaypoint>> getWaypointsByTrack(Long trackID);
	ServiceResult<ArrayList<CWaypoint>> getWaypointByTrack(Long trackID,
			Date fromTime);
	ServiceResult<ArrayList<CWaypoint>> getWaypointsByTrack(Long trackID, Date fromTime, Date toTime);
	
	//////////////////////////////////////////// Manage
	ServiceResult<CManage> insertManage(String trackerUN, Long trackedID);
	ServiceResult<ArrayList<CManage>> getManagesByTracked(Long trackedID);
	ServiceResult<CManage> removeManage(String trackerUN, Long trackedID);
	ServiceResult<Boolean> removeManagesByTracker(String trackerUN);
	ServiceResult<Boolean> removeManagesByTracked(Long trackedID);
	ServiceResult<Boolean> insertManages(Long trackedID, ArrayList<String> arrManage);
	
	//////////////////////////////////////////// Staff
	ServiceResult<CStaff> insertStaff(String trackerUN, Long trackedID);
	ServiceResult<ArrayList<CStaff>> getStaffsByTracked(Long trackedID);
	ServiceResult<CStaff> removeStaff(String trackerUN, Long trackedID);
	ServiceResult<Boolean> removeStaffsByTracker(String trackerUN);
	ServiceResult<Boolean> removeStaffsByTracked(Long trackedID);
	ServiceResult<Boolean> insertStaffs(Long trackedID, ArrayList<String> arrStaff);
	
	//////////////////////////////////////////// TrackedRule
	ServiceResult<CTrackedRule> insertTrackedRule(CTrackedRule ctrackedRule);
	ServiceResult<CTrackedRule> removeTrackedRule(Long trackedRuleID);
	ServiceResult<CTrackedRule> updateTrackedRule(CTrackedRule ctrackedRule);
	ServiceResult<ArrayList<CTrackedRule>> getTrackedRuleByTracked(Long trackedID);
	
	//////////////////////////////////////////// Stuff
	ServiceResult<ArrayList> executeQuery(String sql);
	ServiceResult<Boolean> resetDatabase(String secretKey);
	
	//////////////////////////////////////////// AdminConfig
	ServiceResult<CAdminConfig> addRecord(String name,String value);
	ServiceResult<CAdminConfig> removeRecord(String name);
	ServiceResult<CAdminConfig> getRecord(String name);
	ServiceResult<HashMap<String,String>> getAllRecords();
	ServiceResult<Boolean> setAllRecords(HashMap<String, String> hashMap);
	
	public static class Util {
		public static DatabaseServiceAsync getInstance() {
			return GWT.create(DatabaseService.class);
		}
	}
}

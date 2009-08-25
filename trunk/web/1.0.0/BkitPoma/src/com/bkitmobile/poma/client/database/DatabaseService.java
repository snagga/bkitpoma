package com.bkitmobile.poma.client.database;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.client.database.entity.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DatabaseService extends RemoteService {
	
	//////////////////////////////////////////// Tracker
	ServiceResult<CTracker> insertTracker(CTracker ctracker);
	ServiceResult<CTracker> removeTracker(String trackerUN);
	ServiceResult<CTracker> updateTracker(CTracker ctracker);
	ServiceResult<CTracker> loginTracker(String username, String password);
	ServiceResult<CTracker> logoutTracker();
	ServiceResult<CTracker> getTracker(String username);
	ServiceResult<ArrayList<CTracker>> getAllTrackers();
	ServiceResult<CTracker> isLogined();

	//////////////////////////////////////////// Tracked
	ServiceResult<CTracked> insertTracked(CTracked ctracked, String trackerUN, boolean isManage);
	ServiceResult<CTracked> removeTracked(Long trackedID);
	ServiceResult<CTracked> updateTracked(CTracked ctracked);
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
	ServiceResult<CManage> removeManage(String trackerUN, Long trackedID);
	ServiceResult<Boolean> removeManagesByTracker(String trackerUN);
	ServiceResult<Boolean> removeManagesByTracked(Long trackedID);
	
	//////////////////////////////////////////// Staff
	ServiceResult<CStaff> insertStaff(String trackerUN, Long trackedID);
	ServiceResult<CStaff> removeStaff(String trackerUN, Long trackedID);
	ServiceResult<Boolean> removeStaffsByTracker(String trackerUN);
	ServiceResult<Boolean> removeStaffsByTracked(Long trackedID);
	
	public static class Util {
		public static DatabaseServiceAsync getInstance() {
			return GWT.create(DatabaseService.class);
		}
	}
}

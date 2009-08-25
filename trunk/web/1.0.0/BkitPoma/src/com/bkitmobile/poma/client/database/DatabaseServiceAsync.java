package com.bkitmobile.poma.client.database;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.client.database.entity.CManage;
import com.bkitmobile.poma.client.database.entity.CStaff;
import com.bkitmobile.poma.client.database.entity.CTrack;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.database.entity.CWaypoint;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DatabaseService</code>.
 */
public interface DatabaseServiceAsync {

	// ////////////////////////////////////////// Tracker
	
	/**
	 * Insert new tracker into Database
	 * <pre>
	 * Create an object that hold a ctracker instance and then insert.
	 * ctracker.setPassword("UnencodedPassword");
	 * </pre>
	 * @param ctracker <code>CTracker</code> object that you want to insert into Database
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains <code>CTracker</code> already inserted into Database and has password encoded by <b>MD5 algorithm</b>
	 * @see {@link com.bkitmobile.poma.client.database.CTracker} , {@link com.bkitmobile.poma.server.database.Tracker}
	 */
	void insertTracker(CTracker ctracker,
			AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Remove a tracker from Database by it's username
	 * @param trackerUN Tracker's username
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains <code>CTracker</code> already removed from Database
	 * @see {@link com.bkitmobile.poma.client.database.CTracker} , {@link com.bkitmobile.poma.server.database.Tracker}
	 */
	void removeTracker(String trackerUN,
			AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Update a tracker in Database.
	 * @param ctracker <code>CTracker</code> contains properties need to changed. Any properties that have a NOT NULL value will be updated.
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains <code>CTracker</code> already updated into Database
	 * @see {@link com.bkitmobile.poma.client.database.CTracker} , {@link com.bkitmobile.poma.server.database.Tracker}
	 */
	void updateTracker(CTracker ctracker,
			AsyncCallback<ServiceResult<CTracker>> callback);

	void loginTracker(String username, String password,
			AsyncCallback<ServiceResult<CTracker>> callback);

	void logoutTracker(AsyncCallback<ServiceResult<CTracker>> callback);
	
	void getTracker(String username,
			AsyncCallback<ServiceResult<CTracker>> callback);

	void getAllTrackers(
			AsyncCallback<ServiceResult<ArrayList<CTracker>>> callback);

	void isLogined(AsyncCallback<ServiceResult<CTracker>> callback);

	// ////////////////////////////////////////// Tracked
	void insertTracked(CTracked ctracked, String trackerUN, boolean isManage,
			AsyncCallback<ServiceResult<CTracked>> callback);
	
	void removeTracked(Long trackedID,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void updateTracked(CTracked ctracked,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void loginTrackedFromMobile(Long username, String password,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void loginTrackedFromApi(Long username, String api,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void getTracked(Long username,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void getAllTrackeds(
			AsyncCallback<ServiceResult<ArrayList<CTracked>>> callback);

	void getTrackedsByTracker(String trackerUN,
			AsyncCallback<ServiceResult<ArrayList<CTracked>>> callback);

	void getTrackedsByTrackerManage(String trackerUN,
			AsyncCallback<ServiceResult<ArrayList<CTracked>>> callback);

	void getTrackedsByTrackerStaff(String trackerUN,
			AsyncCallback<ServiceResult<ArrayList<CTracked>>> callback);
	
	// ////////////////////////////////////////// Track
	void newTrack(Long trackedID, AsyncCallback<ServiceResult<CTrack>> callback);

	void removeTrack(Long trackID, AsyncCallback<ServiceResult<CTrack>> callback);

	void removeTracksByTracked(Long trackedID,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void getAllTracks(AsyncCallback<ServiceResult<ArrayList<CTrack>>> callback);

	void getTracksByTracked(Long trackedID,
			AsyncCallback<ServiceResult<ArrayList<CTrack>>> callback);

	void getLastTracksByTracked(Long trackedID, int count,
			AsyncCallback<ServiceResult<ArrayList<CTrack>>> callback);
	
	void getTracksByTracked(Long trackedID, Date fromTime,
			AsyncCallback<ServiceResult<ArrayList<CTrack>>> callback);

	void getTracksByTracked(Long trackedID, Date fromTime, Date toTime,
			AsyncCallback<ServiceResult<ArrayList<CTrack>>> callback);
	
	// ////////////////////////////////////////// Waypoint
	void insertWaypoint(Long trackID, double lat, double lng, long speed,
			AsyncCallback<ServiceResult<CWaypoint>> callback);

	void removeWaypointsByTrack(Long trackID,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void getWaypointsByTrack(Long trackID,
			AsyncCallback<ServiceResult<ArrayList<CWaypoint>>> callback);

	void getWaypointByTrack(Long trackID, Date fromTime,
			AsyncCallback<ServiceResult<ArrayList<CWaypoint>>> callback);
	
	void getWaypointsByTrack(Long trackID, Date fromTime, Date toTime,
			AsyncCallback<ServiceResult<ArrayList<CWaypoint>>> callback);
	
	// ////////////////////////////////////////// Manage
	void insertManage(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CManage>> callback);

	void removeManage(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CManage>> callback);

	void removeManagesByTracker(String trackerUN,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void removeManagesByTracked(Long trackedID,
			AsyncCallback<ServiceResult<Boolean>> callback);

	// ////////////////////////////////////////// Staff
	void insertStaff(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CStaff>> callback);

	void removeStaff(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CStaff>> callback);

	void removeStaffsByTracker(String trackerUN,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void removeStaffsByTracked(Long trackedID,
			AsyncCallback<ServiceResult<Boolean>> callback);
}

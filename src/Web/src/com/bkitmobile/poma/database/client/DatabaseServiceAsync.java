package com.bkitmobile.poma.database.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.bkitmobile.poma.database.client.entity.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DatabaseService</code>.
 */
public interface DatabaseServiceAsync {

	// ////////////////////////////////////////// Tracker

	/**
	 * Insert new tracker into Database
	 * 
	 * <pre>
	 * Create an object that hold a ctracker instance and then insert.
	 * ctracker.setPassword(&quot;UnencodedPassword&quot;);
	 * </pre>
	 * 
	 * @param ctracker
	 *            <code>CTracker</code> object that you want to insert into
	 *            Database
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> already inserted into Database and has
	 *         password encoded by <b>MD5 algorithm</b>
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void insertTracker(CTracker ctracker,
			AsyncCallback<ServiceResult<CTracker>> callback);

	void updateTrackerPassword(String username, String oldPassword,
			String newPassword, AsyncCallback<ServiceResult<CTracker>> callback);
	
	/**
	 * Remove a tracker from Database by it's username
	 * 
	 * @param trackerUN
	 *            Tracker's username
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> already removed from Database
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void removeTracker(String trackerUN,
			AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Update a tracker in Database.
	 * 
	 * @param ctracker
	 *            <code>CTracker</code> contains properties need to changed. Any
	 *            properties that have a NOT NULL value will be updated.
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> already updated into Database
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void updateTracker(CTracker ctracker,
			AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Login into Poma using tracker account (poma/openid/facebook)
	 * 
	 * @param username
	 *            Tracker's username (accept openid or facebook account)
	 * @param password
	 *            Password
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> certificated
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void loginTracker(String username, String password,
			AsyncCallback<ServiceResult<CTracker>> callback);
	
	void loginTracker(String username, String password, boolean remember,
			AsyncCallback<ServiceResult<CTracker>> callback);

	void loginTrackerFromApi(String username, String api,
			AsyncCallback<ServiceResult<CTracker>> callback);
	
	/**
	 * Logout from POMA
	 * 
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> has been logout
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void logoutTracker(AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Get tracker by its username
	 * 
	 * @param username
	 *            Tracker's username
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains
	 *         <code>CTracker</code> has specified <code>username</code>
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void getTracker(String username,
			AsyncCallback<ServiceResult<CTracker>> callback);

	/**
	 * Get all trackers in database
	 * 
	 * @return <code>ServiceResult&ltArrayList&lt;CTracker&gt;&gt;</code> which
	 *         contains a list of <code>CTracker</code>(s) in Database
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void getAllTrackers(
			AsyncCallback<ServiceResult<ArrayList<CTracker>>> callback);

	/**
	 * Determine tracker login or not
	 * 
	 * @return <code>ServiceResult&lt;CTracker&gt;</code> which contains a
	 *         <code>CTracker</code> logging into POMA in current session
	 * @see {@link com.bkitmobile.poma.client.database.CTracker CTracker} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracker Tracker}
	 */
	void isLogined(AsyncCallback<ServiceResult<CTracker>> callback);

	// ////////////////////////////////////////// Tracked
	/**
	 * Insert new tracked into Database
	 * 
	 * @param ctracked
	 *            A CTracked instance that you want to persist
	 * @param trackerUN
	 *            username of ctracked's manager/staff. Leave <code>null</code>
	 *            if you want to add this relation ship manually
	 * @param isManage
	 *            <code>true</code> if trackerUN is ctracker's manager,
	 *            <code>false</code> if otherwise
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains a
	 *         <code>CTracked</code> has been inserted
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void insertTracked(CTracked ctracked, String trackerUN, boolean isManage,
			AsyncCallback<ServiceResult<CTracked>> callback);

	/**
	 * Remove tracked from Database
	 * 
	 * @param trackedID
	 *            Tracked's ID that you want to remove
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains a
	 *         <code>CTracked</code> has been removed
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void removeTracked(Long trackedID,
			AsyncCallback<ServiceResult<CTracked>> callback);

	/**
	 * Update tracked data
	 * 
	 * @param ctracked
	 *            Tracked that you want to update. Any NULL-property will be
	 *            ignored.
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains a
	 *         <code>CTracked</code> has been updated
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void updateTracked(CTracked ctracked,
			AsyncCallback<ServiceResult<CTracked>> callback);

	void updateLastestWaypointTracked(Long trackedID, String waypointPK,
			AsyncCallback<ServiceResult<CWaypoint>> callback);
	
	void getLastestWaypointTracked(Long trackedID,
			AsyncCallback<ServiceResult<CWaypoint>> callback);
	
	/**
	 * Login into POMA from Mobile using tracked's account
	 * 
	 * @param username
	 *            Tracked's ID/username
	 * @param password
	 *            Tracked's password
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains a
	 *         <code>CTracked</code> has been login
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void loginTrackedFromMobile(Long username, String password,
			AsyncCallback<ServiceResult<CTracked>> callback);

	/**
	 * Login into POMA from another place using tracked's API key
	 * 
	 * @param username
	 *            Tracked's username
	 * @param api
	 *            Tracked's API key
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains a
	 *         <code>CTracked</code> has been login
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void loginTrackedFromApi(Long username, String api,
			AsyncCallback<ServiceResult<CTracked>> callback);

	/**
	 * Get tracked by its username
	 * 
	 * @param username
	 *            Tracked's username
	 * @return <code>ServiceResult&lt;CTracked&gt;</code> which contains
	 *         <code>CTracked</code> has specified <code>username</code>
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void getTracked(Long username,
			AsyncCallback<ServiceResult<CTracked>> callback);

	/**
	 * Get all tracked from Database
	 * 
	 * @return <code>ServiceResult&lt;ArrayList&lt;CTracked&gt;&gt;</code> which
	 *         contains a list of <code>CTracked</code>(s)
	 * @see {@link com.bkitmobile.poma.client.database.CTracked CTracked} ,
	 *      {@link com.bkitmobile.poma.server.database.Tracked Tracked}
	 */
	void getAllTrackeds(
			AsyncCallback<ServiceResult<ArrayList<CTracked>>> callback);

	/**
	 * 
	 * @param trackerUN
	 * @param callback
	 */
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

	void getManagesByTracked(Long trackedID,
			AsyncCallback<ServiceResult<ArrayList<CManage>>> callback);
	
	void removeManage(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CManage>> callback);

	void removeManagesByTracker(String trackerUN,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void removeManagesByTracked(Long trackedID,
			AsyncCallback<ServiceResult<Boolean>> callback);
	
	void insertManages(Long trackedID, ArrayList<String> arrManage,
			AsyncCallback<ServiceResult<Boolean>> callback);

	// ////////////////////////////////////////// Staff
	void insertStaff(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CStaff>> callback);

	void getStaffsByTracked(Long trackedID,
			AsyncCallback<ServiceResult<ArrayList<CStaff>>> callback);
	
	void removeStaff(String trackerUN, Long trackedID,
			AsyncCallback<ServiceResult<CStaff>> callback);

	void removeStaffsByTracker(String trackerUN,
			AsyncCallback<ServiceResult<Boolean>> callback);

	void removeStaffsByTracked(Long trackedID,
			AsyncCallback<ServiceResult<Boolean>> callback);
	
	void insertStaffs(Long trackedID, ArrayList<String> arrStaff,
			AsyncCallback<ServiceResult<Boolean>> callback);

	////////////////////////////////////////////// Stuff
	void insertTrackedRule(CTrackedRule ctrackedRule,
			AsyncCallback<ServiceResult<CTrackedRule>> callback);

	void removeTrackedRule(Long trackedRuleID,
			AsyncCallback<ServiceResult<CTrackedRule>> callback);
	
	void resetDatabase(String secretKey,
			AsyncCallback<ServiceResult<Boolean>> callback);

	
	void updateTrackedRule(CTrackedRule ctrackedRule,
			AsyncCallback<ServiceResult<CTrackedRule>> callback);

	void getTrackedRuleByTracked(Long trackedID,
			AsyncCallback<ServiceResult<ArrayList<CTrackedRule>>> callback);

	void executeQuery(String sql, AsyncCallback<ServiceResult<ArrayList>> callback);
	
	
	////////////////////////////////////////////// AdminConfig
	void addRecord(String name, String value,
			AsyncCallback<ServiceResult<CAdminConfig>> callback);

	void removeRecord(String name,
			AsyncCallback<ServiceResult<CAdminConfig>> callback);

	void getRecord(String name,
			AsyncCallback<ServiceResult<CAdminConfig>> callback);

	void getAllRecords(
			AsyncCallback<ServiceResult<HashMap<String,String>>> callback);

	void setAllRecords(HashMap<String, String> hashMap,
			AsyncCallback<ServiceResult<Boolean>> callback);
}

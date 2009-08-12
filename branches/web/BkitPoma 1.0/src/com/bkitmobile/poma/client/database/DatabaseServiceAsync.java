package com.bkitmobile.poma.client.database;

import java.util.*;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DatabaseService</code>.
 */
public interface DatabaseServiceAsync {
	void executeQuery(String sqlQuery, AsyncCallback<String> callback);

	void getTrackedInfoList(String trackerUsername,
			AsyncCallback<ArrayList<Tracked>> asyncCallback);

	void getTrackedList(String trackerUsername, AsyncCallback<String[]> callback);

	void getTrackedDetail(String trackedUN, AsyncCallback<Tracked> callback);

	void getTrackerDetail(String trackerUN, AsyncCallback<Tracker> callback);

	void insertTracker(Tracker tracker, AsyncCallback<Integer> callback);
	
	void insertTracked(String trackerUN,Tracked tracked, AsyncCallback<String> callback);

	void verifyTracker(String us, AsyncCallback<Boolean> callback);

	void setShowInMap(String trackedUN, boolean b,
			AsyncCallback<Integer> callback);

	void setEmbedded(String trackedUN, boolean b,
			AsyncCallback<Integer> callback);

	void updateInfoTracker(Tracker tracker, AsyncCallback<Integer> callback);

	void updateInfoTracked(Tracked tracked, AsyncCallback<Integer> callback);

	void removeTracked(String trackerUN, String trackedUN,
			AsyncCallback<Integer> callback);

	void updateTrackerTypeCus(String trackerUN, int type,
			AsyncCallback<Integer> callback);

	void updateTrackedGPSState(String trackedUN, boolean type,
			AsyncCallback<Integer> callback);

//	void updateManage(String trackerUN, String trackedUN, String schedule,
//			int interval, AsyncCallback<Integer> callback);

	void getTracks(String trackedUN, AsyncCallback<String[]> callback);

	void getWayPoint(String trackedID, AsyncCallback<ArrayList<WayPoint>> callback);

	void updateTrackedShowInMap(String trackedUN, int type,
			AsyncCallback<Integer> callback);

	void verifyTracked(String us, AsyncCallback<Boolean> callback);

	void loginTracker(String username, String password,
			AsyncCallback<Boolean> callback);

	void loginTracked(String username, String password,
			AsyncCallback<Boolean> callback);

	void insertManage(String trackerUN, String trackedUN,
			AsyncCallback<Integer> callback);

	void insertWayPoint(WayPoint wayPoint, AsyncCallback<Integer> callback);

	void insertTrack(String trackedUN, AsyncCallback<String> callback);

	void getNewTrackedUN(AsyncCallback<String> callback);

}

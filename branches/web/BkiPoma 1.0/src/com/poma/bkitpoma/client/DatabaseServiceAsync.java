package com.poma.bkitpoma.client;

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
	
	void insertTracked(Tracked tracked, AsyncCallback<Integer> callback);

	void verifyTracker(String us, AsyncCallback<Boolean> callback);

	void setShowInMap(String trackedUN, boolean b,
			AsyncCallback<Integer> callback);

	void setEmbedded(String trackedUN, boolean b,
			AsyncCallback<Integer> callback);

}

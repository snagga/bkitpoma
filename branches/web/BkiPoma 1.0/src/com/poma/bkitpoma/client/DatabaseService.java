package com.poma.bkitpoma.client;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DatabaseService extends RemoteService {
	String executeQuery(String sqlQuery);

	ArrayList<Tracked> getTrackedInfoList(String trackerUsername);

	String[] getTrackedList(String trackerUsername);

	Tracked getTrackedDetail(String trackedUN);

	Tracker getTrackerDetail(String trackerUN);

	Integer insertTracker(Tracker tracker);

	Integer insertTracked(Tracked tracked);

	boolean verifyTracker(String us);

	Integer setShowInMap(String trackedUN, boolean b);

	Integer setEmbedded(String trackedUN, boolean b);
	
	Integer updateInfoTracker(Tracker tracker);
	
	Integer updateInfoTracked(Tracked tracked);
	
	Integer removeTracked(String trackerUN,String trackedUN);

	public static class Util {

		public static DatabaseServiceAsync getInstance() {

			return GWT.create(DatabaseService.class);
		}
	}
}

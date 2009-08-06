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

	int insertTracker(Tracker tracker);

	int insertTracked(Tracked tracked);

	boolean verifyTracker(String us);

	int setShowInMap(String trackedUN, boolean b);

	int setEmbedded(String trackedUN, boolean b);

	public static class Util {

		public static DatabaseServiceAsync getInstance() {

			return GWT.create(DatabaseService.class);
		}
	}
}

package com.bkitmobile.poma.client.database;

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

	String insertTracked(String trackerUN,Tracked tracked);

	boolean verifyTracker(String us);
	
	boolean verifyTracked(String us);

	Integer setShowInMap(String trackedUN, boolean b);

	Integer setEmbedded(String trackedUN, boolean b);
	
	Integer updateInfoTracker(Tracker tracker);
	
	Integer updateInfoTracked(Tracked tracked);
	
	Integer removeTracked(String trackerUN,String trackedUN);
	
	Integer updateTrackerTypeCus(String trackerUN,int type);
	
	Integer updateTrackedGPSState(String trackedUN,boolean type);
	
	Integer updateTrackedShowInMap(String trackedUN, int type);
	
	Integer insertManage(String trackerUN, String trackedUN);
	
	Integer insertWayPoint(WayPoint wayPoint);
	
	String[] getTracks(String trackedUN);
	
	ArrayList<WayPoint> getWayPoint(String trackID);
	
	boolean loginTracker(String username,String password);
	
	boolean loginTracked(String username,String password);
	
	String insertTrack(String trackedUN);
	
	String getNewTrackedUN();

	public static class Util {

		public static DatabaseServiceAsync getInstance() {

			return GWT.create(DatabaseService.class);
		}
	}

}

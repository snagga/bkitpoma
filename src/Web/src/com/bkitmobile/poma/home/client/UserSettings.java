package com.bkitmobile.poma.home.client;

import java.util.HashMap;

import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.util.client.OptimizeTaskQueue;
import com.bkitmobile.poma.util.client.TimerTask;
import com.bkitmobile.poma.ui.client.map.TrackPointOverlay;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSettings implements IsSerializable {
	public static boolean MAP_MAXIMIZED = false;
	public static EntryPoint ENTRY_POINT;
	public static CTracker ctracker = null;
	public static CTracked ctracked = null;

	//public static ArrayList<CTracked> ctrackedList = new ArrayList<CTracked>();
	
	public static HashMap<Long, CTracked> ctrackedList = new HashMap<Long, CTracked>();
	public static HashMap<Long, TrackPointOverlay> currentTrackOverlay = new HashMap<Long, TrackPointOverlay>();
	
	public static TimerTask timerTask = new TimerTask(500); // Interval: 500ms
	public static OptimizeTaskQueue optimizeTaskQueue = new OptimizeTaskQueue(timerTask);
	
	public static long lastWayPointUpdateTime = System.currentTimeMillis();
	public static long lastTrackUpdateTime = System.currentTimeMillis();
	
	public static HashMap<String,String> hashMapConfig = new HashMap<String, String>();
}
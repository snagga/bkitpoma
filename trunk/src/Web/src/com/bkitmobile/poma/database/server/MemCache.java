package com.bkitmobile.poma.database.server;

import java.util.ArrayList;
import java.util.Collections;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.cache.CacheStatistics;

import com.bkitmobile.poma.database.client.entity.*;

@SuppressWarnings("unchecked")
public class MemCache {
	private static Cache cache;
	private static CacheStatistics cacheStatistics;

	public static Cache getCacheInstance() {
		return cache;
	}

	public static CacheStatistics getCacheStatisticsInstance() {
		return cacheStatistics;
	}

	public static Object put(Object key, Object value) {
		Object obj = null;
		if (cache.containsKey(key)) {
			obj = cache.get(key);
		}
		cache.put(key, value);
		return obj;
	}

	public static void setTrackers(ArrayList<CTracker> ctrackers) {
		if (ctrackers != null)
			cache.put("trackers", ctrackers);
	}

	public static ArrayList<CTracker> getTrackers() {
		Object obj = cache.get("trackers");
		if (obj instanceof ArrayList)
			return (ArrayList<CTracker>) obj;
		return null;
	}

	public static void setTracker(String username, CTracker ctracker) {
		if (username != null && ctracker != null)
			cache.put("tracker#" + username, ctracker);
	}

	public static CTracker getTracker(String username) {
		Object obj = cache.get("tracker#" + username);
		if (obj instanceof CTracker)
			return (CTracker) obj;
		return null;
	}

	public static void setTrackedsyTrackerStaff(String trackerUN, ArrayList<CTracked> ctracked) {
		if (getTracker(trackerUN) instanceof CTracker) {
			cache.put(trackerUN+"#staff", ctracked);
		}
	}
	
	public static ArrayList<CTracked> getTrackedsByTrackerStaff(String trackerUN) {
		Object obj = cache.get(trackerUN+"#staff");
		if (obj instanceof ArrayList) {
			return (ArrayList<CTracked>) obj;
		}
		return null;
	}
	
	public static void setTrackedsyTrackerManage(String trackerUN, ArrayList<CTracked> ctracked) {
		if (getTracker(trackerUN) instanceof CTracker) {
			cache.put(trackerUN+"#manage", ctracked);
		}
	}
	
	public static ArrayList<CTracked> getTrackedsByTrackerManage(String trackerUN) {
		Object obj = cache.get(trackerUN+"#manage");
		if (obj instanceof ArrayList) {
			return (ArrayList<CTracked>) obj;
		}
		return null;
	}

	public static void setTracked(long id, CTracked ctracked) {
		if (ctracked != null)
			cache.put("tracked#" + id, ctracked);
	}

	public static CTracked getTracked(long id) {
		Object obj = cache.get("tracked#" + id);
		if (obj instanceof CTracked)
			return (CTracked) obj;
		return null;
	}
	
	static {
		// Map props = new HashMap();
		// props.put(GCacheFactory.EXPIRATION_DELTA, 3600);
		try {

			CacheFactory cacheFactory = CacheManager.getInstance()
					.getCacheFactory();
			cache = cacheFactory.createCache(Collections.emptyMap());
			cacheStatistics = cache.getCacheStatistics();
		} catch (CacheException ex) {
			ex.printStackTrace();
		}
	}
}

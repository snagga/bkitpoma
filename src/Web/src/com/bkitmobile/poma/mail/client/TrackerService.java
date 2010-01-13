package com.bkitmobile.poma.mail.client;

import com.bkitmobile.poma.database.client.entity.CTracker;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../bkitpoma/trackerservice")
public interface TrackerService extends RemoteService{

	Boolean validateEmailForgotPassword(String trackerUN);
	
	Boolean validateEmailNewTracker(CTracker cTracker);
	
	public static class Util { 
		public static TrackerServiceAsync getInstance() { 
			return GWT.create(TrackerService.class); 
		} 
	} 
}


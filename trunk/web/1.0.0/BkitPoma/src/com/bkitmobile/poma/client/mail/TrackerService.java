package com.bkitmobile.poma.client.mail;

import com.bkitmobile.poma.client.database.entity.CTracker;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("trackerservice")
public interface TrackerService extends RemoteService{

	Boolean validateEmailForgotPassword(String trackerUN);
	
	Boolean validateEmailNewTracker(CTracker cTracker);
}


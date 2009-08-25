package com.bkitmobile.poma.client.mail;

import com.bkitmobile.poma.client.database.entity.CTracker;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TrackerServiceAsync {
	public void validateEmailForgotPassword(String trackerUN,
			AsyncCallback<Boolean> callBack);

	void validateEmailNewTracker(CTracker cTracker,
			AsyncCallback<Boolean> callback);
}

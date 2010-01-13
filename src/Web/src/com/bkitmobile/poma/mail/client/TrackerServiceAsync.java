package com.bkitmobile.poma.mail.client;

import com.bkitmobile.poma.database.client.entity.CTracker;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TrackerServiceAsync {
	void validateEmailForgotPassword(String trackerUN,
			AsyncCallback<Boolean> callBack);

	void validateEmailNewTracker(CTracker cTracker,
			AsyncCallback<Boolean> callback);
}

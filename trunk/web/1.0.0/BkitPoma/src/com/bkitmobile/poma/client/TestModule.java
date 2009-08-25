package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.ui.TrackedPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class TestModule implements EntryPoint {

	@Override
	public void onModuleLoad() {
		TrackedPanel trackedPanel = new TrackedPanel();
		RootPanel.get().add(trackedPanel);
		trackedPanel.loadTrackedList();
		
	}

}

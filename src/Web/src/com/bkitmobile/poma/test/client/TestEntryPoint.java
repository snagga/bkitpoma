package com.bkitmobile.poma.test.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

public class TestEntryPoint implements EntryPoint {

	public static TextArea txtResult;
	
	public static TextArea txtQuery; 
	
	@Override
	public void onModuleLoad() {
		txtQuery = new TextArea();
		txtQuery.setSize("700px", "50px");
		
		RootPanel.get().add(txtQuery);
		
		RootPanel.get().add(new Button("Execute SQL", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Test.executeQuery(txtQuery.getValue());	
			}
		}));
		
		txtResult = new TextArea();
		txtResult.setSize("700px", "300px");
		RootPanel.get().add(txtResult);
		
		RootPanel.get().add(new Button("insertTrackerTable", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Test.insertTrackerTable();	
			}
		}));

		RootPanel.get().add(new Button("insertTrackedTable", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.insertTrackedTable();
			}
		}));

		RootPanel.get().add(new Button("insertManageTable", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.insertManageTable();
			}
		}));

		RootPanel.get().add(new Button("insertStaffTable", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.insertStaffTable();
			}
		}));

		RootPanel.get().add(new Button("insertTrack", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.insertTrack();
			}
		}));

		RootPanel.get().add(new Button("insertWaypoint", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.insertWaypoint();
			}
		}));

		RootPanel.get().add(new Button("Login", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.login();
			}
		}));

		RootPanel.get().add(new Button("isLogin", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.isLogined();
			}
		}));

		RootPanel.get().add(new Button("get Tracks by tracked", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.getTracksByTracked(4L);
			}
		}));

		RootPanel.get().add(new Button("removeTracker", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Test.removeTracker("vo_mita_ov");
			}
		}));

		RootPanel.get().add(new Button("updateTracker", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Test.updateTracker();
			}
		}));

		RootPanel.get().add(new Button("resetDB", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Test.resetDatabase("blablabla");
			}
		}));
		
		RootPanel.get().add(new Button("insertTrackedRule", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Test.insertTrackedRule();
			}
		}));
	}
}

package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TestEntrypoint implements EntryPoint {
	Panel pn;
	DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		pn = new Panel();
		pn.setLayout(new VerticalLayout());
		pn.add(new Button("insertTrackerTable", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertTrackerTable();
			}
		}));

		pn.add(new Button("insertTrackedTable", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertTrackedTable();
			}
		}));

		pn.add(new Button("insertManageTable", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertManageTable();
			}
		}));

		pn.add(new Button("insertStaffTable", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertStaffTable();
			}
		}));

		pn.add(new Button("insertTrack", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertTrack();
			}
		}));

		pn.add(new Button("insertWaypoint", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.insertWaypoint();
			}
		}));

		pn.add(new Button("Login", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.login();
			}
		}));

		pn.add(new Button("isLogin", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.isLogined();
			}
		}));

		pn.add(new Button("get Tracks by tracked", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.getTracksByTracked(4L);
			}
		}));
		
		pn.add(new Button("removeTracker", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				Test.removeTracker("vo_mita_ov");
			}
		}));
		
		new Viewport(pn);
	}

}

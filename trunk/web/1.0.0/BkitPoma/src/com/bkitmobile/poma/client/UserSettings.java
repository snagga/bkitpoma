package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.timer.Task;
import com.bkitmobile.poma.client.timer.TimerTask;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSettings implements IsSerializable {
	public static boolean FIRST_CLICK = false;
	public static boolean MAP_MAXIMIZED = false;
	public static EntryPoint ENTRY_POINT;
	public static String host = "http://bkitpoma.appspot.com";
	public static String homePage = "http://bkitpoma.appspot.com";
	public static CTracker ctracker = null;
	public static TimerTask timerTask = new TimerTask(500); // Interval: 500ms

	static {
		DatabaseService.Util.getInstance().isLogined(
				new AsyncCallback<ServiceResult<CTracker>>() {
					public void onFailure(Throwable caught) {
						ctracker = null;
					};

					public void onSuccess(ServiceResult<CTracker> result) {
						System.out.println(result.getMessage());
						ctracker = result.getResult();
					};
				});
	}
}
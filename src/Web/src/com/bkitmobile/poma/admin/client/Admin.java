package com.bkitmobile.poma.admin.client;

import com.bkitmobile.poma.admin.client.ui.LoginAdminWindow;
import com.bkitmobile.poma.admin.client.ui.ManagerPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window.Location;
import com.gwtext.client.widgets.Viewport;

public class Admin implements EntryPoint {

	@Override
	public void onModuleLoad() {
		LoginAdminWindow.getInstance();
		com.google.gwt.user.client.Window.setTitle("Administrator page");
//		if (Location.getParameter("test") != null
//				&& Location.getParameter("test").equals("1")) {
//			test1();
//			return;
//		}
	}
	
	private void test1(){
//		PublishMapWindow publishMapWindow = PublishMapWindow.getInstance();
//		publishMapWindow.show();
		
	}


}

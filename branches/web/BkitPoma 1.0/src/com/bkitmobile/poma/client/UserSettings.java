package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.database.Tracker;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.IsSerializable;

public class UserSettings implements IsSerializable {
	public static boolean FIRST_CLICK = false;
	public static boolean MAP_MAXIMIZED = false;
	public static EntryPoint ENTRY_POINT;
	public static String host = "http://bkitpoma.appspot.com";
	public static String homePage = "http://bkitpoma.appspot.com";
	public static Tracker tracker = new Tracker("", "", "", "", "", "", "", "",
			"", "");
}

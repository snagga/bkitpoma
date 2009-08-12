package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.ui.MapPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class TestModule implements EntryPoint {
	
	private MapPanel panel;
	
	public void onModuleLoad() {
		final Button button = new Button("Click me");
		button.setIcon("images/MapToolbar/pause.gif");
		button.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				button.setIcon("images/MapToolbar/play.gif");
			}
		});
		
		RootPanel.get().add(button);
	}
	
	public static native int getScreenWidth() /*-{
    	return $wnd.screen.width;
 	}-*/;
	
	public static native int getScreenHeight() /*-{
		return $wnd.screen.height;
	}-*/;
	
}

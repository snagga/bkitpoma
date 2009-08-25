package com.bkitmobile.poma.client.ui;

import com.google.gwt.user.client.Event;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.core.EventObject;

public class MenuPanel extends TabPanel {

	private TrackedPanel trackedPanel;

	public MenuPanel() {
		sinkEvents(Event.ONKEYUP);
		setMargins(0, 0, 0, 0);
		setMonitorResize(true);
		setCollapsible(true);
		System.out.println("MenuPanel");
		trackedPanel = new TrackedPanel();
		trackedPanel.setTitle("Tracked menu");
		add(trackedPanel);
		Panel pnlAnother = new Panel("Another tab");
		pnlAnother.add(new Button("Press",new ButtonListenerAdapter(){
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				trackedPanel.loadTrackedList();
				
				
			}
		}));
		add(pnlAnother);
		
		// active item
		setActiveItem(0);
		
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		trackedPanel.onBrowserEvent(event);
	}
	
	public TrackedPanel getTrackedPanel() {
		return trackedPanel;
	}
	
}

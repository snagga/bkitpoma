package com.poma.bkitpoma.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.maps.client.MapWidget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class BkitPoma implements EntryPoint {

	private MapWidget map;
//	private RecaptchaWidget rw;
	private final DatabaseServiceAsync databaseService = DatabaseService.Util.getInstance();

	public void onModuleLoad() {
		
		Panel panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(0);
		panel.setLayout(new FitLayout());

		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());

		Toolbar toolbar = new Toolbar();
		toolbar.addButton(new ToolbarButton("Insert Tracker", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				
			}
		}));
		borderPanel.setTopToolbar(toolbar);
		
		// add bottom panel
		Panel bottomPanel = new HTMLPanel(
				"<center><p><img src='images/poma/logo-small.png' width='76' height='28' /> &copy; 2009 by BkitMobile</p></center>");
		bottomPanel.setHeight(30);

		BorderLayoutData bottomData = new BorderLayoutData(RegionPosition.SOUTH);
		bottomData.setMinSize(20);
		bottomData.setMaxSize(20);
		bottomData.setMargins(new Margins(0, 0, 0, 0));
		//bottomData.setSplit(true);
		borderPanel.add(bottomPanel, bottomData);
		
		BorderLayoutData menuData = new BorderLayoutData(RegionPosition.EAST);
		menuData.setSplit(true);
		menuData.setMinSize(280);
		menuData.setMaxSize(280);
		menuData.setMargins(new Margins(0, 0, 0, 0));

		ListPanel waypointPanel = new ListPanel(
				new int[] {ListPanel.FIELD_STRING, ListPanel.FIELD_STRING},
				new String[] {"Way point", "abc"}
				);
		waypointPanel.setTitle("Waypoint list");
		//waypointPanel.setWidth("100%");
		//waypointPanel.setAutoExpandColumn(0);
		waypointPanel.setCollapsible(true);
		waypointPanel.collapse();

		MapPanel map = new MapPanel();
		map.add(waypointPanel, new BorderLayoutData(RegionPosition.SOUTH));
		
		borderPanel.add(map,
				new BorderLayoutData(RegionPosition.CENTER));
		MenuPanel menuPanel = new MenuPanel("Menu");
		menuPanel.setWidth(280);
		borderPanel.add(menuPanel, menuData);
		
		
		panel.add(borderPanel);
		new Viewport(panel);
		map.init();
	}
}

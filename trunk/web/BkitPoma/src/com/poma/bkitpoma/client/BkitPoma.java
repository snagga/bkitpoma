package com.poma.bkitpoma.client;

import java.util.ArrayList;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;

import com.gwtext.client.widgets.Window;
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
	private final DatabaseServiceAsync databaseService = DatabaseService.Util.getInstance();

	public void onModuleLoad() {
		
		Panel panel = new Panel();
		panel.setBorder(false);
		panel.setPaddings(0);
		panel.setLayout(new FitLayout());

		Panel borderPanel = new Panel();
		borderPanel.setLayout(new BorderLayout());

		Toolbar toolbar = new Toolbar();
		toolbar.addButton(new ToolbarButton("Test", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
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
		menuData.setMinSize(175);
		menuData.setMaxSize(400);
		menuData.setMargins(new Margins(0, 0, 0, 0));

		borderPanel.add(new MenuPanel("Menu"), menuData);

		MapPanel map = new MapPanel();

		borderPanel.add(map,
				new BorderLayoutData(RegionPosition.CENTER));

		panel.add(borderPanel);

		new Viewport(panel);
		map.init();
	}
}

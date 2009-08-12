package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.localization.BkitPomaConstants;
import com.bkitmobile.poma.client.ui.ListPanel;
import com.bkitmobile.poma.client.ui.MapPanel;
import com.bkitmobile.poma.client.ui.MenuPanel;
import com.bkitmobile.poma.client.ui.RegisterTrackedFormWindow;
import com.bkitmobile.poma.client.ui.RegisterTrackerFormWindow;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapWidget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class BkitPoma implements EntryPoint {

	private MapPanel mapPanel;
	private ToolbarButton languageButton;
	private ToolbarButton loginButton;
	private final DatabaseServiceAsync databaseService = DatabaseService.Util.getInstance();
	static ListPanel waypointPanel;
	private MenuPanel menuPanel;
	private Toolbar toolbar;
	private Panel bottomPanel;
	private Panel mainPanel;
	private Panel centerPanel;
	private int mapWidth;
	private int mapHeight;
	private BkitPomaConstants constants = GWT.create(BkitPomaConstants.class);

	public void onModuleLoad() {
		/*
		 * Panel contains map widget
		 */
		mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());

		/*
		 * Toolbar of main panel
		 */
		toolbar = new Toolbar();
		toolbar.addButton(new ToolbarButton("Insert Tracker", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				Window window = new Window();
				window.add(new RegisterTrackerFormWindow());
				window.setSize(500, 600);
				window.show();
			}
		}));
		toolbar.addButton(new ToolbarButton("Insert Tracked", new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				RegisterTrackedFormWindow registerTrackedForm = new RegisterTrackedFormWindow("TAM1");
				registerTrackedForm.show();
				
//				Window window = new Window();
//				window.add(new RegisterTrackedForm("TAM1"));
//				window.setSize(500, 600);
//				window.show();
				
			}
		}));
		
		languageButton = new ToolbarButton(constants.selectLanguageLabel());
		languageButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO select language to display
			}
		});
		loginButton = new ToolbarButton(constants.loginButtonLabel());
		loginButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO show login window
				Window w = new Window("Login Window");
				w.setSize(300, 300);
				w.show();
			}
		});
		loginButton.setIcon("images/TopToolbar/login.gif");
		toolbar.addButton(languageButton);
		toolbar.addSeparator();
		toolbar.addButton(loginButton);
		//mainPanel.setTopToolbar(toolbar);
		mainPanel.add(toolbar, new BorderLayoutData(RegionPosition.NORTH));
		
		/*
		 * The bottom panel
		 */
		bottomPanel = new HTMLPanel(
				"<center><p><img src='images/poma/logo-small.png' width='76' height='28' /> &copy; 2009 by BkitMobile</p></center>");
		bottomPanel.setHeight(30);
		
		/*
		 * Layout data
		 */
		// layout of menu panel
		BorderLayoutData menuData = new BorderLayoutData(RegionPosition.EAST);
		menuData.setSplit(true);
		menuData.setSplit(false);
		menuData.setMargins(new Margins(0, 0, 0, 0));
		menuData.setFloatable(true);
		
		/*
		 * Menu
		 */
		menuPanel = new MenuPanel("Menu");
		menuPanel.setWidth(240);

		/*
		 * List of waypoint 
		 */
		waypointPanel = new ListPanel(
				new int[] {
						ListPanel.FIELD_STRING, 
						ListPanel.FIELD_STRING,
						ListPanel.FIELD_STRING,
						ListPanel.FIELD_STRING},
				new String[] {
						constants.timeHeader(), 
						constants.trackIdHeader(),
						constants.longtitudeHeader(),
						constants.latitudeHeader()}
				);
		waypointPanel.setTitle(constants.waypointPanelTitle());
		// TODO sample waypoint data
		for (int i = 0; i < 10; ++i)
			waypointPanel.addRecord(new String[] {
					constants.timeHeader() + i, 
					constants.trackIdHeader() + i, 
					constants.longtitudeHeader() + i, 
					constants.latitudeHeader() + i });
		waypointPanel.collapse();
		
		
		/*
		 * Map
		 */
		mapPanel = new MapPanel();

		/*
		 * Center panel
		 */
		centerPanel = new Panel();
		centerPanel.setLayout(new BorderLayout());
		
		// add map and way point list
		centerPanel.add(mapPanel, new BorderLayoutData(RegionPosition.CENTER));
		centerPanel.add(waypointPanel, new BorderLayoutData(RegionPosition.SOUTH));
		
		// resize listener for center panel
		centerPanel.addListener(new PanelListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {
				
				waypointPanel.setWidth(rawWidth + adjWidth);
			}
		});
		
		// maximize and minimize the map panel
		mapPanel.getMaxButton().addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if ( !UserSettings.MAP_MAXIMIZED ) {
					waypointPanel.hide();
					toolbar.hide();
					menuPanel.hide();
					bottomPanel.hide();
					mapWidth = centerPanel.getWidth();
					mapHeight = centerPanel.getHeight();
					centerPanel.setSize(
							com.google.gwt.user.client.Window.getClientWidth(),
							com.google.gwt.user.client.Window.getClientHeight()
							);
					mainPanel.fireEvent("resize");
					mapPanel.getMaxButton().setIcon("images/MapToolbar/min.gif");
					
				} else {
					centerPanel.setSize(mapWidth, mapHeight);
					
					toolbar.show();
					waypointPanel.show();
					menuPanel.show();
					bottomPanel.show();
					
					mapPanel.getMaxButton().setIcon("images/MapToolbar/max.gif");
					mainPanel.fireEvent("resize");
				}
				
				UserSettings.MAP_MAXIMIZED = !UserSettings.MAP_MAXIMIZED;
			}
		});
		
		
		/*
		 * Add to the main panel
		 */
		mainPanel.add(menuPanel, menuData);
		mainPanel.add(bottomPanel, new BorderLayoutData(RegionPosition.SOUTH));
		mainPanel.add(centerPanel, new BorderLayoutData(RegionPosition.CENTER));
		new Viewport(mainPanel);
		mapPanel.init();
	}
}

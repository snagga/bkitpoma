package com.bkitmobile.poma.client;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.localization.BkitPomaConstants;
import com.bkitmobile.poma.client.ui.ContactWindow;
import com.bkitmobile.poma.client.ui.LoginWindow;
import com.bkitmobile.poma.client.ui.MapPanel;
import com.bkitmobile.poma.client.ui.MenuPanel;
import com.bkitmobile.poma.client.ui.RegisterTrackedForm;
import com.bkitmobile.poma.client.ui.RegisterTrackerForm;
import com.bkitmobile.poma.client.ui.TrackedPanel;
import com.bkitmobile.poma.client.ui.TrackerProfileForm;
import com.bkitmobile.poma.client.ui.WayPointTablePanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;

public class BkitPoma implements EntryPoint {
	private MapPanel mapPanel;
	public static WayPointTablePanel waypointPanel;
	private MenuPanel menuPanel;
	private Panel mainPanel;
	private Panel centerPanel;
	private RegisterTrackerForm registerTrackerForm;
	private RegisterTrackedForm registerTrackedForm;
	private TrackerProfileForm trackerProfilePanel;
	public static Panel cardPanel;


	private ToolbarButton vieButton;
	private ToolbarButton btnLogin;
	private ToolbarButton enButton;
	private ToolbarButton btnRegisterNewTracker;
	private ToolbarButton btnRegisterNewTracked;
	private ToolbarButton contactButton;
	private ToolbarButton btnShowTrackerProfile;
	private ToolbarTextItem txtItemTracker;
	private Toolbar toolbar;

	private final DatabaseServiceAsync databaseService = DatabaseService.Util.getInstance();
	
	private BkitPomaConstants constants = GWT.create(BkitPomaConstants.class);

	private DatabaseServiceAsync dbServivce = DatabaseService.Util.getInstance();

	private int mapWidth;
	private int mapHeight;
	private int toolbarHeight = 36;

	private boolean isLogin = false;

	int test = 0;

	public void onModuleLoad() {
		if (test == 1) 
			test();
		else 
			main();
	}

	void test() {
		new Viewport(new MenuPanel());
	}
	
	void testMenuPanel() {
		/*
		 *  use sinkEvents and onBrowserEvent for the top panel
		 */
		final TrackedPanel trackedPanel = new TrackedPanel();
		trackedPanel.setTitle("Tracked Panel");
		
		final TabPanel tabPanel = new TabPanel();
		tabPanel.setSize(500, 500);
		tabPanel.add(trackedPanel);
		tabPanel.add(new Panel("Another tab"));
		tabPanel.setActiveItem(0);
		
		final Panel pomaPanel = new Panel() {
			@Override
			public void onBrowserEvent(Event event) {
				trackedPanel.onBrowserEvent(event);
			}
		};
		pomaPanel.sinkEvents(Event.ONKEYUP);
		pomaPanel.setSize(600, 600);
		pomaPanel.setLayout(new BorderLayout());
		pomaPanel.add(tabPanel, new BorderLayoutData(RegionPosition.EAST));
		
		RootPanel.get().add(pomaPanel);
		trackedPanel.loadTrackedList();
		
	}
	
	void main() {
		/*
		 * Main panel
		 */
		mainPanel = new Panel();
		mainPanel.setLayout(new BorderLayout());

		/*
		 * Toolbar of main panel
		 */
		toolbar = new Toolbar();
		toolbar.setHeight(toolbarHeight);

		// Vietnamese mode
		vieButton = new ToolbarButton();
		vieButton.setIcon("images/flags/vn.ico");
		vieButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO select Vietnamse language to display
			}
		});

		// English mode
		enButton = new ToolbarButton();
		enButton.setIcon("images/flags/en.ico");
		enButton.addListener(new ButtonListenerAdapter() {

			@Override
			public void onClick(Button button, EventObject e) {
				// TODO select English language to display
			}
		});

		// Login button
		btnLogin = new ToolbarButton(constants.loginButtonLabel());
		
		btnLogin.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (isLogin) {
					dbServivce.logoutTracker(new AsyncCallback<ServiceResult<CTracker>>() {
								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(
										ServiceResult<CTracker> result) {
									if (result.isOK()) {
										MessageBox.alert("Log off successfull");
										isLogin = false;
										btnLogin.setText("Login");
										reloadPage();
									} else {
										MessageBox.alert(result.getMessage());
									}
								}

							});
				} else {
					LoginWindow.getInstance().show();
				}
			}
		});
		

		// Poma logo
		HTML logo = new HTML("<img src='images/poma/logo-header.gif' />");

		// Button register new tracker
		btnRegisterNewTracker = new ToolbarButton("Register new tracker",
				new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {
						cardPanel.setActiveItemID(registerTrackerForm.getId());
					}

				});

		// Button register new tracked
		btnRegisterNewTracked = new ToolbarButton(constants
				.insertTrackedLabel(), new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				cardPanel.setActiveItemID(registerTrackedForm.getId());
			}

		});

		// Button show tracker profile
		btnShowTrackerProfile = new ToolbarButton("Show tracker profile",
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						cardPanel.setActiveItemID(trackerProfilePanel.getId());
					}

				});

		// Button contact to admin
		contactButton = new ToolbarButton("Contact");
		contactButton.setIcon("images/silk/email.png");
		contactButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				ContactWindow.getInstance().show();

			}
		});

		// txtItemTracker
		txtItemTracker = new ToolbarTextItem("");

		// Add components to toolbar
		toolbar.addElement(logo.getElement());
		toolbar.addElement(new HTML(
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
				.getElement());

		toolbar.addButton(btnRegisterNewTracked);
		toolbar.addButton(btnRegisterNewTracker);
		toolbar.addButton(btnShowTrackerProfile);
		// toolbar.addSeparator();
		toolbar.addButton(btnLogin);
		toolbar.addSeparator();
		toolbar.addButton(contactButton);

		toolbar.addFill();
		toolbar.addItem(txtItemTracker);
		toolbar.addButton(vieButton);
		toolbar.addButton(enButton);

		/*
		 * Menu
		 */
		menuPanel = new MenuPanel();
		menuPanel.setWidth(240);
		menuPanel.setBorder(false);
		menuPanel.setBodyBorder(false);

		BorderLayoutData menuData = new BorderLayoutData(RegionPosition.EAST);
		menuData.setSplit(true);
		menuData.setSplit(false);
		menuData.setMargins(new Margins(0, 0, 0, 0));
		menuData.setFloatable(true);


		
		/*
		 * Map
		 */
		mapPanel = new MapPanel();

		/*
		 * List of waypoint
		 */
		waypointPanel = new WayPointTablePanel(mapPanel.getMapWidget());
		waypointPanel.setHeight(200);
		waypointPanel.collapse();
		
		/*
		 * Center panel
		 */
		centerPanel = new Panel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setPaddings(5);
		centerPanel.setBorder(false);
		centerPanel.setBodyBorder(false);

		centerPanel.add(mapPanel, new BorderLayoutData(RegionPosition.CENTER));
		centerPanel.add(waypointPanel, new BorderLayoutData(
				RegionPosition.SOUTH));

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
				if (!UserSettings.MAP_MAXIMIZED) {
					waypointPanel.hide();
					toolbar.hide();
					menuPanel.hide();
					mapWidth = centerPanel.getWidth();
					mapHeight = centerPanel.getHeight();
					centerPanel
							.setSize(com.google.gwt.user.client.Window
									.getClientWidth(),
									com.google.gwt.user.client.Window
											.getClientHeight());
					mainPanel.fireEvent("resize");
					mapPanel.getMaxButton()
							.setIcon("images/MapToolbar/min.gif");

				} else {
					centerPanel.setSize(mapWidth, mapHeight);

					toolbar.show();
					waypointPanel.show();
					menuPanel.show();

					mapPanel.getMaxButton()
							.setIcon("images/MapToolbar/max.gif");
					mainPanel.fireEvent("resize");
				}

				UserSettings.MAP_MAXIMIZED = !UserSettings.MAP_MAXIMIZED;
			}
		});

		/*
		 * Register new tracker
		 */
		registerTrackerForm = new RegisterTrackerForm();

		/*
		 * Register new tracked
		 */
		registerTrackedForm = new RegisterTrackedForm("abc");

		/*
		 * Tracker profile panel
		 */
		trackerProfilePanel = new TrackerProfileForm();

		/*
		 * Card panel
		 */
		cardPanel = new Panel();
		cardPanel.setBorder(false);
		cardPanel.setBodyBorder(false);
		cardPanel.setLayout(new CardLayout());

		cardPanel.add(centerPanel);
		cardPanel.add(registerTrackerForm);
		cardPanel.add(registerTrackedForm);
		cardPanel.add(trackerProfilePanel);

		cardPanel.setActiveItem(0);

		/*
		 * Add to the main panel
		 */
		mainPanel.add(toolbar, new BorderLayoutData(RegionPosition.NORTH));
		mainPanel.add(menuPanel, menuData);
		mainPanel.add(cardPanel, new BorderLayoutData(RegionPosition.CENTER));

		// Is login
		dbServivce.isLogined(new AsyncCallback<ServiceResult<CTracker>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ServiceResult<CTracker> result) {
				System.out.println(result.getMessage());
				if (result.isOK()) {
					menuPanel.expand();
					UserSettings.ctracker = result.getResult();
					btnLogin.setText("Log off");
					isLogin = true;
					if (UserSettings.ctracker.getName() != null)
						txtItemTracker.setText(UserSettings.ctracker.getName());
					else
						txtItemTracker.setText(UserSettings.ctracker.getUsername());
					menuPanel.getTrackedPanel().loadTrackedList();
					
					btnRegisterNewTracker.disable();
					
				} else {
					btnShowTrackerProfile.disable();
					btnRegisterNewTracked.disable();
				}
			}
		});
		new Viewport(mainPanel);
		mapPanel.init();
	}

	public static void returnToMap() {
		cardPanel.setActiveItem(0);
	}

	public static void loginSuccessfully() {
		reloadPage();
	}

	public static native void reloadPage() /*-{
		$wnd.location.reload();
	}-*/;
}

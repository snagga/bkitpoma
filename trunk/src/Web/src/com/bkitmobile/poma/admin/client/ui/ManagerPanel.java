package com.bkitmobile.poma.admin.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class ManagerPanel extends Panel implements HistoryListener {

	private Panel centerPanel;
	private Panel westPanel;
	// private Button btnTrackers;

	private TrackerPanel trackerPanel;
	private ConfigPanel configPanel;

	private Toolbar toolbar;

	private int toolbarHeight = 36;
	// private Button btnSettings;

	private HTML hTMLTrackers;
	private HTML hTMLSettings;

	private Store store;

	private final String INIT_STATE = "manage";

	public ManagerPanel() {
		super();
		this.setBorder(true);
		init();
		layout();
		initHistorySupport();
	}

	private void init() {
		initStore();

		toolbar = new Toolbar();
		toolbar.setHeight(toolbarHeight);
		HTML logo = new HTML("<img src='images/poma/logo-header.gif' />");
		toolbar.addElement(logo.getElement());
		toolbar.addElement(new HTML(
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
				.getElement());

		trackerPanel = new TrackerPanel();
		trackerPanel.setId("manage");
		configPanel = new ConfigPanel();
		configPanel.setId("settings");
	}

	private void layout() {
		this.setLayout(new BorderLayout());
		BorderLayoutData centerLayoutData = new BorderLayoutData(
				RegionPosition.CENTER);
		centerLayoutData.setMargins(new Margins(5, 0, 5, 5));

		// setup the west regions layout properties
		BorderLayoutData westLayoutData = new BorderLayoutData(
				RegionPosition.WEST);
		westLayoutData.setMargins(new Margins(5, 5, 0, 5));
		westLayoutData.setCMargins(new Margins(5, 5, 5, 5));
		westLayoutData.setMinSize(155);
		westLayoutData.setMaxSize(350);
		westLayoutData.setSplit(true);

		createWestPanel();
		createCenterPanel();

		centerPanel.add(trackerPanel);
		centerPanel.add(configPanel);

		centerPanel.setActiveItemID(trackerPanel.getId());

		this.add(centerPanel, centerLayoutData);
		this.add(westPanel, westLayoutData);

		this.add(toolbar, new BorderLayoutData(RegionPosition.NORTH));
	}

	private void createCenterPanel() {
		centerPanel = new Panel();
		centerPanel.setLayout(new CardLayout());
		// centerPanel.setActiveItem(0);
		centerPanel.setPaddings(15);
		centerPanel.setBorder(false);
		centerPanel.setBodyBorder(false);
		centerPanel.setActiveItem(0);
	}

	@SuppressWarnings("deprecation")
	private void createWestPanel() {
		westPanel = new Panel();
		westPanel.setId("side-nav");
		westPanel.setTitle("Function");
		westPanel.setLayout(new VerticalLayout());
		westPanel.setWidth(210);
		westPanel.setCollapsible(true);

		hTMLTrackers = new HTML(formatTextListFunction("Manage Trackers", true));
		// HTMLTrackers.setStyleName("gwt-HTML");
		// HTMLTrackers.addStyleName("gwt-HTML");
		// HTMLTrackers.addStyleDependentName("gwt-HTML");
		// HTMLTrackers.setStylePrimaryName("gwt-HTML");
		// HTMLTrackers.getElement().setAttribute("font-size", "10px");
		// hTMLTrackers.setHTML();
		hTMLTrackers.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("manage");
			}
		});
		DOM.setStyleAttribute(hTMLTrackers.getElement(), "cursor", "hand");

		hTMLSettings = new HTML(formatTextListFunction("Settings", false));
		hTMLSettings.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("settings");
			}
		});
		DOM.setStyleAttribute(hTMLSettings.getElement(), "cursor", "hand");

		// btnTrackers = new Button("Manage Trackers",
		// new ButtonListenerAdapter() {
		// @Override
		// public void onClick(Button button, EventObject e) {
		// super.onClick(button, e);
		// centerPanel.setActiveItemID(trackerPanel.getId());
		// }
		// });
		// btnTrackers.setToggleGroup("function");
		//
		// btnSettings = new Button("Settings", new ButtonListenerAdapter() {
		// @Override
		// public void onClick(Button button, EventObject e) {
		// // TODO Auto-generated method stub
		// super.onClick(button, e);
		// centerPanel.setActiveItemID(configPanel.getId());
		// }
		// });
		// btnSettings.setToggleGroup("function");

		westPanel.add(hTMLTrackers);
		westPanel.add(hTMLSettings);

		// westPanel.addButton(btnTrackers);
		// // westPanel.add(btnTrackers);
		// westPanel.addButton(btnSettings);
	}

	private static String formatTextListFunction(String text, boolean selected) {
		if (selected) {
			return "<b><font size='5' face='Arial' color='#1C1CFE'>" + text
					+ "</font></b>";
		} else {
			return "<font size='3' face='Arial' color='#7A7A80'>" + text
					+ "</font>";
		}
	}

	private void centerPanelChanged(String token) {
		if (token.equals("manage")) {
			centerPanel.setActiveItemID(trackerPanel.getId());
			hTMLTrackers
					.setHTML(formatTextListFunction("Manage Trackers", true));
			hTMLSettings.setHTML(formatTextListFunction("Settings", false));
		} else {
			centerPanel.setActiveItemID(configPanel.getId());
			hTMLTrackers.setHTML(formatTextListFunction("Manage Trackers",
					false));
			hTMLSettings.setHTML(formatTextListFunction("Settings", true));
		}

	}

	public void initStore() {
		if (store == null) {
			MemoryProxy proxy = new MemoryProxy(getData());

			RecordDef recordDef = new RecordDef(new FieldDef[] {
					new StringFieldDef("id"), new StringFieldDef("title"),
					new StringFieldDef("icon"), });

			ArrayReader reader = new ArrayReader(0, recordDef);
			store = new Store(proxy, reader);
			store.load();
		}
	}

	private static Object[][] getData() {
		return new Object[][] { new Object[] { "settings", "Settings", null },
				new Object[] { "manage", "Manage Trackers", null } };
	}

	private void initHistorySupport() {
		// add the MainPanel as a history listener
		History.addHistoryListener(this);
		// check to see if there are any tokens passed at startup via the
		// browser's URI
		String token = History.getToken();
		if (token.length() == 0) {
			onHistoryChanged(INIT_STATE);
		} else {
			onHistoryChanged(token);
		}
	}

	/**
	 * this method is called when the fwd/back buttons are invoked on the
	 * browser. this is also invoked when hyperlinks (generated by the app) are
	 * clicked.
	 */
	public void onHistoryChanged(String historyToken) {
		// todo - when the token is passed, do something to update the state of
		// your app
		
		if (historyToken == null || historyToken.length() == 0) {
			historyToken = INIT_STATE;
		}

		centerPanelChanged(historyToken);

		Record record = store.getById(historyToken);
		if (record != null) {
			String title = record.getAsString("title");
			Window.setTitle(title);
		}
	}
}

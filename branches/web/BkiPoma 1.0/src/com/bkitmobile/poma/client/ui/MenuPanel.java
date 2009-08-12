package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class MenuPanel extends Panel {
	public MenuPanel(String title, int width) {
		constructor(title, width);
	}

	public MenuPanel(String title) {
		constructor(title, 400);
	}

	public MenuPanel() {
		constructor("", 400);
	}

	private final AccordionLayout accordion = new AccordionLayout(true);

	@SuppressWarnings("deprecation")
	private void constructor(String title, int width) {
		setTitle(title);
		setLayout(accordion);
		setCollapsible(true);
		//setWidth(width);
		setMonitorResize(true);

		Panel trackedPanel = new Panel();
		trackedPanel.setTitle("Tracked menu");
		trackedPanel.setBorder(false);
		trackedPanel.setIconCls("forlder-icon");
		trackedPanel.add(new TrackedPanel());
		add(trackedPanel);
		

		Panel settingsPanel = new Panel();
		settingsPanel.setHtml("<p>Some settings in here.</p>");
		settingsPanel.setTitle("Settings");
		settingsPanel.setBorder(false);
		settingsPanel.setIconCls("settings-icon");
		add(settingsPanel);
		
	}
}

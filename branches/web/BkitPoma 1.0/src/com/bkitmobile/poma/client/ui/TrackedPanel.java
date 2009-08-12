package com.bkitmobile.poma.client.ui;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.collections.functors.ConstantTransformer;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.localization.TrackedPanelConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListener;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListener;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TrackedPanel extends Panel {
	
	private ListPanel trackedListPanel;
	private ListPanel trackListPanel;
	private LoadingPanel filterPanel;
	private DateField firstDate;
	private DateField lastDate;
	private String trackerUsername;
	private TextField filterField;
	private Panel datePanel;
	private Button filterButton;
	// TODO true when user want to view a track list from filterd dates
	private boolean viewTrack = false;
	private TrackedPanelConstants constants = GWT.create(TrackedPanelConstants.class);
	
	/**
	 * Use when user wants to view tracked list
	 * Auto run when a tracker login
	 * @param trackerUsername
	 */
	public void loadTrackedList(String trackerUsername) {
		this.trackerUsername = trackerUsername;
		loadTracked();
	}
	
	// TODO load track list corresponded to a tracked
	public void loadTrackList(String trackedName) {
		DatabaseService.Util.getInstance().getTracks(trackedName, 
				new AsyncCallback<String[]>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert(caught.toString());
					}

					@Override
					public void onSuccess(String[] result) {
						for (int i = 0; i < result.length; ++i)
							trackListPanel.addRecord(new String[] {result[i]});
					}
			
		});
	}
	
	// TODO load waypoint list corresponded to a track
	// ArrayList<WayPoint> getWayPoint() ???
	public void loadWaypointList(String track) {
		
	}
	
	public TrackedPanel() {
		createForm();
	}
	
	private void createForm() {
		
		this.setLayout(new VerticalLayout());
		// this.setAutoScroll(true);
		
		/*
		 *  Filter of tracked name
		 */
		// TODO center layout for filter panel
		filterPanel = new LoadingPanel();
		filterPanel.setPaddings(5);
		filterPanel.setBorder(false);
		filterPanel.setLayout(new HorizontalLayout(10));
		
		// filter text field
		filterField = new TextField();
		filterField.addListener(new TextFieldListenerAdapter() {
			
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				filter();
			}
			
		});
		ToolTip filterTip = new ToolTip(constants.toolTipText());
		filterTip.applyTo(filterField);
		
		//  filter button
		filterButton = new Button();
		filterButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				filter();
			}
		});
		filterButton.setIcon("/images/TrackedMenu/search.gif");
		ToolTip filterTip2 = new ToolTip(constants.toolTipText2());
		filterTip2.applyTo(filterButton);
		
		// create filter panel
		filterPanel.add(filterField);
		filterPanel.add(filterButton);
		
		
		/*
		 * List of tracked
		 */
		trackedListPanel = new ListPanel(
				new int[] {ListPanel.FIELD_STRING,ListPanel.FIELD_STRING},
				new String[] {constants.trackedNameGrid()});
		// TODO sample tracked data
		trackedListPanel.addRecord(new String[] {"abc"});
		trackedListPanel.addRecord(new String[] {"adef"});
		trackedListPanel.addRecord(new String[] {"xyz"});
		for (int i = 0; i < 10; ++i)
			trackedListPanel.addRecord(new String[] {"sdflds" });
		trackedListPanel.setHeight(130);
		
		
		/*
		 *  Date filter
		 */
		// First date
		firstDate = new DateField("From date", "d-m-Y");
		firstDate.setWidth(80);
		firstDate.setReadOnly(true);
		firstDate.setEmptyText(constants.fromDateEmptyText());
		firstDate.addListener(new FirstDateHandler());
		firstDate.setValidator(new Validator() {
			@Override
			public boolean validate(String value) throws ValidationException {
				if ( lastDate.getValue() != null && firstDate.getValue().compareTo(lastDate.getValue()) > 0 )
					return false;
				return true;
			}
		});
		
		// Last date
		lastDate = new DateField("To date", "d-m-Y");
		lastDate.setWidth(80);
		lastDate.setEmptyText(constants.toDateEmptyText());
		lastDate.setReadOnly(true);
		lastDate.addListener(new LastDateHandler());
		lastDate.setValidator(new Validator() {

			@Override
			public boolean validate(String value) throws ValidationException {
				if ( firstDate.getValue() != null && lastDate.getValue().compareTo(firstDate.getValue()) < 0 )
					return false;
				return true;
			}
			
		});
		lastDate.addListener(new FieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				
				if (viewTrack) {
					// TODO show track corresponded to tracked in filtered dates
					
				}
			}
		});
		
		// Date filter panel
		datePanel = new Panel();
		datePanel.setLayout(new HorizontalLayout(5));
		datePanel.setPaddings(5);
		datePanel.add(new Label(constants.fromDateLabel()));
		datePanel.add(firstDate);
		datePanel.add(new Label(constants.toDateLabel()));
		datePanel.add(lastDate);
		
		/*
		 * List of track
		 */
		trackListPanel = new ListPanel(
				new int[] {ListPanel.FIELD_STRING,ListPanel.FIELD_STRING},
				new String[] { constants.trackNameGrid() }
				);
		// TODO sample track data
		for (int i = 0; i < 10; ++i)
			trackListPanel.addRecord(new String[] {"track" + i});
		trackListPanel.setHeight(130);
		
		
		/*
		 *  Add component to main panel
		 */
		this.add(filterPanel);
		this.add(trackedListPanel);
		this.add(datePanel);
		this.add(trackListPanel);
	}
	
	private void filter() {
		trackedListPanel.filter(0, filterField.getText(), true);
		lastDate.setValue(firstDate.getRawValue());
	}

	private void loadTracked() {
		DatabaseServiceAsync dsa = DatabaseService.Util.getInstance();
		dsa.getTrackedList(this.trackerUsername,
				new AsyncCallback<String[]>() {

			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert(caught.toString());
			}

			@Override
			public void onSuccess(final String[] result) {
				
				DeferredCommand.addPause();
				DeferredCommand.addCommand(new Command() {
					
					public void execute() {
						filterPanel.startLoading(constants.loadTrackedMessage());
					}
					
				});
				DeferredCommand.addPause();
				DeferredCommand.addCommand(new Command() {
					
					public void execute() {
				        
				        // load tracked list
						String[][] data = new String[result.length][];
						for (int i = 0; i < result.length; ++i)
							data[i] = new String[] {result[i]};
						trackedListPanel.setData(data);
					}
					
				});
				DeferredCommand.addPause();
				DeferredCommand.addCommand(new Command() {
					
					public void execute() {
						filterPanel.stopLoading();
					}
					
				});
				
			}
			
		});
	}
	
	private class LastDateHandler extends DatePickerListenerAdapter {
		@Override
		public void onSelect(DatePicker dataPicker, Date date) {
			if ( firstDate.getValue() != null && date.compareTo(firstDate.getValue()) < 0 ) {
				MessageBox.alert(constants.lastDateWarning());
			} 
		}
		
	}
	
	private class FirstDateHandler extends DatePickerListenerAdapter  {
		@Override
		public void onSelect(DatePicker dataPicker, Date date) {
			if ( lastDate.getValue() != null && date.compareTo(lastDate.getValue()) > 0 ) {
				MessageBox.alert(constants.firstDateWarning());
				viewTrack = false;
			} else
				viewTrack = true;
		}
		
	}
	
}

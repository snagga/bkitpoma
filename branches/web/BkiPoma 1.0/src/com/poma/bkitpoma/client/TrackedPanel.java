package com.poma.bkitpoma.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.TextFieldListener;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
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
	
	public void setTrackerUsername(String trackerUsername) {
		this.trackerUsername = trackerUsername;
		loadData();
	}
	
	public TrackedPanel() {
		createForm();
		//loadData();
	}
	
	public TrackedPanel(String trackerUsername) {
		this();
		setTrackerUsername(trackerUsername);
	}
	
	private void createForm() {
		this.setLayout(new VerticalLayout());
		this.setPaddings(10, 10, 10, 10);
		this.setAutoScroll(true);
		
		// suggest box
		filterPanel = new LoadingPanel();
		//filterPanel.setPaddings(10);
		filterPanel.setLayout(new HorizontalLayout(10));
		filterPanel.add(new Label("Name of trackeds"));
		filterField = new TextField();
		filterField.addListener(new TextFieldListenerAdapter() {
			
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				trackedListPanel.filter(0, filterField.getText(), true);
				lastDate.setValue(firstDate.getRawValue());
			}
			
		});
		ToolTip filterTip = new ToolTip("Press enter to filter tracked name");
		filterTip.applyTo(filterField);
		filterPanel.add(filterField);
		
		// tracked list
		trackedListPanel = new ListPanel(
				new int[] {ListPanel.FIELD_STRING,ListPanel.FIELD_STRING},
				new String[] {"Tracked name","Atribute"});
		trackedListPanel.setWidth(250);
		trackedListPanel.setPaddings(5);
		// TODO sample data
		trackedListPanel.addRecord(new String[] {"abc"});
		trackedListPanel.addRecord(new String[] {"adef"});
		trackedListPanel.addRecord(new String[] {"xyz"});
		for (int i = 0; i < 10; ++i)
			trackedListPanel.addRecord(new String[] {"sdflds" });
		
		
		// date filter
		firstDate = new DateField("From date", "d-m-Y");
		firstDate.setReadOnly(true);
		firstDate.addListener(new FirstDateHandler());
		firstDate.setValidator(new Validator() {
			@Override
			public boolean validate(String value) throws ValidationException {
				if ( lastDate.getValue() != null && firstDate.getValue().compareTo(lastDate.getValue()) > 0 )
					return false;
				return true;
			}
		});
		
		lastDate = new DateField("To date", "d-m-Y");
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
		
		Panel datePanel = new Panel();
		datePanel.setLayout(new HorizontalLayout(5));
		datePanel.add(new Label("From"));
		datePanel.add(firstDate);
		datePanel.add(new Label("To"));
		datePanel.add(lastDate);
		
		// track list
		trackListPanel = new ListPanel(
				new int[] {ListPanel.FIELD_STRING,ListPanel.FIELD_STRING},
				new String[] {"Track name","Attribute"}
				);
		trackListPanel.setWidth(250);
		//trackListPanel.collapse();
		
		// add component to main panel
		this.add(filterPanel);
		this.add(trackedListPanel);
		this.add(datePanel);
		this.add(trackListPanel);
	}

	private void loadData() {
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
						filterPanel.startLoading("Load tracked names ...");
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
				MessageBox.alert("Your must enter a larger date than \"From date\"");
			} 
		}
		
	}
	
	private class FirstDateHandler extends DatePickerListenerAdapter {
		@Override
		public void onSelect(DatePicker dataPicker, Date date) {
			if ( lastDate.getValue() != null && date.compareTo(lastDate.getValue()) > 0 ) {
				MessageBox.alert("You must enter a smaller date than \"To date\"");
			} 
		}
	}
	
}

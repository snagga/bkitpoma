package com.poma.bkitpoma.client;

import java.util.Date;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.TimeField;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TrackedPanel extends Panel {
	
	private TrackedListPanel trackedListPanel;
	private TrackListPanel trackListPanel;
	private ComboBox suggestBox;
	private LoadingPanel suggestPanel;
	private DateField firstDate;
	private DateField lastDate;
	private String trackerUsername;
	
	public void setTrackerUsername(String trackerUsername) {
		this.trackerUsername = trackerUsername;
	}
	
	public TrackedPanel(String trackerUsername) {
		this.trackerUsername = trackerUsername;
		createForm();
		
//		DatabaseServiceAsync dsa = DatabaseService.Util.getInstance();
//		dsa.getTrackedList(this.trackerUsername,
//				new AsyncCallback<String[]>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				MessageBox.alert(caught.toString());
//			}
//
//			@Override
//			public void onSuccess(final String[] result) {
//				
//				DeferredCommand.addPause();
//				DeferredCommand.addCommand(new Command() {
//					
//					public void execute() {
//						suggestPanel.startLoading("Load tracked names ...");
//					}
//					
//				});
//				DeferredCommand.addPause();
//				DeferredCommand.addCommand(new Command() {
//					
//					public void execute() {
//						// load tracked names
//						Store store = new SimpleStore("names", result);
//				        store.load();
//				        suggestBox.setStore(store);
//				        
//				        // load tracked list
//						String[][] data = new String[result.length][];
//						for (int i = 0; i < result.length; ++i)
//							data[i] = new String[] {result[i]};
//						trackedListPanel.setData(data);
//					}
//					
//				});
//				DeferredCommand.addPause();
//				DeferredCommand.addCommand(new Command() {
//					
//					public void execute() {
//						suggestPanel.stopLoading();
//					}
//					
//				});
//				
//			}
//			
//		});

	}
	
	private void createForm() {
		this.setLayout(new VerticalLayout());
		this.setPaddings(10, 10, 10, 10);
		
		// suggest box
		suggestPanel = new LoadingPanel();
		suggestPanel.setPaddings(10);
		suggestPanel.setLayout(new ColumnLayout());
		suggestPanel.add(new Label("Name of trackeds"), new ColumnLayoutData(0.5));
        suggestBox = new ComboBox();
        suggestBox.setMinChars(1);
        suggestBox.setFieldLabel("Tracked names");
        suggestBox.setDisplayField("names");
        suggestBox.setMode(ComboBox.LOCAL);
        suggestBox.setEmptyText("Enter a name");
        suggestBox.setLoadingText("Searching...");
        suggestBox.setTypeAhead(true);
        suggestBox.setSelectOnFocus(true);
        suggestBox.setHideTrigger(true);
		suggestPanel.add(suggestBox, new ColumnLayoutData(0.5));
		
		// tracked list
		trackedListPanel = new TrackedListPanel(
				new int[] {TrackedListPanel.FIELD_STRING},
				new String[] {"Tracked name"});
		trackedListPanel.setPaddings(5);
		trackedListPanel.setTitle("Tracked display");
		
		// date filter
		Panel firstDatePanel = new Panel("Date filter");
		firstDatePanel.setPaddings(10);
		firstDatePanel.setLayout(new ColumnLayout());
		firstDatePanel.add(new Label("From date"), new ColumnLayoutData(0.5));
		firstDate = new DateField("", "d-M-Y");
		firstDate.setReadOnly(true);
		firstDate.addListener(new FirstDateHandler());
		firstDatePanel.add(firstDate, new ColumnLayoutData(0.5));
		
		Panel lastDatePanel = new Panel();
		lastDatePanel.setPaddings(10);
		lastDatePanel.setLayout(new ColumnLayout());
		lastDatePanel.add(new Label("To date"), new ColumnLayoutData(0.5));
		lastDate = new DateField("", "d-M-Y");
		lastDate.setReadOnly(true);
		lastDate.addListener(new LastDateHandler());
		lastDatePanel.add(lastDate, new ColumnLayoutData(0.5));
		
		// track list
		trackListPanel = new TrackListPanel();
		trackListPanel.setPaddings(5);
		trackListPanel.setTitle("Track display");
		
		// add component to main panel
		this.add(suggestPanel);
		this.add(trackedListPanel);
		this.add(firstDatePanel);
		this.add(lastDatePanel);
		this.add(trackListPanel);
	}
	
	private class LastDateHandler extends DatePickerListenerAdapter {
		@Override
		public void onSelect(DatePicker dataPicker, Date date) {
			if ( firstDate.getValue() != null && date.compareTo(firstDate.getValue()) < 0 ) {
				MessageBox.alert("Your must enter a larger date than \"From date\"");
			} else 
				lastDate.setValue(date.toString() + "23:59:59");
		}
		
	}
	
	private class FirstDateHandler extends DatePickerListenerAdapter {
		@Override
		public void onSelect(DatePicker dataPicker, Date date) {
			if ( lastDate.getValue() != null && date.compareTo(lastDate.getValue()) > 0 ) {
				MessageBox.alert("You must enter a smaller date than \"To date\"");
				firstDate.setValue("");
			} 
		}
	}
	
}

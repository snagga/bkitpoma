package com.bkitmobile.poma.client.ui;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTrack;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.bkitmobile.poma.client.localization.TrackedPanelConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TrackedPanel extends Panel {

	// private TablePanel trackedListPanel;
	private TablePanel trackListPanel;
	// private LoadingPanel filterPanel;
	private Panel pnlTest;
	private Panel datePanel;

	private DateField dateFirst;
	private DateField dateLast;

	// TODO true when user want to view a track list from filterd dates
	private boolean viewTrack = false;

	private TrackedPanelConstants constants = GWT
			.create(TrackedPanelConstants.class);

	DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	private int listHeight = 150;
	private int pageSize = 6;
	private WayPointTablePanel wp;

	private Store store = null;

	private ComboBox cb;

	private Object[][] data = new Object[1][1];

	/**
	 * Use when user wants to view tracked list Auto run when a tracker login
	 * 
	 * @param trackerUsername
	 */
	public void loadTrackedList() {

		dbAsync.getTrackedsByTracker(UserSettings.ctracker.getUsername(),
				new AsyncCallback<ServiceResult<ArrayList<CTracked>>>() {

					@Override
					public void onFailure(Throwable caught) {
						System.out.println(caught);
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CTracked>> result) {
						// load tracked list
						System.out.println("loadTrackedList");
						if (result != null) {
							System.out.println(result.getMessage());
							ArrayList<CTracked> trackedList = result
									.getResult();
							if (trackedList == null || trackedList.size() == 0) {
								System.out.println("Tracked list null");
								// trackedListPanel.stopLoading();
								return;
							}

							String[][] data = new String[trackedList.size()][];
							for (int i = 0; i < trackedList.size(); ++i) {
								data[i] = new String[] { trackedList.get(i)
										.getUsername().toString() };
								// trackedListPanel.addRecord(data[i]);
							}
							// trackedListPanel.setData(data);

						} else {
							MessageBox.alert("Tracked list null");
						}

						// For Combobox
						ArrayList<CTracked> trackedList = result.getResult();
						String[][] data = new String[trackedList.size()][];
						for (int i = 0; i < trackedList.size(); i++) {
							String name = trackedList.get(i).getName().equals(
									"") ? String.valueOf(trackedList.get(i)
									.getUsername()) : trackedList.get(i)
									.getName();
							data[i] = new String[] {
									String.valueOf(trackedList.get(i)
											.getUsername()), name };
						}

						store.setDataProxy(new PagingMemoryProxy(data));
						store.load();

						// trackedListPanel.stopLoading();
					}

				});

		// trackedListPanel.startLoading("Load tracked list");
	}

	// TODO load track list corresponded to a tracked
	public void loadTrackList(Long trackedID) {
		dbAsync.getTracksByTracked(trackedID,
				new AsyncCallback<ServiceResult<ArrayList<CTrack>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();

					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CTrack>> result) {

						// load tracked list
						if (result != null) {
							System.out.println(result.getMessage());
							ArrayList<CTrack> trackList = result.getResult();
							if (trackList == null || trackList.size() == 0) {
								trackListPanel.stopLoading();
								return;
							}

							String[][] data = new String[trackList.size()][];
							for (int i = 0; i < trackList.size(); ++i)
								data[i] = new String[] { trackList.get(i)
										.getTrackID().toString() };
							trackListPanel.setData(data);

						} else {
							MessageBox.alert("Track list null");
						}

						trackListPanel.stopLoading();

					}

				});

		trackListPanel.startLoading("Load track list");
	}

	public void loadWaypointList(Long trackID) {

	}

	public TrackedPanel() {
		createForm();
	}

	@Override
	public void onBrowserEvent(Event event) {
		System.out.println(event);
		if (DOM.eventGetType(event) == Event.ONKEYUP) {
			System.out.println(cb.getText());
			System.out.println("shit");
			// filter();
		}
	}

	private void createForm() {

		this.setLayout(new VerticalLayout());
		this.sinkEvents(Event.ONKEYUP);
		// this.setAutoScroll(true);

		MemoryProxy proxy = new PagingMemoryProxy(data);
		System.out.println(data);
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("trackedusername"),
				new StringFieldDef("trackedname") });

		System.out.println("Running");

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		cb = new ComboBox();
		cb.setMinChars(1);
		cb.setFieldLabel("Tracked List");
		cb.setDisplayField("trackedname");
		cb.setMode(ComboBox.REMOTE);
		cb.setTriggerAction(ComboBox.ALL);
		cb.setEmptyText("Enter tracked to filter");
		cb.setLoadingText("Searching...");
		cb.setTypeAhead(true);
		cb.setSelectOnFocus(true);
		cb.setWidth(250);
		cb.setPageSize(4);

		store.load();
		cb.setStore(store);
		cb.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				System.out.println(record.getAsString("trackedusername"));
				loadTrackList(Long.parseLong(record
						.getAsString("trackedusername")));
			}
		});

		pnlTest = new Panel();
		pnlTest.add(cb);

		/*
		 * List of tracked
		 */
		// trackedListPanel = new TablePanel(new int[] {
		// TablePanel.FIELD_STRING,
		// TablePanel.FIELD_STRING }, new String[] { constants
		// .trackedNameGrid() });
		// sample tracked data
		// trackedListPanel.addRecord(new String[] { "abc" });
		// trackedListPanel.addRecord(new String[] { "adef" });
		// trackedListPanel.addRecord(new String[] { "xyz" });
		// for (int i = 0; i < 10; ++i)
		// trackedListPanel.addRecord(new String[] { "sdflds" });
		// trackedListPanel.setHeight(listHeight);
		// trackedListPanel.addGridRowListener(new GridRowListenerAdapter() {
		// @Override
		// public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
		// String trackedId = trackedListPanel.getRecord(
		// rowIndex + (trackedListPanel.getCurrentPage() - 1)
		// * trackedListPanel.getPageSize()).get(0);
		// // MessageBox.alert(trackedId);
		// loadTrackList(Long.parseLong(trackedId));
		// }
		// });
		// trackedListPanel.setPageSize(pageSize);
		/*
		 * Date filter
		 */
		// First date
		dateFirst = new DateField("From date", "d-m-Y");
		dateFirst.setWidth(80);
		dateFirst.setReadOnly(true);
		dateFirst.setEmptyText(constants.fromDateEmptyText());

		dateFirst.setValidator(new Validator() {
			@Override
			public boolean validate(String value) throws ValidationException {
				if (dateLast.getValue() != null
						&& dateFirst.getValue().compareTo(dateLast.getValue()) > 0) {
					return false;
				}
				return true;
			}
		});

		// Last date
		dateLast = new DateField("To date", "d-m-Y");
		dateLast.setWidth(80);
		dateLast.setEmptyText(constants.toDateEmptyText());
		dateLast.setReadOnly(true);

		// test
		this.addEvent("shit");

		dateLast.addListener(new DatePickerListenerAdapter() {
			@Override
			public void onSelect(DatePicker dataPicker, Date date) {
				if (dateLast.getValue() != null
						&& date.compareTo(dateLast.getValue()) > 0) {
					MessageBox.alert(constants.firstDateWarning());
					viewTrack = false;
				} else
					viewTrack = true;
			}
		});
		// dateLast.setValidator(new Validator() {
		//
		// @Override
		// public boolean validate(String value) throws ValidationException {
		// if (dateFirst.getValue() != null
		// && dateLast.getValue().compareTo(dateFirst.getValue()) < 0)
		// return false;
		// return true;
		// }
		//
		// });
		dateLast.addListener(new FieldListenerAdapter() {
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
		datePanel.add(dateFirst);
		datePanel.add(new Label(constants.toDateLabel()));
		datePanel.add(dateLast);

		/*
		 * List of track
		 */
		trackListPanel = new TablePanel(new int[] { TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING }, new String[] { constants
				.trackNameGrid() });
		// // TODO sample track data
		// for (int i = 0; i < 10; ++i)
		// trackListPanel.addRecord(new String[] { "track" + i });
		trackListPanel.setHeight(listHeight);
		trackListPanel.setPageSize(pageSize);
		trackListPanel.addGridRowListener(new GridRowListenerAdapter() {
			@Override
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				String trackId = trackListPanel.getRecord(
						rowIndex).get(0);
				BkitPoma.waypointPanel.getWayPointsByTrack(trackId);
			}
		});

		/*
		 * Add components to main panel
		 */
		this.add(pnlTest);
		// this.add(trackedListPanel);
		this.add(datePanel);
		this.add(trackListPanel);
	}

	// private void filter() {
	// System.out.println(txtFilter.getText());
	// trackedListPanel.filter(0, txtFilter.getText(), true);
	// dateLast.setValue(dateFirst.getRawValue());
	// }

	// private void filter2(){
	// if (cb.getText().equals("")){
	// store.clearFilter();
	// cb.setStore(store);
	// store.load();
	// }else{
	// store.filter("trackedname", cb.getText());
	// cb.setStore(store);
	// store.load();
	// }
	// }

}

package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTrack;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.localization.client.TrackedPanelConstants;
import com.bkitmobile.poma.util.client.OneTimeTask;
import com.bkitmobile.poma.util.client.Task;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.event.PanelListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * This is Panel in MenuPanel, include: combobox for tracked, tablepanel for
 * track, tablepanel for waypoint
 */
public class TrackedDetailPanel extends Panel {

	private static TrackedDetailPanel trackedDetailPanel;
	public static boolean isUpdateDone = true;

	private static TablePanel trackTable;
	private WayPointPanel waypointTable;

	private FormPanel datePanel;

	private DateField dateFrom;
	private DateField dateTo;

	private TrackedPanelConstants constants = GWT
			.create(TrackedPanelConstants.class);

	DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	private int heightTrackList = 270;
	private int pageSizeTrack = 5;

	private final int rowGridHeight = 20;
	private final int waypointHeaderHeight = 52;

	public static final int LABEL_WIDTH = 100;
	public static final int COMBOBOX_WIDTH = MenuPanel.WIDTH - LABEL_WIDTH - 10;
	public static final int FIELD_WIDTH = COMBOBOX_WIDTH - 50;
	private int idWidth = 60;

	private boolean filter = false;
	private Store store = null;

	private ComboBox cbTrackedList;

	private Object[][] data;

	private ArrayList<CTrack> trackList;
	private long currentTrackedID = 0;

	/**
	 * Format date in Track table panel
	 */
	private DateTimeFormat dateFormat = DateTimeFormat.getFormat(constants
			.datetimeformat());
	private Toolbar toolbar;
	private RecordDef recordDef;
	private Button btnFilter;

	private long trackId;

	/**
	 * Default constructor
	 */
	public TrackedDetailPanel() {
		trackedDetailPanel = this;
		init();
		addListener();
		layout();
	}

	/**
	 * Initial form
	 */
	private void init() {
		// init combobox
		data = new Object[][] { { "", "" } };
		MemoryProxy proxy = new PagingMemoryProxy(data);

		recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("trackedusername"),
				new StringFieldDef("trackedname") });

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		cbTrackedList = new ComboBox();
		cbTrackedList.setMinChars(1);
		cbTrackedList.setFieldLabel(constants.cbTrackedList_title());
		cbTrackedList.setDisplayField("trackedname");
		cbTrackedList.setMode(ComboBox.REMOTE);
		cbTrackedList.setTriggerAction(ComboBox.ALL);
		cbTrackedList.setForceSelection(true);
		cbTrackedList.setLoadingText(constants.cbTrackedList_loadingtext());
		cbTrackedList.setTypeAhead(true);
		cbTrackedList.setSelectOnFocus(true);
		cbTrackedList.setWidth(COMBOBOX_WIDTH);
		cbTrackedList.setPageSize(4);
		cbTrackedList.setValueField("trackedusername");
		cbTrackedList.setResizable(true);

		store.load();
		cbTrackedList.setStore(store);
		cbTrackedList.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				MapPanel.clearOverlays();
				loadTrackList(Long.parseLong(record
						.getAsString("trackedusername")));
				if (filter)
					filterTrack();
			}
		});

		toolbar = new Toolbar();
		toolbar.addButton(new ToolbarButton(constants.lbl_device_list(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						MenuPanel.getInstance().trackedPage();
					}
				}, "images/arrow_left.png"));

		toolbar.addField(cbTrackedList);

		// TODO Date filter
		// from date
		dateFrom = new DateField(constants.dateFirst_title(), "d-m-Y");
		dateFrom.setWidth(FIELD_WIDTH);
		dateFrom.setMaxValue(new Date());
		dateFrom.setEmptyText(constants.fromDateEmptyText());
		dateFrom.setValidateOnBlur(true);
		dateFrom.setReadOnly(true);

		dateFrom.setValidator(new Validator() {
			@Override
			public boolean validate(String value) throws ValidationException {
				if (dateTo.getValue() != null && dateFrom.getValue() != null) {
					if (dateFrom.getValue().compareTo(dateTo.getValue()) > 0) {
						dateFrom.markInvalid("Error");
						filter = false;
						btnFilter.disable();
						return false;
					} else {
						filter = true;
						btnFilter.enable();
						dateTo.clearInvalid();
						return true;
					}
				}

				filter = false;
				btnFilter.disable();
				return false;
			}
		});

		// dateFrom.addListener(new FieldListenerAdapter() {
		// @Override
		// public void onValid(Field field) {
		// filterTracked();
		// }
		// });

		// to date
		dateTo = new DateField(constants.dateLast_title(), "d-m-Y");
		dateTo.setWidth(FIELD_WIDTH);
		dateTo.setEmptyText(constants.toDateEmptyText());
		dateTo.setMaxValue(new Date());
		dateTo.setValidateOnBlur(true);
		dateTo.setReadOnly(true);

		dateTo.setValidator(new Validator() {
			@Override
			public boolean validate(String value) throws ValidationException {
				if (dateFrom.getValue() != null && dateTo.getValue() != null) {
					if (dateFrom.getValue().compareTo(dateTo.getValue()) > 0) {
						dateTo.markInvalid("Error");
						filter = false;
						btnFilter.disable();
						return false;
					} else {
						filter = true;
						btnFilter.enable();
						dateFrom.clearInvalid();
						return true;
					}
				}

				if (dateTo.getValue() != null && dateFrom.getValue() == null) {
					dateFrom.markInvalid("Error");
				}

				filter = false;
				btnFilter.disable();
				return false;
			}
		});

		// filter button
		btnFilter = new Button(constants.lbl_filter_button(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						btnFilter.disable();
						filterTrack();
					}
				});
		btnFilter.disable();

		ToolbarButton btnKMLTrack = new ToolbarButton(".   .",
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						if (!trackTable.getGridPanel().getSelectionModel()
								.hasSelection()) {
							MessageBox.alert(constants
									.warning_no_row_selected_track());
							return;
						}
						Window.open("/bkitpoma/kml?tracked="
								+ UserSettings.ctracked.getUsername()
								+ "&apitracked="
								+ UserSettings.ctracked.getApiKey() + "&track="
								+ trackId, "_blank", "");
					}
				});
		btnKMLTrack.setIcon("/images/TrackedMenu/kml.jpg");
		btnKMLTrack.setTooltip(constants.btnKMLTrack());

		// Track list
		trackTable = new TablePanel(new int[] { TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING, TablePanel.FIELD_STRING },
				new String[] { constants.trackNameGrid(),
						constants.trackStartDate(), constants.trackEndDate() });
		trackTable.setLayout(new FitLayout());
		trackTable.getGridPanel()
				.setSelectionModel(new RowSelectionModel(true));
		trackTable.getBottomToolbar().addButton(btnKMLTrack);
		// trackTable.setHeight(heightTrackList);
		// trackTable.setPageSize(pageSizeTrack);

		int sizes[] = { idWidth, (MenuPanel.WIDTH - idWidth) / 2,
				(MenuPanel.WIDTH - idWidth) / 2 };
		try {
			trackTable.setColumnWidthManually(sizes, true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		waypointTable = new WayPointPanel();
		waypointTable.setLayout(new FitLayout());
		waypointTable.setAutoHeight(true);
		waypointTable.setPageSize(200 / rowGridHeight);
		try {
			waypointTable.setColumnWidthManually(new int[] { MenuPanel.WIDTH },
					false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addListener() {

		trackTable.addGridRowListener(new GridRowListenerAdapter() {
			@Override
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				trackId = Long.parseLong(trackTable.getRecord(rowIndex).get(0));
				waypointTable.loadWayPointsByTrack(String.valueOf(trackId));
			}
		});

		this.addListener(new PanelListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {
				trackTable.setGridHeight(MenuPanel.getInstance().getHeight() - 330);
				trackTable.setHeight(MenuPanel.getInstance().getHeight() - 310);
				trackTable
						.setPageSize((MenuPanel.getInstance().getHeight() - 330)
								/ rowGridHeight);
				BkitPoma.resize();
			}
		});
	}

	private void layout() {
		this.setLayout(new VerticalLayout(3));

		datePanel = new FormPanel();
		datePanel.setButtonAlign(Position.CENTER);
		datePanel.setLabelWidth(LABEL_WIDTH);
		datePanel.add(dateFrom);
		datePanel.add(dateTo);

		this.setTopToolbar(toolbar);
		this.add(datePanel);
		this.add(btnFilter);
		this.add(trackTable);
		this.add(waypointTable);
	}

	/**
	 * Use when user wants to view tracked list Auto run when a tracker login,
	 * after load bring into cbTrackedList
	 * 
	 * @param trackerUsername
	 */
	public void loadTrackedList() {
		ArrayList<CTracked> trackedList = new ArrayList<CTracked>(
				UserSettings.ctrackedList.values());
		if (trackedList == null || trackedList.size() == 0)
			return;

		String[][] data = new String[trackedList.size()][];
		for (int i = 0; i < trackedList.size(); i++) {
			String name;
			if (trackedList.get(i).getName().equals("")) {
				name = String.valueOf(trackedList.get(i).getUsername());
			} else {
				name = trackedList.get(i).getName() + " ("
						+ String.valueOf(trackedList.get(i).getUsername())
						+ ")";
			}
			data[i] = new String[] {
					String.valueOf(trackedList.get(i).getUsername()), name };
		}

		store.setDataProxy(new PagingMemoryProxy(data));
		store.load();
		String name = trackedList.get(0).getName().equals("") ? String
				.valueOf(trackedList.get(0).getUsername()) : trackedList.get(0)
				.getName();
		cbTrackedList.setValue(name);

		// filter if needed
		if (filter)
			filterTrack();
	}

	/**
	 * Add a new tracked to this panel
	 * 
	 * @param ctracked
	 *            :
	 */
	public void addNewTracked(CTracked ctracked) {
		Record record = recordDef.createRecord(new Object[] {
				ctracked.getUsername(), ctracked.getName() });
		store.add(record);
		store.commitChanges();
	}

	/**
	 * Call to Database to get all Track by Tracked
	 * 
	 * @param trackedID
	 *            TrackedID when tracker click in the tracked table panel
	 */
	private void loadTrackList(final Long trackedID) {
		if (UserSettings.ctrackedList.containsKey(trackedID)) {
			final CTracked tracked = UserSettings.ctrackedList.get(trackedID);

			currentTrackedID = -1;

			trackTable.startLoading("Loading ...");
			trackTable.removeAllRecords();
			dbAsync.getTracksByTracked(trackedID,
					new AsyncCallback<ServiceResult<ArrayList<CTrack>>>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							trackTable.stopLoading();
						}

						@Override
						public void onSuccess(
								ServiceResult<ArrayList<CTrack>> result) {

							if (result.isOK()) {

								trackList = result.getResult();
								if (!(trackList == null || trackList.size() == 0)) {

									for (int i = 0; i < trackList.size(); ++i) {

										String dateStart = trackList.get(i)
												.getBeginTime() == null ? ""
												: dateFormat.format(trackList
														.get(i).getBeginTime());
										String dateEnd = trackList.get(i)
												.getEndTime() == null ? ""
												: dateFormat.format(trackList
														.get(i).getEndTime());

										trackTable.addRecord(new String[] {
												trackList.get(i).getTrackID()
														.toString(), dateStart,
												dateEnd });
									}
									WayPointPanel.getInstance().setTracked(
											tracked);
								}

								currentTrackedID = trackedID;
							} else {
								MessageBox.alert(result.getMessage());
							}

							if (filter) {
								filterTrack();
							}
							trackTable.stopLoading();
						}

					});
		}
	}

	/**
	 * When user click dateField, this function will be called to filter track
	 * between start date and end date
	 */
	private void filterTrack() {
		trackTable.removeAllRecords();

		ArrayList<CTrack> tmp = new ArrayList<CTrack>();
		for (int i = 0; i < trackList.size(); ++i) {
			CTrack cTrack = trackList.get(i);
			if (cTrack.getBeginTime() == null
					|| (cTrack.getBeginTime().compareTo(dateFrom.getValue()) >= 0 && cTrack
							.getBeginTime().compareTo(dateTo.getValue()) <= 0)
					|| (cTrack.getBeginTime().getYear() == dateTo.getValue()
							.getYear())
					&& cTrack.getBeginTime().getMonth() == dateTo.getValue()
							.getMonth()
					&& cTrack.getBeginTime().getDate() == dateTo.getValue()
							.getDate()) {
				tmp.add(cTrack);
			}

		}

		// no track list satisfies filter condition
		if (tmp.size() == 0) {
			// MessageBox.alert("No device satisfies the filtered condition");
			waypointTable.removeAllWayPoints();
		}

		for (int i = 0; i < tmp.size(); i++) {
			String dateStart = tmp.get(i).getBeginTime() == null ? ""
					: dateFormat.format(tmp.get(i).getBeginTime());
			String dateEnd = tmp.get(i).getEndTime() == null ? "" : dateFormat
					.format(tmp.get(i).getEndTime());
			trackTable.addRecord(new String[] {
					tmp.get(i).getTrackID().toString(), dateStart, dateEnd });
		}

	}

	/**
	 * Timer call to update track
	 */
	public void updateTrackList() {
		if (currentTrackedID != 0) {

			DatabaseService.Util.getInstance().getTracksByTracked(
					currentTrackedID,
					new Date(UserSettings.lastTrackUpdateTime),
					new AsyncCallback<ServiceResult<ArrayList<CTrack>>>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							updateDone();
						}

						@Override
						public void onSuccess(
								ServiceResult<ArrayList<CTrack>> result) {

							ArrayList<CTrack> trackList = result.getResult();

							if (trackList != null) {
								ArrayList<ArrayList<String>> oldRecords = trackTable.getRecords();
								String oldTrackID = "";
								for (ArrayList<String> record : oldRecords) {
									oldTrackID += ";" + record.get(0) + ";";
								}
								for (CTrack ctrack : trackList) {
									String dateStart = ctrack.getBeginTime() == null ? ""
											: dateFormat.format(ctrack
													.getBeginTime());
									String dateEnd = ctrack.getEndTime() == null ? ""
											: dateFormat.format(ctrack
													.getEndTime());
									if (!oldTrackID.contains(";"
											+ ctrack.getTrackID() + ";")) {
										// check duplicate
										trackTable.addRecord(new String[] {
												ctrack.getTrackID() + "",
												dateStart, dateEnd }, 0);
									}

								}
							}
							updateDone();
						}
					});
		} else {
			updateDone();
		}
	}

	private void updateDone() {
		isUpdateDone = true;
		UserSettings.lastTrackUpdateTime = System.currentTimeMillis();
	}

	/**
	 * @return Instance of trackedPanel
	 */
	public static TrackedDetailPanel getInstance() {
		return trackedDetailPanel == null ? new TrackedDetailPanel()
				: trackedDetailPanel;
	}

	/**
	 * Show detail of a tracked
	 * 
	 * @param trackedID
	 */
	public void showDetail(Long trackedID) {
		loadTrackedList();

		waypointTable.setCurrentTrackID("");
		waypointTable.removeAllWayPoints();

		Record[] records = cbTrackedList.getStore().getRecords();
		for (Record record : records) {
			if (record.getAsString("trackedusername").equals("" + trackedID)) {
				cbTrackedList.setValue(record.getAsString("trackedname"));
				loadTrackList(Long.parseLong(record
						.getAsString("trackedusername")));
				break;
			}
		}
	}

	public void removeTracked(Long trackedID) {
		trackTable.removeAllRecords();
		waypointTable.removeAllWayPoints();
		waypointTable.setCurrentTrackID("");

		Record[] records = cbTrackedList.getStore().getRecords();
		for (Record record : records) {
			if (record.getAsString("trackedusername").equals("" + trackedID)) {
				cbTrackedList.getStore().remove(record);
				// cbTrackedList.getStore().commitChanges();
				break;
			}
		}
	}

}

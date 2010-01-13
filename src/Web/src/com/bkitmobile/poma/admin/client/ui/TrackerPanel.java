package com.bkitmobile.poma.admin.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bkitmobile.poma.admin.client.StaticMethod;
import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.mail.client.TrackerService;
import com.bkitmobile.poma.mail.client.TrackerServiceAsync;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.BoxComponentListenerAdapter;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.PropertyGridPanel;
import com.gwtext.client.widgets.grid.event.EditorGridListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;

public class TrackerPanel extends LoadingPanel {

	protected final int COMBO_WIDTH = 200;

	private ArrayList<CTracker> arrCTrackers;
	private CTracker selectedCTracker;
	private DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();
	private TrackerServiceAsync trackerServiceAsync = TrackerService.Util
			.getInstance();
	private EditorGridPanel gridTracker;
	private MemoryProxy proxy;
	private Store store;
	private PropertyGridPanel gridDetail = new PropertyGridPanel();
	private int selectedIndex = 0;

	private Button btnResetDetail;
	private Button btnApplyDetail;

	private Button btnRemoveTracker;
	private Button btnSendConfirm;

	private ArrayReader reader;

	private RecordDef recordDef;

	private ColumnModel columnModel;

	private ColumnConfig[] columns;

	public TrackerPanel() {
		super();
		gridTracker = new EditorGridPanel();
		init();
		initGridTracker();
		initTrackerDetail();
		layout();
	}

	private void loadTrackers() {
		// e = Ext.get(this.getId());
		// e.mask(loadingMessage);
		arrCTrackers.clear();
		dbAsync
				.getAllTrackers(new AsyncCallback<ServiceResult<ArrayList<CTracker>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CTracker>> result) {
						if (result.isOK()) {
							arrCTrackers = result.getResult();
							refreshTrackerList();
							
							displayDetailTracker(0);
						}
					}

				});
	}

	private void initGridTracker() {
		gridTracker.setFrame(true);
		gridTracker.setStripeRows(true);
		gridTracker.setAutoScroll(true);
		gridTracker.setHeight(Window.getClientHeight() - 73);
		gridTracker.setEnableHdMenu(false);

		gridTracker.addGridCellListener(new GridCellListenerAdapter() {
			@Override
			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				super.onCellClick(grid, rowIndex, colindex, e);
				btnRemoveTracker.setDisabled(false);
				selectedIndex = rowIndex;
				selectedCTracker = arrCTrackers.get(selectedIndex).clone();
				if (!selectedCTracker.isActived())
					btnSendConfirm.setDisabled(false);
				else
					btnSendConfirm.setDisabled(true);
				displayDetailTracker(selectedIndex);
			}
		});

		columns = new ColumnConfig[] { new ColumnConfig("Tracker Username",
				"tracker", 160, true, null, "tracker") };
		columnModel = new ColumnModel(columns);
		gridTracker.setColumnModel(columnModel);

		recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("tracker") });

		proxy = new MemoryProxy(new String[1][]);
		reader = new ArrayReader(recordDef);
		// store = new SimpleStore("tracker",new String[1]);
		store = new Store(proxy, reader);
		gridTracker.setStore(store);
		gridTracker.setAutoExpandColumn("tracker");
//		gridTracker.doOnRender(new Function() {
//			public void execute() {
//
//				gridTracker.getSelectionModel().selectFirstRow();
//			}
//		});
		store.load();

		loadTrackers();
	}

	private void initTrackerDetail() {
		// gridDetail = new PropertyGridPanel();
		gridDetail.setId("props-grid");
		gridDetail.setWidth(300);
		gridDetail.setAutoHeight(true);
		gridDetail.setHeight(100);
		gridDetail.setAutoScroll(true);
		gridDetail.setSorted(false);

		GridView view = new GridView();
		view.setForceFit(true);
		view.setScrollOffset(2); // the grid will never have scrollbars
		gridDetail.setView(view);

		gridDetail.setCustomEditors(getMap());
		gridDetail.addEditorGridListener(new EditorGridListenerAdapter() {
			@Override
			public void onAfterEdit(GridPanel grid, Record record,
					String field, Object newValue, Object oldValue,
					int rowIndex, int colIndex) {
				super.onAfterEdit(grid, record, field, newValue, oldValue,
						rowIndex, colIndex);
				btnResetDetail.setDisabled(false);
				btnApplyDetail.setDisabled(false);

				changeCTrackerAt(rowIndex, newValue);

				Element cell = grid.getView().getCell(rowIndex, colIndex);
				cell.setInnerHTML("<font color='red'>" + newValue + "</font>");
			}
		});

	}

	private void changeCTrackerAt(int rowIndex, Object value) {
		switch (rowIndex) {
		// case 1:
		// selectedCTracker.setPassword((String) value);
		// break;
		case 1:
			selectedCTracker.setName((String) value);
			break;
		case 2:
			selectedCTracker.setBirthday((Date) value);
			break;
		case 3:
			selectedCTracker.setTel((String) value);
			break;
		case 4:
			selectedCTracker.setAddr((String) value);
			break;
		case 5:
			selectedCTracker.setEmail((String) value);
			break;
		case 6:
			selectedCTracker.setType((Integer) value);
			break;
		case 7:
			selectedCTracker.setGmt((Integer) value);
			break;
		case 8:
			selectedCTracker.setLang((String) value);
			break;
		case 9:
			selectedCTracker.setCountry((String) value);
			break;
		case 10:
			selectedCTracker.setActived((Boolean) value);
			break;
		case 11:
			selectedCTracker.setEnabled((Boolean) value);
			break;
		}
	}

	private void init() {
		arrCTrackers = new ArrayList<CTracker>();
		// arrCTrackersDefault = new ArrayList<CTracker>();
		btnResetDetail = new Button("Reset", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				displayDetailTracker(selectedIndex);
			}
		});
		btnResetDetail.setDisabled(true);

		btnApplyDetail = new Button("Apply", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				dbAsync.updateTracker(selectedCTracker,
						new AsyncCallback<ServiceResult<CTracker>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ServiceResult<CTracker> result) {
								if (result.isOK()) {
									arrCTrackers.set(selectedIndex, result
											.getResult());
									selectedCTracker = result.getResult();
									if (selectedCTracker.isActived())
										btnSendConfirm.setDisabled(false);
									displayDetailTracker(selectedIndex);
								} else {
									MessageBox.alert(result.getMessage());
									selectedCTracker = arrCTrackers.get(
											selectedIndex).clone();
									displayDetailTracker(selectedIndex);
								}
							}

						});
			}
		});
		btnApplyDetail.setDisabled(true);

		btnRemoveTracker = new Button("Remove", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				MessageBox.confirm("Confirmation",
						"Do you want to delete this tracker from database?",
						new MessageBox.ConfirmCallback() {

							@Override
							public void execute(String btnID) {
								if (btnID.toLowerCase().equals("yes")) {
									dbAsync
											.removeTracker(
													arrCTrackers.get(
															selectedIndex)
															.getUsername(),
													new AsyncCallback<ServiceResult<CTracker>>() {

														@Override
														public void onFailure(
																Throwable caught) {
															caught
																	.printStackTrace();
														}

														@Override
														public void onSuccess(
																ServiceResult<CTracker> result) {
															if (!result.isOK()) {
																MessageBox
																		.alert(result
																				.getMessage());
															} else {
																loadTrackers();
															}
														}

													});
								}
							}

						});
			}
		});
		btnRemoveTracker.setDisabled(true);

		btnSendConfirm = new Button("Send confirm register mail",
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);
						trackerServiceAsync.validateEmailNewTracker(
								selectedCTracker, new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(Boolean result) {
										if (result) {
											MessageBox
													.alert("Send successfully");
										} else {
											MessageBox.alert("Send fail");
										}
									}

								});
					}
				});
		btnSendConfirm.setDisabled(true);

	}

	private void layout() {
		gridTracker.setButtonAlign(Position.CENTER);
		gridTracker.addButton(btnRemoveTracker);
		gridTracker.addButton(btnSendConfirm);

		gridDetail.setButtonAlign(Position.CENTER);
		gridDetail.addButton(btnResetDetail);
		gridDetail.addButton(btnApplyDetail);

		 this.addListener(new BoxComponentListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {
				 gridTracker.setHeight(Window.getClientHeight() - 73);
			}
		});

		this.setLayout(new ColumnLayout());
		this.add(gridTracker, new ColumnLayoutData(.5));
		this.add(gridDetail, new ColumnLayoutData(.5));
	}

	private NameValuePair[] getNameValuePairs(CTracker cTracker) {
		if (cTracker == null)
			cTracker = new CTracker();
		DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy/MM/dd");
		NameValuePair[] source = new NameValuePair[12];
		source[0] = new NameValuePair("username", cTracker.getUsername());
		// source[1] = new NameValuePair("password", cTracker.getPassword());
		source[1] = new NameValuePair("name", getValue(cTracker.getName()));
		source[2] = new NameValuePair("birthday",
				cTracker.getBirthday() == null ? "" : dateTimeFormat
						.format(cTracker.getBirthday()));
		source[3] = new NameValuePair("tel", getValue(cTracker.getTel()));
		source[4] = new NameValuePair("addr", getValue(cTracker.getAddr()));
		source[5] = new NameValuePair("email", getValue(cTracker.getEmail()));
		source[6] = new NameValuePair("type", cTracker.getType());
		source[7] = new NameValuePair("gmt", cTracker.getGmt());
		source[8] = new NameValuePair("lang", cTracker.getLang());
		source[9] = new NameValuePair("country", cTracker.getCountry());
		source[10] = new NameValuePair("active", cTracker.isActived());
		source[11] = new NameValuePair("enable", cTracker.isEnabled());
		return source;
	}

	private static String getValue(String txt) {
		return txt == null ? " " : txt;
	}

	private void displayDetailTracker(int index) {
		NameValuePair[] source = getNameValuePairs(arrCTrackers.get(index));
		
		gridDetail.setSource(source);
	}

	private String[][] convertArrTrackers2String2D() {
		String[][] arr2D = new String[arrCTrackers.size()][];
		for (int i = 0; i < arrCTrackers.size(); i++) {
			arr2D[i] = new String[] { arrCTrackers.get(i).getUsername() };
		}

		return arr2D;
	}

	private void refreshTrackerList() {
		String[][] arr2DTrackers = convertArrTrackers2String2D();
		proxy = new MemoryProxy(arr2DTrackers);
		store.setDataProxy(proxy);
		store.load();
	}

	private Map<String, GridEditor> getMap() {
		// Field for time zone
		ComboBox cbTimeZone = new ComboBox();
		Store timeZoneStore = new SimpleStore(
				new String[] { "timezone", "gmt" }, StaticMethod.TIME_ZONES);
		timeZoneStore.load();
		cbTimeZone.setStore(timeZoneStore);
		cbTimeZone.setDisplayField("timezone");
		cbTimeZone.setValueField("gmt");
		cbTimeZone.setWidth(COMBO_WIDTH);
		cbTimeZone.setListWidth(COMBO_WIDTH);
		cbTimeZone.setForceSelection(true);

		// DateField for birthday
		DateField dateField = new DateField();
		dateField.setFormat("m/d/Y");

		// Field for type
		SimpleStore cbStore = new SimpleStore("trackertype", new String[] {
				"0", "1", "2", "3" });
		ComboBox cbType = new ComboBox();
		cbType.setDisplayField("trackertype");
		cbType.setStore(cbStore);

		// ComboBox for Country
		ComboBox cbCountry = new ComboBox();
		Store countryStore = new SimpleStore(new String[] { "country_codes",
				"country_names" }, StaticMethod.COUNTRIES_NAME);
		countryStore.load();
		cbCountry.setStore(countryStore);
		cbCountry.setForceSelection(true);
		cbCountry.setDisplayField("country_names");
		cbCountry.setValueField("country_codes");
		cbCountry.setWidth(COMBO_WIDTH);
		cbCountry.setListWidth(COMBO_WIDTH);

		// ComboBox for language
		ComboBox cbLanguage = new ComboBox("Language");
		Store languageStore = new SimpleStore(new String[] { "locales",
				"languages" }, StaticMethod.LANGUAGE);
		languageStore.load();
		cbLanguage.setStore(languageStore);
		cbLanguage.setDisplayField("languages");
		cbLanguage.setValueField("locales");
		cbLanguage.setForceSelection(true);

		TextField txtUsername = new TextField();
		txtUsername.setDisabled(true);

		Map<String, GridEditor> customEditors = new HashMap<String, GridEditor>();
		// ArrayList<GridEditor> gridEditors = new ArrayList<GridEditor>();
		// gridEditors.add(new GridEditor(txtUsername));// 1 username
		// // gridEditors.add(new GridEditor(new TextField()));// password
		// gridEditors.add(new GridEditor(new TextField()));// 2 name
		// gridEditors.add(new GridEditor(dateField));// birthday
		// gridEditors.add(new GridEditor(new TextField()));// tel
		// gridEditors.add(new GridEditor(new TextField()));// addr
		// gridEditors.add(new GridEditor(new TextField()));// type
		// gridEditors.add(new GridEditor(cbType));// type
		// gridEditors.add(new GridEditor(cbTimeZone));// Timezone
		// gridEditors.add(new GridEditor(cbLanguage));// lang
		// gridEditors.add(new GridEditor(cbCountry));// Country
		// gridEditors.add(new GridEditor(new Checkbox()));// Active
		// gridEditors.add(new GridEditor(new Checkbox()));// enable

		customEditors.put("username", new GridEditor(txtUsername));
		// customEditors.put("password",new GridEditor(new TextField()));
		customEditors.put("name", new GridEditor(new TextField()));
		customEditors.put("birthday", new GridEditor(dateField));
		customEditors.put("tel", new GridEditor(new TextField()));
		customEditors.put("addr", new GridEditor(new TextField()));
		customEditors.put("email", new GridEditor(new TextField()));
		customEditors.put("type", new GridEditor(cbType));
		customEditors.put("gmt", new GridEditor(cbTimeZone));
		customEditors.put("lang", new GridEditor(cbLanguage));
		customEditors.put("country", new GridEditor(cbCountry));
		customEditors.put("active", new GridEditor(new Checkbox()));
		customEditors.put("enable", new GridEditor(new Checkbox()));

		return customEditors;
	}

}

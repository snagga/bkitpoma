package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CManage;
import com.bkitmobile.poma.database.client.entity.CStaff;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.localization.client.ShareTrackedWindowConstants;
import com.bkitmobile.poma.util.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * <b>ShareTrackedWindow</b> Window allows user to share device with other
 * users. Have 2 permission: manage and staff. See user manual for more detail
 * 
 * @author Tam Vo Minh
 * 
 */
public class ShareTrackedWindow extends Window {

	private static ShareTrackedWindow permitTrackedWindow;
	private Long trackedID;
	private DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	private Image imageForward;
	private Image imageBack;

	/**
	 * Manage
	 */
	private Button btnInsertManage;
	private TextField txtManage;
	private Button btnRemoveManage;

	private GridPanel gridManage;
	private MemoryProxy proxyManage;
	private Store storeManage;
	// private String[][] arrManage2 = new String[1][];
	private int selectedManage = -1;

	/**
	 * Staff
	 */
	private TextField txtStaff;
	private Button btnInsertStaff;
	private Button btnRemoveStaff;

	private GridPanel gridStaff;
	private MemoryProxy proxyStaff;
	private Store storeStaff;
	// private String[][] arrStaff2 = new String[1][];
	private int selectedStaff = -1;

	private Button btnApply;
	private Button btnReset;
	private ArrayList<String> arrManageDefault;
	private ArrayList<String> arrStaffDefault;
	private ArrayList<String> arrManage;
	private ArrayList<String> arrStaff;
	private ArrayList<String> arrTrackers;

	private ShareTrackedCallback callback;

	private ShareTrackedWindowConstants constants = GWT
			.create(ShareTrackedWindowConstants.class);

	/**
	 * Get instance of this window
	 * 
	 * @return instance of this window
	 */
	public static ShareTrackedWindow getInstance() {
		return permitTrackedWindow != null ? permitTrackedWindow
				: new ShareTrackedWindow();
	}

	/**
	 * After call get instance, you must call to reload manage and staff list of
	 * this tracked
	 * 
	 * @param trackedID
	 *            id of device
	 * @param arrManage
	 *            arrManage contains all manage users of this device. If you
	 *            initialize for the first time, it is null, we will call
	 *            database to load for you
	 * @param arrStaff
	 *            arrStaff contains all staff users of this device. If you
	 *            initialize for the first time, it is null, we will call
	 *            database to load for you
	 */
	public void setTrackedID(Long trackedID, ArrayList<String> arrManage,
			ArrayList<String> arrStaff) {
		
		this.trackedID = trackedID;

		this.setTitle(constants.window_title() + " " + trackedID);
		if (arrManage == null || arrStaff == null) {
			loadManageData();
			loadStaffData();
		} else {
			this.arrManageDefault.clear();
			this.arrManageDefault.addAll(arrManage);
			this.arrManage.clear();
			this.arrManage.addAll(arrManage);
			refreshManageGrid();

			this.arrStaffDefault.clear();
			this.arrStaffDefault.addAll(arrStaff);
			this.arrStaff.clear();
			this.arrStaff.addAll(arrStaff);
			refreshStaffGrid();
		}
		imageForward.setUrl("/images/arrowforward-disable.png");
		imageBack.setUrl("/images/arrowback-disable.png");
	}

	/**
	 * Default Constructor: To prevent bugs, you should create class by calling
	 * getInstance() method
	 */
	public ShareTrackedWindow() {
		super();
		permitTrackedWindow = this;

		initUI();
		initStuff();
		layout();

		loadTrackers();
		// loadManageData();
		// loadStaffData();
	}

	/**
	 * Initialize UI
	 */
	private void initUI() {
		this.setPaddings(5);
		this.setCloseAction(Window.HIDE);
		this.setButtonAlign(Position.RIGHT);
		this.setResizable(false);
		this.setSize(500, 400);
		this.setModal(true);
		// this.setWidth(500);// (500, 600);
		// this.setAutoHeight(true);

		txtManage = new TextField();
		txtManage.setWidth(100);
		txtManage.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				super.onSpecialKey(field, e);
				// Catch ENTER event
				if (e.getCharCode() == 13) {
					insertManage(txtManage.getText());
					txtManage.setValue("");
				}
			}
		});

		txtStaff = new TextField();
		txtStaff.setWidth(100);
		txtStaff.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				super.onSpecialKey(field, e);
				// Catch ENTER event
				if (e.getCharCode() == 13) {
					insertStaff(txtStaff.getText());
					txtStaff.setValue("");
				}
			}
		});

		btnRemoveManage = new Button(constants.btnRemoveManage(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);

						if (selectedManage == -1)
							return;
						if (arrManage.size() <= 1) {
							MessageBox.alert(constants.msb_removemanage());
							return;
						}
						arrManage.remove(selectedManage);
						refreshManageGrid();

						setEnabledManage(false);
					}
				});
		btnRemoveManage.setDisabled(true);

		btnRemoveStaff = new Button(constants.btnRemoveStaff(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);

						if (selectedStaff == -1)
							return;
						arrStaff.remove(selectedStaff);
						refreshStaffGrid();

						setEnabledStaff(false);
					}
				});
		btnRemoveStaff.setDisabled(true);

		btnInsertManage = new Button(constants.btnInsertManage(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);
						insertManage(txtManage.getText());
						txtManage.setValue("");
					}
				});
		btnInsertManage.setMinWidth(50);

		btnInsertStaff = new Button(constants.btnInsertStaff(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);
						insertStaff(txtStaff.getText());
					}
				});
		btnInsertStaff.setMinWidth(50);

		imageForward = new Image("/images/arrowforward-disable.png");
		imageForward.setPixelSize(32, 32);
		imageForward.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedManage == -1)
					return;
				changeFromManageToStaff(arrManage.get(selectedManage));
				setEnabledManage(false);
			}
		});

		imageBack = new Image("/images/arrowback-disable.png");
		imageBack.setPixelSize(32, 32);
		imageBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedStaff == -1)
					return;
				changeFromStaffToManage(arrStaff.get(selectedStaff));
				setEnabledStaff(false);
			}
		});

		btnApply = new Button(constants.btnApply(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);

						dbAsync.removeManagesByTracked(trackedID,
								new AsyncCallback<ServiceResult<Boolean>>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(
											ServiceResult<Boolean> result) {
										if (result.isOK()) {

											dbAsync
													.removeStaffsByTracked(
															trackedID,
															new AsyncCallback<ServiceResult<Boolean>>() {

																@Override
																public void onFailure(
																		Throwable caught) {
																	caught
																			.printStackTrace();
																}

																@Override
																public void onSuccess(
																		ServiceResult<Boolean> result) {
																	if (result
																			.isOK()) {
																		insertAllManages(trackedID);
																	} else {
																		MessageBox
																				.alert(constants
																						.msb_removemanages()
																						+ " "
																						+ result
																								.getMessage());
																		return;
																	}
																}

															});

										} else {
											MessageBox
													.alert(constants
															.msb_removestaffs()
															+ " "
															+ result
																	.getMessage());
											return;
										}
									}

								});
					}
				});

		btnReset = new Button(constants.btnReset(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				arrManage.clear();
				arrManage.addAll(arrManageDefault);
				refreshManageGrid();

				arrStaff.clear();
				arrStaff.addAll(arrStaffDefault);
				refreshStaffGrid();
			}
		});

		initGridManage();
		initGridStaff();
	}

	/**
	 * Initialize others expect UI
	 */
	private void initStuff() {
		arrManageDefault = new ArrayList<String>();
		arrManage = new ArrayList<String>();

		arrStaffDefault = new ArrayList<String>();
		arrStaff = new ArrayList<String>();

		arrTrackers = new ArrayList<String>();
	}

	/**
	 * Insert all manage username in arrManage into database
	 * 
	 * @param trackedID
	 */
	private void insertAllManages(final Long trackedID) {

		dbAsync.insertManages(trackedID, arrManage,
				new AsyncCallback<ServiceResult<Boolean>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<Boolean> result) {
						if (result.isOK()) {

							if (arrStaff.size() > 0) {
								insertAllStaffs(trackedID);
							} else {
								hide();
								if (callback != null) {
									callback.onApplyOperation(arrManage,
											arrStaff);
									
								}
							}
						} else {
							MessageBox.alert(result.getMessage());
						}
					}

				});
	}

	/**
	 * Insert all staff username in arrManage into database
	 * 
	 * @param trackedID
	 */
	private void insertAllStaffs(Long trackedID) {

		dbAsync.insertStaffs(trackedID, arrStaff,
				new AsyncCallback<ServiceResult<Boolean>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<Boolean> result) {
						if (result.isOK()) {
							hide();
							if (callback != null) {
								callback.onApplyOperation(arrManage, arrStaff);
							}
						} else {
							MessageBox.alert(result.getMessage());
						}
					}

				});
	}

	/**
	 * Layout for this window
	 */
	private void layout() {
		this.setLayout(new BorderLayout());

		Panel pnlTop = new Panel();
		pnlTop.setBorder(false);
		pnlTop.setAutoHeight(true);
		pnlTop.setLayout(new ColumnLayout());

		Panel pnl1 = new Panel();
		pnl1.setBorder(false);
		pnl1.setLayout(new HorizontalLayout(5));
		pnl1.add(txtManage);
		pnl1.add(btnInsertManage);
		pnl1.add(btnRemoveManage);

		Panel pnl2 = new Panel();
		pnl2.setBorder(false);
		pnl2.setLayout(new HorizontalLayout(5));
		pnl2.add(txtStaff);
		pnl2.add(btnInsertStaff);
		pnl2.add(btnRemoveStaff);

		pnlTop.add(pnl1, new ColumnLayoutData(.455));
		Label lblMiddle = new Label("");
		lblMiddle.setWidth(45);
		pnlTop.add(lblMiddle, new ColumnLayoutData(.09));
		pnlTop.add(pnl2, new ColumnLayoutData(.455));

		Panel pnlMiniButton = new Panel();
		pnlMiniButton.setBorder(false);
		pnlMiniButton.setLayout(new VerticalLayout());
		Label lbl = new Label();
		lbl.setSize(40, 100);
		pnlMiniButton.add(lbl);
		pnlMiniButton.add(imageForward);
		pnlMiniButton.add(imageBack);

		// pnlMiniButton.add(imageForward,new
		// BorderLayoutData(RegionPosition.CENTER));
		// pnlMiniButton.add(imageBack,new
		// BorderLayoutData(RegionPosition.CENTER));

		Panel pnlCenter = new Panel();
		pnlCenter.setBorder(false);
		pnlCenter.setLayout(new ColumnLayout());

		// gridManage.addButton(btnRemoveManage);
		// gridStaff.addButton(btnRemoveStaff);
		pnlCenter.add(gridManage, new ColumnLayoutData(.455));
		pnlCenter.add(pnlMiniButton, new ColumnLayoutData(.09));
		pnlCenter.add(gridStaff, new ColumnLayoutData(.455));

		BorderLayoutData btnNorth = new BorderLayoutData(RegionPosition.NORTH);
		btnNorth.setAutoHide(true);
		this.add(pnlTop, btnNorth);
		this.add(pnlCenter, new BorderLayoutData(RegionPosition.CENTER));
		this.addButton(btnReset);
		this.addButton(btnApply);

	}

	/**
	 * init grid panel for displaying manage user
	 */
	private void initGridManage() {
		RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("manage") });

		gridManage = new GridPanel();

		proxyManage = new MemoryProxy(new String[1][]);

		ArrayReader reader = new ArrayReader(recordDef);
		storeManage = new Store(proxyManage, reader);
		storeManage.load();
		gridManage.setStore(storeManage);

		ColumnConfig ccManage = new ColumnConfig(constants.ccManage(), "manage", 150,
				false, null, "manage");
		ccManage.setResizable(false);
		ccManage.setWidth(100);
		ColumnConfig[] columns = new ColumnConfig[] { ccManage };

		ColumnModel columnModel = new ColumnModel(columns);
		columnModel.setColumnWidth("manage", 100);
		gridManage.setColumnModel(columnModel);
		gridManage.setFrame(true);
		gridManage.setStripeRows(true);
		gridManage.setAutoExpandColumn("manage");
		gridManage.setEnableHdMenu(false);
		gridManage.addGridCellListener(new GridCellListenerAdapter() {
			@Override
			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				super.onCellClick(grid, rowIndex, colindex, e);
				selectedManage = rowIndex;
				setEnabledManage(true);
			}
		});
		gridManage.setButtonAlign(Position.RIGHT);
		gridManage.setAutoScroll(true);

		gridManage.setHeight(296);
		// gridManage.setWidth(600);
	}

	/**
	 * init grid panel for displaying staff user
	 */
	private void initGridStaff() {
		RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("staff") });

		gridStaff = new GridPanel();

		proxyStaff = new MemoryProxy(new String[1][]);

		ArrayReader reader = new ArrayReader(recordDef);
		storeStaff = new Store(proxyStaff, reader);
		storeStaff.load();
		gridStaff.setStore(storeStaff);

		ColumnConfig ccStaff = new ColumnConfig(constants.ccStaff(), "staff", 150, false,
				null, "staff");
		ccStaff.setResizable(false);
		ccStaff.setWidth(100);

		ColumnConfig[] columns = new ColumnConfig[] { ccStaff };

		ColumnModel columnModel = new ColumnModel(columns);
		columnModel.setColumnWidth("staff", 100);
		gridStaff.setColumnModel(columnModel);
		gridStaff.setFrame(true);
		gridStaff.setStripeRows(true);
		gridStaff.setAutoExpandColumn("staff");
		gridStaff.setEnableHdMenu(false);
		gridStaff.addGridCellListener(new GridCellListenerAdapter() {
			@Override
			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				super.onCellClick(grid, rowIndex, colindex, e);
				setEnabledStaff(true);
				selectedStaff = rowIndex;
			}
		});
		gridStaff.setButtonAlign(Position.RIGHT);
		gridStaff.setAutoScroll(true);

		gridStaff.setHeight(296);
		// gridStaff.setWidth(600);
	}

	/**
	 * Call to database to get all manage user, then load into arrManage
	 */
	private void loadManageData() {
		arrManageDefault.clear();
		
		dbAsync.getManagesByTracked(trackedID,
				new AsyncCallback<ServiceResult<ArrayList<CManage>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CManage>> result) {
						if (result.isOK()) {
							for (int i = 0; i < result.getResult().size(); i++) {
								arrManageDefault.add(result.getResult().get(i)
										.getTrackerUN());
							}
							arrManage.clear();
							arrManage.addAll(arrManageDefault);
							refreshManageGrid();
						} else {
							MessageBox.alert(result.getMessage());
						}
					}

				});
	}

	/**
	 * Call to database to get all staff user, then load into arrStaff
	 */
	private void loadStaffData() {
		arrStaffDefault.clear();
		dbAsync.getStaffsByTracked(trackedID,
				new AsyncCallback<ServiceResult<ArrayList<CStaff>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CStaff>> result) {
						if (result.isOK()) {
							for (int i = 0; i < result.getResult().size(); i++) {
								arrStaffDefault.add(result.getResult().get(i)
										.getTrackerUN());
							}
							arrStaff.clear();
							arrStaff.addAll(arrStaffDefault);
							refreshStaffGrid();
						}
					}

				});
	}

	/**
	 * Call to database to get all username of trackers, then load into
	 * arrTrackers to check whether username that user input is valid or not
	 */
	private void loadTrackers() {
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
							for (CTracker cTracker : result.getResult()) {
								arrTrackers.add(cTracker.getUsername());
							}
						} else {
							System.err.println(result.getMessage());
						}
					}

				});
	}

	/**
	 * When user operate insert new manage. <b>Note</b> This method does not
	 * call to database
	 * 
	 * @param trackerUN
	 *            new username that user want to add into manage list
	 */
	private void insertManage(final String trackerUN) {
		if (!arrTrackers.contains(trackerUN)) {
			MessageBox.alert(Format.format(constants.msb_insertManage_trackernotexist(), trackerUN));
			return;
		} else if (arrStaff.contains(trackerUN)) {
			MessageBox.alert(Format.format(constants.msb_insertManage_error(), trackerUN));
		} else if (arrManage.contains(trackerUN)){
			MessageBox.alert(Format.format(constants.msb_insertManage_already_exist(), trackerUN));
		}else {
			arrManage.add(trackerUN);
			refreshManageGrid();
		}

	}

	/**
	 * When user operate insert new staff. <b>Note</b> This method does not call
	 * to database
	 * 
	 * @param trackerUN
	 *            new username that user want to add into staff list
	 */
	private void insertStaff(final String trackerUN) {
		if (!arrTrackers.contains(trackerUN)) {
			MessageBox.alert(Format.format(constants.msb_insertStaff_trackernotexist(),trackerUN));
			return;
		} else if (arrManage.contains(trackerUN)) {
			MessageBox.alert(Format.format(constants.msb_insertStaff_error(),trackerUN));
		} else if (arrManage.contains(trackerUN)){
			MessageBox.alert(Format.format(constants.msb_insertStaff_already_exist(), trackerUN));
		}else {
			arrStaff.add(trackerUN);
			refreshStaffGrid();
		}
	}

	/**
	 * when user press imageBack, remove from staff list, then insert into
	 * manage list. <b>Note</b> This method does not call to database
	 * 
	 * @param trackerUN
	 *            new username that user want to change from staff list into
	 *            manage list
	 */
	private void changeFromStaffToManage(final String trackerUN) {
		arrManage.add(arrStaff.get(selectedStaff));
		arrStaff.remove(selectedStaff);
		refreshManageGrid();
		refreshStaffGrid();
	}

	/**
	 * when user press imageForward, remove from manage list, then insert into
	 * staff list. <b>Note</b> This method does not call to database
	 * 
	 * @param trackerUN
	 *            new username that user want to change from manage list into
	 *            staff list
	 */
	private void changeFromManageToStaff(final String trackerUN) {
		if (arrManage.size() <= 1) {
			MessageBox.alert(constants.msb_changeFromManageToStaff());
			return;
		}
		arrStaff.add(arrManage.get(selectedManage));
		arrManage.remove(selectedManage);
		refreshManageGrid();
		refreshStaffGrid();
	}

	/**
	 * called whenever user do operation affect staff list
	 */
	private void refreshStaffGrid() {
		String[][] arrStaff2D = Utils.convertArrayList2String2D(arrStaff);
		proxyStaff = new MemoryProxy(arrStaff2D);
		storeStaff.setDataProxy(proxyStaff);
		storeStaff.load();
	}

	/**
	 * called whenever user do operation affect manage list
	 */
	private void refreshManageGrid() {
		String[][] arrManage2D = Utils.convertArrayList2String2D(arrManage);
		proxyManage = new MemoryProxy(arrManage2D);
		storeManage.setDataProxy(proxyManage);
		storeManage.load();
	}

	/**
	 * Disable or enable some button/ image when user do operation affect manage
	 * list
	 * 
	 * @param b
	 *            set whether true/false
	 */
	private void setEnabledManage(boolean b) {
		if (b) {
			btnRemoveManage.setDisabled(false);
			imageForward.setUrl("/images/arrowforward.png");
		} else {
			btnRemoveManage.setDisabled(true);
			imageForward.setUrl("/images/arrowforward-disable.png");
			selectedManage = -1;
		}
	}

	/**
	 * Disable or enable some button/ image when user do operation affect staff
	 * list
	 * 
	 * @param b
	 *            set whether true/false
	 */
	private void setEnabledStaff(boolean b) {
		if (b) {
			btnRemoveStaff.setDisabled(false);
			imageBack.setUrl("/images/arrowback.png");
		} else {
			btnRemoveStaff.setDisabled(true);
			imageBack.setUrl("/images/arrowback-disable.png");
			selectedStaff = -1;
		}
	}

	/**
	 * programmer call when he show this window for user
	 * 
	 * @param callback
	 */
	public void show(ShareTrackedCallback callback) {
		this.callback = callback;
		super.show();
	}

}

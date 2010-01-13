package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CManage;
import com.bkitmobile.poma.database.client.entity.CStaff;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.util.client.Utils;
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

public class PermitTrackedWindow extends Window {

	private static PermitTrackedWindow permitTrackedWindow;
	private Long trackedID;
	private DatabaseServiceAsync dbAsync = DatabaseService.Util.getInstance();

	private Image imageForward;
	private Image imageBack;

	/**
	 * Staff
	 */
	private Button btnInsertStaff;
	private TextField txtStaff;
	private Button btnRemoveStaff;

	private GridPanel gridStaff;
	private MemoryProxy proxyStaff;
	private Store storeStaff;
	// private String[][] arrStaff2 = new String[1][];
	private int selectedStaff = -1;

	/**
	 * Manage
	 */
	private TextField txtManage;
	private Button btnInsertManage;
	private Button btnRemoveManage;

	private GridPanel gridManage;
	private MemoryProxy proxyManage;
	private Store storeManage;
	// private String[][] arrManage2 = new String[1][];
	private int selectedManage = -1;

	private Button btnApply;
	private Button btnReset;
	private ArrayList<String> arrStaffDefault;
	private ArrayList<String> arrManageDefault;
	private ArrayList<String> arrStaff;
	private ArrayList<String> arrManage;
	private ArrayList<String> arrTrackers;

	private PermitTrackedCallback callback;

	public static PermitTrackedWindow getInstance() {
		return permitTrackedWindow != null ? permitTrackedWindow
				: new PermitTrackedWindow();
	}

	public void setTrackedID(Long trackedID) {
		this.trackedID = trackedID;
		loadStaffData();
		loadManageData();
		imageForward.setUrl("/images/arrowforward-disable.png");
		imageBack.setUrl("/images/arrowback-disable.png");
	}

	public PermitTrackedWindow() {
		super();
		permitTrackedWindow = this;

		initUI();
		initStuff();
		layout();

		
		loadTrackers();

		// loadStaffData();
		// loadManageData();
	}

	private void initUI() {
		this.setTitle("Permisssion");
		this.setPaddings(5);
		this.setCloseAction(Window.HIDE);
		this.setButtonAlign(Position.RIGHT);
		this.setResizable(false);
		this.setSize(500, 400);
		this.setModal(true);
		// this.setWidth(500);// (500, 600);
		// this.setAutoHeight(true);

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

		btnRemoveStaff = new Button("Remove", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);

				if (selectedStaff == -1)
					return;
				arrStaff.remove(selectedStaff);
				refreshStaffGrid();

				setEnabledStaff(false);

				// dbAsync.removeStaff(arrStaff2[selectedStaff][0], trackedID,
				// new AsyncCallback<ServiceResult<CStaff>>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// caught.printStackTrace();
				// }
				//
				// @Override
				// public void onSuccess(ServiceResult<CStaff> result) {
				// if (!result.isOK())
				// MessageBox.alert("Can't remove tracker "
				// + arrStaff2[selectedStaff][0]
				// + " from tracked " + trackedID
				// + " staff list\n");
				// else
				// loadStaffData();
				// }
				//
				// });
			}
		});
		btnRemoveStaff.setDisabled(true);

		btnRemoveManage = new Button("Remove", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);

				if (selectedManage == -1)
					return;
				if (arrManage.size() <= 1) {
					MessageBox.alert("Manage list must more than 1 object");
					return;
				}

				arrManage.remove(selectedManage);
				refreshManageGrid();

				setEnabledManage(false);
				// dbAsync.removeManage(arrManage2[selectedManage][0],
				// trackedID,
				// new AsyncCallback<ServiceResult<CManage>>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// caught.printStackTrace();
				// }
				//
				// @Override
				// public void onSuccess(ServiceResult<CManage> result) {
				// if (!result.isOK())
				// MessageBox.alert("Can't remove tracker "
				// + arrManage2[selectedManage][0]
				// + " from tracked " + trackedID
				// + " manage list\n");
				// else
				// loadManageData();
				// }
				//
				// });
			}
		});
		btnRemoveManage.setDisabled(true);

		btnInsertStaff = new Button("Insert", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				insertStaff(txtStaff.getText());
				txtStaff.setValue("");
			}
		});
		btnInsertStaff.setMinWidth(50);

		btnInsertManage = new Button("Insert", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				insertManage(txtManage.getText());
			}
		});
		btnInsertManage.setMinWidth(50);

		imageForward = new Image("/images/arrowforward-disable.png");
		imageForward.setPixelSize(32, 32);
		imageForward.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (selectedStaff == -1)
					return;
				changeFromStaffToManage(arrStaff.get(selectedStaff));
				setEnabledStaff(false);
			}
		});

		imageBack = new Image("/images/arrowback-disable.png");
		imageBack.setPixelSize(32, 32);
		imageBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				if (selectedManage == -1)
					return;
				changeFromManageToStaff(arrManage.get(selectedManage));
				setEnabledManage(false);
			}
		});

		btnApply = new Button("Apply", new ButtonListenerAdapter() {
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
							public void onSuccess(ServiceResult<Boolean> result) {
								if (result.isOK()) {
									for (String tracker : arrManage) {
										dbAsync
												.insertManage(
														tracker,
														trackedID,
														new AsyncCallback<ServiceResult<CManage>>() {

															@Override
															public void onFailure(
																	Throwable caught) {
																caught
																		.printStackTrace();
															}

															@Override
															public void onSuccess(
																	ServiceResult<CManage> result) {
																if (!result
																		.isOK()) {
																	MessageBox
																			.alert(result
																					.getMessage());
																}
															}

														});
									}
								} else {
									MessageBox
											.alert("Error in remove manage list");
									return;
								}
							}

						});

				dbAsync.removeStaffsByTracked(trackedID,
						new AsyncCallback<ServiceResult<Boolean>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ServiceResult<Boolean> result) {
								if (result.isOK()) {
									for (String tracker : arrStaff) {
										dbAsync
												.insertStaff(
														tracker,
														trackedID,
														new AsyncCallback<ServiceResult<CStaff>>() {

															@Override
															public void onFailure(
																	Throwable caught) {
																caught
																		.printStackTrace();
															}

															@Override
															public void onSuccess(
																	ServiceResult<CStaff> result) {
																if (!result
																		.isOK()) {
																	MessageBox
																			.alert(result
																					.getMessage());
																}
															}

														});
									}
								} else {
									MessageBox.alert(result.getMessage());
									return;
								}
							}
						});

				hide();
				if (callback != null) {
					callback.onApplyOperation(arrStaff,arrManage);
				}
			}
		});

		btnReset = new Button("Reset", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				arrStaff = new ArrayList<String>(arrStaffDefault);
				arrManage = new ArrayList<String>(arrManageDefault);
				loadStaffData();
				loadManageData();
			}
		});

		initGridStaff();
		initGridManage();
	}

	private void initStuff() {
		arrStaffDefault = new ArrayList<String>();
		arrManageDefault = new ArrayList<String>();

		arrStaff = new ArrayList<String>();
		arrManage = new ArrayList<String>();

		arrTrackers = new ArrayList<String>();
	}

	private void layout() {
		this.setLayout(new BorderLayout());

		Panel pnlTop = new Panel();
		pnlTop.setBorder(false);
		pnlTop.setAutoHeight(true);
		pnlTop.setLayout(new ColumnLayout());

		Panel pnl1 = new Panel();
		pnl1.setBorder(false);
		pnl1.setLayout(new HorizontalLayout(5));
		pnl1.add(txtStaff);
		pnl1.add(btnInsertStaff);
		pnl1.add(btnRemoveStaff);

		Panel pnl2 = new Panel();
		pnl2.setBorder(false);
		pnl2.setLayout(new HorizontalLayout(5));
		pnl2.add(txtManage);
		pnl2.add(btnInsertManage);
		pnl2.add(btnRemoveManage);

		pnlTop.add(pnl1, new ColumnLayoutData(.455));
		pnlTop.add(new Label(), new ColumnLayoutData(.09));
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

		// gridStaff.addButton(btnRemoveStaff);
		// gridManage.addButton(btnRemoveManage);
		pnlCenter.add(gridStaff, new ColumnLayoutData(.455));
		pnlCenter.add(pnlMiniButton, new ColumnLayoutData(.09));
		pnlCenter.add(gridManage, new ColumnLayoutData(.455));

		BorderLayoutData btnNorth = new BorderLayoutData(RegionPosition.NORTH);
		btnNorth.setAutoHide(true);
		this.add(pnlTop, btnNorth);
		this.add(pnlCenter, new BorderLayoutData(RegionPosition.CENTER));
		this.addButton(btnReset);
		this.addButton(btnApply);

	}

	private void initGridStaff() {
		RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("staff") });

		gridStaff = new GridPanel();

		proxyStaff = new MemoryProxy(new String[1][]);

		ArrayReader reader = new ArrayReader(recordDef);
		storeStaff = new Store(proxyStaff, reader);
		storeStaff.load();
		gridStaff.setStore(storeStaff);

		ColumnConfig ccStaff = new ColumnConfig("Staff", "staff", 150, false,
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
				
				selectedStaff = rowIndex;
				setEnabledStaff(true);
			}
		});
		gridStaff.setButtonAlign(Position.RIGHT);
		gridStaff.setAutoScroll(true);

		gridStaff.setHeight(296);
		// gridStaff.setWidth(600);
	}

	private void initGridManage() {
		RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("manage") });

		gridManage = new GridPanel();

		proxyManage = new MemoryProxy(new String[1][]);

		ArrayReader reader = new ArrayReader(recordDef);
		storeManage = new Store(proxyManage, reader);
		storeManage.load();
		gridManage.setStore(storeManage);

		ColumnConfig ccManage = new ColumnConfig("Manage", "manage", 150, false,
				null, "manage");
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
				setEnabledManage(true);
				selectedManage = rowIndex;
			}
		});
		gridManage.setButtonAlign(Position.RIGHT);
		gridManage.setAutoScroll(true);

		gridManage.setHeight(296);
		// gridManage.setWidth(600);
	}

	private void loadStaffData() {
		
		arrStaff.clear();
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
								arrStaff.add(result.getResult().get(i)
										.getTrackerUN());
							}

							refreshStaffGrid();
						} else {
							
						}
					}

				});
	}

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

	private void loadManageData() {
		arrManage.clear();
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
								arrManage.add(result.getResult().get(i)
										.getTrackerUN());
							}
							refreshManageGrid();
						}
					}

				});
	}

	private void insertStaff(final String trackerUN) {
		
		if (!arrTrackers.contains(trackerUN)) {
			MessageBox.alert(trackerUN
					+ " not in database. Can't insert tracker " + trackerUN
					+ " to tracked " + trackedID + " 's staff list \n");
			return;
		} else if (arrManage.contains(trackerUN)) {
			MessageBox.alert("You must remove " + trackerUN
					+ " in manage list before inserting in staff list");
		} else {
			arrStaff.add(trackerUN);
			refreshStaffGrid();
			
		}
		// dbAsync.insertStaff(trackerUN, trackedID,
		// new AsyncCallback<ServiceResult<CStaff>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ServiceResult<CStaff> result) {
		// if (!result.isOK())
		// MessageBox.alert("Can't insert tracker "
		// + trackerUN + " to tracked " + trackedID
		// + " 's staff list \n");
		// else
		// loadStaffData();
		// }
		//
		// });
	}

	private void insertManage(final String trackerUN) {
		// dbAsync.insertManage(trackerUN, trackedID,
		// new AsyncCallback<ServiceResult<CManage>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ServiceResult<CManage> result) {
		// if (!result.isOK())
		// MessageBox.alert("Can't insert tracker "
		// + trackerUN + " to tracked " + trackedID
		// + " 's manage list \n");
		// else
		// loadManageData();
		// }
		//
		// });
		if (!arrTrackers.contains(trackerUN)) {
			MessageBox.alert(trackerUN
					+ " not in database. Can't insert tracker " + trackerUN
					+ " to tracked " + trackedID + " 's manage list \n");
			return;
		} else if (arrStaff.contains(trackerUN)) {
			MessageBox.alert("You must remove " + trackerUN
					+ " in staff list before inserting in manage list");
		} else {
			arrManage.add(trackerUN);
			refreshManageGrid();
		}
	}

	private void changeFromManageToStaff(final String trackerUN) {
		
		if (arrManage.size() <= 1) {
			MessageBox.alert("Manage list must more than 1 object");
			return;
		}
		arrStaff.add(arrManage.get(selectedManage));
		arrManage.remove(selectedManage);
		refreshStaffGrid();
		refreshManageGrid();

		// dbAsync.removeManage(trackerUN, trackedID,
		// new AsyncCallback<ServiceResult<CManage>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ServiceResult<CManage> result) {
		// if (!result.isOK())
		// MessageBox.alert("Can't remove tracker "
		// + trackerUN + " from tracked " + trackedID
		// + " manage list\n");
		// else {
		// loadManageData();
		// insertStaff(trackerUN);
		// }
		// }
		//
		// });
	}

	private void changeFromStaffToManage(final String trackerUN) {
		
		arrManage.add(arrStaff.get(selectedStaff));
		arrStaff.remove(selectedStaff);
		refreshStaffGrid();
		refreshManageGrid();

		// dbAsync.removeStaff(trackerUN, trackedID,
		// new AsyncCallback<ServiceResult<CStaff>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ServiceResult<CStaff> result) {
		// if (!result.isOK())
		// MessageBox.alert("Can't remove tracker "
		// + trackerUN + " from tracked " + trackedID
		// + " staff list");
		// else {
		// loadStaffData();
		// insertManage(trackerUN);
		// }
		// }
		//
		// });
	}

	private void refreshManageGrid() {
		String[][] arrManage2 = Utils.convertArrayList2String2D(arrManage);
		proxyManage = new MemoryProxy(arrManage2);
		storeManage.setDataProxy(proxyManage);
		storeManage.load();
	}

	private void refreshStaffGrid() {
		String[][] arrStaff2 = Utils.convertArrayList2String2D(arrStaff);
		proxyStaff = new MemoryProxy(arrStaff2);
		storeStaff.setDataProxy(proxyStaff);
		storeStaff.load();
	}

	private void setEnabledStaff(boolean b) {
		if (b) {
			btnRemoveStaff.setDisabled(false);
			imageForward.setUrl("/images/arrowforward.png");
		} else {
			btnRemoveStaff.setDisabled(true);
			imageForward.setUrl("/images/arrowforward-disable.png");
			selectedStaff = -1;
		}
	}

	private void setEnabledManage(boolean b) {
		if (b) {
			btnRemoveManage.setDisabled(false);
			imageBack.setUrl("/images/arrowback.png");
		} else {
			btnRemoveManage.setDisabled(true);
			imageBack.setUrl("/images/arrowback-disable.png");
			selectedManage = -1;
		}
	}

	public void show(PermitTrackedCallback callback) {
		this.callback = callback;
		super.show();
	}

}

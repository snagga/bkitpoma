package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTrack;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.localization.client.MapToolbarConstants;
import com.bkitmobile.poma.localization.client.TrackedPanelConstants;
import com.bkitmobile.poma.ui.client.map.TrackPointOverlay;
import com.bkitmobile.poma.ui.client.map.WayPointOverlay;
import com.bkitmobile.poma.util.client.OneTimeTask;
import com.bkitmobile.poma.util.client.OptimizeTaskQueue;
import com.bkitmobile.poma.util.client.Task;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TrackedPanel extends LoadingPanel {
	private static Long currentTrackedID;
	private Long previousTrackedID;

	private int currentRow = -1;
	private int previousRow = -1;

	private static TrackedPanel trackedPanel;
	private Toolbar toolbar;
	private static TablePanel trackedTable;

	private Task taskUpdateTrackedInfo = null;
	private ToolbarButton btnRemoveTracked;
	private ToolbarButton btnShareTracked;
	private Label lblLatitude;
	private Label lblLongtitude;
	private Label lblSpeed;
	public static final int LABEL_WIDTH = 100;
	public static final int COMBOBOX_WIDTH = MenuPanel.WIDTH - LABEL_WIDTH - 50;

	private long lastShowInMapCellTime = 0;
	private long lastFollowCellTime = 0;

	private MapToolbarConstants mapToolbarConstants = GWT
			.create(MapToolbarConstants.class);

	private Panel trackedGeoInfoPanel;

	private TrackedPanelConstants constants = GWT
			.create(TrackedPanelConstants.class);

	private ToolbarButton btnKMLTracked;

	public static TrackedPanel getInstance() {
		return trackedPanel == null ? new TrackedPanel() : trackedPanel;
	}

	private TrackedPanel() {
		super();
		trackedPanel = this;

		initToolbar();
		initTrackedTable();

		initTrackedGeoInfoPanel();

		addListener();
		layout();
	}

	private void initToolbar() {
		toolbar = new Toolbar();
		ToolbarButton btnDetail = new ToolbarButton(constants
				.lbl_toolbar_detail(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (!trackedTable.getGridPanel().getSelectionModel()
						.hasSelection()) {
					MessageBox.alert(constants.warning_no_row_selected());
					return;
				}
				MenuPanel.getInstance().detailPage();
			}
		});
		btnDetail.setIcon("/images/TrackedMenu/detail.png");
		btnDetail.setTooltip(constants.lbl_toolbar_detail_tool_tip());

		ToolbarButton btnProfile = new ToolbarButton(constants
				.lbl_toolbar_profile(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (!trackedTable.getGridPanel().getSelectionModel()
						.hasSelection()) {
					MessageBox.alert(constants.warning_no_row_selected());
					return;
				}

				BkitPoma
						.displayItemID(TrackedProfileForm.getInstance().getId());
				TrackedProfileForm.getInstance().resetForm();
			}
		});
		btnProfile.setIcon("/images/TrackedMenu/profile.png");
		btnProfile.setTooltip(constants.lbl_toolbar_profile_tool_tip());

		ToolbarButton btnCreateTracked = new ToolbarButton(constants
				.lbl_toolbar_create(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				BkitPoma.displayItemID(RegisterTrackedForm.getInstance()
						.getId());
			}
		});
		btnCreateTracked.setIcon("/images/silk/user_add.gif");

		btnRemoveTracked = new ToolbarButton(constants.lbl_toolbar_remove(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						if (currentTrackedID == null) {
							MessageBox.alert(constants
									.warning_no_row_selected());
							return;
						}
						TrackedPanel.getInstance().removeTracked();
					}
				});
		btnRemoveTracked.setIcon("/images/silk/user_delete.gif");
		btnRemoveTracked.setTooltip(constants.lbl_toolbar_remove_tool_tip());

		btnShareTracked = new ToolbarButton(constants.lbl_toolbar_share(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {

						if (currentTrackedID == null) {
							MessageBox.alert(constants
									.warning_no_row_selected());
							return;
						}

						ShareTrackedWindow permitTrackedWindow = ShareTrackedWindow

						.getInstance();
						
						
						permitTrackedWindow.setTrackedID(currentTrackedID,
								TrackedProfileForm.arrManage,
								TrackedProfileForm.arrStaff);
						permitTrackedWindow.show(new ShareTrackedCallback() {
							@Override
							public void onApplyOperation(
									ArrayList<String> arrManage,
									ArrayList<String> arrStaff) {

								TrackedProfileForm.getInstance().refresh(
										arrManage, arrStaff);

							}
						});
					}
				});
		btnShareTracked.setIcon("/images/TrackedMenu/share.png");
		btnShareTracked.setMinWidth(50);
		ToolTip shareTrackedTip = new ToolTip(constants
				.lbl_toolbar_share_tool_tip());
		shareTrackedTip.applyTo(btnShareTracked);

		toolbar.addButton(btnDetail);
		toolbar.addButton(btnProfile);
		toolbar.addButton(btnCreateTracked);
		toolbar.addButton(btnRemoveTracked);
		toolbar.addButton(btnShareTracked);
	}

	private void initTrackedTable() {
		btnKMLTracked = new ToolbarButton(".   .", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (!trackedTable.getGridPanel().getSelectionModel()
						.hasSelection()) {
					MessageBox.alert(constants.warning_no_row_selected());
					return;
				}

				Window.open("/bkitpoma/kml?tracked="
						+ UserSettings.ctracked.getUsername() + "&apitracked="
						+ UserSettings.ctracked.getApiKey(), "_blank", "");
			}
		});
		btnKMLTracked.setTooltip(constants.btnKMLTracked());
		// btnKMLTracked.setMinWidth(35);
		btnKMLTracked.setIcon("/images/TrackedMenu/kml.jpg");

		trackedTable = new TablePanel(new int[] { TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING, TablePanel.FIELD_RADIO,
				TablePanel.FIELD_BOOLEAN }, new String[] {
				constants.lbl_device_id(), constants.lbl_device_name(),
				"<img src='/images/TrackedMenu/spy.png' />",
				"<img src='/images/TrackedMenu/eye.png' />" });
		trackedTable.getBottomToolbar().addButton(btnKMLTracked);

		trackedTable.setTooltip(2, constants.lbl_follow_device());
		trackedTable.setTooltip(3, constants.lbl_show_in_map());
		trackedTable.setPageSize(10);
		try {
			trackedTable.setColumnWidthManually(new int[] { 60, 190, 30, 30 },
					true);
		} catch (Exception ex) {
			trackedTable.setColumnWidthBasedOnContent();
		}
		trackedTable.setGridHeight(Window.getClientHeight() - 200);
		trackedTable.getGridPanel().setSelectionModel(new RowSelectionModel(true));

	}

	private void layout() {
		this.setLayout(new VerticalLayout());
		this.setTopToolbar(toolbar);
		this.add(trackedTable);
		this.add(trackedGeoInfoPanel);

		// hide trackedGeoInfoPanel when first load because no tracked
		// information to display
		trackedGeoInfoPanel.hide();

	}

	private void removeTracked() {
		MessageBox.confirm(constants.lbl_confirm(), constants
				.lbl_remove_device()
				+ " " + currentTrackedID, new MessageBox.ConfirmCallback() {
			@Override
			public void execute(String btnID) {
				if (btnID.equals("yes")) {
					CaptchaWindow.getInstance().show(new Function() {
						@Override
						public void execute() {
							_removeTracked();
						}
					});
				}
			}
		});
	}

	private void _removeTracked() {

		DatabaseService.Util.getInstance().removeTracked(currentTrackedID,
				new AsyncCallback<ServiceResult<CTracked>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<CTracked> result) {
						if (!result.isOK()) {
							MessageBox.alert(result.getMessage());
						} else {
							// remove record in tracked panel
							trackedTable.removeRecord(currentRow);
							// remove tracked from map
							MapPanel.removeFromMap(currentTrackedID);
							// remove tracked in TrackedDetailPanel combobox
							TrackedDetailPanel.getInstance().removeTracked(
									currentTrackedID);
							// remove tracked in UserSettings
							UserSettings.ctrackedList.remove(currentTrackedID);
							// deselect row of grid in TrackedPanel
							trackedTable.getGridPanel().getSelectionModel()
									.deselectRow(currentRow);
							// reset title of GeoPanel
							trackedGeoInfoPanel.setTitle(constants
									.lbl_geo_panel_title());

							// show noTrackedPanel when there is no row in Grid
							if (UserSettings.ctrackedList.size() == 0)
								MenuPanel.getInstance().noTrackedPage();

							// reset some variable
							currentRow = -1;
							previousRow = -1;
							currentTrackedID = null;
							previousTrackedID = null;
						}
					}

				});
	}

	private void addListener() {
		trackedTable.addListener(new PanelListenerAdapter() {
			@Override
			public void onRender(Component component) {
			super.onRender(component);

			// disable refresh buttonn in paging toolbar
			trackedTable.getPagingToolbar().getRefreshButton().setVisible(false);
			}
			});
		
		// row click event
		trackedTable.addGridRowListener(new GridRowListenerAdapter() {
			@Override
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				// get trackedID
				currentRow = rowIndex;
				currentTrackedID = Long.parseLong(trackedTable.getRecord(
						currentRow).get(0));
				TrackedProfileForm.arrManage.clear();
				TrackedProfileForm.arrStaff.clear();
				

				UserSettings.ctracked = UserSettings.ctrackedList
						.get(currentTrackedID);

				// show tracked property
				getTrackedInfo();

				// TODO check for new tracked, a tracked has no track

				DatabaseService.Util.getInstance().getTracksByTracked(
						currentTrackedID,
						new AsyncCallback<ServiceResult<ArrayList<CTrack>>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ServiceResult<ArrayList<CTrack>> result) {
								if (result.isOK()) {

									if (result.getResult() == null
											|| result.getResult().size() == 0) {
										MessageBox.alert(constants
												.lbl_cannot_show_in_map());
										return;

									} else {
										boolean isShowInMap = UserSettings.ctrackedList
												.get(currentTrackedID)
												.getShowInMap() == null ? false

										: UserSettings.ctrackedList.get(
												currentTrackedID)
												.getShowInMap();
										if (isShowInMap
												&& UserSettings.currentTrackOverlay
														.containsKey(currentTrackedID)) {
											TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
													.get(currentTrackedID);

											if (trackOverlay.getWaypoint(0) != null) {

												WayPointOverlay point = trackOverlay
														.getWaypointOverlay(0);
												if (point != null) {
													MapPanel
															.getInstance()
															.setCenter(
																	point
																			.getLatLng());

													point.showInfoWindow();

												}
											}
											MapPanel.getInstance()
													.boundToTracked(
															currentTrackedID);

										}
									}
								}
							}

						});

			}
		});

		// grid cell click event
		trackedTable.getGridPanel().addGridCellListener(
				new GridCellListenerAdapter() {
					@Override
					public void onCellClick(GridPanel grid, int rowIndex,
							int colIndex, EventObject e) {
						Record rec = grid.getStore().getAt(rowIndex);
						String columnName = colIndex + "";

						// Handle show in map mode
						if (grid.getColumnModel().getDataIndex(colIndex)
								.equals(columnName)
								&& e.getTarget(".checkbox", 1) != null) {
							if (System.currentTimeMillis()
									- lastShowInMapCellTime <= 900) {
								return;
							}
							lastShowInMapCellTime = System.currentTimeMillis();

							rec.set(columnName, !rec.getAsBoolean(columnName));

							if (rec.getAsBoolean(columnName)) {
								showTrackedInMap();
							} else {
								hideTrackedInMap();
							}
						}

						// Handle follow tracked mode
						else if (grid.getColumnModel().getDataIndex(colIndex)
								.equals(columnName)
								&& e.getTarget(".radio", 1) != null) {
							if (System.currentTimeMillis() - lastFollowCellTime <= 800) {
								return;
							}
							lastFollowCellTime = System.currentTimeMillis();

							rec.set(columnName, !rec.getAsBoolean(columnName));

							boolean showInMap = UserSettings.ctrackedList.get(
									currentTrackedID).getShowInMap() == null ? false
									: UserSettings.ctrackedList.get(
											currentTrackedID).getShowInMap();

							if (rec.getAsBoolean(columnName)) {
								if (previousRow != -1)
									trackedTable.getGridPanel().getStore()
											.getAt(previousRow).set(columnName,
													false);
							}

							if (!showInMap) {
								checkShowInMap(true);
								showTrackedInMap();
								return;
							}

							if (rec.getAsBoolean(columnName)) {
								MapPanel.setTrackedFollow(currentTrackedID,
										previousTrackedID);
								previousTrackedID = currentTrackedID;
								previousRow = currentRow;
							} else {
								MapPanel.setTrackedFollow(null,
										currentTrackedID);
								previousTrackedID = null;
								previousRow = -1;
							}

						}

					}
				});

	}

	public void loadTrackeds() {
		if (UserSettings.ctracker == null)
			return;
		startLoading(constants.lbl_loading());
		DatabaseService.Util.getInstance().getTrackedsByTracker(
				UserSettings.ctracker.getUsername(),
				new AsyncCallback<ServiceResult<ArrayList<CTracked>>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						stopLoading();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CTracked>> result) {

						if (result.isOK() && result.getResult() != null) {
							ArrayList<CTracked> arrResult = result.getResult();

							for (CTracked ctracked : arrResult) {
								if (ctracked == null)
									continue;
								UserSettings.ctrackedList.put(ctracked
										.getUsername(), ctracked);

								boolean isShowInMap = ctracked.getShowInMap() == null ? false
										: ctracked.getShowInMap();
								trackedTable.addRecord(new String[] {
										ctracked.getUsername().toString(),
										ctracked.getName(), "false",
										"" + isShowInMap });

							}

							addTrackedsToMap();
							TrackedDetailPanel.getInstance().loadTrackedList();
						}
						stopLoading();
					}
				});
	}

	public void showTrackedInMap() {
		UserSettings.ctrackedList.get(currentTrackedID).setShowInMap(true);

		

		MapPanel
				.addToMap(UserSettings.ctrackedList.get(currentTrackedID),
						trackedTable.getGridPanel().getStore()
								.getAt(currentRow).getAsBoolean("2"),
						currentTrackedID, previousTrackedID);

		UserSettings.optimizeTaskQueue.put(OptimizeTaskQueue
				.activeDeactiveTracked(currentTrackedID), new OneTimeTask(10) {
			@Override
			public void executeOne() {
				updateShowInMap(true);
			}
		});
	}

	public void hideTrackedInMap() {
		UserSettings.ctrackedList.get(currentTrackedID).setShowInMap(false);
		
		MapPanel.removeFromMap(currentTrackedID);
		trackedTable.getGridPanel().getStore().getAt(currentRow)
				.set("2", false);
		previousTrackedID = null;
		previousRow = -1;

		UserSettings.optimizeTaskQueue.put(OptimizeTaskQueue
				.activeDeactiveTracked(currentTrackedID), new OneTimeTask(10) {
			@Override
			public void executeOne() {
				updateShowInMap(false);
			}
		});
	}

	private void updateShowInMap(boolean showed) {
		CTracked ctracked = new CTracked();
		ctracked.setUsername(currentTrackedID);
		ctracked.setShowInMap(showed);

		DatabaseService.Util.getInstance().updateTracked(ctracked,
				new AsyncCallback<ServiceResult<CTracked>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<CTracked> result) {
						if (!result.isOK())
							MessageBox.alert(result.getMessage());
					}

				});
	}

	public static Long getCurrentTrackedID() {
		return currentTrackedID;
	}

	private void initTrackedGeoInfoPanel() {
		lblLatitude = new Label(constants.lbl_long());
		lblLatitude.setCls("gwt-Label");
		lblLatitude.setWidth(MenuPanel.WIDTH);
		lblLatitude.setSize(MenuPanel.WIDTH, MenuPanel.LABEL_HEIGHT);

		lblLongtitude = new Label(constants.lbl_lat());
		lblLongtitude.setCls("gwt-Label");
		lblLongtitude.setWidth(MenuPanel.WIDTH);
		lblLongtitude.setSize(MenuPanel.WIDTH, MenuPanel.LABEL_HEIGHT);

		lblSpeed = new Label(constants.lbl_speed());
		lblSpeed.setCls("gwt-Label");
		lblSpeed.setWidth(MenuPanel.WIDTH);
		lblSpeed.setSize(MenuPanel.WIDTH, MenuPanel.LABEL_HEIGHT);

		trackedGeoInfoPanel = new Panel();

		trackedGeoInfoPanel.setLayout(new VerticalLayout());
		trackedGeoInfoPanel.setBorder(false);
		trackedGeoInfoPanel.setBodyBorder(false);
		trackedGeoInfoPanel.setTitle(constants.lbl_geo_panel_title());
		trackedGeoInfoPanel.setHeight(3 * MenuPanel.LABEL_HEIGHT);

		trackedGeoInfoPanel.add(lblLatitude);
		trackedGeoInfoPanel.add(lblLongtitude);
		trackedGeoInfoPanel.add(lblSpeed);
	}

	private void getTrackedInfo() {
		if (trackedGeoInfoPanel.isHidden())
			trackedGeoInfoPanel.show();
		trackedGeoInfoPanel.setTitle(constants.lbl_geo_panel_title() + " "
				+ currentTrackedID);
		CTracked ctracked = UserSettings.ctrackedList.get(currentTrackedID);

		checkShowInMap(ctracked.getShowInMap() == null ? false : ctracked
				.getShowInMap());

		addUpdateTrackedInfoTask();
	}

	private void checkShowInMap(boolean checked) {

		trackedTable.getGridPanel().getStore().getAt(currentRow).set("3",
				checked);
	}

	private void addUpdateTrackedInfoTask() {
		if (taskUpdateTrackedInfo == null) {
			taskUpdateTrackedInfo = new Task(2) {
				@Override
				public void execute() {
					getGeoInfo();
				}
			};
			UserSettings.timerTask.addTask(taskUpdateTrackedInfo);
		}
	}

	private void getGeoInfo() {
		TrackPointOverlay trackPointOverlay = null;
		if (UserSettings.currentTrackOverlay.containsKey(currentTrackedID))

			trackPointOverlay = UserSettings.currentTrackOverlay
					.get(currentTrackedID);

		// get longtitude
		String longtitude = trackPointOverlay == null ? constants.lbl_unknow()
				: trackPointOverlay.getWaypointOverlay(0) == null ? constants
						.lbl_unknow() : trackPointOverlay.getWaypointOverlay(0)
						.getLongitudeInDegree();

		// get latitude
		String latitude = trackPointOverlay == null ? constants.lbl_unknow()
				: trackPointOverlay.getWaypointOverlay(0) == null ? constants
						.lbl_unknow() : trackPointOverlay.getWaypointOverlay(0)
						.getLatitudeInDegree();

		lblLatitude.setText(constants.lbl_lat() + " = " + latitude);
		lblLongtitude.setText(constants.lbl_long() + " = " + longtitude);

		// tracked lost signal of standing

		if (trackPointOverlay != null
				&& trackPointOverlay.getWaypointOverlay(0) != null
				&& trackPointOverlay.getWaypointOverlay(0).getInfoContent()
						.contains(mapToolbarConstants.infoStatus())) {

			lblSpeed.setText(constants.lbl_standing());
			return;
		}

		lblSpeed.setText(constants.lbl_speed()
				+ " = "
				+ (trackPointOverlay == null ? constants.lbl_unknow()
						: trackPointOverlay.getWaypoint(0) == null ? constants
								.lbl_unknow() : trackPointOverlay
								.getWaypoint(0).getSpeed()
								+ " m/s")

		);

	}

	public void addTrackedsToMap() {
		ArrayList<CTracked> trackedList = new ArrayList<CTracked>(
				UserSettings.ctrackedList.values());

		for (CTracked tracked : trackedList) {
			boolean isShowedInMap = tracked.getShowInMap() == null ? false
					: tracked.getShowInMap();
			if (isShowedInMap) {
				MapPanel.addToMap(tracked);
			}
		}
	}

	public void addNewTracked(CTracked ctracked) {
		trackedTable.startLoading("Loading ...");

		// add tracked to UserSettings
		UserSettings.ctrackedList.put(ctracked.getUsername(), ctracked);

		// add tracked to grid in TrackedPanel
		boolean isShowInMap = ctracked.getShowInMap() == null ? false
				: ctracked.getShowInMap();
		trackedTable.addRecord(new String[] {
				ctracked.getUsername().toString(), ctracked.getName(), "false",
				"" + isShowInMap });

		// add tracked to combobox in TrackedDetailPanel
		TrackedDetailPanel.getInstance().addNewTracked(ctracked);

		trackedTable.stopLoading();
	}
}

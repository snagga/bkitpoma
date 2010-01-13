package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.database.client.entity.CWaypoint;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.localization.client.MapToolbarConstants;
import com.bkitmobile.poma.ui.client.map.TrackPointOverlay;
import com.bkitmobile.poma.ui.client.map.WayPointOverlay;
import com.bkitmobile.poma.util.client.Task;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;

public class MapToolbar extends Toolbar {

	public enum ProgressStatus {
		PLAY, PAUSE, STOP, REPLAY
	}

	public enum TrackedViewMode {
		DEMO, TRACK, MULTI_TRACK
	}

	private static MapToolbar mapToolbar;

	private static final String IMAGE_PLAY = "images/MapToolbar/play.png";
	private static final String IMAGE_PAUSE = "images/MapToolbar/pause.png";
	private static final String IMAGE_STOP = "images/MapToolbar/stop.png";
	private static final String IMAGE_REPLAY = "images/MapToolbar/replay.png";

	private MapToolbarConstants mapToolbarConstants = GWT
			.create(MapToolbarConstants.class);

	private NumberField ratioField2;
	private CheckItem ratioItem_1_1;
	private CheckItem ratioItem_1_2;
	private CheckItem ratioItem_1_4;
	private CheckItem ratioItem_1_8;
	private CheckItem ratioCustomItem;
	ToolbarButton maxButton;
	private int speed = 5;
	private ProgressStatus curProgressStatus;
	private ProgressStatus preProgressStatus;
	private ToolbarButton playButton;
	private ToolbarButton pauseButton;
	private ToolbarButton stopButton;
	private ToolbarButton replayButton;
	private CheckItem speedItem5;
	private CheckItem speedItem10;
	private CheckItem speedItem30;
	private CheckItem speedItem60;
	private CheckItem speedItem100;
	private ToolbarButton followButton;
	private ToolbarButton speedButton;
	private Menu ratioMenu;
	private ToolbarButton ratioButton;
	private Menu speedMenu;
	private Label countDownLabel;

	private NumberWindow numberWindow;

	private Task countDownTask;

	private ArrayList<CWaypoint> demoWayPointList = new ArrayList<CWaypoint>();
	private int demoCount;

	private TrackedViewMode curTrackedViewMode;
	private TrackedViewMode nextTrackedViewMode;

	/**
	 * Constructor
	 */
	public MapToolbar() {
		mapToolbar = this;

		init();

		layout();
		addListener();
	}

	/**
	 * Layout the toolbar
	 */
	private void layout() {
		addButton(playButton);
		addButton(pauseButton);
		addButton(stopButton);
		addButton(replayButton);
		addButton(ratioButton);
		addSeparator();

		addButton(speedButton);
		addText("s");
		addSpacer();
		addSpacer();
		addSpacer();

		addItem(new ToolbarItem(countDownLabel.getElement()));
		addSeparator();
		addButton(followButton);

		addFill();
		addButton(maxButton);

		setTrackedViewMode(TrackedViewMode.MULTI_TRACK);

		preProgressStatus = curProgressStatus = ProgressStatus.PLAY;
	}

	/**
	 * Initialize the toolbar
	 */
	private void init() {
		playButton = new ToolbarButton(mapToolbarConstants.playButton());
		playButton.setIcon(IMAGE_PLAY);
		playButton.setDisabled(true);

		pauseButton = new ToolbarButton(mapToolbarConstants.pauseButton());
		pauseButton.setIcon(IMAGE_PAUSE);

		stopButton = new ToolbarButton(mapToolbarConstants.stopButton());
		stopButton.setIcon(IMAGE_STOP);

		ratioButton = new ToolbarButton(mapToolbarConstants.ratioButton());

		replayButton = new ToolbarButton(mapToolbarConstants.replayButton());
		replayButton.setIcon(IMAGE_REPLAY);

		ratioMenu = new Menu();

		ratioItem_1_1 = new CheckItem("1:1");
		ratioItem_1_2 = new CheckItem("1:2");
		ratioItem_1_4 = new CheckItem("1:4");
		ratioItem_1_8 = new CheckItem("1:8");
		ratioCustomItem = new CheckItem(mapToolbarConstants.customRatio());

		ratioItem_1_1.setChecked(true);
		ratioItem_1_1.setGroup("ratio");
		ratioItem_1_2.setGroup("ratio");
		ratioItem_1_4.setGroup("ratio");
		ratioItem_1_8.setGroup("ratio");
		ratioCustomItem.setGroup("ratio");

		ratioField2 = new NumberField();
		ratioField2.setWidth(30);

		ratioMenu.addItem(ratioItem_1_1);
		ratioMenu.addItem(ratioItem_1_2);
		ratioMenu.addItem(ratioItem_1_4);
		ratioMenu.addItem(ratioItem_1_8);

		ratioMenu.addSeparator();
		ratioMenu.addItem(ratioCustomItem);

		ratioButton.setMenu(ratioMenu);

		speedMenu = new Menu();
		speedItem5 = new CheckItem("5", true);
		speedItem5.setTitle("5");
		speedItem10 = new CheckItem("10");
		speedItem10.setTitle("10");
		speedItem30 = new CheckItem("30");
		speedItem30.setTitle("30");
		speedItem60 = new CheckItem("60");
		speedItem60.setTitle("60");
		speedItem100 = new CheckItem("100");
		speedItem100.setTitle("100");

		speedItem5.setGroup("speed");
		speedItem10.setGroup("speed");
		speedItem30.setGroup("speed");
		speedItem60.setGroup("speed");
		speedItem100.setGroup("speed");

		speedMenu.addItem(speedItem5);
		speedMenu.addItem(speedItem10);
		speedMenu.addItem(speedItem30);
		speedMenu.addItem(speedItem60);
		speedMenu.addItem(speedItem100);

		speedButton = new ToolbarButton("5");
		speedButton.setMenu(speedMenu);

		countDownLabel = new Label("<" + speed + ">");

		followButton = new ToolbarButton(mapToolbarConstants.followButton());
		followButton.setEnableToggle(true);

		maxButton = new ToolbarButton();
		maxButton.setIcon("/images/MapToolbar/min.gif");

		numberWindow = new NumberWindow();
	}

	/**
	 * Add listener for the comoponent of the toolbar
	 */
	private void addListener() {
		playButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				setProgressStatus(ProgressStatus.PLAY);
			}
		});

		pauseButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				setProgressStatus(ProgressStatus.PAUSE);
			}
		});
		stopButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				setProgressStatus(ProgressStatus.STOP);
			}
		});
		replayButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				setProgressStatus(ProgressStatus.REPLAY);
			}
		});
		ratioCustomItem.addListener(new CheckItemListenerAdapter() {

			@Override
			public void onClick(BaseItem item, EventObject e) {
				numberWindow.show();
			}
		});
		ratioItem_1_1.addListener(new CheckItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				WayPointPanel table = WayPointPanel.getInstance();
				if (table.getRatio() != 1) {
					table.setRatio(1);
				}
			}
		});
		ratioItem_1_2.addListener(new CheckItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				WayPointPanel table = WayPointPanel.getInstance();
				if (table.getRatio() != 2) {
					table.setRatio(2);
				}
			}
		});
		ratioItem_1_4.addListener(new CheckItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				WayPointPanel table = WayPointPanel.getInstance();
				if (table.getRatio() != 4) {
					table.setRatio(4);
				}
			}
		});
		ratioItem_1_8.addListener(new CheckItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				WayPointPanel table = WayPointPanel.getInstance();
				if (table.getRatio() != 8) {
					table.setRatio(8);
				}
			}
		});
		speedMenu.addListener(new MenuListenerAdapter() {
			@Override
			public void onItemClick(BaseItem item, EventObject e) {
				speedButton.setText(item.getTitle());
				speed = Integer.parseInt(item.getTitle());
			}
		});
		followButton.addListener(new ButtonListenerAdapter() {

			@Override
			public void onToggle(Button button, boolean pressed) {
				WayPointPanel.getInstance().getTrackPointOverlay()
						.setFollowLastWaypointMode(pressed);
			}
		});
		countDownTask = new Task(2) {
			private boolean isUpdateMultiTrackDone = true;
			private ArrayList<CTracked> trackedUpdate = new ArrayList<CTracked>();
			private ArrayList<CWaypoint> newWayPoint = new ArrayList<CWaypoint>();

			@Override
			public void execute() {

				String strcountDown = countDownLabel.getElement()
						.getInnerText();
				strcountDown = strcountDown.substring(1,
						strcountDown.length() - 1);
				int currentTime = Integer.parseInt(strcountDown);
				currentTime = (currentTime + speed - 1) % (speed);
				countDownLabel.getElement().setInnerText(
						"<" + currentTime + ">");

				// update track in mode multi_track
				demoMultiTracked(currentTime);

				if (currentTime == 0) {
					switch (curTrackedViewMode) {
					case MULTI_TRACK:
						updateMultiTrack();
						break;
					case TRACK:
						updateTrackedDetailPanel();
						break;
					case DEMO:
						excuteDemo();
						break;
					}
				}
			}

			private CWaypoint calculateMiddleWaypoint(CWaypoint point1,
					CWaypoint point2, int currentTime) {
				int lngFactor = 1, latFactor = 1;

				if (point1.getLat() > point2.getLat()) {
					latFactor = -1;
				}
				if (point1.getLng() > point2.getLng()) {
					lngFactor = -1;
				}

				double ratio = 1.0 - (currentTime) * 1.0 / speed;

				CWaypoint pointMiddle = new CWaypoint(point1.getLat()
						+ Math.abs(point1.getLat() - point2.getLat())
						* latFactor * ratio, point1.getLng()
						+ Math.abs(point1.getLng() - point2.getLng())
						* lngFactor * ratio, point2.getSpeed(), point2
						.getTrackID());

				return pointMiddle;
			}

			ArrayList<CTracked> removeTrackedUpdate = new ArrayList<CTracked>();

			private void demoMultiTracked(int currentTime) {
				try {
					if (curTrackedViewMode != TrackedViewMode.MULTI_TRACK
							&& trackedUpdate.size() > 0) {
						trackedUpdate.clear();
						newWayPoint.clear();
					} else if (trackedUpdate.size() > 0) {

						for (CTracked tracked : trackedUpdate) {
							if (UserSettings.currentTrackOverlay
									.containsKey(tracked.getUsername())) {
								TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
										.get(tracked.getUsername());

								if (currentTime == speed - 1) {
									// bat dau chay

									CWaypoint point1 = trackOverlay
											.getWaypoint(0);
									if (point1 == null) {
										if (trackedUpdate.contains(tracked)) {
											removeTrackedUpdate.add(tracked);
											continue;
										}
									}
									CWaypoint point2 = newWayPoint
											.get(trackedUpdate.indexOf(tracked));
									CWaypoint pointMiddle = calculateMiddleWaypoint(
											point1, point2, currentTime);

									trackOverlay.addWayPoint(pointMiddle, 0);
									if (trackOverlay.getVertexCount() > 2) {
										trackOverlay.removeWayPoint(2);
									}
									// while (trackOverlay.getVertexCount() > 2)
									// {
									// trackOverlay.removeWayPoint(2);
									// }

								} else if (currentTime == 0) {
									// ket thuc chay
									trackOverlay.addWayPoint(
											newWayPoint.get(trackedUpdate
													.indexOf(tracked)), 0);
									removeTrackedUpdate.add(tracked);
									if (trackOverlay.getVertexCount() >= 2) {
										trackOverlay.removeWayPoint(1);
									}
								} else {
									// dang chay
									CWaypoint point1 = trackOverlay
											.getWaypoint(0);
									if (point1 == null) {
										if (trackedUpdate.contains(tracked)) {
											removeTrackedUpdate.add(tracked);
											continue;
										}
									}
									CWaypoint point2 = newWayPoint
											.get(trackedUpdate.indexOf(tracked));
									CWaypoint pointMiddle = calculateMiddleWaypoint(
											point1, point2, currentTime);
									trackOverlay.addWayPoint(pointMiddle, 0);
									if (trackOverlay.getVertexCount() > 2) {
										trackOverlay.removeWayPoint(1);
									}
								}
							}
						}

						if (removeTrackedUpdate.size() > 0) {
							for (CTracked tracked : removeTrackedUpdate) {
								newWayPoint.remove(trackedUpdate
										.indexOf(tracked));
								trackedUpdate.remove(tracked);
							}
							removeTrackedUpdate.clear();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// show in map
				if (MapPanel.removeFromMapList.size() > 0) {
					Object[] trackedIDs = MapPanel.removeFromMapList.toArray();
					for (Object trackedID : trackedIDs) {
						Long userName = (Long) trackedID;
						// CTracked tracked = UserSettings.ctrackedList
						// .get(userName);
						// boolean isShowInMap = tracked.getShowInMap() == null
						// ? false
						// : tracked.getShowInMap();
						TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
								.remove(userName);
						trackOverlay.removeOverlay();

						if (trackedUpdate.contains(userName)) {
							newWayPoint.remove(trackedUpdate
									.indexOf(UserSettings.ctrackedList
											.get(userName)));
							trackedUpdate.remove(UserSettings.ctrackedList
									.get(userName));
						}

						MapPanel.removeFromMapList.remove(userName);
					}
				}
			}

			private void updateMultiTrack() {
				if (UserSettings.ctrackedList.size() != 0
						&& isUpdateMultiTrackDone) {

					isUpdateMultiTrackDone = false;
					for (final CTracked tracked : UserSettings.ctrackedList
							.values()) {

						if (!UserSettings.currentTrackOverlay
								.containsKey(tracked.getUsername())) {
							// handler new device
							boolean showInMap = tracked.getShowInMap() == null ? false
									: tracked.getShowInMap();

							if (showInMap) {
								boolean isFollow = MapPanel
										.isTrackedFollow(tracked.getUsername());
								MapPanel.addToMap(tracked, isFollow, tracked
										.getUsername(), null);
							}
						} else {
							DatabaseService.Util
									.getInstance()
									.getLastestWaypointTracked(
											tracked.getUsername(),
											new AsyncCallback<ServiceResult<CWaypoint>>() {

												@Override
												public void onFailure(
														Throwable caught) {
													caught.printStackTrace();
													isUpdateMultiTrackDone = true;
												}

												@Override
												public void onSuccess(
														ServiceResult<CWaypoint> result) {
													CWaypoint point = result
															.getResult();
													// TrackPointOverlay
													TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
															.get(tracked
																	.getUsername());
													if (trackOverlay
															.getWaypoint(0) == null) {
														isUpdateMultiTrackDone = true;
														return;
													}
													if (point != null
															&& trackOverlay != null
															&& trackOverlay
																	.getVertexCount() > 0) {
														// check new point is
														// equal
														// to old point or not

														if (!trackOverlay
																.getWaypoint(0)
																.equals(point)) {
															WayPointOverlay waypoint = trackOverlay
																	.getWaypointOverlay(0);
															String infoContent = waypoint
																	.getInfoContent();
															waypoint
																	.setInfoWindowContent(infoContent
																			.replaceFirst(
																					mapToolbarConstants
																							.infoStatus()
																							+ "<br/>",
																					""));

															trackedUpdate
																	.add(tracked);
															newWayPoint
																	.add(point);
														} else {
															WayPointOverlay waypoint = trackOverlay
																	.getWaypointOverlay(0);
															String infoContent = waypoint
																	.getInfoContent();

															if (waypoint == null) {
																return;
															}
															if (infoContent
																	.indexOf(mapToolbarConstants
																			.infoStatus()) == -1) {
																if (trackOverlay
																		.getVertexCount() > 1) {
																	trackOverlay
																			.removeOverlay();
																	UserSettings.currentTrackOverlay
																			.remove(tracked
																					.getUsername());

																	TrackPointOverlay newTrackOverlay = new TrackPointOverlay(
																			new CWaypoint[] { point },
																			tracked);

																	newTrackOverlay
																			.setFollowLastWaypointMode(trackOverlay
																					.isFollowLastWaypointMode());

																	UserSettings.currentTrackOverlay
																			.put(
																					tracked
																							.getUsername(),
																					newTrackOverlay);
																	newTrackOverlay
																			.addTrackPointOverlayToMap();
																	waypoint = newTrackOverlay
																			.getWaypointOverlay(0);
																}
																waypoint
																		.appendIntoInfoWindowContent(mapToolbarConstants
																				.infoStatus()
																				+ "<br/>");
															}
														}
													}
													isUpdateMultiTrackDone = true;
												}
											});
						}
					}
				}
			}

			private void updateTrackedDetailPanel() {
				if (WayPointPanel.isUpdateDone) {
					WayPointPanel.isUpdateDone = false;
					WayPointPanel.getInstance().updateWayPointList();
				}
				if (TrackedDetailPanel.isUpdateDone) {
					TrackedDetailPanel.isUpdateDone = false;
					TrackedDetailPanel.getInstance().updateTrackList();
				}
			}

			private void excuteDemo() {
				if (demoCount == demoWayPointList.size()) {
					return;
				}
				if (demoCount == 0 && demoWayPointList.size() > 0) {
					CWaypoint point = demoWayPointList.get(demoWayPointList
							.size() - 1);
					WayPointPanel.getInstance().setWayPoints(
							new CWaypoint[] { point });
					++demoCount;
					return;
				}

				CWaypoint point = demoWayPointList.get(demoWayPointList.size()
						- 1 - demoCount++);
				WayPointPanel.getInstance().addRecord(point, 0);
			}
		};
	}

	/**
	 * Set progress status for the toolbar
	 * 
	 * @param status
	 */
	public void setProgressStatus(ProgressStatus status) {
		preProgressStatus = curProgressStatus;
		curProgressStatus = status;
		switch (status) {
		case PLAY:
			playButton.setDisabled(true);
			pauseButton.setDisabled(false);
			stopButton.setDisabled(false);
			if (curTrackedViewMode != TrackedViewMode.MULTI_TRACK
					&& !WayPointPanel.getInstance().getCurrentTrackID().equals(
							"")) {
				replayButton.setDisabled(false);
			}
			if (preProgressStatus == ProgressStatus.REPLAY) {
				demoWayPointList.clear();
				WayPointPanel.getInstance().removeAllWayPoints();

				if (curTrackedViewMode == TrackedViewMode.DEMO) {
					if (nextTrackedViewMode != TrackedViewMode.MULTI_TRACK) {
						setTrackedViewMode(TrackedViewMode.TRACK);
						WayPointPanel.getInstance()
								.loadWayPointsByTrack(
										WayPointPanel.getInstance()
												.getCurrentTrackID());
					}
				}
			}

			countDownTask.notFinish();
			if (!UserSettings.timerTask.getTaskList().contains(countDownTask)) {
				UserSettings.timerTask.getTaskList().add(countDownTask);
			}
			break;
		case PAUSE:
			countDownTask.finish();
			playButton.setDisabled(false);
			pauseButton.setDisabled(true);
			stopButton.setDisabled(false);
			if (curTrackedViewMode != TrackedViewMode.MULTI_TRACK
					&& !WayPointPanel.getInstance().getCurrentTrackID().equals(
							"")) {
				replayButton.setDisabled(false);
			}
			break;
		case STOP:
			demoCount = 0;
			countDownTask.finish();
			countDownLabel.setText("<" + speed + ">");
			playButton.setDisabled(false);
			pauseButton.setDisabled(true);
			stopButton.setDisabled(true);
			if (curTrackedViewMode != TrackedViewMode.MULTI_TRACK
					&& !WayPointPanel.getInstance().getCurrentTrackID().equals(
							"")) {
				replayButton.setDisabled(false);
			}
			if (curTrackedViewMode == TrackedViewMode.DEMO) {
				MapPanel.clearOverlays();
			}
			break;
		case REPLAY:
			playButton.setDisabled(false);
			pauseButton.setDisabled(false);
			stopButton.setDisabled(false);
			replayButton.setDisabled(true);

			if (curTrackedViewMode == TrackedViewMode.TRACK
					|| preProgressStatus == ProgressStatus.STOP) {
				TrackPointOverlay trackPointOverlay = WayPointPanel
						.getInstance().getTrackPointOverlay();
				if (trackPointOverlay != null) {
					demoWayPointList = trackPointOverlay.getWaypoints();
					WayPointPanel.getInstance().removeAllWayPoints();
				}
				demoCount = 0;
			}
			setTrackedViewMode(TrackedViewMode.DEMO);
			countDownTask.notFinish();
			if (!UserSettings.timerTask.getTaskList().contains(countDownTask)) {
				UserSettings.timerTask.getTaskList().add(countDownTask);
			}
			break;
		}
	}

	/**
	 * Return the current progress status
	 * 
	 * @return
	 */
	public ProgressStatus getProgressStatus() {
		return curProgressStatus;
	}

	/**
	 * Set Replay button visible
	 * 
	 * @param disabled
	 */
	public void setReplayButtonDisable(boolean disabled) {
		replayButton.setDisabled(disabled);
	}

	/**
	 * Return the CountDountTask
	 * 
	 * @return
	 */
	public Task getCountDownTask() {
		return countDownTask;
	}

	/**
	 * Return the static instance of the Toolbar
	 * 
	 * @return
	 */
	public static MapToolbar getInstance() {
		return mapToolbar;
	}

	/**
	 * Return the current TrackedViewMode
	 * 
	 * @return
	 */
	public TrackedViewMode getTrackedViewMode() {
		return curTrackedViewMode;
	}

	/**
	 * Set TrackedViewMode
	 * 
	 * @param mode
	 */
	public void setTrackedViewMode(TrackedViewMode mode) {

		nextTrackedViewMode = mode;
		switch (mode) {
		case MULTI_TRACK:
			MapPanel.clearOverlays();
			followButton.setVisible(false);
			ratioButton.setVisible(false);
			replayButton.setVisible(false);
			if (curTrackedViewMode == TrackedViewMode.DEMO) {
				setProgressStatus(ProgressStatus.PLAY);
			}
			break;
		case TRACK:
			MapPanel.clearOverlays();
			demoWayPointList.clear();
			followButton.setVisible(true);
			ratioButton.setVisible(true);
			replayButton.setVisible(true);
			replayButton.setDisabled(true);
			break;
		case DEMO:
			if (curTrackedViewMode != TrackedViewMode.DEMO) {
				MapPanel.clearOverlays();
			}
			break;
		}
		curTrackedViewMode = mode;
	}
}

/**
 * A window enable user enter only number
 * 
 * @author Le Trong Nghia
 * 
 */
class NumberWindow extends Window {
	private int value;
	private TextField tfNumberField;
	private Button btOk;
	private Button btCancel;
	private MapToolbarConstants mapToolbarConstants = GWT
			.create(MapToolbarConstants.class);

	/**
	 * Constructor
	 */
	public NumberWindow() {
		setModal(true);
		setTitle(mapToolbarConstants.enterRatioLabel());
		setWidth(220);

		FormPanel formPanel = new FormPanel();
		tfNumberField = new TextField("1 ");
		tfNumberField.setWidth(120);

		btOk = new Button(mapToolbarConstants.okButton());
		btCancel = new Button(mapToolbarConstants.cancelButton());

		FieldSet fieldSet = new FieldSet();
		fieldSet.add(tfNumberField);
		fieldSet.setLabelWidth(30);

		formPanel.add(fieldSet);

		Panel southPanel = new Panel();
		southPanel.setLayout(new HorizontalLayout(5));
		southPanel.addButton(btOk);
		southPanel.addButton(btCancel);
		southPanel.setButtonAlign(Position.CENTER);

		add(formPanel);
		add(southPanel, new BorderLayoutData(RegionPosition.SOUTH));
		setButtonAlign(Position.CENTER);
		setAutoHeight(true);

		addListener();
	}

	/**
	 * Add listener for the component
	 */
	private void addListener() {
		tfNumberField.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					checkValue();
				}
			}
		});

		btOk.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				checkValue();
			}
		});

		btCancel.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				hide();
			}
		});
	}

	/**
	 * Check if the value user input is a number or not
	 */
	private void checkValue() {
		try {
			value = Integer.parseInt(tfNumberField.getValueAsString());
			if (value < 1) {
				throw new NumberFormatException();
			}
			hide();
			WayPointPanel.getInstance().setRatio(value);
		} catch (NumberFormatException ex) {
			MessageBox.alert(mapToolbarConstants.inputNumberAlertMessage());
		}
	}

	@Override
	public void show() {
		tfNumberField.setValue("");
		btCancel.focus();
		super.show();
	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void collapse() {
		super.collapse();
	}

	/**
	 * Get the number user input
	 * 
	 * @return
	 */
	public int getValue() {
		return value;
	}
}
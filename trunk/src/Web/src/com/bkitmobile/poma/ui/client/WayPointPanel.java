package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;
import java.util.Date;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.database.client.entity.CWaypoint;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.localization.client.TrackedPanelConstants;
import com.bkitmobile.poma.localization.client.WayPointTablePanelConstants;
import com.bkitmobile.poma.ui.client.MapToolbar.ProgressStatus;
import com.bkitmobile.poma.ui.client.map.TrackPointOverlay;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

public class WayPointPanel extends TablePanel {

	private static WayPointPanel wayPointTablePanel;
	public static boolean isUpdateDone = true;

	private static TrackedPanelConstants constants = GWT
			.create(TrackedPanelConstants.class);
	private static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat(constants.datetimeformat());

	private TrackPointOverlay trackPointOverlay;

	private String currentTrackID = "";
	private static WayPointTablePanelConstants wayPointTablePanelConstants = GWT
			.create(WayPointTablePanelConstants.class);

	private int ratioCount;
	private int ratio = 1;
	private CTracked tracked;

	/**
	 * Create WayPointTablePanel with empty content
	 * 
	 * @param mapWidget
	 */
	public WayPointPanel() {
		super(new int[] { TablePanel.FIELD_STRING },
				new String[] { wayPointTablePanelConstants.time() });
		wayPointTablePanel = this;
		setTitle(wayPointTablePanelConstants.panelTitleNormal());

		setListener();
	}

	/**
	 * Create WayPointTablePanel with an array of CWaypoint
	 */
	public WayPointPanel(CWaypoint[] wayPoints) {
		super(new int[] { TablePanel.FIELD_STRING },
				new String[] { wayPointTablePanelConstants.time() },
				getDataForGrid(wayPoints));
		wayPointTablePanel = this;

		setTitle(wayPointTablePanelConstants.panelTitleNormal());

		setListener();
		setWayPoints(wayPoints);
	}

	/**
	 * Set current Device is tracked
	 * 
	 * @param tracked
	 */
	public void setTracked(CTracked tracked) {
		this.tracked = tracked;
	}

	/**
	 * Add an CWaypoint into this WayPointTablePanel
	 * 
	 * @param wayPoint
	 * @param index
	 */
	public void addRecord(CWaypoint wayPoint, final int index) {
		ratioCount = (ratioCount + 1) % ratio;
		if (ratioCount == 0) {
			super.addRecord(getDataForRow(wayPoint), index);
			if (trackPointOverlay == null) {
				trackPointOverlay = new TrackPointOverlay(
						new CWaypoint[] { wayPoint }, tracked);
			} else {
				trackPointOverlay.addWayPoint(wayPoint, index);
			}
		}
	}

	/**
	 * Remove the specific record
	 */
	public void removeRecord(int index) {
		super.removeRecord(index);
		trackPointOverlay.removeWayPoint(index);
	}

	/**
	 * Replace the content with an arrray of CWaypoint
	 */
	public void setWayPoints(CWaypoint[] waypoints) {
		// prepare
		CWaypoint[] tempPoints = new CWaypoint[waypoints.length / ratio];
		for (int i = 0; i < tempPoints.length; i++) {
			tempPoints[i] = waypoints[i * ratio];
		}
		waypoints = tempPoints;

		ratioCount = waypoints.length % ratio;

		// add
		boolean isFollow = false;
		if (trackPointOverlay != null) {
			isFollow = trackPointOverlay.isFollowLastWaypointMode();
		}

		removeAllWayPoints();

		trackPointOverlay = new TrackPointOverlay(waypoints, tracked
				.getIconPath(), "", tracked);

		trackPointOverlay.setFollowLastWaypointMode(isFollow);

		trackPointOverlay.addTrackPointOverlayToMap();
		setData(getDataForGrid(trackPointOverlay));
	}

	/**
	 * Set the Listener for the WayPointTablePanel
	 */
	private void setListener() {
		addGridRowListener(new GridRowListenerAdapter() {

			@Override
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				if (!trackPointOverlay.isVisible()) {
					setVisible(true);
				}

				int currentRowIndex = rowIndex + (getCurrentPage() - 1)
						* getPageSize();
				trackPointOverlay.getWaypointOverlay(currentRowIndex)
						.showInfoWindow();
				MapPanel.getMapWidgetInstance().setCenter(
						trackPointOverlay.getWaypoint(currentRowIndex)
								.getLatLng());

			}
		});

	}

	/**
	 * Get a list of CWaypoint from server with a given trackID
	 * 
	 * @param trackID
	 */
	public void loadWayPointsByTrack(final String trackID) {
		if (trackID.equals("")) {
			return;
		}
		BkitPoma.startLoading("Loading...");
		currentTrackID = trackID;
		MapToolbar toolbar = MapPanel.getInstance().getToolbar();
		if (toolbar.getProgressStatus() == ProgressStatus.REPLAY) {
			toolbar.setProgressStatus(ProgressStatus.PLAY);
			return;
		}

		DatabaseService.Util.getInstance().getWaypointsByTrack(
				Long.parseLong(trackID),
				new AsyncCallback<ServiceResult<ArrayList<CWaypoint>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						currentTrackID = "";
						BkitPoma.stopLoading();
						setTitle(wayPointTablePanelConstants.panelTitleNormal());
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CWaypoint>> result) {
						if (result.isOK()) {
							ArrayList<CWaypoint> list = result.getResult();
							CWaypoint[] waypoints = new CWaypoint[list.size()];

							for (int i = 0; i < waypoints.length; i++) {
								waypoints[i] = list.get(i);
							}
							setWayPoints(waypoints);
							UserSettings.lastWayPointUpdateTime = System
									.currentTimeMillis();

							MapPanel.setMaxLatLngBound();

							MapToolbar.getInstance().setReplayButtonDisable(
									false);
							setTitle(wayPointTablePanelConstants
									.panelTitleWithTrackID()
									+ currentTrackID);
						} else {
							MessageBox.alert(result.getMessage());
							setTitle(wayPointTablePanelConstants
									.panelTitleNormal());
						}
						BkitPoma.stopLoading();
					}
				});
	}

	/**
	 * Set the current device ID is tracked
	 * 
	 * @param trackID
	 */
	public void setCurrentTrackID(String trackID) {
		currentTrackID = trackID;
		if (currentTrackID == "")
			setTitle(wayPointTablePanelConstants.panelTitleNormal());
	}

	/**
	 * Get the current device ID is tracked
	 * 
	 * @return
	 */
	public String getCurrentTrackID() {
		return currentTrackID;
	}

	/**
	 * Remove all record
	 */
	public void removeAllWayPoints() {

		try {
			removeAllRecords();
			if (trackPointOverlay != null) {
				trackPointOverlay.removeOverlay();
			}

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	/**
	 * Get an array of String to add into WayPointTablePanel with a given
	 * CWaypoint
	 * 
	 * @param wayPoint
	 * @return
	 */
	private static String[] getDataForRow(CWaypoint wayPoint) {
		String data[] = new String[1];
		data[0] = dateFormat.format(wayPoint.getTime());
		return data;
	}

	/**
	 * Get a matrix of String to replace the content of WayPointTablePanel with
	 * a given TrackPointOverlay
	 * 
	 * @param trackPointOverlay
	 * @return
	 */
	private static String[][] getDataForGrid(TrackPointOverlay trackPointOverlay) {
		String data[][] = new String[trackPointOverlay.getVertexCount()][1];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = trackPointOverlay.getWaypoint(i).getTime() == null ? ""
					: dateFormat.format(trackPointOverlay.getWaypoint(i)
							.getTime());
		}
		return data;
	}

	/**
	 * Get a matrix of String to replace the content of WayPointTablePanel with
	 * a given matrix of CWaypoint
	 * 
	 * @param wayPoints
	 * @return
	 */
	private static String[][] getDataForGrid(CWaypoint[] wayPoints) {
		String data[][] = new String[wayPoints.length][1];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = wayPoints[i].getTime().toString();
		}
		return data;
	}

	/**
	 * Get WayPointTablePanel 's instance
	 * 
	 * @return
	 */
	public static WayPointPanel getInstance() {
		return wayPointTablePanel;
	}

	/**
	 * Update WayPointTablePanel with some new current device ID 's CWaypoint
	 * from server
	 */
	public void updateWayPointList() {
		if (!currentTrackID.equals("")) {

			long trackID = Long.parseLong(currentTrackID);

			DatabaseService.Util.getInstance().getWaypointByTrack(trackID,
					new Date(UserSettings.lastWayPointUpdateTime),
					new AsyncCallback<ServiceResult<ArrayList<CWaypoint>>>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
							updateDone();
						}

						@Override
						public void onSuccess(
								ServiceResult<ArrayList<CWaypoint>> result) {

							if (result.isOK()) {
								ArrayList<CWaypoint> listWayPoint = result
										.getResult();

								for (int i = 0; i < listWayPoint.size(); i++) {
									ratioCount = (ratioCount + 1) % ratio;
									if (ratioCount == 0) {
										addRecord(listWayPoint.get(i), 0);
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
		UserSettings.lastWayPointUpdateTime = System.currentTimeMillis();
	}

	/**
	 * Get trackPoint overlay that created by CWaypoint held by WayPointPanel
	 * 
	 * @return
	 */
	public TrackPointOverlay getTrackPointOverlay() {
		return trackPointOverlay;
	}

	/**
	 * Set the ratio for the WayPointPanel
	 * 
	 * @param ratio
	 */
	public void setRatio(final int ratio) {
		this.ratio = ratio;

		loadWayPointsByTrack(currentTrackID);
	}

	/**
	 * Get the current ratio of the map
	 * 
	 * @return
	 */
	public int getRatio() {
		return ratio;
	}
}
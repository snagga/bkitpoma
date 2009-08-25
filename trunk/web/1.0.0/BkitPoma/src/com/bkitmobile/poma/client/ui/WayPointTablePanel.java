package com.bkitmobile.poma.client.ui;

import java.util.ArrayList;

import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CWaypoint;
import com.bkitmobile.poma.client.ui.map.TrackPointOverlay;
import com.bkitmobile.poma.client.ui.map.WayPointOverlay;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

public class WayPointTablePanel extends TablePanel {

	private TrackPointOverlay trackPointOverlay;
	private MapWidget mapWidget;

	private Timer insertSpeedTimer;
	private int previousViewPoint;

	public WayPointTablePanel(MapWidget mapWidget) {
		super(new int[] { TablePanel.FIELD_STRING, TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING, TablePanel.FIELD_STRING },
				new String[] { "Time", "Speed", "Longitude", "Latitude" });
		setTitle("WayPoint Panel");
		this.mapWidget = mapWidget;
		setListener();
	}

	public WayPointTablePanel(TrackPointOverlay trackPointOverlay,
			MapWidget mapWidget) {
		super(new int[] { TablePanel.FIELD_STRING, TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING, TablePanel.FIELD_STRING },
				new String[] { "Time", "Speed", "Longitude", "Latitude" },
				getDataForGrid(trackPointOverlay));
		setTitle("WayPoint Panel");
		// TODO Auto-generated constructor stub
		setTitle("Track ID : " + trackPointOverlay.getWaypoint(0).getTrackID()
				+ "");
		this.trackPointOverlay = trackPointOverlay;
		this.mapWidget = mapWidget;
		init();
		setListener();

	}

	public WayPointTablePanel(CWaypoint[] wayPoints, MapWidget mapWidget) {
		super(new int[] { TablePanel.FIELD_STRING, TablePanel.FIELD_STRING,
				TablePanel.FIELD_STRING, TablePanel.FIELD_STRING },
				new String[] { "Time", "Speed", "Longitude", "Latitude" },
				getDataForGrid(wayPoints));
		setTitle("WayPoint Panel");
		// TODO Auto-generated constructor stub
		setTitle("Track ID : " + trackPointOverlay.getWaypoint(0).getTrackID()
				+ "");
		trackPointOverlay = new TrackPointOverlay(wayPoints, mapWidget);

		this.mapWidget = mapWidget;
		init();
		setListener();

	}

	public void addRecord(CWaypoint wayPoint, final int index) {
		super.addRecord(getDataForRow(wayPoint), index);
		trackPointOverlay.addWayPoint(wayPoint, index, mapWidget);
		trackPointOverlay.getWaypointOverlay(previousViewPoint).setVisible(
				false);
		previousViewPoint = index;

		new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (trackPointOverlay.getWaypointOverlay(index)
						.isInfoWindowContentNull()) {
					this.schedule(1000);
				} else {
					WayPointOverlay wayPointOverlay = trackPointOverlay
							.getWaypointOverlay(index);
					wayPointOverlay
							.appendIntoInfoWindowContent("<b>Speed :</b>"
									+ wayPointOverlay.getWayPoint().getSpeed());
					this.cancel();
				}
			}
		}.run();
	}

	public void removeRecord(int index) {
		super.removeRecord(index);
		trackPointOverlay.removeWayPoint(index);
		if (getRecordsCount() == 1) {
			super.removeRecord(0);
			previousViewPoint = -1;
		} else {
			if (index == previousViewPoint) {
				previousViewPoint = trackPointOverlay.getVertexCount() - 1;
				trackPointOverlay.getWaypointOverlay(previousViewPoint)
						.setVisible(true);
			}
		}
	}

	private void init() {
		mapWidget.addOverlay(trackPointOverlay);
		trackPointOverlay.addTrackPointOverlayToMap();

		previousViewPoint = trackPointOverlay.getVertexCount() - 1;
		for (int i = 0; i < trackPointOverlay.getVertexCount() - 1; i++) {
			trackPointOverlay.getWaypointOverlay(i).setVisible(false);
		}
	}

	public void setTrackPointOverlay(TrackPointOverlay trackPointOverlay) {
		this.trackPointOverlay = trackPointOverlay;
		setData(getDataForGrid(trackPointOverlay));
		trackPointOverlay.addTrackPointOverlayToMap();

	}

	public void setWayPoints(CWaypoint[] waypoints) {
		trackPointOverlay = new TrackPointOverlay(waypoints, mapWidget);
		setData(getDataForGrid(trackPointOverlay));
		trackPointOverlay.addTrackPointOverlayToMap();
		trackPointOverlay.addTrackPointOverlayToMap();
	}

	private void setListener() {
		addGridRowListener(new GridRowListenerAdapter() {

			@Override
			public void onRowClick(GridPanel grid, int rowIndex, EventObject e) {
				// TODO Auto-generated method stub
				if (!trackPointOverlay.isVisible()) {
					setVisible(true);
				}
				
				trackPointOverlay.getWaypointOverlay(previousViewPoint)
						.setVisible(false);
				previousViewPoint = rowIndex + (getCurrentPage() - 1)
						* getPageSize();
				trackPointOverlay.getWaypointOverlay(previousViewPoint)
						.showInfoWindow(mapWidget);
				trackPointOverlay.getWaypointOverlay(previousViewPoint)
						.setVisible(true);
			}
		});

		if (getRecordsCount() > 0) {
			insertSpeedTimer = new Timer() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					if (trackPointOverlay.getWaypointOverlay(
							trackPointOverlay.getVertexCount() - 1)
							.isInfoWindowContentNull()) {
						insertSpeedTimer.schedule(1000);
					} else {
						for (int i = 0; i < trackPointOverlay.getVertexCount(); i++) {
							WayPointOverlay wayPointOverlay = trackPointOverlay
									.getWaypointOverlay(i);
							wayPointOverlay
									.appendIntoInfoWindowContent("<b>Speed :</b>"
											+ wayPointOverlay.getWayPoint()
													.getSpeed());
						}
						insertSpeedTimer.cancel();
					}
				}
			};
			insertSpeedTimer.run();
		}
	}

	public void getWayPointsByTrack(String trackID) {
		removeAllOverlay();
		// TODO database async
		DatabaseService.Util.getInstance().getWaypointsByTrack(
				Long.parseLong(trackID),
				new AsyncCallback<ServiceResult<ArrayList<CWaypoint>>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("fail to load way point");
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CWaypoint>> result) {
						// TODO Auto-generated method stub
						ArrayList<CWaypoint> list = result.getResult();
						CWaypoint[] waypoints = new CWaypoint[list.size()];

						for (int i = 0; i < waypoints.length; i++) {
							waypoints[i] = list.get(i);
						}

						setWayPoints(waypoints);
					}

				});
	}

	public void removeAllOverlay() {
		if (trackPointOverlay != null) {
			mapWidget.removeOverlay(trackPointOverlay);
			for (int i = 0; i < trackPointOverlay.getVertexCount(); i++) {
				mapWidget
						.removeOverlay(trackPointOverlay.getWaypointOverlay(i));
			}
		}

	}

	private static String[] getDataForRow(CWaypoint wayPoint) {
		String data[] = new String[4];
		data[0] = wayPoint.getTime().toString();
		data[1] = wayPoint.getSpeed().toString();
		data[2] = wayPoint.getLat().toString();
		data[3] = wayPoint.getLng().toString();
		return data;
	}

	private static String[][] getDataForGrid(TrackPointOverlay trackPointOverlay) {
		String data[][] = new String[trackPointOverlay.getVertexCount()][4];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = trackPointOverlay.getWaypoint(i).getTime().toString();
			data[i][1] = trackPointOverlay.getWaypoint(i).getSpeed().toString();
			data[i][2] = trackPointOverlay.getWaypoint(i).getLat().toString();
			data[i][3] = trackPointOverlay.getWaypoint(i).getLng().toString();
		}
		return data;
	}

	private static String[][] getDataForGrid(CWaypoint[] wayPoints) {
		String data[][] = new String[wayPoints.length][4];
		for (int i = 0; i < data.length; i++) {
			data[i][0] = wayPoints[i].getTime().toString();
			data[i][1] = wayPoints[i].getSpeed().toString();
			data[i][2] = wayPoints[i].getLat().toString();
			data[i][3] = wayPoints[i].getLng().toString();
		}
		return data;
	}
}

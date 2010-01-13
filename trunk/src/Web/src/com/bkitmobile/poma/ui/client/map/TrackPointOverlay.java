package com.bkitmobile.poma.ui.client.map;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.util.client.Task;
import com.bkitmobile.poma.ui.client.MapPanel;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.PolylineClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polyline;

public class TrackPointOverlay extends Polyline {

	// public static long trackedIDFollowed = 0;

	private ArrayList<WayPointOverlay> updateWayPointList = new ArrayList<WayPointOverlay>();

	private static TrackPointOverlay trackPointOverlay;

	private ArrayList<WayPointOverlay> wayPoints = new ArrayList<WayPointOverlay>();
	private boolean isChoosen = false;
	private static PolyStyleOptions TRACKPOINT_STYLE_NORMAL = PolyStyleOptions
			.newInstance("#ffa600");
	private static PolyStyleOptions TRACKPOINT_STYLE_CHOOSEN = PolyStyleOptions
			.newInstance("#ff0000");

	private boolean followLastWayPointMode = false;

	WaypointClickListener clickListener = new WaypointClickListener();

	CTracked tracked;

	/**
	 * Create a track point overlay
	 * 
	 * @param wayPoints
	 *            - an array of CWaypoint
	 * @param foregroundURL
	 *            - an array of Image URL
	 * @param shadowURL
	 *            - an array of shadow URL
	 */
	public TrackPointOverlay(CWaypoint[] wayPoints, String[] foregroundURL,
			String[] shadowURL, CTracked tracked) {
		super(getLatLngArray(wayPoints));
		this.tracked = tracked;

		trackPointOverlay = this;

		if (wayPoints.length == foregroundURL.length
				&& wayPoints.length == shadowURL.length) {
			for (int i = 0; i < wayPoints.length; i++) {
				WayPointOverlay overlay = new WayPointOverlay(wayPoints[i],
						foregroundURL[i], shadowURL[i], tracked);

				updateWayPointList.add(overlay);

				overlay.addMarkerClickHandler(clickListener);
				this.wayPoints.add(overlay);
			}
		}

		setListener();
		setStrokeStyle(TRACKPOINT_STYLE_NORMAL);
	}

	/**
	 * 
	 * Create a track point overlay
	 * 
	 * @param wayPoints
	 *            - an array of CWaypoint
	 * @param foregroundURL
	 *            - Image URL
	 * @param shadowURL
	 *            - Shadow URL
	 */
	public TrackPointOverlay(CWaypoint[] wayPoints, String foregroundURL,
			String backgroundURL, CTracked tracked) {
		super(getLatLngArray(wayPoints));
		this.tracked = tracked;

		trackPointOverlay = this;

		for (int i = 0; i < wayPoints.length; i++) {
			WayPointOverlay overlay = new WayPointOverlay(wayPoints[i],
					foregroundURL, backgroundURL, tracked);
			overlay.addMarkerClickHandler(clickListener);

			updateWayPointList.add(overlay);

			this.wayPoints.add(overlay);
		}

		setListener();

		setStrokeStyle(TRACKPOINT_STYLE_NORMAL);
	}

	/**
	 * 
	 * Create a track point overlay
	 * 
	 * @param wayPoints
	 *            - an array of CWaypoint
	 */
	public TrackPointOverlay(CWaypoint[] wayPoints, CTracked tracked) {
		super(getLatLngArray(wayPoints));
		this.tracked = tracked;
		trackPointOverlay = this;

		for (int i = 0; i < wayPoints.length; i++) {
			WayPointOverlay overlay = new WayPointOverlay(wayPoints[i], tracked);
			overlay.addMarkerClickHandler(clickListener);

			updateWayPointList.add(overlay);

			this.wayPoints.add(overlay);
		}

		setListener();

		setStrokeStyle(TRACKPOINT_STYLE_NORMAL);
	}

	/**
	 * Convert an array of Cwaypoint into array of LatLng
	 * 
	 * @param wayPoints
	 *            - an array of CWaypoint
	 * @return an array of LatLng
	 */
	private static LatLng[] getLatLngArray(CWaypoint[] wayPoints) {
		LatLng[] latlngArray = new LatLng[wayPoints.length];
		for (int i = 0; i < latlngArray.length; i++) {
			latlngArray[i] = wayPoints[i].getLatLng();
		}
		return latlngArray;
	}

	/**
	 * Set track point overlay state
	 */
	private void setTrackpointState() {
		if (isChoosen) {
			setStrokeStyle(TRACKPOINT_STYLE_CHOOSEN);
		} else {
			setStrokeStyle(TRACKPOINT_STYLE_NORMAL);
		}
	}

	/**
	 * set listener for track point overlay
	 */
	private void setListener() {

		addPolylineClickHandler(new PolylineClickHandler() {
			@Override
			public void onClick(PolylineClickEvent event) {
				isChoosen = !isChoosen;
				setTrackpointState();
			}
		});

	}

	/**
	 * Add track point overlay and its way point overlay to map
	 */
	public void addTrackPointOverlayToMap() {

		final TrackPointOverlay trackPointOverlay = this;
		if (wayPoints.size() > 0) {
			WayPointOverlay point = wayPoints.get(0);
			if (point != null) {
				MapPanel.getInstance().panTo(point.getLatLng());
			}
		}

		for (int i = 0; i < wayPoints.size(); i++) {
			WayPointOverlay point = wayPoints.get(i);
			MapPanel.getInstance().addOverlay(point);
		}
		if (trackPointOverlay != null) {
			MapPanel.getInstance().addOverlay(trackPointOverlay);

			for (int i = 1; i < wayPoints.size(); i++) {
				WayPointOverlay curPoint = wayPoints.get(i);
				WayPointOverlay prePoint = wayPoints.get(i - 1);
				curPoint.calculateDirection(prePoint);
			}
		}
	}

	/**
	 * Add a CWaypoint to track point overlay
	 * 
	 * @param point
	 *            - an CWaypoint
	 * @param index
	 */
	public void addWayPoint(CWaypoint point, int index) {
		WayPointOverlay overlay = new WayPointOverlay(point, tracked);

		wayPoints.add(index, overlay);

		overlay.addMarkerClickHandler(clickListener);

		insertVertex(index, point.getLatLng());
		if (getVertexCount() > 1) {
			if (index == 0) {
				WayPointOverlay point0 = getWaypointOverlay(0);
				WayPointOverlay point1 = getWaypointOverlay(1);
				if (point1 != null && point0 != null) {
					point1.calculateDirection(point0);
				}
			} else if (index == getVertexCount()) {
				overlay.calculateDirection(wayPoints.get(index - 1));
			} else {
				wayPoints.get(index + 1).calculateDirection(overlay);
				overlay.calculateDirection(wayPoints.get(index - 1));
			}
		}

		if (index == 0 && followLastWayPointMode) {
			MapPanel.getInstance().panTo(overlay.getLatLng());
		}

		updateWayPointList.add(trackPointOverlay.getWaypointOverlay(index));

		MapPanel.getInstance().addOverlay(overlay);
	}

	/**
	 * Set normal color of track point overlay
	 * 
	 * @param color
	 */
	public void setTrackPointNormalStyleColor(String color) {
		TRACKPOINT_STYLE_NORMAL.setColor(color);
	}

	/**
	 * Set color of the track point overlay when it was chosen
	 * 
	 * @param color
	 */
	public void setTrackPointChoosenStyleColor(String color) {
		TRACKPOINT_STYLE_CHOOSEN.setColor(color);
	}

	/**
	 * Set weight of the normal color of track point overlay
	 * 
	 * @param weight
	 */
	public void setTrackPointNormalStyleWeight(int weight) {
		TRACKPOINT_STYLE_NORMAL.setWeight(weight);
	}

	/**
	 * Set weight of the normal color of track point overlay Set weight of the
	 * 
	 * @param weight
	 */
	public void setTrackPointChoosenStyleWeight(int weight) {
		TRACKPOINT_STYLE_CHOOSEN.setWeight(weight);
	}

	/**
	 * Set the opacity of track point overlay 's normal color overlay
	 * 
	 * @param opacity
	 */
	public void setTrackPointNormalStyleOpacity(int opacity) {
		TRACKPOINT_STYLE_NORMAL.setOpacity(opacity);
	}

	/**
	 * Set the opacity of track point overlay 's color overlay when it was
	 * chosen
	 * 
	 * @param opacity
	 */
	public void setTrackPointChoosenStyleOpacity(int opacity) {
		TRACKPOINT_STYLE_CHOOSEN.setOpacity(opacity);
	}

	/**
	 * Get the specific waypoint overlay of the track point overlay
	 * 
	 * @param index
	 * @return
	 */
	public WayPointOverlay getWaypointOverlay(int index) {
		if (wayPoints == null || wayPoints.size() <= index)
			return null;
		return wayPoints.get(index);
	}

	/**
	 * Get the specific CWaypoint of the track point overlay
	 * 
	 * @param index
	 * @return
	 */
	public CWaypoint getWaypoint(int index) {
		if (wayPoints == null || wayPoints.size() <= index)
			return null;
		return wayPoints.get(index).getWayPoint();
	}

	public ArrayList<CWaypoint> getWaypoints() {
		ArrayList<CWaypoint> points = new ArrayList<CWaypoint>();
		for (WayPointOverlay point : wayPoints) {
			points.add(point.getWayPoint());
		}
		return points;
	}

	/**
	 * Set the track point overlay visible or not
	 */
	public void setVisible(boolean visible) {
		setVisible(visible);
		for (int i = 0; i < wayPoints.size(); i++) {
			wayPoints.get(i).setVisible(visible);
		}
	}

	/**
	 * Remove a vertex of the track point overlay. If there is a vertex left,
	 * the track point overlay is removed
	 * 
	 * @param index
	 */
	public void removeWayPoint(int index) {
		if (wayPoints.size() == 2) {
			for (WayPointOverlay wayPointOverlay : wayPoints) {
				if (wayPointOverlay != null) {
					MapPanel.getInstance().removeOverlay(wayPointOverlay);
				}
			}
			MapPanel.getInstance().removeOverlay(this);
			wayPoints.clear();
			return;
		}
		if (getWaypoint(index) == null) {
			return;
		}
		deleteVertex(index);
		WayPointOverlay point = wayPoints.get(index);
		MapPanel.getInstance().removeOverlay(point);
		wayPoints.remove(index);
	}

	/**
	 * Remove this trackpoint overlay and its instances
	 */
	public void removeOverlay() {
		MapPanel mapPanel = MapPanel.getInstance();
		for (WayPointOverlay point : wayPoints) {
			mapPanel.removeOverlay(point);
		}
		mapPanel.removeOverlay(this);
	}

	public void setFollowLastWaypointMode(boolean follow) {
		followLastWayPointMode = follow;
		if (trackPointOverlay.getVertexCount() > 0) {
			CWaypoint point = getWaypoint(0);
			if (point == null)
				return;
			MapPanel.getInstance().setCenter(point.getLatLng());
		}
	}

	public boolean isFollowLastWaypointMode() {
		return followLastWayPointMode;
	}

	public ArrayList<WayPointOverlay> getWayPointOverlays() {
		return wayPoints;
	}

	/**
	 * Listener for the trackpoint overlay when it is clicked
	 * 
	 * @author USER
	 * 
	 */
	class WaypointClickListener implements MarkerClickHandler {

		@Override
		public void onClick(MarkerClickEvent event) {
			// TODO Auto-generated method stub
			isChoosen = !isChoosen;
			setTrackpointState();
		}
	}
}

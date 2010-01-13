package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.database.client.entity.CWaypoint;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.localization.client.MapPanelConstants;
import com.bkitmobile.poma.ui.client.MapToolbar.TrackedViewMode;
import com.bkitmobile.poma.ui.client.map.TrackPointOverlay;
import com.bkitmobile.poma.ui.client.map.WayPointOverlay;
import com.bkitmobile.poma.util.client.Task;
import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.Control.CustomControl;
import com.google.gwt.maps.client.event.MapAddOverlayHandler;
import com.google.gwt.maps.client.event.MapClearOverlaysHandler;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapInfoWindowOpenHandler;
import com.google.gwt.maps.client.event.MapMoveEndHandler;
import com.google.gwt.maps.client.event.MapRemoveOverlayHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.LatLngBounds;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class MapPanel extends LoadingPanel {

	private static MapWidget mapWidget;
	private static MapPanel mapPanel;
	private LatLng center;
	private MapToolbar toolbar;
	private Panel panel;
	private PMMapTypeControl customControl;
	private static Long curTrackedFollow = null;

	private static ArrayList<WayPointOverlay> waypointList = new ArrayList<WayPointOverlay>();
	public static ArrayList<Long> removeFromMapList = new ArrayList<Long>();

	/**
	 * 
	 * @return the static instance of MapPanel
	 */
	public static MapWidget getMapWidgetInstance() {
		if (mapWidget == null)
			mapWidget = new MapWidget();
		return mapWidget;
	}

	/**
	 * Constructor
	 */
	public MapPanel() {
		constructor();
	}

	/**
	 * Constructor
	 */
	public MapPanel(String title) {
		super(title);
		constructor();
	}

	/**
	 * construct the map panel
	 */
	public void constructor() {
		setLayout(new BorderLayout());

		panel = new Panel();
		mapWidget = getMapWidgetInstance();

		mapWidget.addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				LatLng point = event.getLatLng();
				if (point != null) {
					System.out.println(point.getLatitude() + ", "
							+ point.getLongitude());
				}
			}
		});

		mapWidget.addInfoWindowOpenHandler(new MapInfoWindowOpenHandler() {

			@Override
			public void onInfoWindowOpen(MapInfoWindowOpenEvent event) {
				mapWidget.setCenter(center);
			}
		});

		mapWidget.addMapMoveEndHandler(new MapMoveEndHandler() {

			@Override
			public void onMoveEnd(MapMoveEndEvent event) {
				center = mapWidget.getCenter();
			}
		});

		mapWidget.addMapAddOverlayHandler(new MapAddOverlayHandler() {

			@Override
			public void onAddOverlay(MapAddOverlayEvent event) {
				Overlay overlay = event.getOverlay();
				if (overlay instanceof WayPointOverlay) {
					WayPointOverlay point = (WayPointOverlay) overlay;

					waypointList.add(point);
				}
			}
		});

		mapWidget.addMapClearOverlaysHandler(new MapClearOverlaysHandler() {

			@Override
			public void onClearOverlays(MapClearOverlaysEvent event) {
				waypointList.clear();
			}
		});

		mapWidget.addMapRemoveOverlayHandler(new MapRemoveOverlayHandler() {

			@Override
			public void onRemoveOverlay(MapRemoveOverlayEvent event) {
				Overlay overlay = event.getOverlay();
				if (overlay instanceof WayPointOverlay) {
					WayPointOverlay point = (WayPointOverlay) overlay;
					waypointList.remove(point);
				}
			}
		});

		panel.add(mapWidget);
		mapWidget.setSize("100%", "100%");

		mapWidget.setScrollWheelZoomEnabled(true);

		add(panel, new BorderLayoutData(RegionPosition.CENTER));

		setCenter(10.760065, 106.662469);
		setZoomLevel(17);

		panel.addListener(new PanelListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {

				mapWidget.checkResizeAndCenter();
			}
		});

		toolbar = new MapToolbar();

		customControl = new PMMapTypeControl();
		mapWidget.addControl(customControl);
		mapWidget.addControl(new LargeMapControl3D());
		mapPanel = this;
	}

	/**
	 * Set the LatLngBound for the map, enable you to see all Waypoint in map
	 */
	public static void setMaxLatLngBound() {
		LatLngBounds bound = LatLngBounds.newInstance();

		for (WayPointOverlay point : waypointList) {
			bound.extend(point.getLatLng());
		}

		int zoomLevel = mapWidget.getBoundsZoomLevel(bound);

		mapPanel.setZoomLevel(zoomLevel);
		mapWidget.panTo(bound.getCenter());
	}

	/**
	 * Set the LatLngBound for the map, enable you to see all Waypoint of
	 * specific device in map
	 * 
	 * @param currentTrackedID
	 */
	public void boundToTracked(Long currentTrackedID) {
		if (!UserSettings.currentTrackOverlay.containsKey(currentTrackedID)) {
			return;
		}
		LatLngBounds bound = LatLngBounds.newInstance();
		TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
				.get(currentTrackedID);
		for (WayPointOverlay overlay : trackOverlay.getWayPointOverlays()) {
			bound.extend(overlay.getLatLng());
		}

		MapWidget widget = MapPanel.getMapWidgetInstance();
		int zoomLevel = widget.getBoundsZoomLevel(bound);

		mapPanel.setZoomLevel(zoomLevel - 1);

		widget.setCenter(bound.getCenter());
	}

	/**
	 * 
	 * @return maxButton of the MapPanel
	 */
	public ToolbarButton getMaxButton() {
		return toolbar.maxButton;
	}

	/**
	 * initialize the MapPanel
	 */
	public void init() {
		mapWidget.checkResizeAndCenter();
	}

	/**
	 * Set center for the MapWidget
	 * 
	 * @param lat
	 * @param lng
	 */
	public void setCenter(double lat, double lng) {
		center = LatLng.newInstance(lat, lng);
		mapWidget.panTo(center);
	}

	/**
	 * Set center for the MapWidget
	 * 
	 * @param center
	 */
	public void setCenter(LatLng center) {
		this.center = center;
		mapWidget.setCenter(center);
	}

	/**
	 * Set center for the map with animation
	 * 
	 * @param center
	 */
	public void panTo(LatLng center) {
		this.center = center;
		mapWidget.panTo(center);
	}

	/**
	 * Set zoom level for the map
	 * 
	 * @param level
	 */
	public void setZoomLevel(int level) {
		mapWidget.setZoomLevel(level);
	}

	/**
	 * Add overlay to the MapWidget
	 * 
	 * @param overlay
	 */
	public void addOverlay(Overlay overlay) {
		if (overlay != null) {
			mapWidget.addOverlay(overlay);
		}
	}

	/**
	 * Remove overlay in the MapWidget
	 * 
	 * @param overlay
	 */
	public void removeOverlay(Overlay overlay) {
		if (overlay != null) {
			mapWidget.removeOverlay(overlay);
		}
	}

	/**
	 * Get static innstance of the MapPanel
	 * 
	 * @return
	 */
	public static MapPanel getInstance() {
		return mapPanel;
	}

	/**
	 * Set top toolbar visible
	 */
	public void setToolbarVisible() {

		Task countDownTask = toolbar.getCountDownTask();

		if (!UserSettings.timerTask.getTaskList().contains(countDownTask)) {
			setTopToolbar(toolbar);
			UserSettings.timerTask.getTaskList().add(countDownTask);
			customControl.setTraceAllButtonVisible(true);
		}
	}

	/**
	 * Clear all overlay in the map
	 */
	public static void clearOverlays() {
		mapWidget.clearOverlays();
		waypointList.clear();
	}

	/**
	 * Set Device view mode of the MapToolbar
	 * 
	 * @param mode
	 */
	public void setTrackedViewMode(TrackedViewMode mode) {
		toolbar.setTrackedViewMode(mode);

		clearOverlays();
	}

	/**
	 * Add lastest Waypoint of device into the map
	 * 
	 * @param tracked
	 */
	public static void addToMap(final CTracked tracked) {
		addToMap(tracked, false, null, null);
	}

	/**
	 * Add lastest Waypoint of device into the map
	 * 
	 * @param tracked
	 */
	public static void addToMap(final CTracked tracked, final boolean follow,
			final Long curID, final Long preID) {
		if (tracked.getShowInMap() != null && !tracked.getShowInMap()) {
			return;
		}

		if (removeFromMapList.contains(tracked.getUsername())) {
			removeFromMapList.remove(tracked.getUsername());
		}

		if (UserSettings.currentTrackOverlay.containsKey(tracked.getUsername())) {
			TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
					.get(tracked.getUsername());
			trackOverlay.addTrackPointOverlayToMap();
			if (follow) {
				setTrackedFollow(curID, preID);
			}
			return;
		}

		DatabaseService.Util.getInstance().getLastestWaypointTracked(
				tracked.getUsername(),
				new AsyncCallback<ServiceResult<CWaypoint>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<CWaypoint> result) {
						CWaypoint point = result.getResult();

						if (point != null) {
							final TrackPointOverlay trackOverlay = new TrackPointOverlay(
									new CWaypoint[] { point }, tracked);

							UserSettings.currentTrackOverlay.put(tracked
									.getUsername(), trackOverlay);
							if (follow) {
								setTrackedFollow(curID, preID);
							}

							trackOverlay.addTrackPointOverlayToMap();
						}
					}
				});
	}

	/**
	 * Return the MapToolbar
	 * 
	 * @return
	 */
	public MapToolbar getToolbar() {
		return toolbar;
	}

	/**
	 * Set follow lastest Waypoint mode of the device
	 * 
	 * @param currentTrackedID
	 *            device will be followed
	 * @param previousTrackedID
	 *            remove follow mode
	 */
	public static void setTrackedFollow(Long currentTrackedID,
			Long previousTrackedID) {
		if (previousTrackedID != null) {
			if (UserSettings.currentTrackOverlay.containsKey(previousTrackedID))
				UserSettings.currentTrackOverlay.get(previousTrackedID)
						.setFollowLastWaypointMode(false);
			curTrackedFollow = null;
		}

		if (currentTrackedID != null) {
			if (UserSettings.currentTrackOverlay.containsKey(currentTrackedID))
				UserSettings.currentTrackOverlay.get(currentTrackedID)
						.setFollowLastWaypointMode(true);
			curTrackedFollow = currentTrackedID;
		}
	}

	public static boolean isTrackedFollow(Long trackedID) {
		if (curTrackedFollow == null) {
			return false;
		}
		return trackedID.longValue() == curTrackedFollow.longValue();
	}

	/**
	 * Remove Waypoint and TrackPoint of the specific Device from map
	 * 
	 * @param trackedID
	 */
	public static void removeFromMap(Long trackedID) {
		if (UserSettings.currentTrackOverlay.containsKey(trackedID)) {
			// 
			// TrackPointOverlay trackOverlay = UserSettings.currentTrackOverlay
			// .remove(trackedID);

			removeFromMapList.add(trackedID);
			// trackOverlay.removeOverlay();
		}
	}

}

/**
 * Control of the map, enable user to set maptype
 * 
 * @author Le Trong Nghia
 * 
 */
class PMMapTypeControl extends CustomControl {

	protected PMMapTypeControl() {
		super(new ControlPosition(ControlAnchor.TOP_RIGHT, 3, 3));
	}

	private static MapPanelConstants constants = GWT
			.create(MapPanelConstants.class);

	private Button traceAllButton;

	@Override
	protected Widget initialize(final MapWidget map) {
		Grid grid = new Grid(1, 2);

		Menu mapTypeMenu = new Menu();

		CheckItem trafficItem = new CheckItem(constants.trafficLabel(), true);
		trafficItem.setGroup("maptype");
		trafficItem.addListener(new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				int zoomLevel = MapPanel.getMapWidgetInstance().getZoomLevel();
				MapType mapType = MapType.getNormalMap();

				map.setCurrentMapType(mapType);
				map.setZoomLevel(zoomLevel);
			}
		});

		CheckItem satelliteItem = new CheckItem("Satelitte", true);
		satelliteItem.setGroup("maptype");
		satelliteItem.addListener(new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				int zoomLevel = map.getZoomLevel();
				MapType mapType = MapType.getSatelliteMap();

				map.setCurrentMapType(mapType);
				map.setZoomLevel(zoomLevel);
			}
		});

		CheckItem hybridItem = new CheckItem(constants.hybridLabel());
		hybridItem.setGroup("maptype");
		hybridItem.addListener(new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				int zoomLevel = MapPanel.getMapWidgetInstance().getZoomLevel();
				MapType mapType = MapType.getHybridMap();

				map.setCurrentMapType(mapType);
				map.setZoomLevel(zoomLevel);
			}
		});

		CheckItem terrainItem = new CheckItem(constants.terrainLabel());
		terrainItem.setGroup("maptype");
		terrainItem.addListener(new BaseItemListenerAdapter() {
			@Override
			public void onClick(BaseItem item, EventObject e) {
				int zoomLevel = MapPanel.getMapWidgetInstance().getZoomLevel();
				MapType mapType = MapType.getPhysicalMap();

				map.setCurrentMapType(mapType);
				map.setZoomLevel(zoomLevel);
			}
		});

//		CheckItem earthItem = new CheckItem(constants.earthLabel());
//		earthItem.setGroup("maptype");
//		earthItem.addListener(new BaseItemListenerAdapter() {
//			@Override
//			public void onClick(BaseItem item, EventObject e) {
//				map.setCurrentMapType(MapType.getEarthMap());
//			}
//		});

		mapTypeMenu.addItem(trafficItem);
		mapTypeMenu.addItem(satelliteItem);
		mapTypeMenu.addItem(hybridItem);
		mapTypeMenu.addItem(terrainItem);
		// mapTypeMenu.addItem(earthItem);

		Button mapTypeButton = new Button(constants.mapTypeLabel());
		mapTypeButton.setMenu(mapTypeMenu);

		traceAllButton = new Button(constants.traceAllLabel());

		traceAllButton.addListener(new ButtonListenerAdapter() {

			@Override
			public void onClick(Button button, EventObject e) {
				MapPanel.setMaxLatLngBound();
			}

		});

		traceAllButton.setVisible(false);

		grid.setWidget(0, 0, mapTypeButton);
		grid.setWidget(0, 1, traceAllButton);

		return grid;
	}

	@Override
	public boolean isSelectable() {
		return false;
	}

	public void setTraceAllButtonVisible(boolean visible) {
		traceAllButton.setVisible(visible);
	}
}
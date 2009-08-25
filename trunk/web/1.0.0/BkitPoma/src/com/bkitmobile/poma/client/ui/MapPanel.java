package com.bkitmobile.poma.client.ui;

import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.control.Control.CustomControl;
import com.google.gwt.maps.client.event.MapAddMapTypeHandler;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapDragStartHandler;
import com.google.gwt.maps.client.event.MapRightClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;

public class MapPanel extends LoadingPanel {

	private MapWidget mapWidget;
	private LatLng center;
	private MapToolbar toolbar;
	private Panel mapPanel;
	private Menu popupMenu;

	public MapPanel() {
		constructor();
	}

	public MapPanel(String title) {
		super(title);
		constructor();
	}

	public void constructor() {
		setLayout(new BorderLayout());
		popupMenu = new Menu();
		for (int i = 0; i < 10; ++i)
			popupMenu.addItem(new Item("menu " + i));
		popupMenu.addItem(new Item("one"));
		popupMenu.addItem(new Item("two"));
		popupMenu.addListener(new MenuListenerAdapter() {
			@Override
			public void onItemClick(BaseItem item, EventObject e) {
				// TODO menu on map panel

			}
		});

		toolbar = new MapToolbar();
		setTopToolbar(toolbar);

		mapPanel = new Panel();
		mapWidget = new MapWidget();
		mapWidget.addMapRightClickHandler(new MapRightClickHandler() {

			@Override
			public void onRightClick(MapRightClickEvent event) {
				// int x = event.getPoint().getX() + Window.getScrollLeft();
				// int y = event.getPoint().getY() + Window.getScrollTop();
				int x = event.getPoint().getX();
				int y = event.getPoint().getY() + mapWidget.getAbsoluteTop();
				popupMenu.showAt(x, y);
			}

		});
		mapWidget.addMapClickHandler(new MapClickHandler() {

			@Override
			public void onClick(MapClickEvent event) {
				popupMenu.hide();
			}

		});
		mapWidget.addMapDragStartHandler(new MapDragStartHandler() {
			@Override
			public void onDragStart(MapDragStartEvent event) {
				popupMenu.hide();
			}
		});
		mapPanel.add(mapWidget);
		mapWidget.setSize("100%", "100%");

		// mapWidget.setUIToDefault();
		mapWidget.setScrollWheelZoomEnabled(true);

		add(mapPanel, new BorderLayoutData(RegionPosition.CENTER));

		setCenter(10.760065, 106.662469);
		setZoomLevel(17);

		mapPanel.addListener(new PanelListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {

				mapWidget.checkResizeAndCenter();
			}
		});

		mapWidget.addControl(new PMMapTypeControl());
		mapWidget.addControl(new LargeMapControl3D());
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
			event.preventDefault();
			// TODO show popup menu
		}
	}

	public void setMaxIcon() {
		// if (UserSettings.MAP_MAXIMIZED)
	}

	public ToolbarButton getMaxButton() {
		return toolbar.maxButton;
	}

	public void init() {
		mapWidget.checkResizeAndCenter();
	}

	public void setCenter(double lat, double lng) {
		center = LatLng.newInstance(lat, lng);
		mapWidget.setCenter(center);
	}

	public void setZoomLevel(int level) {
		mapWidget.setZoomLevel(level);
	}

	public void addOverlay(Overlay overlay) {
		mapWidget.addOverlay(overlay);
	}

	public void setMapWidget(MapWidget map) {
		this.mapWidget = map;
	}

	public MapWidget getMapWidget() {
		return this.mapWidget;
	}
}

class PMMapTypeControl extends CustomControl {

	protected PMMapTypeControl() {
		super(new ControlPosition(ControlAnchor.TOP_RIGHT, 3, 3));
	}

	@Override
	protected Widget initialize(final MapWidget map) {
		// TODO Auto-generated method stub
		Grid grid = new Grid(1, 4);

		Button trafficButton = new Button("Traffic");

		trafficButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				int zoomLevel = map.getZoomLevel();
				map.setCurrentMapType(MapType.getNormalMap());
				map.setZoomLevel(zoomLevel);
			}
		});

		Button hybridButton = new Button("Hybrid");

		hybridButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				int zoomLevel = map.getZoomLevel();
				map.setCurrentMapType(MapType.getHybridMap());
				map.setZoomLevel(zoomLevel);
			}
		});

		Button terrainButton = new Button("Terrain");

		terrainButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				int zoomLevel = map.getZoomLevel();
				map.setCurrentMapType(MapType.getPhysicalMap());
				map.setZoomLevel(zoomLevel);
			}
		});

		Button earthButton = new Button("Earth");

		earthButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				map.setCurrentMapType(MapType.getEarthMap());
			}
		});

		grid.setWidget(0, 0, trafficButton);
		grid.setWidget(0, 1, hybridButton);
		grid.setWidget(0, 2, terrainButton);
		grid.setWidget(0, 3, earthButton);

		return grid;
	}

	@Override
	public boolean isSelectable() {
		// TODO Auto-generated method stub
		return false;
	}
}

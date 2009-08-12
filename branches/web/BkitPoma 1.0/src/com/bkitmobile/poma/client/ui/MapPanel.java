package com.bkitmobile.poma.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MapDragStartHandler;
import com.google.gwt.maps.client.event.MapRightClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.MenuListener;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;


public class MapPanel extends LoadingPanel{

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
//				int x = event.getPoint().getX() + Window.getScrollLeft();
//				int y = event.getPoint().getY() + Window.getScrollTop();
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
		
		//mapWidget.setUIToDefault();
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
		
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
			event.preventDefault();
			// TODO show popup menu
		}
	}
	
	public void setMaxIcon() {
//		if (UserSettings.MAP_MAXIMIZED)
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
	
	public void addOverlay(Overlay overlay){
		mapWidget.addOverlay(overlay);
	}
	
	public void setMapWidget(MapWidget map) {
		this.mapWidget = map;
	}
	
	public MapWidget getMapWidget() {
		return this.mapWidget;
	}
}

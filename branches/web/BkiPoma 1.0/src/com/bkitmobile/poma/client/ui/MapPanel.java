package com.bkitmobile.poma.client.ui;

import com.google.gwt.maps.client.CopyrightCollection;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.TileLayer;
import com.google.gwt.maps.client.control.Control;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.overlay.Overlay;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;


public class MapPanel extends LoadingPanel{

	private MapWidget mapWidget;
	private LatLng center;
	private MapToolbar toolbar;
	private Panel mapPanel;

	public MapPanel() {
		constructor();
	}
	
	public MapPanel(String title) {
		super(title);
		constructor();
	}
	
	public void constructor() {
		setLayout(new BorderLayout());
		
		toolbar = new MapToolbar();
		setTopToolbar(toolbar);
		
		mapPanel = new Panel(); 
		mapWidget = new MapWidget();
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

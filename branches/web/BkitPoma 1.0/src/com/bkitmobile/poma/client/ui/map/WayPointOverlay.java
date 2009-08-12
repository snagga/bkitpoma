package com.bkitmobile.poma.client.ui.map;

import com.bkitmobile.poma.client.database.WayPoint;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerMouseOutHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

public class WayPointOverlay extends Marker {

	private static MapWidget mapWidget;
	WayPoint wayPoint;
	InfoWindowContent infoWindowContent;
	Icon icon;

	public WayPointOverlay(WayPoint wayPoint) {
		// TODO Auto-generated constructor stub
		super(wayPoint.getLatLng());
		this.wayPoint = wayPoint;
		setListener();
	}

	public WayPointOverlay(WayPoint wayPoint, Icon icon) {
		// TODO Auto-generated constructor stub
		super(wayPoint.getLatLng(), newMarkerOption(icon));
		this.wayPoint = wayPoint;

		setListener();
	}

	public WayPointOverlay(WayPoint wayPoint, String foregroundURL,
			String backgroundURL) {
		// TODO Auto-generated constructor stub
		super(LatLng.newInstance(Double.parseDouble(wayPoint.getLattitude()),
				Double.parseDouble(wayPoint.getLongtitude())), newMarkerOption(
				foregroundURL, backgroundURL));
		this.wayPoint = wayPoint;
		setListener();
	}

	public static void setMapWidget(MapWidget mapWidget) {
		WayPointOverlay.mapWidget = mapWidget;

	}

	private void setListener() {
		setInfoWindowContent(null);
		addMarkerClickHandler(new MarkerClickHandler() {

			@Override
			public void onClick(MarkerClickEvent event) {
				// TODO Auto-generated method stub
				LatLng point = getLatLng();
				LatLng newPoint = LatLng.newInstance(point.getLatitude(), point
						.getLongitude() + 0.001);
				setLatLng(newPoint);
			}

		});

		addMarkerMouseOverHandler(new MarkerMouseOverHandler() {

			@Override
			public void onMouseOver(MarkerMouseOverEvent event) {
				// TODO Auto-generated method stub
				InfoWindow infoWindow = mapWidget.getInfoWindow();
				infoWindow.open(getMarkerOverlay(), infoWindowContent);
			}
		});
		addMarkerMouseOutHandler(new MarkerMouseOutHandler() {

			@Override
			public void onMouseOut(MarkerMouseOutEvent event) {
				// TODO Auto-generated method stub
				closeInfoWindow();
			}
		});
	}

	public void setInfoWindowContent(String content) {
		if (content == null) {
			LatLng point = getLatLng();
			content = "<html>" + point.getLatitude() + "</br>"
					+ point.getLongitude() + "</html>";
		}
		infoWindowContent = new InfoWindowContent(content);
	}

	public Marker getMarkerOverlay() {
		return this;
	}

	private static MarkerOptions newMarkerOption(Icon icon) {
		com.bkitmobile.poma.client.ui.map.Icon.setIconAttributeToDefault(icon);
		MarkerOptions option = MarkerOptions.newInstance(icon);

		return option;
	}

	private static MarkerOptions newMarkerOption(String foregroundURL,
			String backgroundURL) {
		Icon icon = Icon.newInstance(foregroundURL);
		if (backgroundURL != null) {
			icon.setShadowURL(backgroundURL);
		}
		com.bkitmobile.poma.client.ui.map.Icon.setIconAttributeToDefault(icon);
		MarkerOptions option = MarkerOptions.newInstance(icon);
		return option;
	}
}
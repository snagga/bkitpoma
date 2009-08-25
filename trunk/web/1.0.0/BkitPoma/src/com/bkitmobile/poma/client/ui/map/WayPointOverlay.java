package com.bkitmobile.poma.client.ui.map;

import com.bkitmobile.poma.client.database.entity.CWaypoint;
import com.bkitmobile.poma.client.ui.map.Icon.IconSize;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MarkerMouseOutHandler;
import com.google.gwt.maps.client.event.MarkerMouseOverHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;

public class WayPointOverlay extends Marker {

	private static MapWidget mapWidget;
	CWaypoint wayPoint;
	InfoWindowContent infoWindowContent;
	String infoContent;
	Icon icon;

	public WayPointOverlay(CWaypoint wayPoint) {
		// TODO Auto-generated constructor stub
		super(wayPoint.getLatLng());
		this.wayPoint = wayPoint;
		setListener();
	}

	public WayPointOverlay(CWaypoint wayPoint, Icon icon) {
		// TODO Auto-generated constructor stub
		super(wayPoint.getLatLng(), newMarkerOption(icon));
		this.wayPoint = wayPoint;
		setListener();
	}

	public WayPointOverlay(CWaypoint wayPoint, String foregroundURL,
			String backgroundURL) {
		// TODO Auto-generated constructor stub
		super(wayPoint.getLatLng(), newMarkerOption(foregroundURL,
				backgroundURL));
		this.wayPoint = wayPoint;
		setListener();
	}

	public static void setMapWidget(MapWidget mapWidget) {
		WayPointOverlay.mapWidget = mapWidget;
	}

	private void setListener() {
		setInfoWindowContent(null);

		addMarkerMouseOverHandler(new MarkerMouseOverHandler() {

			@Override
			public void onMouseOver(MarkerMouseOverEvent event) {
				// TODO Auto-generated method stub
				InfoWindow infoWindow = mapWidget.getInfoWindow();
				if (infoContent != null) {
					infoWindow.open(getMarkerOverlay(), infoWindowContent);
				}

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
			final LatLng point = getLatLng();
			Geocoder geoCoder = new Geocoder();
			geoCoder.getLocations(point, new LocationCallback() {

				@Override
				public void onFailure(int statusCode) {
					// TODO Auto-generated method stub
					infoContent = "<html><b>LatLng :</b><br />-"
							+ point.getLatitude() + "<br />-"
							+ point.getLongitude() + "<br /></html>";
					infoWindowContent = new InfoWindowContent(infoContent);

				}

				@Override
				public void onSuccess(JsArray<Placemark> locations) {
					// TODO Auto-generated method stub
					infoContent = "<html><b>Address :</b>";
					for (int i = 0; i < locations.length() - 1; i++) {
						infoContent += locations.get(i).getAddress();
					}
					String city = locations.get(locations.length() - 1)
							.getAddress();
					infoContent = infoContent.replaceAll(city, "");
					infoContent += city + "<br /><b>LatLng :</b><br />-"
							+ point.getLatitude() + "<br />-"
							+ point.getLongitude() + "<br /></html>";
					infoWindowContent = new InfoWindowContent(infoContent);
				}
			});
			return;
		}
		infoContent = content.replaceAll("<html>", "");
		infoContent = infoContent.replaceAll("</html>", "");
		infoContent = "<html>" + infoContent + "</html>";
		infoWindowContent = new InfoWindowContent(infoContent);
	}

	public void appendIntoInfoWindowContent(String content) {
		infoContent = infoContent.replaceAll("</html>", "");

		content = content.replaceAll("<html>", "");
		content = content.replaceAll("</html>", "");

		infoContent += content + "<br /></html>";
		infoWindowContent = new InfoWindowContent(infoContent);
	}

	public boolean isInfoWindowContentNull() {
		return infoWindowContent == null;
	}

	public Marker getMarkerOverlay() {
		return this;
	}

	public CWaypoint getWayPoint() {
		return wayPoint;
	}

	public void showInfoWindow(MapWidget mapWidget) {
		InfoWindow infoWindow = mapWidget.getInfoWindow();
		infoWindow.open(this, infoWindowContent);
	}

	private static MarkerOptions newMarkerOption(Icon icon) {
		com.bkitmobile.poma.client.ui.map.Icon.setIconAttribute(icon,
				IconSize.SMALL);
		MarkerOptions option = MarkerOptions.newInstance(icon);

		return option;
	}

	private static MarkerOptions newMarkerOption(String foregroundURL,
			String backgroundURL) {
		Icon icon = Icon.newInstance(foregroundURL);
		if (backgroundURL != null) {
			icon.setShadowURL(backgroundURL);
		}
		com.bkitmobile.poma.client.ui.map.Icon.setIconAttribute(icon,
				IconSize.SMALL);
		MarkerOptions option = MarkerOptions.newInstance(icon);
		return option;
	}
}
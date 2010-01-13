package com.bkitmobile.poma.ui.client.map;

import com.bkitmobile.poma.database.client.entity.*;
import com.bkitmobile.poma.localization.client.WayPointOverlayConstants;
import com.bkitmobile.poma.ui.client.*;
import com.bkitmobile.poma.ui.client.map.Icon.IconAnchor;
import com.bkitmobile.poma.ui.client.map.Icon.IconSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.event.MarkerDoubleClickHandler;
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

	public static enum Quarter {
		FIRST, SECOND, THIRD, FOUTH
	}

	private CWaypoint wayPoint;
	private InfoWindowContent infoWindowContent;
	private String infoContent;

	private boolean isDoubleClick = false;

	private static final String MAIN_DIRECTION_URL = "images/arrow-png/arrow-";
	private static final String EXTENSION_DIRECTION_URL = ".png";

	private WayPointOverlayConstants wayPointOverlayConstants = GWT
			.create(WayPointOverlayConstants.class);

	private static final int NUM_DIRECTION = 16;

	private String latLngInDegree[] = new String[2];

	/**
	 * Create a WayPointoverlay with a given CWaypoint
	 * 
	 * @param wayPoint
	 */
	public WayPointOverlay(CWaypoint wayPoint, CTracked tracked) {
		super(wayPoint.getLatLng(), newMarkerOption(tracked.getIconPath(), ""));

		constructor(wayPoint, tracked);
		setListener();
	}

	/**
	 * Create a WayPointoverlay with a given CWaypoint and an icon
	 * 
	 * @param wayPoint
	 * @param icon
	 */
	public WayPointOverlay(CWaypoint wayPoint, Icon icon, CTracked tracked) {
		super(wayPoint.getLatLng(), newMarkerOption(icon));
		constructor(wayPoint, tracked);
		setListener();
	}

	/**
	 * Create a WayPointoverlay with a given CWaypoint, imageURl, shadowURL
	 * 
	 * @param wayPoint
	 * @param imageURL
	 * @param shadowURL
	 */
	public WayPointOverlay(CWaypoint wayPoint, String imageURL,
			String shadowURL, CTracked tracked) {
		super(wayPoint.getLatLng(), newMarkerOption(imageURL, shadowURL));
		constructor(wayPoint, tracked);
		setListener();
	}

	private void constructor(CWaypoint wayPoint, CTracked tracked) {
		this.wayPoint = wayPoint;

		latLngInDegree = convertLatLngToDegrees(wayPoint.getLatLng());

		String name = tracked.getName() != null ? "<b>"
				+ wayPointOverlayConstants.name() + " </b>" + tracked.getName()
				+ " ( <b>ID</b> : " + tracked.getUsername() + " )<br/>"
				: "<b>ID: </b> " + tracked.getUsername() + "<br/>";

		infoContent = name + "<b>" + wayPointOverlayConstants.latitude()
				+ "</b> " + latLngInDegree[0] + "<br />" + "<b>"
				+ wayPointOverlayConstants.longitude() + "</b>"
				+ latLngInDegree[1] + "<br />" + "<b>"
				+ wayPointOverlayConstants.speed() + "</b>"
				+ getWayPoint().getSpeed() + " m/s </br>";
		infoWindowContent = new InfoWindowContent(infoContent);

	}

	/**
	 * Set listener for the wWayPointOverlay
	 */
	private void setListener() {

		setInfoWindowContent(null);

		addMarkerDoubleClickHandler(new MarkerDoubleClickHandler() {

			@Override
			public void onDoubleClick(MarkerDoubleClickEvent event) {
				MapPanel.getMapWidgetInstance().setCenter(getLatLng());
				InfoWindow infoWindow = MapPanel.getMapWidgetInstance()
						.getInfoWindow();
				if (infoContent != null) {
					infoWindow.open(getMarkerOverlay(), infoWindowContent);
				}

				isDoubleClick = true;
			}
		});

		addMarkerMouseOverHandler(new MarkerMouseOverHandler() {

			@Override
			public void onMouseOver(MarkerMouseOverEvent event) {
				InfoWindow infoWindow = MapPanel.getMapWidgetInstance()
						.getInfoWindow();
				if (infoContent != null) {
					infoWindow.open(getMarkerOverlay(), infoWindowContent);
				}
				isDoubleClick = false;
			}
		});
		addMarkerMouseOutHandler(new MarkerMouseOutHandler() {

			@Override
			public void onMouseOut(MarkerMouseOutEvent event) {
				if (!isDoubleClick) {
					closeInfoWindow();
				}
			}
		});
	}

	/**
	 * Set the content for the infowindow
	 * 
	 * @param content
	 */
	public void setInfoWindowContent(String content) {
		if (content == null) {
			final LatLng point = getLatLng();
			Geocoder geoCoder = new Geocoder();
			geoCoder.getLocations(point, new LocationCallback() {

				@Override
				public void onFailure(int statusCode) {
					String address = "<b>" + wayPointOverlayConstants.address()
							+ "</b>";
					infoContent = infoContent.replaceFirst("<br/>", "<br/> "
							+ address + "Unknown" + "<br/>");
					infoWindowContent = new InfoWindowContent(infoContent);
				}

				@Override
				public void onSuccess(JsArray<Placemark> locations) {
					String address = "<b>" + wayPointOverlayConstants.address()
							+ "</b>";
					String addresses[] = new String[locations.length()];
					for (int i = 0; i < locations.length(); i++) {
						addresses[i] = locations.get(i).getAddress()
								.toUpperCase();
					}
					for (int i = addresses.length - 1; i > 0; i--) {
						for (int j = i - 1; j >= 0; j--) {
							addresses[j] = addresses[j].replaceAll(
									addresses[i], "");
						}
					}

					for (String content : addresses) {
						address += content;
					}

					infoContent = infoContent.replaceFirst("<br/>", "<br/> "
							+ address + "<br/>");
					infoWindowContent = new InfoWindowContent(infoContent);
				}
			});
			return;
		}
		infoContent = content.replaceAll("<html>", "");
		infoContent = infoContent.replaceAll("</html>", "");
		infoWindowContent = new InfoWindowContent(infoContent);
	}

	public String getInfoContent() {
		return infoContent;
	}

	/*
	 * Append infowindow content with the given content
	 */
	public void appendIntoInfoWindowContent(String content) {
		content = content.replaceAll("<html>", "");
		content = content.replaceAll("</html>", "");

		infoContent += content + "<br />";
		infoWindowContent = new InfoWindowContent(infoContent);
	}

	/**
	 * Check if the infowindow content is null
	 * 
	 * @return
	 */
	public boolean isInfoWindowContentNull() {
		return infoWindowContent == null;
	}

	/**
	 * Return the marker of this waypointoverlay
	 */
	public Marker getMarkerOverlay() {
		return this;
	}

	/**
	 * Return the CWaypoint of this WayPointOverlay
	 */
	public CWaypoint getWayPoint() {
		return wayPoint;
	}

	/**
	 * Show the infowindow of this overlay
	 */
	public void showInfoWindow() {
		if (infoContent != null) {
			InfoWindow infoWindow = MapPanel.getMapWidgetInstance()
					.getInfoWindow();
			infoWindow.open(this, infoWindowContent);
		}
	}

	/**
	 * Create new MarkerOption with given icon
	 * 
	 * @param icon
	 * @return
	 */
	private static MarkerOptions newMarkerOption(Icon icon) {
		com.bkitmobile.poma.ui.client.map.Icon.setIconAttribute(icon,
				IconSize.SMALL, IconAnchor.BASELINE_CENTER);
		MarkerOptions option = MarkerOptions.newInstance(icon);

		return option;
	}

	/**
	 * Create new MarkerOption with given image and shadow URL
	 * 
	 *@param imageURL
	 *@param shadowURL
	 * @return
	 */
	private static MarkerOptions newMarkerOption(String imageURL,
			String shadowURL) {
		// if (imageURL.trim().equals("")) {
		// return null;
		// }
		Icon icon = Icon.newInstance(imageURL);
		if (shadowURL != null) {
			icon.setShadowURL(shadowURL);
		}
		com.bkitmobile.poma.ui.client.map.Icon.setIconAttribute(icon,
				IconSize.SMALL, IconAnchor.BASELINE_CENTER);
		MarkerOptions option = MarkerOptions.newInstance(icon);
		return option;
	}

	/**
	 * Calculate the direction of this overlay with the given overlay
	 * 
	 * @param point
	 */
	public void calculateDirection(WayPointOverlay point) {

		LatLng firstPoint = getLatLng();
		LatLng secondPoint = point.getLatLng();
		LatLng thirdPoint = LatLng.newInstance(firstPoint.getLatitude(),
				secondPoint.getLongitude());

		double hypotenuse = firstPoint.distanceFrom(secondPoint);
		double side = firstPoint.distanceFrom(thirdPoint);

		if (hypotenuse == 0) {
			return;
		}
		double angle = Math.toDegrees(Math.acos(side / hypotenuse));

		Quarter position = null;

		if (firstPoint.getLongitude() <= secondPoint.getLongitude()) {
			if (firstPoint.getLatitude() > secondPoint.getLatitude()) {
				position = Quarter.FOUTH;
			} else {
				position = Quarter.FIRST;
			}
		} else {
			if (firstPoint.getLatitude() > secondPoint.getLatitude()) {
				position = Quarter.THIRD;
			} else {
				position = Quarter.SECOND;
			}
		}

		switch (position) {
		case FIRST:
			break;
		case SECOND:
			angle = 180 - angle;
			break;
		case THIRD:
			angle += 180;
			break;
		case FOUTH:
			angle = 360 - angle;
			break;
		}

		double betweenAngle = (360.0 / NUM_DIRECTION);

		int nearestAngel = (int) Math.floor(angle / betweenAngle + 0.5)
				% NUM_DIRECTION;

		String icon = MAIN_DIRECTION_URL + nearestAngel
				+ EXTENSION_DIRECTION_URL;

		setImage(icon);
		getIcon().setShadowURL("");
	}

	private static String[] convertLatLngToDegrees(LatLng point) {
		String degrees[] = new String[2];
		double latInDouble = point.getLatitude();
		latInDouble = Math.abs(Math.round(latInDouble * 1000000));
		degrees[0] = ((Math.floor(latInDouble / 1000000))
				+ "\u00B0 "
				+ Math.floor(((latInDouble / 1000000) - Math
						.floor(latInDouble / 1000000)) * 60)
				+ "' "
				+ (Math.floor(((((latInDouble / 1000000) - Math
						.floor(latInDouble / 1000000)) * 60) - Math
						.floor(((latInDouble / 1000000) - Math
								.floor(latInDouble / 1000000)) * 60)) * 100000) * 60 / 100000) + "\" ");

		degrees[0] += latInDouble > 0 ? " N" : " S";

		double lngInDouble = point.getLongitude();
		lngInDouble = Math.abs(Math.round(lngInDouble * 1000000));
		degrees[1] = ((Math.floor(lngInDouble / 1000000))
				+ "\u00B0 "
				+ Math.floor(((lngInDouble / 1000000) - Math
						.floor(lngInDouble / 1000000)) * 60)
				+ "' "
				+ (Math.floor(((((lngInDouble / 1000000) - Math
						.floor(lngInDouble / 1000000)) * 60) - Math
						.floor(((lngInDouble / 1000000) - Math
								.floor(lngInDouble / 1000000)) * 60)) * 100000) * 60 / 100000) + "\" ");
		degrees[1] += lngInDouble > 0 ? " E" : " W";

		return degrees;
	}

	public String[] getLatLngInDegree() {
		return latLngInDegree;
	}

	public String getLatitudeInDegree() {
		return latLngInDegree[0];
	}

	public String getLongitudeInDegree() {
		return latLngInDegree[1];
	}
}
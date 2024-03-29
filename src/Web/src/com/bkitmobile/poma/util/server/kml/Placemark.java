package com.bkitmobile.poma.util.server.kml;

import java.util.List;

public class Placemark extends Feature {

	private Geometry geometry;
	
	public Placemark() {}
	
	public Placemark(String name) {
		setName(name);
	}
	
	public Placemark(String name, Boolean visability, Boolean open, AtomAuthor atomAuthor, AtomLink atomLink, String address, String xalAddressDetails, String phoneNumber, String snippet, Integer snippetMaxLines,String description, AbstractView abstractView, TimePrimitive timePrimitive, String styleUrl, List<StyleSelector> styleSelectors, Region region, ExtendedData extendedData, Geometry geometry) {
		super(name, visability, open, atomAuthor, atomLink, address, xalAddressDetails, phoneNumber, snippet, snippetMaxLines, description, abstractView, timePrimitive, styleUrl, styleSelectors, region, extendedData);
		this.geometry = geometry;
	}
	
	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
	public void setLocation(double longitude, double latitude) {
		setGeometry(new Point(longitude, latitude));
	}

	public void write(Kml kml) throws KmlException {
		kml.println("<Placemark" + getIdAndTargetIdFormatted(kml) + ">", 1);
		writeInner(kml);
		if (geometry != null) {
			geometry.write(kml);
		}
		kml.println(-1, "</Placemark>");
	}
	
	public void writeDelete(Kml kml) throws KmlException {
		kml.println("<Placemark" + getIdAndTargetIdFormatted(kml) + "></Placemark>");
	}	
}

package com.bkitmobile.poma.util.server.kml;

public class AtomLink {

	private String href;

	public AtomLink() {}
	
	public AtomLink(String href) {
		this.href = href;
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public void write(Kml kml) throws KmlException {
		if (href == null) {
			throw new KmlException("href not set for atom:Link");
		}
		kml.println("<atom:link href=\"" + href + "\" />");
		kml.setAtomElementsIncluded(true);
	}
	
}

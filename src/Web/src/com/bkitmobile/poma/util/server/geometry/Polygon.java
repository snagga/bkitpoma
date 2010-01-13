package com.bkitmobile.poma.util.server.geometry;

import java.util.ArrayList;

public class Polygon {
	private ArrayList<Geocode> vertexes;

	/**
	 * @param vertexs
	 *            the vertexes to set
	 */
	public void setVertexes(ArrayList<Geocode> vertexes) {
		this.vertexes = vertexes;
	}

	/**
	 * @return the vertexes
	 */
	public ArrayList<Geocode> getVertexes() {
		return vertexes;
	}

	public Polygon(Geocode[] vertexes) {
		this.vertexes = new ArrayList<Geocode>();
		for (Geocode geocode : vertexes) {
			this.vertexes.add(geocode);
		}
	}

	public Polygon(ArrayList<Geocode> vertexes) {
		this.vertexes = vertexes == null ? null : (ArrayList<Geocode>) vertexes.clone();
	}

	public boolean inPolygon(Geocode point) {
		Geocode[] vertexes = null;
		this.vertexes.toArray(vertexes);
		return Polygon.inPolygon(point, vertexes);
	}

	public static boolean inPolygon(Geocode point, Geocode[] vertexes) {
		int i, j, npol = vertexes.length;
		boolean c = false;
		for (i = 0, j = npol - 1; i < npol; j = i++) {
			if ((((vertexes[i].getLongitude() <= point.getLongitude()) && (point
					.getLongitude() < vertexes[j].getLongitude())) || ((vertexes[j]
					.getLongitude() <= point.getLongitude()) && (point
					.getLongitude() < vertexes[i].getLongitude())))
					&& (point.getLatitude() < (vertexes[j].getLatitude() - vertexes[i]
							.getLatitude())
							* (point.getLongitude() - vertexes[i]
									.getLongitude())
							/ (vertexes[j].getLongitude() - vertexes[i]
									.getLongitude())
							+ vertexes[i].getLatitude()))
				c = !c;
		}
		return c;
	}

	public static void main(String[] s) {
		System.out.println(Polygon.inPolygon(new Geocode(0.0, -0.5),
				new Geocode[] { new Geocode(0.0, 0.0), new Geocode(5.0, 0.0),
						new Geocode(0.0, 5.0), }));
		System.out.println(new Polygon(new Geocode[] { new Geocode(0, 1) })
				.equals(new Polygon(new Geocode[] { new Geocode(0, 1) })));
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Polygon))
			return false;
		
		Polygon that = (Polygon) obj;
		
		return that.getVertexes().equals(this.getVertexes());
	}
	
	@Override
	protected Polygon clone() {
		return new Polygon(this.vertexes);
	}
}

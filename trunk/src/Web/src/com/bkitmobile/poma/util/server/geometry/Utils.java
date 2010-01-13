package com.bkitmobile.poma.util.server.geometry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
	
	/**
	 * Calculate the distance between two point
	 * @param begin Begin point
	 * @param end End point
	 * @return distance in m
	 */
	public static long getDistance(Geocode begin, Geocode end) {
		long R = 6371000L; // m
		double dLat = Math.toRadians(end.getLatitude() - begin.getLatitude());
		double dLon = Math.toRadians(end.getLongitude() - begin.getLongitude());
		
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(begin.getLatitude())) * Math.cos(Math.toRadians(end.getLatitude()))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		long d = (long)(R * c);
		return d;
	}
	
	/**
	 * Calculate the distance between two point
	 * @param lat1 Latitude of begin point
	 * @param lon1 Longitude of begin point
	 * @param lat2 Latitude of end point
	 * @param lon2 Longitude of end point
	 * @return distance in m
	 */
	public static long getDistance(double lat1, double lon1, double lat2, double lon2) {
		return getDistance(new Geocode(lat1, lon1), new Geocode(lat2, lon2));
	}
	
	/**
	 * Determine phone's location by cellid and lac
	 * @param cellID Cell's ID
	 * @param lac Location Area Code
	 * @return location of the phone
	 */
	public static Geocode getLocation(int cellID, int lac) {
		URL url = null;
		try {
			url = new URL("http://www.google.com/glm/mmap");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		DataInputStream is = null;
		DataOutputStream os = null;

		HttpURLConnection httpConn = null;
		double lat = 0, lng = 0;

		try {
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");

			httpConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)");
//			httpConn.setRequestProperty("Accept_Language", "en-US");
			// Content-Type is must to pass parameters in POST Request
//			httpConn.setRequestProperty("Content-Type",
//					"application/binary");
//			
			if (!httpConn.getDoOutput()) httpConn.setDoOutput(true);
			os = new DataOutputStream(httpConn.getOutputStream());

			os.writeShort(21);
			os.writeLong(0);
			os.writeUTF("fr");
			os.writeUTF("Nokia N95 8Gb");
			os.writeUTF("1.3.1");
			os.writeUTF("Web");
			os.writeByte(27);

			os.writeInt(0);
			os.writeInt(0);
			os.writeInt(3);
			os.writeUTF("");

			os.writeInt(cellID);
			os.writeInt(lac);

			os.writeInt(0);
			os.writeInt(0);
			os.writeInt(0);
			os.writeInt(0);
			os.flush();
			os.close();
			
			if (!httpConn.getDoInput()) httpConn.setDoInput(true);
			
			is = new DataInputStream(httpConn.getInputStream());
			is.readShort();
			is.readByte();

			int errorCode = is.readInt();
			if (errorCode == 0) {
				lat = (double) is.readInt() / 1000000D;
				lng = (double) is.readInt() / 1000000D;
				// Read the rest of the data
				is.readInt();
				is.readInt();
				is.readUTF();
				System.out.println("Lat, Long: " + lat + "," + lng);
			} else
				System.out.println("Error: " + errorCode);
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {
			if (is != null)
				try {
					is.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			if (os != null)
				try {
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			if (httpConn != null)
				try {
					httpConn.disconnect();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
		return new Geocode(lat, lng);
	}
}
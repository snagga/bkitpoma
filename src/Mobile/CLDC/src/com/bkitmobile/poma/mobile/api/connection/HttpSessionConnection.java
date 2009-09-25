package com.bkitmobile.poma.mobile.api.connection;

import java.io.*;
import javax.microedition.io.*;

/**
 * Represents a session connection through HTTP Protocol
 * @author Hieu Rocker
 */
public class HttpSessionConnection {
	/**
	 * Session ID of this connection
	 */
	private String sessionID;

	/**
	 * Get SessionID of this connection
	 * @return
	 */
	public String getSessionID() {
		return sessionID;
	}
	
	/**
	 * Set SessionID of this connection
	 * @param sessionID
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Create an instance of HttpSessionConnection
	 */
	public HttpSessionConnection() {
	}
	
	/**
	 * Create an instance of HttpSessionConnection with specified session id
	 * @param sessionID
	 */
	public HttpSessionConnection(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Request a HTTP GET
	 * @param url Url of Http GET
	 * @return Result of <code>url</code>
	 * @throws IOException
	 */
	public String httpGet(String url) throws IOException {
		HttpConnection httpConn = (HttpConnection) Connector.open(url);
		if (getSessionID() != null) {
			httpConn.setRequestProperty("Cookie", getSessionID());
		}
		
		httpConn.setRequestMethod(HttpConnection.GET);
		
		InputStream is = httpConn.openInputStream();
		String cookie = httpConn.getHeaderField("Set-Cookie");

		if (cookie != null) {
			int semicolon = cookie.indexOf(';');
			setSessionID(cookie.substring(0, semicolon));
			System.out.println(getSessionID());
		}

		int ch = 0;
		StringBuffer sb = new StringBuffer();
		while ((ch = is.read()) != -1) {
			sb.append((char) ch);
		}

		is.close();
		httpConn.close();
		return sb.toString();
	}

	/**
	 * Request a HTTP POST
	 * @param url Url of Http POST
	 * @param params parameters (param1=value1&param2=value2...) of the request
	 * @return Result of <code>url</code>
	 * @throws IOException
	 */
	public String httpPost(String url, String params) throws IOException {
		HttpConnection httpConn = (HttpConnection) Connector.open(url);
		if (getSessionID() != null) {
			httpConn.setRequestProperty("Cookie", getSessionID());
		}
		
		httpConn.setRequestMethod(HttpConnection.POST);
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		

		OutputStream os = httpConn.openOutputStream();
		os.write(params.getBytes());

		InputStream is = httpConn.openInputStream();
		String cookie = httpConn.getHeaderField("Set-Cookie");

		if (cookie != null) {
			int semicolon = cookie.indexOf(';');
			setSessionID(cookie.substring(0, semicolon));
			System.out.println(getSessionID());
		}

		int ch = 0;
		StringBuffer sb = new StringBuffer();
		while ((ch = is.read()) != -1) {
			sb.append((char) ch);
		}

		is.close();
		os.close();
		httpConn.close();
		return sb.toString();
	}
	
	/**
	 * Request a HTTP POST
	 * @param url Url of Http POST
	 * @param rawData 
	 * @return Result of <code>url</code>
	 * @throws IOException
	 */
	public String doPost(String url, byte[] rawData) throws IOException {
		HttpConnection httpConn = (HttpConnection) Connector.open(url);
		
		httpConn.setRequestMethod(HttpConnection.POST);
		httpConn.setRequestProperty("User-Agent", "POMA Mobile");		
//		httpConn.setRequestProperty("Content-Type", "application/octet-stream");
		
		OutputStream os = httpConn.openOutputStream();
		os.write(rawData);

		InputStream is = httpConn.openInputStream();

		int ch = 0;
		StringBuffer sb = new StringBuffer();
		while ((ch = is.read()) != -1) {
			sb.append((char) ch);
		}

		is.close();
		os.close();
		httpConn.close();
		return sb.toString();
	}
}
package com.bkitmobile.poma.client.database;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;


public class WayPoint implements IsSerializable{
	private HashMap hash = new HashMap();

	/**
	 * Default Constructor
	 */
	public WayPoint() {
//		hash.put("time", "");
//		hash.put("trackid", "");
//		hash.put("longtitude", "");
//		hash.put("lattitude", "");
		this("", "", "", "","");
	}
	
	public WayPoint(String time,String trackid,String longtitude,String lattitude,String speed){
		hash.put("time", time);
		hash.put("trackid", trackid);
		hash.put("longtitude", longtitude);
		hash.put("lattitude", lattitude);
		hash.put("speed", speed);
	}
	
	public void setTime(String time){
		hash.put("time",time);
	}
	
	public String getTime(){
		return String.valueOf(hash.get("time"));
	}
	
	public void setTrackID(String trackID){
		hash.put("trackid", trackID);
	}
	
	public String getTrackID(){
		return String.valueOf(hash.get("trackid"));
	}
	
	public void setLongtitude(String longtitude){
		hash.put("longtitude",longtitude);
	}
	
	public String getLongtitude(){
		return String.valueOf(hash.get("longtitude"));
	}
	
	public void setLattitude(String lat){
		hash.put("lattitude", lat);
	}
	
	public String getLattitude(){
		return String.valueOf(hash.get("lattitude"));
	}
	
	public void setSpeed(String speed){
		hash.put("speed", speed);
	}
	
	public String getSpeed(){
		return String.valueOf(hash.get("speed"));
	}
}

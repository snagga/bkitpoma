package com.bkitmobile.poma.client.database;

import com.google.gwt.user.client.rpc.*;
import java.util.*;

public class Tracker extends Entity implements IsSerializable {
	/**
	 * HashMap that will always contain strings for both keys and values
	 * 
	 * @gwt.typeArgs <java.lang.String, java.lang.String>
	 */
	private HashMap trackerInfo;

	/**
	 * Default Constructor
	 */
	public Tracker() {
		trackerInfo = new HashMap();
		trackerInfo.put("username", "");
		trackerInfo.put("password", "");
		trackerInfo.put("name", "");
		trackerInfo.put("birthday", "");
		trackerInfo.put("tel", "");
		trackerInfo.put("addr", "");
		trackerInfo.put("email", "");
		trackerInfo.put("typeCus", "");
		trackerInfo.put("state", "");
		trackerInfo.put("gmt", "");
		trackerInfo.put("lang", "");
		trackerInfo.put("country","");
	}
	
	public Tracker(String username,String password,String name,String birthday,String tel,String addr,String email, String gmt,String lang,String country){
		trackerInfo = new HashMap();
		trackerInfo.put("username", username);
		trackerInfo.put("password", password);
		trackerInfo.put("name", name);
		trackerInfo.put("birthday", birthday);
		trackerInfo.put("tel", tel);
		trackerInfo.put("addr", addr);
		trackerInfo.put("email", email);
		trackerInfo.put("typeCus", "0");
		trackerInfo.put("state", "0");
		trackerInfo.put("gmt", gmt);
		trackerInfo.put("lang", lang);
		trackerInfo.put("country",country);
	}

	/**
	 * Method used to set the contact's name
	 * 
	 * @param name
	 *            contact's name
	 */
	public void setUsername(String name) {
		trackerInfo.put("username", name);
	}

	public void setPassword(String name) {
		trackerInfo.put("password", name);
	}
	
	public void setName(String name) {
		trackerInfo.put("name", name);
	}
	
	public void setBirthday(String name) {
		trackerInfo.put("birthday", name);
	}

	public void setTel(String name) {
		trackerInfo.put("tel", name);
	}

	public void setAddr(String name) {
		trackerInfo.put("addr", name);
	}
	
	public void setEmail(String name) {
		trackerInfo.put("email", name);
	}

	public void setTypeCus(String name) {
		trackerInfo.put("typeCus", name);
	}

	public void setState(String name) {
		trackerInfo.put("state", name);
	}
	
	public void setGMT(String gmt){
		trackerInfo.put("gmt", gmt);
	}
	
	public void setLang(String lang){
		trackerInfo.put("lang",lang);
	}
	
	public void setCountry(String country){
		trackerInfo.put("country", country);
	}
	
	public String getUsername() {
		return (String) trackerInfo.get("username");
	}

	public String getPassword() {
		return (String) trackerInfo.get("password");
	}
	
	public String getName() {
		return (String) trackerInfo.get("name");
	}
	
	public String getBirthday() {
		return (String) trackerInfo.get("birthday");
	}

	public String getTel() {
		return (String) trackerInfo.get("tel");
	}

	public String getAddr() {
		return (String) trackerInfo.get("address");
	}
	
	public String getEmail() {
		return (String) trackerInfo.get("email");
	}

	public String getTypeCus() {
		return (String) trackerInfo.get("typeCus");
	}

	public String getState() {
		return (String) trackerInfo.get("state");
	}
	
	public String getGMT(){
		return (String)trackerInfo.get("gmt");
	}
	
	public String getLang(){
		return (String)trackerInfo.get("lang");
	}
	
	public String getCountry(){
		return (String)trackerInfo.get("country");
	}

}// end class Contact

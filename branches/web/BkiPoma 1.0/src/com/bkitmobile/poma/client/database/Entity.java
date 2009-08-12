package com.bkitmobile.poma.client.database;
import java.util.Date;

public class Entity {
	public Date commitTime;
	
	public void commitCached(){
		commitTime = new Date();
	}
}

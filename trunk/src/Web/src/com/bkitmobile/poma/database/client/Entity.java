package com.bkitmobile.poma.database.client;
import java.util.Date;

public class Entity {
	public Date commitTime;
	
	public void commitCached(){
		commitTime = new Date();
	}
}

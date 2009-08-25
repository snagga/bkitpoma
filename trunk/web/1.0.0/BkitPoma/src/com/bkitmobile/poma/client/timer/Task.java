package com.bkitmobile.poma.client.timer;

public abstract class Task {
	private int clockCircle = 0;
	
	public Task(int clockCircle){
		this.clockCircle = clockCircle;
	}
	
	public abstract void execute();
	
	public void setClockCircle(int clockCircle) {
		this.clockCircle = clockCircle;
	}
	public int getClockCircle() {
		return clockCircle;
	}
}

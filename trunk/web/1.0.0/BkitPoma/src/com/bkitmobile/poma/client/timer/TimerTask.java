package com.bkitmobile.poma.client.timer;

import java.util.ArrayList;
import com.google.gwt.user.client.Timer;

public class TimerTask extends Timer {
	private ArrayList<Task> taskList = new ArrayList<Task>();
	private int clockCircle = 0;
	private int unitCircle = 500;
	
	public TimerTask() {
	}
	
	public TimerTask(int periodMillis) {
		this.unitCircle = periodMillis;
	}
	
	@Override
	public void run() {
		scheduleRepeating(unitCircle);
	}

	@Override
	public void scheduleRepeating(int periodMillis) {
		clockCircle++;
		for (Task task : taskList) {
			if (clockCircle % task.getClockCircle() == 0) {
				task.execute();
			}
		}
	}
	
	public void setTaskList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}

	public ArrayList<Task> getTaskList() {
		return taskList;
	}
}


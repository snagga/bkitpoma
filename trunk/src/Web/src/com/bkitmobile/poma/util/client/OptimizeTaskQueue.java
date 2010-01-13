package com.bkitmobile.poma.util.client;

import java.util.HashMap;

public class OptimizeTaskQueue extends HashMap<String, Task> {

	private static final String TRACKED_SHOW_HIDE = "showinmap";
	private static final String TRACKED_ACTIVE_DEACTIVE = "enable";
	
	public static final String showHideTracked(long trackedID) {
		return TRACKED_SHOW_HIDE + "#" + trackedID;
	}
	
	public static final String activeDeactiveTracked(long trackedID) {
		return TRACKED_ACTIVE_DEACTIVE + "#" + trackedID;
	}
	
	private TimerTask taskManager;
	
	private static final long serialVersionUID = -6738516957827287208L;
	
	public OptimizeTaskQueue(TimerTask taskManager) {
		this.taskManager = taskManager;
	}
	
	@Override
	public synchronized Task put(String key, Task value) {
		if (this.containsKey(key)) {
			remove(key);
		} else {
			taskManager.addTask(value);
		}
		return super.put(key, value);
	}
	
	@Override
	public synchronized void clear() {
		for (Task task:this.values()) {
			task.finish();
			taskManager.removeTask(task);
		}
		super.clear();
	}
	
	@Override
	public synchronized Task remove(Object key) {
		if (!this.containsKey(key)) {
			return null;
		}
		this.get(key).finish();
		return super.remove(key);
	}

	/**
	 * @param taskManager the taskManager to set
	 */
	public void setTaskManager(TimerTask taskManager) {
		this.taskManager = taskManager;
	}

	/**
	 * @return the taskManager
	 */
	public TimerTask getTaskManager() {
		return taskManager;
	}
}

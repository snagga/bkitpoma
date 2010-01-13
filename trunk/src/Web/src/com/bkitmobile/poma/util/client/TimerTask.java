package com.bkitmobile.poma.util.client;

import java.util.ArrayList;
import com.google.gwt.user.client.Timer;

/**
 * Represents a TimerTask - which helps us manage tasks with specified interval
 * 
 * @author Hieu Rocker
 */
public class TimerTask extends Timer {
	private ArrayList<Task> taskList = new ArrayList<Task>();
	private ArrayList<Task> removeTaskList = new ArrayList<Task>();
	private long clockCircle = 0;
	private int unitCircle = 500;
	private boolean isDone = true;

	/**
	 * Create a TimerTask instance with interval is 500ms
	 */
	public TimerTask() {
		scheduleRepeating(this.unitCircle);
	}

	/**
	 * Create a TimerTask instance with specified interval
	 * 
	 * @param periodMillis
	 *            TimerTask's interval in milliseconds
	 */
	public TimerTask(int periodMillis) {
		this.unitCircle = periodMillis;
		scheduleRepeating(this.unitCircle);
	}

	@Override
	public void run() {
		if (isDone) {
			isDone = false;
			removeTaskList.clear();
			clockCircle++;
			int size = taskList.size();

			for (int i = 0; i < size; i++) {
				Task task = taskList.get(i);
				if (task.getLazyCircleCount() < task.getLazyCircle()) {
					task.setLazyCircleCount(task.getLazyCircleCount() + 1);
					continue;
				}
				if (task.isPause())
					continue;

				if (task.isFinish()) {
					removeTaskList.add(task);
					continue;
				}
				if (clockCircle % task.getClockCircle() == 0) {
					task.execute();
					if (task.isFinish()) {
						removeTaskList.add(task);
					}
				}
			}
			for (Task task : removeTaskList) {
				taskList.remove(task);
			}
			isDone = true;
		}
	}

	/**
	 * Change the TaskList
	 * 
	 * @param taskList
	 *            TaskList is an <code>ArrayList&lt;Task&gt;</code>
	 */
	public void setTaskList(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}

	/**
	 * Get TaskList of this TimerTask
	 * 
	 * @return <code>ArrayList&lt;Task&gt;</code>
	 */
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public void removeTask(final Task task) {
		if (!removeTaskList.contains(task))
			removeTaskList.add(task);
	}

	public void addTask(final Task task) {
		if (!taskList.contains(task))
			taskList.add(task);
	}
}
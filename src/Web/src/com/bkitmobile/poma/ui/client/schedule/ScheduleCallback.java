package com.bkitmobile.poma.ui.client.schedule;

/**
 * Interface that provides the callback name so that it can be called once the apply operation
 * @author Tam Vo Minh
 *
 */
public interface ScheduleCallback {
	/**
	 * 
	 * @param schedule
	 */
	public void onApplyOperation(Schedule schedule);
}

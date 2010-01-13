package com.bkitmobile.poma.ui.client.schedule;

/**
 * Data for each schedule item
 * @author Hieu
 *
 */
public class ScheduleItem {
	public int begin;
	public int end;

	public ScheduleItem() {
	}

	public ScheduleItem(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	public String toString(boolean halfDay) {
		return Schedule.parseHour(this.begin, halfDay) + " -> "
				+ Schedule.parseHour(this.end, halfDay);
	}
}

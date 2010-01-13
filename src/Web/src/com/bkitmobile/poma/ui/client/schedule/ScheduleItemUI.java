package com.bkitmobile.poma.ui.client.schedule;

import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;

/**
 * 
 * @author Hieu
 *
 */
public class ScheduleItemUI extends Checkbox {
	private ScheduleItem scheduleItem;

	private Schedule _schedule;

	public ScheduleItemUI(ScheduleItem item, Schedule schedule) {
		super((item.begin >= 10 ? "" : "0") + item.begin + "h -> "
				+ (item.end >= 10 ? "" : "0") + item.end + "h");
		this.setScheduleItem(item);
		this._schedule = schedule;
		this.setTitle((item.begin >= 10 ? "" : "0") + item.begin + "h -> "
				+ (item.end >= 10 ? "" : "0") + item.end + "h");
		this.setCls("gwt-ScheduleItemUI");

		this.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				_schedule.setSchedule(getScheduleItem().begin, getScheduleItem().end - 1,
						checked);
			}
		});
	}

	/**
	 * @param schedule
	 *            the schedule to set
	 */
	public void setSchedule(Schedule schedule) {
		this._schedule = schedule;
	}

	/**
	 * @return the schedule
	 */
	public Schedule getSchedule() {
		return _schedule;
	}

	/**
	 * @param scheduleItem the scheduleItem to set
	 */
	public void setScheduleItem(ScheduleItem scheduleItem) {
		this.scheduleItem = scheduleItem;
	}

	/**
	 * @return the scheduleItem
	 */
	public ScheduleItem getScheduleItem() {
		return scheduleItem;
	}
}

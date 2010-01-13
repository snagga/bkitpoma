package com.bkitmobile.poma.ui.client.schedule;

import java.util.Date;
import java.util.Vector;

/**
 * 
 * @author Hieu
 *
 */
public class Schedule {
	private boolean[] scheduleTime = new boolean[] { true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true };
	private ScheduleListener listener;

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(ScheduleListener listener) {
		this.listener = listener;
	}

	/**
	 * Default constructor
	 */
	public Schedule() {
	}

	/**
	 * Constructor
	 * 
	 * @param bytes
	 *            :initial for array of bytes<li>!= 0 to track <li>0 to un-track
	 */
	public Schedule(byte[] bytes) {
		setSchedule(bytes);
	}

	public Schedule(boolean[] bytes) {
		setSchedule(bytes);
	}

	/**
	 * Constructor
	 * 
	 * @param startTime
	 *            : Start time
	 * @param endTime
	 *            : End time
	 * @param b
	 *            : true to track, false to un-track
	 */
	public Schedule(int startTime, int endTime, boolean b) {
		setSchedule(startTime, endTime, b);
	}

	/**
	 * Get schedule time with form array of byte<br/><u>Note</u><li>1 is choose
	 * for this time<li>0 is choose for this time
	 * 
	 * @return array of byte (24 byte)
	 */
	public byte[] toBytes() {
		byte[] b = new byte[24];
		for (int i = 0; i < 24; i++) {
			b[i] = (byte) (scheduleTime[i] ? 1 : 0);
		}
		return b;
	}

	public ScheduleItem[] getScheduleItems() {
		Vector<ScheduleItem> arr = new Vector<ScheduleItem>();
		int start = -1;
		int end = -1;
		for (int i = 0; i < 24; i++) {
			if (scheduleTime[i]) {
				start = start == -1 ? i : start;
				end = i;
				continue;
			}
			if (start <= end && end >= 0) {
				arr.add(new ScheduleItem(start, end + 1));
				start = -1;
				end = -1;
			}
		}
		if (start <= end && end >= 0) {
			arr.add(new ScheduleItem(start, end + 1));
			start = -1;
			end = -1;
		}
		ScheduleItem[] res = new ScheduleItem[arr.size()];
		System.out.println(res.length);
		arr.toArray(res);
		return res;
	}

	/**
	 * Get schedule time with form String<br/><u>Note</u><li>1 is choose for
	 * this time<li>0 is choose for this time
	 * 
	 * @return array of byte (24 byte)
	 */
	public String toString(boolean halfDay) {
		String s = "";
		for (ScheduleItem item : getScheduleItems()) {
			if (s.equals("")) {
				s = parseHour(item.begin, halfDay) + " -> "
						+ parseHour(item.end, halfDay);
			} else {
				s += ", " + parseHour(item.begin, halfDay) + " -> "
						+ parseHour(item.end, halfDay);
			}
		}
		return s;
	}

	public static void main(String[] args) {
		Schedule s = new Schedule(
				new boolean[] { true, true, true, true, false, true, false,
						true, true, false, true, false, true, true, false,
						true, false, true, true, false, true, false, true, true });
		System.out.println(s.toString(false));
	}

	public static String parseHour(int hours, boolean halfDay) {
		String s = "";
		if (halfDay) {
			int hour = hours == 12 || hours == 24 ? 12 : hours % 12;
			if (hour < 10) {
				s = "0";
			}
			s += hour + ":00" + (hours <= 12 ? "AM" : "PM");
		} else {
			if (hours < 10) {
				s = "0";
			}
			s += hours + ":00";
		}
		return s;
	}

	/**
	 * @param date
	 *            : date want to check
	 * @return<li> true indicate tracked will be tracked <li>false indicates
	 *             tracked will not be tracked
	 */
	public boolean isInSchedule(Date date) {
		int hour = date.getHours();
		int min = date.getMinutes();
		if (min == 0) {
			if (hour == 0) {
				return scheduleTime[0];
			} else {
				return scheduleTime[hour] || scheduleTime[hour - 1];
			}
		} else {
			return scheduleTime[hour];
		}
	}

	/**
	 * @return <li>true indicate tracked will be tracked <li>false indicates
	 *         tracked will not be tracked
	 */
	public boolean isInSchedule() {
		return isInSchedule(new Date());
	}

	public void setAt(int index, boolean b) {
		if (scheduleTime[index] == b)
			return;
		scheduleTime[index] = b;
		if (listener != null)
			listener.onChanged(index, b);
	}

	public boolean getAt(int index) {
		return scheduleTime[index];
	}

	public void setSchedule(byte[] bytes) {
		for (int i = 0; i < 24; i++) {
			setAt(i, bytes[i] == 0 ? false : true);
		}
	}
	
	public void setSchedule(boolean[] bytes) {
		for (int i = 0; i < bytes.length; i++)
			setAt(i, bytes[i]);
	}

	public void setSchedule(int startTime, int endTime, boolean b) {
		for (int i = startTime; i <= endTime; i++) {
			setAt(i, b);
		}
	}
}

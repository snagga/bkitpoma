package com.bkitmobile.poma.ui.client.schedule;

import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.util.client.Task;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

/**
 * 
 * @author Hieu
 * 
 */
public class ScheduleWindow extends Window {
	private boolean am = true;

	private static ScheduleWindow scheduleWindow;

	private ScheduleItemUI[] itemUIarr = new ScheduleItemUI[24];
	private CTracked tracked;
	private Schedule submitSchedule;
	private Schedule schedule;
	private Panel pnAM;
	private Panel pnPM;
	private Panel contentPanel;

	private ScheduleCallback callback;

	public static ScheduleWindow getInstance() {
		// setToSubmitScheduleTime();
		return scheduleWindow != null ? scheduleWindow : new ScheduleWindow();
	}

	// Label lb = new Label();

	public ScheduleWindow() {
		super();
		scheduleWindow = this;

		this.setLayout(new BorderLayout());
		this.setCloseAction(Window.HIDE);
		this.setButtonAlign(Position.CENTER);

		// this.add(lb, new BorderLayoutData(RegionPosition.SOUTH));
		Panel panelTop = new Panel();
		panelTop.setLayout(new HorizontalLayout(20));
		panelTop.setHeight(25);
		panelTop.setButtonAlign(Position.CENTER);
		Radio rdAM = new Radio("AM", new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				if (checked) {
					am = true;
					contentPanel.setActiveItemID(pnAM.getId());
				}
			}
		});

		Radio rdPM = new Radio("PM", new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				if (checked) {
					am = false;
					contentPanel.setActiveItemID(pnPM.getId());
				}
			}
		});

		rdAM.setName("am_pm");
		rdPM.setName("am_pm");
		panelTop.setBodyStyle("text-align:center;");
		panelTop.add(rdAM);
		rdAM.setChecked(true);
		panelTop.add(rdPM);

		this.add(panelTop, new BorderLayoutData(RegionPosition.NORTH));

		this.submitSchedule = new Schedule();
		this.schedule = new Schedule();
		// submitSchedule.setListener(new ScheduleListener() {
		// @Override
		// public void onChanged(int index, boolean b) {
		// lb.setText(submitSchedule.toString(true));
		// }
		// });

		_reset();

		pnAM = new Panel();
		pnAM.setLayout(new ColumnLayout());

		Panel pn1 = new Panel();
		pn1.setLayout(new VerticalLayout());
		for (int i = 0; i < 6; i++) {
			pn1.add(itemUIarr[i]);
		}

		Panel pn2 = new Panel();
		pn2.setLayout(new VerticalLayout());
		for (int i = 6; i < 12; i++) {
			pn2.add(itemUIarr[i]);
		}

		pnAM.add(pn1, new ColumnLayoutData(0.5));
		pnAM.add(pn2, new ColumnLayoutData(0.5));

		pnPM = new Panel();
		pnPM.setLayout(new ColumnLayout());

		Panel pn3 = new Panel();
		pn3.setLayout(new VerticalLayout());
		for (int i = 12; i < 18; i++) {
			pn3.add(itemUIarr[i]);
		}

		Panel pn4 = new Panel();
		pn4.setLayout(new VerticalLayout());
		for (int i = 18; i < 24; i++) {
			pn4.add(itemUIarr[i]);
		}

		pnPM.add(pn3, new ColumnLayoutData(0.5));
		pnPM.add(pn4, new ColumnLayoutData(0.5));

		contentPanel = new Panel();
		contentPanel.setLayout(new CardLayout());
		contentPanel.add(pnAM);
		contentPanel.add(pnPM);
		contentPanel.setActiveItemID(pnAM.getId());

		this.add(contentPanel, new BorderLayoutData(RegionPosition.CENTER));
		this.addButton(new Button("Apply", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				assignSchedule();
				hide();
				
				for (byte b: submitSchedule.toBytes()){
					
				}
				
				
				if (callback != null) {
					callback.onApplyOperation(submitSchedule);
				}
			}
		}));

		this.addButton(new Button("Reset", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				submitSchedule.setSchedule(schedule.toBytes());
				setToSubmitScheduleTime();
			}
		}));

		this.addButton(new Button("Cancel", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				scheduleWindow.hide();
			}
		}));

		this.setHeight(205);
		this.setWidth(300);
	}

	public void reset() {
		UserSettings.timerTask.getTaskList().add(new Task(2) {
			@Override
			public void execute() {
				_reset();
				finish();
			}
		});
	}

	private void _reset() {
//		this.submitSchedule.setSchedule(schedule.toBytes());
		for (int i = 0; i < itemUIarr.length; i++) {
			if (itemUIarr[i] == null)
				itemUIarr[i] = new ScheduleItemUI(new ScheduleItem(i, i + 1),
						submitSchedule);
			itemUIarr[i].getScheduleItem().begin = i;
			itemUIarr[i].getScheduleItem().end = i + 1;
			itemUIarr[i].setChecked(submitSchedule.getAt(i));
		}
	}

	private void assignSchedule() {
	}

	/**
	 * @param tracked
	 *            the tracked to set
	 */
	public void setTracked(CTracked tracked) {
		this.schedule = new Schedule(tracked.getSchedule());
		this.submitSchedule = new Schedule(this.schedule.toBytes());
		this.tracked = tracked;
	}

	/**
	 * @param schedule
	 *            the schedule to set
	 */
	public void setSchedule(Schedule schedule) {
		this.submitSchedule = schedule;
		this.schedule = new Schedule(submitSchedule.toBytes());
	}

	/**
	 * @return the schedule
	 */
	public Schedule getSubmitSchedule() {
		return submitSchedule;
	}

	public void show(ScheduleCallback callback) {
		this.callback = callback;
		super.show();
	}

	private void setToSubmitScheduleTime() {
		for (int i = 0; i < 24; i++) {
			itemUIarr[i].setChecked(submitSchedule.getAt(i));
		}
	}
}
package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.localization.MapToolbarConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ProgressBar;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.menu.Adapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.MenuListenerAdapter;

public class MapToolbar extends Toolbar {

	public enum ProgressStatus {
		PLAY, PAUSE, STOP,
	}

	private static final int PROGRESS_DELAY = 1000;
	private static final String IMAGE_PLAY = "images/MapToolbar/play.gif";
	private static final String IMAGE_PAUSE = "images/MapToolbar/pause.gif";
	private static final String IMAGE_STOP = "images/MapToolbar/stop.gif";

	private MapToolbarConstants mapToolbarConstants = GWT
			.create(MapToolbarConstants.class);

	private NumberField ratioField1;
	private NumberField ratioField2;
	private CheckItem ratioItem_1_1;
	private CheckItem ratioItem_1_2;
	private CheckItem ratioItem_1_4;
	private CheckItem ratioItem_1_8;
	private CheckItem ratioCustomItem;
	ToolbarButton maxButton;
	private int speed = 5;
	private ProgressStatus progressStatus;
	private Timer progressTimer;
	private ToolbarButton playButton;
	private ToolbarButton pauseButton;
	private ToolbarButton stopButton;
	private CheckItem speedItem5;
	private CheckItem speedItem10;
	private CheckItem speedItem30;
	private CheckItem speedItem60;
	private CheckItem speedItem100;
	private ToolbarButton speedButton;
	private Menu ratioMenu;
	private ToolbarButton ratioButton;
	private Menu speedMenu;
	private Panel customRatioPanel;
	private Label countDownLabel;

	public MapToolbar() {
		init();

		addListener();

		progressTimer.scheduleRepeating(PROGRESS_DELAY);
	}

	private void init() {
		playButton = new ToolbarButton(mapToolbarConstants.playButton());
		playButton.setIcon(IMAGE_PLAY);
		playButton.setDisabled(true);

		pauseButton = new ToolbarButton(mapToolbarConstants.pauseButton());
		pauseButton.setIcon(IMAGE_PAUSE);

		stopButton = new ToolbarButton(mapToolbarConstants.stopButton());
		stopButton.setIcon(IMAGE_STOP);

		ratioButton = new ToolbarButton(mapToolbarConstants.ratioButton());

		ratioMenu = new Menu();

		ratioItem_1_1 = new CheckItem("1:1");
		ratioItem_1_2 = new CheckItem("1:2");
		ratioItem_1_4 = new CheckItem("1:4");
		ratioItem_1_8 = new CheckItem("1:8");
		ratioCustomItem = new CheckItem(mapToolbarConstants.customRatio());

		ratioItem_1_1.setChecked(true);
		ratioItem_1_1.setGroup("ratio");
		ratioItem_1_2.setGroup("ratio");
		ratioItem_1_4.setGroup("ratio");
		ratioItem_1_8.setGroup("ratio");
		ratioCustomItem.setGroup("ratio");

		customRatioPanel = new Panel();
		customRatioPanel.setLayout(new HorizontalLayout(20));
		customRatioPanel.setBorder(false);
		customRatioPanel.setPaddings(5);

		ratioField1 = new NumberField();
		ratioField2 = new NumberField();
		ratioField1.setWidth(30);
		ratioField2.setWidth(30);
		customRatioPanel.add(ratioField1);
		customRatioPanel.add(new Label(":"));
		customRatioPanel.add(ratioField2);
		customRatioPanel.setDisabled(true);

		Adapter adapter = new Adapter(customRatioPanel);

		ratioMenu.addItem(ratioItem_1_1);
		ratioMenu.addItem(ratioItem_1_2);
		ratioMenu.addItem(ratioItem_1_4);
		ratioMenu.addItem(ratioItem_1_8);

		ratioMenu.addSeparator();
		ratioMenu.addItem(ratioCustomItem);
		ratioMenu.addItem(adapter);

		ratioButton.setMenu(ratioMenu);

		this.addButton(playButton);
		this.addButton(pauseButton);
		this.addButton(stopButton);
		this.addSeparator();
		this.addButton(ratioButton);
		this.addSeparator();

		speedMenu = new Menu();
		speedItem5 = new CheckItem("5", true);
		speedItem5.setTitle("5");
		speedItem10 = new CheckItem("10");
		speedItem10.setTitle("10");
		speedItem30 = new CheckItem("30");
		speedItem30.setTitle("30");
		speedItem60 = new CheckItem("60");
		speedItem60.setTitle("60");
		speedItem100 = new CheckItem("100");
		speedItem100.setTitle("100");

		speedItem5.setGroup("speed");
		speedItem10.setGroup("speed");
		speedItem30.setGroup("speed");
		speedItem60.setGroup("speed");
		speedItem100.setGroup("speed");

		speedMenu.addItem(speedItem5);
		speedMenu.addItem(speedItem10);
		speedMenu.addItem(speedItem30);
		speedMenu.addItem(speedItem60);
		speedMenu.addItem(speedItem100);

		speedButton = new ToolbarButton("Speed");
		speedButton.setText("5");
		speedButton.setMenu(speedMenu);

		addButton(speedButton);
		addText("s");
		addSpacer();

		addText("Elappse: ");

		countDownLabel = new Label(speed + "");
		this.addElement(countDownLabel.getElement());

		maxButton = new ToolbarButton();
		maxButton.setIcon("images/MapToolbar/max.gif");
		this.addFill();
		this.addButton(maxButton);

		progressStatus = ProgressStatus.PLAY;
	}

	private void addListener() {
		playButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				setProgressStatus(ProgressStatus.PLAY);
				playButton.setDisabled(true);
				pauseButton.setDisabled(false);
				stopButton.setDisabled(false);
			}
		});

		pauseButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				setProgressStatus(ProgressStatus.PAUSE);
				playButton.setDisabled(false);
				pauseButton.setDisabled(true);
				stopButton.setDisabled(false);
			}
		});

		stopButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				setProgressStatus(ProgressStatus.STOP);
				playButton.setDisabled(false);
				pauseButton.setDisabled(true);
				stopButton.setDisabled(true);
			}
		});

		ratioCustomItem.addListener(new CheckItemListenerAdapter() {

			@Override
			public void onCheckChange(CheckItem item, boolean checked) {
				customRatioPanel.setDisabled(!ratioCustomItem.isChecked());
			}
		});

		speedMenu.addListener(new MenuListenerAdapter() {
			@Override
			public void onItemClick(BaseItem item, EventObject e) {
				// TODO Auto-generated method stub
				speedButton.setText(item.getTitle());
				speed = Integer.parseInt(item.getTitle());
			}
		});

		progressTimer = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				int currentTime = Integer.parseInt(countDownLabel.getElement()
						.getInnerText());
				currentTime = (currentTime + speed - 1) % (speed);
				countDownLabel.getElement().setInnerText(currentTime + "");
			}
		};
	}

	public int[] getRatioIsChecked() {
		int[] ratio = new int[2];
		if (ratioItem_1_1.isChecked()) {
			ratio[0] = 1;
			ratio[1] = 1;
		} else if (ratioItem_1_2.isChecked()) {
			ratio[0] = 1;
			ratio[1] = 2;
		} else if (ratioItem_1_4.isChecked()) {
			ratio[0] = 1;
			ratio[1] = 4;
		} else if (ratioItem_1_8.isChecked()) {
			ratio[0] = 1;
			ratio[1] = 8;
		} else if (ratioCustomItem.isChecked()) {
			String ratioText1 = ratioField1.getText();
			String ratioText2 = ratioField2.getText();
			if (ratioText1.equals("") || ratioText2.equals("")) {
				ratioText1 = "1";
				ratioText2 = "1";
				ratioField1.setValue("1");
				ratioField2.setValue("1");
			}
			ratio[0] = Integer.parseInt(ratioText1);
			ratio[1] = Integer.parseInt(ratioText2);
		}
		return ratio;
	}

	private void setProgressStatus(ProgressStatus status) {
		progressStatus = status;
		switch (progressStatus) {
		case PLAY:
			progressTimer.scheduleRepeating(PROGRESS_DELAY);
			break;
		case PAUSE:
			progressTimer.cancel();
			break;
		case STOP:
			progressTimer.cancel();
			break;
		}
	}
}

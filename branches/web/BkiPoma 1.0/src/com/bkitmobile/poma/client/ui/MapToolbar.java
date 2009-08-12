package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.localization.MapToolbarConstants;
import com.google.gwt.core.client.GWT;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.NumberField;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.menu.Adapter;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;

public class MapToolbar extends Toolbar {

	private static final String IMAGE_PLAY = "images/MapToolbar/play.gif";
	private static final String IMAGE_PAUSE = "images/MapToolbar/pause.gif";
	private static final String IMAGE_STOP = "images/MapToolbar/stop.gif";
	
	MapToolbarConstants mapToolbarConstants = GWT.create(MapToolbarConstants.class);
	
	private NumberField ratioField1;
	private NumberField ratioField2;
	private NumberField speedField;
	CheckItem ratioItem_1_1;
	CheckItem ratioItem_1_2;
	CheckItem ratioItem_1_4;
	CheckItem ratioItem_1_8;
	CheckItem ratioCustomItem;
	ToolbarButton maxButton;

	public MapToolbar() {

		ToolbarButton playButton = new ToolbarButton(mapToolbarConstants.playButton());
		playButton.setIcon(IMAGE_PLAY);
		playButton.setDisabled(true);

		ToolbarButton pauseButton = new ToolbarButton(mapToolbarConstants.pauseButton());
		pauseButton.setIcon(IMAGE_PAUSE);

		ToolbarButton stopButton = new ToolbarButton(mapToolbarConstants.stopButton());
		stopButton.setIcon(IMAGE_STOP);

		final ToolbarButton ratioButton = new ToolbarButton(mapToolbarConstants.ratioButton());

		final Menu ratioMenu = new Menu();

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

		final Panel customPanel = new Panel();
		customPanel.setLayout(new HorizontalLayout(20));
		customPanel.setBorder(false);
		customPanel.setPaddings(5);

		ratioField1 = new NumberField();
		ratioField2 = new NumberField();
		ratioField1.setWidth(30);
		ratioField2.setWidth(30);
		customPanel.add(ratioField1);
		customPanel.add(new Label(":"));
		customPanel.add(ratioField2);
		customPanel.setDisabled(true);

		ratioCustomItem.addListener(new CheckItemListenerAdapter() {

			@Override
			public void onCheckChange(CheckItem item, boolean checked) {
				customPanel.setDisabled(!ratioCustomItem.isChecked());
			}
		});

		Adapter adapter = new Adapter(customPanel);

		ratioMenu.addItem(ratioItem_1_1);
		ratioMenu.addItem(ratioItem_1_2);
		ratioMenu.addItem(ratioItem_1_4);
		ratioMenu.addItem(ratioItem_1_8);

		ratioMenu.addSeparator();
		ratioMenu.addItem(ratioCustomItem);
		ratioMenu.addItem(adapter);

		ratioButton.setMenu(ratioMenu);
		
		speedField = new NumberField();
		speedField.setWidth(40);

		this.addButton(playButton);
		this.addButton(pauseButton);
		this.addButton(stopButton);
		this.addSeparator();
		this.addButton(ratioButton);
		this.addSeparator();
		this.addText(mapToolbarConstants.speedField());
		this.addField(speedField);
		
		maxButton = new ToolbarButton();
		maxButton.setIcon("images/MapToolbar/max.gif");
		
		this.addFill();
		this.addButton(maxButton);
		
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
}

package com.poma.bkitpoma.client;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;

public class MapToolbar extends Toolbar {
	
	public MapToolbar(){
		
		ToolbarButton playButton = new ToolbarButton("Play");
		playButton.setIcon("images/play.gif");
		
		ToolbarButton pauseButton = new ToolbarButton("Pause");
		pauseButton.setIcon("images/pause.gif");
		
		ToolbarButton stopButton = new ToolbarButton("Stop");
		stopButton.setIcon("images/stop.gif");
		
		ToolbarButton ratioButton = new ToolbarButton("Ratio");
		
		Menu ratioMenu = new Menu();
		
		CheckItem item_1_1 = new CheckItem("1:1");
		item_1_1.setGroup("ratio");
		item_1_1.setChecked(true);
		CheckItem item_1_2 = new CheckItem("1:2");
		item_1_2.setGroup("ratio");
		CheckItem item_1_4 = new CheckItem("1:4");
		item_1_4.setGroup("ratio");
		CheckItem item_1_8 = new CheckItem("1:8");
		item_1_8.setGroup("ratio");
		ratioMenu.addItem(item_1_1);
		ratioMenu.addItem(item_1_2);
		ratioMenu.addItem(item_1_4);
		ratioMenu.addItem(item_1_8);
		
		ratioButton.setMenu(ratioMenu);
		
		this.addButton(playButton);
		this.addButton(pauseButton);
		this.addButton(stopButton);
		this.addSeparator();
		this.addButton(ratioButton);
	}

}

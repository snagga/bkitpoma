package com.bkitmobile.poma.client.ui;

/* 
 * GWT-Ext Widget Library 
 * Copyright 2007 - 2008, GWT-Ext LLC., and individual contributors as indicated 
 * by the @authors tag. See the copyright.txt in the distribution for a 
 * full listing of individual contributors. 
 * 
 * This is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation; either version 3 of 
 * the License, or (at your option) any later version. 
 * 
 * This software is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * Lesser General Public License for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this software; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org. 
 */

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.*;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.*;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class SchedulePopUpWindow extends Window {
	private HTML[][] data = null;
	private MemoryProxy proxy = null;
	private Store store = null;

	public SchedulePopUpWindow() {
		this.setTitle("Config Schedule for Tracked");
		this.setIconCls("paste-icon");
		this.setPaddings(5, 5, 5, 0);
		this.setSize(600, 400);
//		this.setAutoWidth(true);
//		this.setAutoHeight(true);
		this.setAutoScroll(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setLayout(new FitLayout());
		this.setModal(true);

		this.addTool(new Tool(Tool.REFRESH, new Function() {
			public void execute() {
				//Clear Table here
				selectScheduleDataAllWith(data, "o");
				store.load();
			}
		}, "Clear"));
		
		this.addTool(new Tool(Tool.GEAR, new Function() {
			public void execute() {
				//Select all table here
				selectScheduleDataAllWith(data, "x");
				store.load();
			}
		}, "Select All"));

		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("first"), new StringFieldDef("10015"),
				new StringFieldDef("11530"), new StringFieldDef("13045"),
				new StringFieldDef("14560"), new StringFieldDef("20015"),
				new StringFieldDef("21530"), new StringFieldDef("23045"),
				new StringFieldDef("24560"),

		});

		data = getCompanyData();
		proxy = new MemoryProxy(data);

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();

		ColumnConfig ccFirst = new ColumnConfig("Time", "first", 30);
		ccFirst.setSortable(false);
		ccFirst.setResizable(false);

		ColumnConfig cc10015 = new ColumnConfig("+0:00-0:15", "10015", 30);
		cc10015.setSortable(false);
		cc10015.setResizable(false);

		ColumnConfig cc11530 = new ColumnConfig("+0:15-0:30", "11530", 30);
		cc11530.setSortable(false);
		cc11530.setResizable(false);

		ColumnConfig cc13045 = new ColumnConfig("+0:45-0:60", "13045", 30);
		cc13045.setSortable(false);
		cc13045.setResizable(false);

		ColumnConfig cc14560 = new ColumnConfig("+1:00-1:15", "14560", 30);
		cc14560.setSortable(false);
		cc14560.setResizable(false);

		ColumnConfig cc20015 = new ColumnConfig("+1:15-1:30", "20015", 30);
		cc20015.setSortable(false);
		cc20015.setResizable(false);

		ColumnConfig cc21530 = new ColumnConfig("+1:30-1:45", "21530", 30);
		cc21530.setSortable(false);
		cc21530.setResizable(false);

		ColumnConfig cc23045 = new ColumnConfig("+1:45-2:00", "23045", 30);
		cc23045.setSortable(false);
		cc23045.setResizable(false);

		ColumnConfig cc24560 = new ColumnConfig("+0:00-0:15", "24560", 30);
		cc24560.setSortable(false);
		cc24560.setResizable(false);

		BaseColumnConfig[] columns = new BaseColumnConfig[] {
				// column ID is company which is later used in
				// setAutoExpandColumn
				ccFirst, cc10015, cc11530, cc13045, cc14560, cc20015, cc21530,
				cc23045, cc24560 };

		ColumnModel columnModel = new ColumnModel(columns);

		GridPanel grid = new GridPanel();
		grid.setStore(store);
		grid.setColumnModel(columnModel);

		grid.setTitle("Grid with Numbered Rows and Force Fit");
		grid.setHeight(300);
		grid.setWidth(700);
		grid.setHeader(true);
		grid.setIconCls("grid-icon");
		grid.setDisableSelection(false);
		grid.setAutoDestroy(false);
		grid.setClosable(false);
		grid.setEnableColumnHide(false);
		grid.setEnableColumnMove(false);
		grid.setEnableDragDrop(false);

		grid.addGridCellListener(new GridCellListenerAdapter() {

			@Override
			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				// TODO Auto-generated method stub
				super.onCellClick(grid, rowIndex, colindex, e);
				// System.out.println("Click at: " + rowIndex + " " + colindex);
				setChange(data, rowIndex, colindex);
				// proxy = new MemoryProxy(data);
				// store.setDataProxy(proxy);
				store.load();
			}

		});
		GridView view = new GridView();
		view.setForceFit(true);
		grid.setView(view);

		this.add(grid);
	//	RootPanel.get().add(panel);
	}
	
	public String getStringBinarySchedule(){
		String result = "";
		for (int i=0;i<data.length;i++){
			for (int j=0;j<data[i].length;j++){
				if (data[i][j].getText().equals("o")){
					result += "0";
				}else{
					result += "1";
				}
			}
		}
		return result;
	}

	private HTML[][] getCompanyData() {
		return new HTML[][] {
				// new Object[]{"3m Co", new Double(72), new Double(02),
				// new Double(03), "9/1 12:00am", "MMM", "Manufacturing"},
				new HTML[] { new HTML("00:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("02:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("04:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("06:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("08:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("10:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("12:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("14:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("16:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("18:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("20:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") },
				new HTML[] { new HTML("22:00"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o"),
						new HTML("o"), new HTML("o"), new HTML("o") } };
	}
	
	private static void selectScheduleDataAllWith(HTML[][] data,String text){
		for (int i=0;i<data.length;i++){
			for (int j=1;j<data[i].length;j++){
				data[i][j].setHTML(text);
			}
		}
	}
	
	private static void setChange(HTML[][] arrHTML, int rowIdx, int columnIdx) {
		// arrHTML[rowIdx][columnIdx].setHTML(
		// "<html><head><style type=\"text/css\">body{background: 00ff00}</style></head><body>Tam</body></html>"
		// );
		arrHTML[rowIdx][columnIdx].setHTML("Tam");
	}
}
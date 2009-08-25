package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.localization.LRegisterTrackedForm;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

/**
 * <code>SchedulePopUpWindow</code> used when tracker username register new
 * tracked object. It let tracker username to config schedule time to mange
 * tracked object
 */
public class ScheduleWindow extends Window {
	private MyHTML[][] data = null;
	private MemoryProxy proxy = null;
	private Store store = null;
	private GridPanel grid = null;

	public ScheduleWindow(LRegisterTrackedForm local) {
		// Initalize for SchedulePopUpWindow's Window
		this.setTitle(local.window_schedule_title());
		this.setIconCls("paste-icon");
		this.setPaddings(5, 5, 5, 0);
		this.setSize(600, 400);
		this.setAutoScroll(true);
		this.setMaximizable(false);
		this.setResizable(false);
		this.setLayout(new FitLayout());
		this.setModal(true);

		// Column index name to display column name respectively
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("first"), new StringFieldDef("10015"),
				new StringFieldDef("11530"), new StringFieldDef("13045"),
				new StringFieldDef("14560"), new StringFieldDef("20015"),
				new StringFieldDef("21530"), new StringFieldDef("23045"),
				new StringFieldDef("24560"), new StringFieldDef("allrow")

		});

		data = getData();
		proxy = new MemoryProxy(data);

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();

		// Column name and column index name
		ColumnConfig ccFirst = new ColumnConfig(local.first_column_lbl(),
				"first", 30);
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

		ColumnConfig ccAllRow = new ColumnConfig("", "allrow", 10);
		cc24560.setSortable(false);
		cc24560.setResizable(false);

		BaseColumnConfig[] columns = new BaseColumnConfig[] { ccFirst, cc10015,
				cc11530, cc13045, cc14560, cc20015, cc21530, cc23045, cc24560,
				ccAllRow };

		ColumnModel columnModel = new ColumnModel(columns);

		// Create and Initialize for grid panel
		grid = new GridPanel();
		grid.setStore(store);
		grid.setColumnModel(columnModel);
		grid.setTitle(local.grid_title());
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

		// Add clear button to grid panel
		grid.addTool(new Tool(Tool.REFRESH, new Function() {
			public void execute() {
				// Clear Table here
				selectScheduleAllDataWith(false);
				store.load();
			}
		}, local.tool_clear()));

		// Add 'select all' button to grid panel
		grid.addTool(new Tool(Tool.GEAR, new Function() {
			public void execute() {
				// Select all table here
				selectScheduleAllDataWith(true);
				store.load();
			}
		}, local.tool_select_all()));

		grid.addGridCellListener(new GridCellListenerAdapter() {

			@Override
			public void onCellClick(GridPanel grid, int rowIndex, int colindex,
					EventObject e) {
				super.onCellClick(grid, rowIndex, colindex, e);
				System.out.println(rowIndex + " " + colindex);
				if (colindex != 0 && colindex != 9) {
					data[rowIndex][colindex]
							.setChecked(!data[rowIndex][colindex].getChecked());
					// proxy = new MemoryProxy(data);
					// store.setDataProxy(proxy);
					store.load();
				} else if (colindex == 9) {
					boolean tmp = data[rowIndex][1].getChecked();
					for (int i = 1; i < 9; i++) {
						data[rowIndex][i].setChecked(!tmp);
						store.load();
					}
				}

			}

		});
		GridView view = new GridView();
		view.setForceFit(true);
		grid.setView(view);

		this.add(grid);
	}

	/**
	 * Get schedule time for this tracked
	 * 
	 * @return byte array <li><b>0</b> This time will not track <li><b>1</b>
	 *         This time will track
	 */
	public byte[] getStringBinarySchedule() {
		byte[] result = new byte[96];
		int idx = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 1; j < data[i].length - 1; j++) {
				if (data[i][j].getChecked()) {
					result[idx++] = 1;
				} else {
					result[idx++] = 0;
				}
			}
		}
		return result;
	}

	private class MyHTML extends HTML {
		static final String CHOOSE = "X";
		static final String UNCHOOSE = "O";
		boolean checked = true;

		public MyHTML() {
			super(CHOOSE);
		}

		public MyHTML(String txt) {
			super(txt);
		}

		public void setChecked(boolean b) {
			checked = b;
			if (b)
				this.setText(CHOOSE);
			else
				this.setText(UNCHOOSE);
		}

		public boolean getChecked() {
			return this.checked;
		}
	}

	/**
	 * Initial data
	 * @return
	 */
	private MyHTML[][] getData() {
		return new MyHTML[][] {
				// new xbject[]{"3m Cx", new Dxuble(72), new Dxuble(02),
				// new Dxuble(03), "9/1 12:00am", "MMM", "Manufacturing"},
				new MyHTML[] { new MyHTML("00:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("02:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("04:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("06:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("08:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("10:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("12:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("14:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("16:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("18:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("20:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") },
				new MyHTML[] { new MyHTML("22:00"), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML(), new MyHTML(),
						new MyHTML(), new MyHTML(), new MyHTML("V") } };
	}

	private void selectScheduleAllDataWith(boolean b) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 1; j < data[i].length - 1; j++) {
				data[i][j].setChecked(b);
			}
		}
	}
}
package com.bkitmobile.poma.admin.client.ui;

import java.util.ArrayList;

import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.BooleanFieldDef;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.PagingToolbar;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.EditorGridPanel;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListenerAdapter;

public class EditableTablePanel extends Panel {
	public enum ColumnWidthModeEnum {
		AUTO_WIDTH, NONAUTO_WIDTH, BASED_CONTENT, MANUALLY
	}

	public static final int FIELD_DATE = 0;
	public static final int FIELD_STRING = 1;
	public static final int FIELD_BOOLEAN = 2;

	private static final String DATE_FORMAT = "m/d/Y";
	private static final int CHARACTER_WIDTH = 8;

	private PagingMemoryProxy proxy;
	private boolean pagingEnable = true;
	private EditorGridPanel gridPanel;
	private Store store;
	protected PagingToolbar pagingToolbar;
	private int pageSize;
	private ArrayReader reader;
	private ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
	private RecordDef recordDef;
	// private boolean isNewData = true;
	private ColumnConfig columnConfigs[];
	private ColumnModel columnModel;
	private ColumnWidthModeEnum columnWidthMode = ColumnWidthModeEnum.AUTO_WIDTH;
	private int[] maxLength;
	private Field[] fields;
	private GridView gridView;
	private String[] columnHeader;
	private int[] columnFieldType;
	private ArrayList<Integer> uneditFields;

	// private ListPanelConstants listPanelConstants = GWT
	// .create(ListPanelConstants.class);

	/**
	 * Create a grid panel
	 * 
	 * @param columnFieldType
	 *            - an int array to define the type of columns
	 * @param columnHeader
	 *            - an String array that will be display in columns' headers
	 */
	public EditableTablePanel(int columnFieldType[], String columnHeader[],
			ArrayList<Integer> uneditFields, int pageSize) {
		this.columnHeader = columnHeader;
		this.columnFieldType = columnFieldType;
		this.uneditFields = uneditFields;
		this.pageSize = pageSize;
		initial();
	}

	/**
	 * Create a grid panel
	 * 
	 * @param columnFieldType
	 *            - an int array to define the type of columns
	 * @param columnHeader
	 *            - an String array that will be display in columns' headers
	 * @param data
	 *            - data for the grid panel
	 */
	public EditableTablePanel(int[] columnFieldType, String[] columnHeader,
			ArrayList<Integer> uneditFields, String[][] data, int pageSize) {
		this(columnFieldType, columnHeader, uneditFields, pageSize);
		for (int i = 0; i < data.length; i++) {
			ArrayList<String> row = new ArrayList<String>();
			for (int j = 0; j < data[i].length; j++) {
				row.add(data[i][j]);
			}
			this.data.add(row);
		}

		proxy = new PagingMemoryProxy(data);
		store.setDataProxy(proxy);
	}

	/**
	 * The construtor for the Panel
	 * 
	 * @param columnFieldType
	 *            - an int array to define the type of columns
	 * @param columnHeader
	 *            - an String array that will be display in columns' headers
	 * @param fields
	 *            when you want to edit a cell
	 */
	private void initial() {

		gridPanel = new EditorGridPanel();
		gridView = new GridView();
		gridPanel.setView(gridView);
		fields = new Field[columnFieldType.length];

		recordDef = new RecordDef(getRecordDef(columnFieldType));
		reader = new ArrayReader(recordDef);

		store = new Store(reader);
		columnConfigs = getColumnConfigs();
		columnModel = new ColumnModel(columnConfigs);

		gridPanel.setColumnModel(columnModel);

		gridPanel.setStore(store);
		add(gridPanel);

		setHeight(150);
		setGridHeight(130);
		setWidth("100%");
		setGridWidth("100%");

		setCollapsible(true);

		pagingToolbar = new PagingToolbar(store);

		setBottomToolbar(pagingToolbar);

		gridPanel.setLoadMask(true);

		this.addListener(new PanelListenerAdapter() {
			@Override
			public void onResize(BoxComponent component, int adjWidth,
					int adjHeight, int rawWidth, int rawHeight) {
				gridPanel.setWidth(adjWidth);
				if (columnWidthMode == ColumnWidthModeEnum.AUTO_WIDTH) {
					setColumnWidthFitToPanel();
				}
			}

			@Override
			public void onRender(Component component) {
				// TODO Auto-generated method stub
				setPageSize(pageSize);

				if (columnWidthMode == ColumnWidthModeEnum.AUTO_WIDTH) {
					setColumnWidthFitToPanel();
				}
			}
		});

		maxLength = new int[columnConfigs.length];
		for (int i = 0; i < maxLength.length; i++) {
			maxLength[i] = columnModel.getColumnHeader(i + "").length();
		}

		// pagingToolbar.setBeforePageText(listPanelConstants.beforePageText());
		// pagingToolbar.setPrevText(listPanelConstants.previousText());
		// pagingToolbar.setFirstText(listPanelConstants.firstPageText());
		// pagingToolbar.setLastText(listPanelConstants.lastPageText());
		// pagingToolbar.setNextText(listPanelConstants.nextText());
		// pagingToolbar.setRefreshText(listPanelConstants.refreshText());
		// pagingToolbar.setAfterPageText(listPanelConstants.afterPageText());
	}

	public void addGridCellListener(GridCellListenerAdapter adapter) {
		gridPanel.addGridCellListener(adapter);
	}

	/**
	 * set the Item per Page for the grid
	 * 
	 * @param pageSize
	 *            - number of item per page
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;

		if (data.size() > 0) {
			if (pagingEnable) {
				pagingToolbar.setPageSize(pageSize);
			} else {
				store.load();
			}
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Set grid inside with the given width
	 * 
	 * @param width
	 *            - int
	 */
	public void setGridWidth(int width) {
		gridPanel.setWidth(width);
	}

	/**
	 * Set grid inside with the given width
	 * 
	 * @param width
	 *            - String
	 */
	public void setGridWidth(String width) {
		gridPanel.setWidth(width);
	}

	/**
	 * Set grid inside with the given height
	 * 
	 * @param height
	 *            - int
	 */
	public void setGridHeight(int height) {
		gridPanel.setHeight(height);
	}

	/**
	 * Set grid inside with the given height
	 * 
	 * @param height
	 *            - String
	 */
	public void setGridHeight(String height) {
		gridPanel.setHeight(height);
	}

	/**
	 * Set the visibility of the PagingToolbar
	 * 
	 * @param enable
	 *            - boolean
	 */
	public void setPagingToolbarEnable(boolean enable) {
		pagingEnable = enable;
		setPageSize(pageSize);
		pagingToolbar.setVisible(pagingEnable);
	}

	/**
	 * Set the data in grid with the given data
	 * 
	 * @param data
	 *            - String[][]
	 */
	public void setData(String[][] data) {
		// if (isNewData == true) {
		this.data.clear();
		for (int i = 0; i < data.length; i++) {
			ArrayList<String> row = new ArrayList<String>();
			for (int j = 0; j < data[i].length; j++) {
				row.add(data[i][j]);
			}
			this.data.add(row);
		}
		// findMaxColumnLengths();
		// } else {
		// isNewData = true;
		// }

		proxy = new PagingMemoryProxy(data);
		store.setDataProxy(proxy);
		setPageSize(pageSize);
		store.load();
		


		// if (columnWidthMode == ColumnWidthModeEnum.BASED_CONTENT) {
		// setColumnWidthBasedOnContent();
		// }
	}

	/**
	 * Set data in grid with the given data
	 * 
	 * @param data
	 *            - ArrayList<ArrayList<String>>
	 */
	private void setData(ArrayList<ArrayList<String>> data) {
		String[][] dataInString = new String[data.size()][];
		for (int i = 0; i < dataInString.length; i++) {
			ArrayList<String> row = data.get(i);
			dataInString[i] = new String[row.size()];
			for (int j = 0; j < dataInString[i].length; j++) {
				dataInString[i][j] = row.get(j);
			}
		}
		// isNewData = false;
		setData(dataInString);
	}

	/**
	 * Append a record to the end of the data
	 * 
	 * @param data
	 *            - String[]
	 */
	public void addRecord(String[] data) {
		addRecord(data, store.getTotalCount());
	}

	/**
	 * Insert a record into a given position
	 * 
	 * @param data
	 *            - String[]
	 * @param position
	 *            - int
	 */
	public void addRecord(String data[], int position) {
		ArrayList<String> newRow = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			newRow.add(data[i]);
		}
		this.data.add(position, newRow);
		setData(this.data);

		boolean isChange = false;
		for (int i = 0; i < newRow.size(); i++) {
			if (maxLength[i] < newRow.get(i).length()) {
				isChange = true;
				maxLength[i] = newRow.get(i).length();
			}
		}
		if (isChange && columnWidthMode == ColumnWidthModeEnum.BASED_CONTENT) {
			setColumnWidthBasedOnContent();
		}
	}

	/**
	 * Remove a record at the given position
	 * 
	 * @param position
	 *            - int
	 */
	public void removeRecord(int position) {
		if (position < data.size()) {
			ArrayList<String> row = data.remove(position);
			setData(data);

			boolean isChange = false;
			for (int i = 0; i < row.size(); i++) {
				if (maxLength[i] == row.get(i).length()) {
					isChange = true;
					break;
				}
			}

			if (isChange) {
				findMaxColumnLengths();
				if (columnWidthMode == ColumnWidthModeEnum.BASED_CONTENT) {
					setColumnWidthBasedOnContent();
				}
			}
		}
	}

	/**
	 * Return the number of record in grid
	 * 
	 * @return an int
	 */
	public int getRecordsCount() {
		return data.size();
	}

	/**
	 * Return a number of column in grid
	 * 
	 * @return an int
	 */
	public int getColumnCount() {
		if (data.size() == 0) {
			return 0;
		}
		return data.get(0).size();
	}

	/**
	 * Set a given index of column that will be auto expand, default index is 0.
	 * Cannot modify the auto expand column of the grid after the web page have
	 * rendered
	 * 
	 * @param index
	 *            - int
	 */
	public void setAutoExpandColumn(int index) {
		gridPanel.setAutoExpandColumn(index + "");
	}

	/**
	 * Get the grid panel
	 * 
	 * @return GridPanel
	 */
	public EditorGridPanel getEditorGridPanel() {
		return gridPanel;
	}

	/**
	 * Filter the grid with the given filter
	 * 
	 * @param columnIndex
	 *            - index of column that will be filtered
	 * @param filter
	 *            - String that will use to filter the grid
	 * @param anyMatch
	 *            - if true, columns of records have the filter in it will be
	 *            filter, if false, only records have columns' content equal
	 *            with the filter will be filter
	 */
	public void filter(int columnIndex, String filter, boolean anyMatch) {
		ArrayList<ArrayList<String>> filterRecord = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < data.size(); i++) {
			ArrayList<String> row = data.get(i);
			if (anyMatch) {
				if (row.get(columnIndex).indexOf(filter) != -1) {
					filterRecord.add(row);
				}
			} else {
				if (row.get(columnIndex).equals(filter)) {
					filterRecord.add(row);
				}
			}
		}
		setData(filterRecord);
	}

	/**
	 * Clear the filter of grid
	 */
	public void clearFilter() {
		setData(data);
	}

	/**
	 * chua hoan thien
	 */
	public void addGridRowListener(GridRowListenerAdapter adapter) {
		gridPanel.addGridRowListener(adapter);
	}

	/**
	 * Set the column auto width mode to column width fit to the panel mode
	 */
	public void setColumnWidthFitToPanel() {
		columnWidthMode = ColumnWidthModeEnum.AUTO_WIDTH;

		int columnWidth = getWidth() / columnConfigs.length;
		for (int i = 0; i < columnConfigs.length; i++) {
			columnModel.setColumnWidth(i + "", columnWidth);
			columnConfigs[i].setResizable(true);
		}
	}

	/**
	 * Set the column auto width mode to column width based on content mode
	 */
	public void setColumnWidthBasedOnContent() {
		columnWidthMode = ColumnWidthModeEnum.BASED_CONTENT;

		for (int i = 0; i < maxLength.length; i++) {
			columnModel.setColumnWidth(i + "", maxLength[i] * CHARACTER_WIDTH);
			columnConfigs[i].setResizable(false);
		}
	}

	/**
	 * Turn off auto column width mode
	 */
	public void setColumnWidthNonAuto() {
		columnWidthMode = ColumnWidthModeEnum.NONAUTO_WIDTH;
		for (int i = 0; i < columnConfigs.length; i++) {
			columnConfigs[i].setResizable(true);
		}
	}

	/**
	 * Creator: Tam </br> User can set Width of column by insert arr size </br>
	 * <b>Note: </b> <li>If size of sizes is not matched with the number of
	 * column header throws Exception <li>If the sum of all sizes member is not
	 * equal with the width of table panel, the last colum will get remaining
	 * spaces
	 */
	public void setColumnWidthManully(int[] sizes, boolean resizable)
			throws Exception {
		columnWidthMode = ColumnWidthModeEnum.MANUALLY;
		if (columnConfigs.length != sizes.length) {
			throw new Exception(
					"Size of sizes do not match with the number of header");
		}
		for (int i = 0; i < columnConfigs.length; i++) {
			columnConfigs[i].setResizable(resizable);
			columnModel.setColumnWidth(i + "", sizes[i]);
		}

	}

	/**
	 * Find the max lengths content of columns, use for auto column width based
	 * on content mode
	 */
	private void findMaxColumnLengths() {
		for (int i = 0; i < data.size(); i++) {
			ArrayList<String> row = data.get(i);
			for (int j = 0; j < row.size(); j++) {
				int currentLength = row.get(j).length();
				if (maxLength[j] < currentLength) {
					maxLength[j] = currentLength;
				}
			}
		}
	}

	/**
	 * Set the column width with the given mode
	 * 
	 * @param mode
	 *            - ColumnWidthModeEnum
	 */
	public void setColumnWidthMode(ColumnWidthModeEnum mode) {
		columnWidthMode = mode;

		switch (columnWidthMode) {
		case AUTO_WIDTH:
			setColumnWidthFitToPanel();
			break;
		case BASED_CONTENT:
			setColumnWidthBasedOnContent();
			break;
		case NONAUTO_WIDTH:
			setColumnWidthNonAuto();
			break;
		}
	}

	public ArrayList<String> getRecord(int index) {
		return data
				.get((pagingToolbar.getCurrentPage() - 1) * pageSize + index);
	}

	public ArrayList<String> getAbsoluteRecord(int index) {
		return data.get(index);
	}

	public int getCurrentPage() {
		return pagingToolbar.getCurrentPage();
	}

	public Field getFieldAt(int colIndex) {
		return fields[colIndex];
	}

	private FieldDef[] getRecordDef(int[] columnFieldType) {
		FieldDef[] fieldDef = new FieldDef[columnFieldType.length];
		for (int i = 0; i < columnFieldType.length; i++) {
			try {
				fieldDef[i] = getFieldDef(columnFieldType[i], i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fieldDef;
	}

	private FieldDef getFieldDef(int fieldType, int position) throws Exception {
		switch (fieldType) {
		case FIELD_STRING:
			return new StringFieldDef(position + "");
		case FIELD_DATE:
			return new DateFieldDef(position + "", DATE_FORMAT);
		case FIELD_BOOLEAN:
			return new BooleanFieldDef(position + "");
		default:
			throw new Exception("Field Type is incorrect");
		}
	}

	private ColumnConfig[] getColumnConfigs() {
		ColumnConfig columnConfig[] = new ColumnConfig[columnHeader.length];
		for (int i = 0; i < columnConfig.length; i++) {
			columnConfig[i] = new ColumnConfig(columnHeader[i], i + "", 20);
			switch (columnFieldType[i]) {
			case FIELD_STRING:
				if (!uneditFields.contains(i)) {
					fields[i] = new TextField();
					columnConfig[i].setEditor(new GridEditor(fields[i]));
				}
				break;
			case FIELD_DATE:
				if (!uneditFields.contains(i)) {
					DateField dateField = new DateField();
					dateField.setFormat(DATE_FORMAT);
					fields[i] = dateField;
					columnConfig[i].setEditor(new GridEditor(fields[i]));
				}
				break;
			case FIELD_BOOLEAN:
				columnConfig[i].setRenderer(new Renderer() {
					@Override
					public String render(Object value,
							CellMetadata cellMetadata, Record record,
							int rowIndex, int colNum, Store store) {
						boolean checked = ((Boolean) value).booleanValue();
						return "<img class=\"checkbox\" src=\"js/ext/resources/images/default/menu/"
								+ (checked ? "checked.gif" : "unchecked.gif")
								+ "\"/>";
					}
				});
				break;
			default:
				throw new UnsupportedOperationException(
						"This mode is not supportted now!");
			}

		}
		return columnConfig;
	}
}

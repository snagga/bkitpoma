/*
 Copyright (c) 2007-2009 WebAppShowCase DBA Appcloem (http://www.appcloem.com). All rights reserved.
Apache Software License 2.0
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL WebAppShowCase
OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.appcloem.jiql.gwt.jiqladmin.client;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.Viewport;

import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;

import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

import java.util.Vector;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.PagingOptions;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.ui.client.layout.GridLayoutData;
import com.google.gwt.gen2.table.client.CachedTableModel;
import org.gwt.mosaic.ui.client.table.PagingScrollTable2;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import org.gwt.mosaic.ui.client.Label;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.Constants;
import com.appcloem.gwt.client.AppcloemEntryPoint;
import com.appcloem.gwt.client.GlobalObjects;
import com.appcloem.gwt.client.QMap;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jiqladmin extends AppcloemEntryPoint {

	static LanguageConstantsWithLookup langP;
	static Label statusL;

	public static Label getStatusLabel() {
		return statusL;
	}

	public static LanguageConstantsWithLookup getLanguageProperties() {
		return langP;
	}

	private final ClickListener clickListener = new ClickListener() {
		public void onClick(Widget sender) {
			final Button btn = (Button) sender;
			InfoPanel.show(btn.getHTML(), "Stop poking me!");
		}
	};

	static JiqlAdminServiceAsync calService1 = null;

	public static JiqlAdminServiceAsync getService() {
		return calService1;
	}

	public void setLabel(String name, ButtonBase bb) {
		ButtonCallback lab = new ButtonCallback(bb);
		getLabel(name, lab);
		// MessageBox.alert(Window.getTitle(), lab.getLabel() + ":" + name);

	}

	public static LayoutPanel getConentPanel() {
		return content;
	}

	public static LayoutPanel getQueryPanel() {
		return content1;
	}

	public String getLabel(String name) {
		LabelCallback lab = new LabelCallback();
		getLabel(name, lab);
		// MessageBox.alert(Window.getTitle(), lab.getLabel() + ":" + name);

		return lab.getLabel();

	}

	public void getLabel(final String name, AsyncCallback lab) {

		// final StringBuffer lab = new StringBuffer();

		calService1.getLabel(name, lab);
		/*
		 * calService1.getLabel(name,new AsyncCallback<String>() { public void
		 * onFailure(Throwable caught) { MessageBox.alert(Window.getTitle(),
		 * name + ":" + caught.toString()); }
		 * 
		 * public void onSuccess(String v) {
		 * 
		 * lab.append(v);
		 * 
		 * }
		 * 
		 * }
		 * 
		 * 
		 * 
		 * );
		 */
		// return lab.toString();
	}

	// QueryResult qr = null;
	CaptionLayoutPanel grd = null;
	Vector<String> cols = new Vector<String>();
	int rowCount = 0;
	int pageSize = 5;
	boolean hasResult = false;

	boolean hasResult() {
		return hasResult;
	}

	int getRowCount() {
		return rowCount;
	}

	int getPageSize() {
		return pageSize;
	}

	boolean rcomplete = false;

	public void setResult(QueryResult qr) {
		hasResult = qr.hasResult();
		rowCount = qr.getRowCount();
		pageSize = qr.getPageSize();
		cols = qr.getColumnNames();
		rcomplete = true;

		QueryTableModel tableModel = new QueryTableModel(this);

		String coln = null;
		String colv = null;
		QMap row = null;
		if (hasResult()) {

			tableModel.setZeroModeEnabled(false);
			tableModel.setRPCModeEnabled(true);
		}

		CachedTableModel cachedTableModel = new CachedTableModel<QMap>(
				tableModel);
		cachedTableModel.setPreCachedRowCount(getRowCount());
		cachedTableModel.setPostCachedRowCount(0);
		cachedTableModel.setRowCount(getRowCount());
		DefaultTableDefinition tableDefinition = new DefaultTableDefinition<QMap>();
		String cn = null;
		for (int ct = 0; ct < cols.size(); ct++) {
			cn = cols.elementAt(ct);
			QueryColumnDefinition<String> columnDef = new QueryColumnDefinition<String>(
					cn) {
				@Override
				public String getCellValue(QMap rowValue) {
					return rowValue.get(getName());
				}

				@Override
				public void setCellValue(QMap rowValue, String cellValue) {
					rowValue.put(getName(), cellValue);
				}
			};
			columnDef.setMinimumColumnWidth(cn.length());
			columnDef.setColumnSortable(false);
			columnDef.setColumnTruncatable(false);
			tableDefinition.addColumnDefinition(columnDef);
		}

		PagingScrollTable2 scrollTable = new PagingScrollTable2<QMap>(
				cachedTableModel, tableDefinition);

		if (!hasResult) {
			scrollTable.setEmptyTableWidget(new HTML(getLanguageProperties()
					.noDataToDisplay()));
		}
		scrollTable.setPageSize(getPageSize());

		scrollTable.setCellPadding(3);
		scrollTable.setCellSpacing(0);

		scrollTable
				.setResizePolicy(PagingScrollTable2.ResizePolicy.UNCONSTRAINED);

		final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
				Orientation.VERTICAL));
		layoutPanel.setPadding(0);

		layoutPanel.add(scrollTable, new BoxLayoutData(FillStyle.BOTH));
		PagingOptions pagingOptions = new PagingOptions(scrollTable);
		layoutPanel.add(new WidgetWrapper(pagingOptions), new BoxLayoutData(
				FillStyle.HORIZONTAL, true));

		LayoutPanel grpl = new LayoutPanel(new BorderLayout());

		grd.add(layoutPanel);
		scrollTable.reloadPage();
		scrollTable.gotoFirstPage();

		statusL = new Label(" ");
		// statusL.setVisible(false);
		content1.add(statusL);
		// ,new BorderLayoutData(BorderLayout.Region.CENTER)
		// content.add(grd, new BoxLayoutData(FillStyle.BOTH));

		grpl.add(grd, new BorderLayoutData(true));
		content1.add(grpl, new BoxLayoutData(FillStyle.BOTH));

		// , new BorderLayoutData(true)
		RootPanel.get().add(viewport);

		scrollTable.reloadPage();
		scrollTable.gotoFirstPage();

		/*
		 * Vector<String> cols = qr.getColumnNames(); QueryTableModel tableModel
		 * = new QueryTableModel(this);
		 * 
		 * Vector<QMap> rows = qr.getRows(); String coln = null; String colv =
		 * null; QMap row = null; if (qr.hasResult()){
		 * 
		 * tableModel.setZeroModeEnabled(false);
		 * tableModel.setRPCModeEnabled(true); } CachedTableModel
		 * cachedTableModel = new CachedTableModel<QMap>(tableModel);
		 * cachedTableModel.setPreCachedRowCount(qr.getRowCount());
		 * cachedTableModel.setPostCachedRowCount(0);
		 * cachedTableModel.setRowCount(qr.getRowCount());
		 * DefaultTableDefinition tableDefinition = new
		 * DefaultTableDefinition<QMap>(); String cn = null; for (int ct = 0;ct
		 * < cols.size();ct++) { cn = cols.elementAt(ct);
		 * QueryColumnDefinition<String> columnDef = new
		 * QueryColumnDefinition<String>( cn) {
		 * 
		 * @Override public String getCellValue(QMap rowValue) { return
		 * rowValue.get(getName()); }
		 * 
		 * @Override public void setCellValue(QMap rowValue, String cellValue) {
		 * rowValue.put(getName(),cellValue); } };
		 * columnDef.setMinimumColumnWidth(cn.length());
		 * columnDef.setColumnSortable(false);
		 * columnDef.setColumnTruncatable(false);
		 * tableDefinition.addColumnDefinition(columnDef); }
		 * 
		 * 
		 * PagingScrollTable2 scrollTable = new
		 * PagingScrollTable2<QMap>(cachedTableModel,tableDefinition);
		 * scrollTable.setPageSize(qr.getPageSize());
		 * 
		 * scrollTable.setCellPadding(3); scrollTable.setCellSpacing(0);
		 * 
		 * 
		 * scrollTable.setResizePolicy(PagingScrollTable2.ResizePolicy.UNCONSTRAINED
		 * );
		 * 
		 * 
		 * final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
		 * Orientation.VERTICAL)); layoutPanel.setPadding(0);
		 * 
		 * layoutPanel.add(scrollTable); PagingOptions pagingOptions = new
		 * PagingOptions(scrollTable); layoutPanel.add(new
		 * WidgetWrapper(pagingOptions), new BoxLayoutData(
		 * FillStyle.HORIZONTAL, true));
		 * 
		 * grd.add(layoutPanel); scrollTable.reloadPage();
		 * scrollTable.gotoFirstPage();
		 */

	}

	// JiqlAdminService calService2 = null;
	/**
	 * This is the entry point method.
	 */
	Viewport viewport = null;
	static LayoutPanel content = null;
	static LayoutPanel content1 = null;
	LayoutPanel mainP;

	public void onModuleLoad() {
		super.onModuleLoad();
		langP = (LanguageConstantsWithLookup) GWT
				.create(LanguageConstantsWithLookup.class);
		calService1 = (JiqlAdminServiceAsync) GWT
				.create(JiqlAdminService.class);
		ServiceDefTarget target = (ServiceDefTarget) calService1;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "jiqlwebadmin";
		target.setServiceEntryPoint(moduleRelativeURL);

		GlobalObjects.setAGWTServiceAsync(calService1);

		viewport = new Viewport();

		mainP = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		mainP.add(MenuMgr.create());

		content = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		// final LayoutPanel content = new LayoutPanel(new BoxLayout(
		// Orientation.VERTICAL));
		content1 = new LayoutPanel(new BoxLayout(Orientation.VERTICAL));
		content.setPadding(20);
		content.setWidgetSpacing(10);
		content1.setPadding(20);
		content1.setWidgetSpacing(10);
		content.add(content1, new BoxLayoutData(FillStyle.BOTH));
		mainP.add(content, new BoxLayoutData(FillStyle.BOTH));
		viewport.add(mainP);

		LayoutPanel toolBox = new LayoutPanel(new BoxLayout(
				Orientation.HORIZONTAL));
		// HorizontalPanel toolBox = new HorizontalPanel();
		// toolBox.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolBox.setPadding(10);
		toolBox.setWidgetSpacing(5);
		// toolBox.setBorderWidth(0);
		// toolBox.setSpacing(0);

		TextBox t = new TextBox();
		t.setHeight("20px");
		t.setVisibleLength(80);
		// HorizontalPanel hp = new HorizontalPanel();
		toolBox.add(t);
		// ClickHandler qph = new QueryClickHandler(t);
		grd = new CaptionLayoutPanel("Results");

		Button b = new Button("",
				new QueryClickListener(t, grd, this, viewport));
		b.setHeight("20px");
		setLabel("Query", b);
		toolBox.add(b);
		/*
		 * HorizontalPanel toolBox2 = new HorizontalPanel();
		 * toolBox2.setHeight("30px");
		 * toolBox2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		 * toolBox2.setSpacing(50); toolBox2.add(toolBox);
		 */

		content1.add(toolBox);
		// new BorderLayoutData(BorderLayout.Region.NORTH)
		// CaptionLayoutPanel grd = new CaptionLayoutPanel("Results");
		grd.setPadding(20);
		grd.setWidgetSpacing(10);

		QueryCallback2 cb = new QueryCallback2(this);
		calService1.getQuery(cb);

		/*
		 * QueryCallback2 cb = new QueryCallback2(this);
		 * calService1.getQuery(cb);
		 * 
		 * 
		 * Timer tt = new Timer() { int ctt = 0;
		 * 
		 * @Override public void run() {
		 * 
		 * 
		 * } };
		 * 
		 * // Schedule the timer to run once in 5 seconds. for (int ct = 0;ct <
		 * 40;ct++) { if (rcomplete)break;
		 * 
		 * 
		 * tt.scheduleRepeating(1000); }
		 * 
		 * tt.cancel();
		 * 
		 * if (!rcomplete) MessageBox.alert(Window.getTitle(), "TIMEOUT");
		 */
		try {

			// QueryResult qr2 = calService1.getQuery();
			// setResult(qr2);
		} catch (Throwable e) {
			MessageBox.alert(Window.getTitle(), e.toString());

		}

		// content.add(grd, new BorderLayoutData(true));
		// RootPanel.get().add(viewport);

		/*
		 * QueryTableModel tableModel = new QueryTableModel(this);
		 * 
		 * //Vector<QMap> rows = qr.getRows(); String coln = null; String colv =
		 * null; QMap row = null; if (hasResult()){
		 * 
		 * tableModel.setZeroModeEnabled(false);
		 * tableModel.setRPCModeEnabled(true); }
		 * 
		 * CachedTableModel cachedTableModel = new
		 * CachedTableModel<QMap>(tableModel);
		 * cachedTableModel.setPreCachedRowCount(getRowCount());
		 * cachedTableModel.setPostCachedRowCount(0);
		 * cachedTableModel.setRowCount(getRowCount()); DefaultTableDefinition
		 * tableDefinition = new DefaultTableDefinition<QMap>(); String cn =
		 * null; for (int ct = 0;ct < cols.size();ct++) { cn =
		 * cols.elementAt(ct); QueryColumnDefinition<String> columnDef = new
		 * QueryColumnDefinition<String>( cn) {
		 * 
		 * @Override public String getCellValue(QMap rowValue) { return
		 * rowValue.get(getName()); }
		 * 
		 * @Override public void setCellValue(QMap rowValue, String cellValue) {
		 * rowValue.put(getName(),cellValue); } };
		 * columnDef.setMinimumColumnWidth(cn.length());
		 * columnDef.setColumnSortable(false);
		 * columnDef.setColumnTruncatable(false);
		 * tableDefinition.addColumnDefinition(columnDef); }
		 * 
		 * 
		 * PagingScrollTable2 scrollTable = new
		 * PagingScrollTable2<QMap>(cachedTableModel,tableDefinition);
		 * 
		 * if (!hasResult) { scrollTable.setEmptyTableWidget(new HTML(
		 * "There is no data to display")); }
		 * scrollTable.setPageSize(getPageSize());
		 * 
		 * scrollTable.setCellPadding(3); scrollTable.setCellSpacing(0);
		 * 
		 * 
		 * scrollTable.setResizePolicy(PagingScrollTable2.ResizePolicy.UNCONSTRAINED
		 * );
		 * 
		 * 
		 * final LayoutPanel layoutPanel = new LayoutPanel(new BoxLayout(
		 * Orientation.VERTICAL)); layoutPanel.setPadding(0);
		 * 
		 * layoutPanel.add(scrollTable,new BoxLayoutData(FillStyle.BOTH));
		 * PagingOptions pagingOptions = new PagingOptions(scrollTable);
		 * layoutPanel.add(new WidgetWrapper(pagingOptions), new BoxLayoutData(
		 * FillStyle.HORIZONTAL, true));
		 * 
		 * grd.add(layoutPanel); scrollTable.reloadPage();
		 * scrollTable.gotoFirstPage();
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * content.add(grd, new BorderLayoutData(true));
		 * 
		 * // RootPanel.get().add(viewport);
		 * 
		 * scrollTable.reloadPage(); scrollTable.gotoFirstPage();
		 */

	}

}
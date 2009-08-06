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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import org.gwt.mosaic.forms.client.layout.CellConstraints;
import org.gwt.mosaic.forms.client.layout.FormLayout;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.HTML;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.ToolBar;

import org.gwt.mosaic.ui.client.Caption;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.CollapsedListener;
import org.gwt.mosaic.ui.client.Caption.CaptionRegion;
import org.gwt.mosaic.ui.client.layout.BorderLayout;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BorderLayout.Region;
import org.gwt.mosaic.ui.client.CaptionLayoutPanel;
import org.gwt.mosaic.ui.client.ImageButton;
import org.gwt.mosaic.ui.client.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import org.gwt.mosaic.ui.client.Viewport;
import java.util.List;
import org.gwt.mosaic.ui.client.table.ScrollTable2;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;
import java.util.Set;
import com.appcloem.gwt.client.StatusMgr;
import com.appcloem.gwt.client.QMap;

public class TablesMgr {

static LayoutPanel mainP = null;

//static Viewport mainP = null;
static CaptionLayoutPanel westPanel = null;
static CaptionLayoutPanel centerPanel = null;


public static Widget createMenu(){

 final ToolBar toolBar = new ToolBar();
//QUERY TABLES IMPORT
    ToolButton b1 = new ToolButton(Jiqladmin.getLanguageProperties().create(),new CreateButtonHandler());
    //pushButton.addClickHandler(this);
    toolBar.add(b1);
    toolBar.addSeparator();

    b1 = new ToolButton(Jiqladmin.getLanguageProperties().drop(),new DropTableButtonHandler());
    //pushButton.addClickHandler(this);
    toolBar.add(b1);
    toolBar.addSeparator();
    b1 = new ToolButton(Jiqladmin.getLanguageProperties().export());
    //pushButton.addClickHandler(this);
    toolBar.add(b1);
    return toolBar;

}
public static void show(){
show(null);
}
public static void show(String selT){
 			 StatusMgr.show();
StatusMgr.appendln(Jiqladmin.getLanguageProperties().loading());
 if (mainP == null){
//	mainP = buildButtonBar1Panel();
   //mainP = new Viewport();
   mainP = new LayoutPanel();
  final LayoutPanel lp = new LayoutPanel(new BorderLayout());
    lp.setPadding(0);


   // final CaptionLayoutPanel northPanel = new CaptionLayoutPanel("");
      final LayoutPanel northPanel = new LayoutPanel();

    //northPanel.add();


	northPanel.add(createMenu());
    lp.add(northPanel, new BorderLayoutData(Region.NORTH,
        0.07, true));
   // lp.setCollapsed(northPanel, true);*/






    westPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().tables());
    final ImageButton collapseBtn3 = new ImageButton(
        Caption.IMAGES.toolCollapseLeft());
    westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
    //westPanel.add(new Label(Jiqladmin.getLanguageProperties().noTablesFound()));

    collapseBtn3.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        lp.setCollapsed(westPanel, !lp.isCollapsed(westPanel));
        lp.layout();
      }
    });

    lp.add(westPanel, new BorderLayoutData(Region.WEST,
        0.2, true));





    centerPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().details());
    //centerPanel.getHeader().add(Showcase.IMAGES.gwtLogoThumb().createImage());
    centerPanel.add(new WidgetWrapper(new HTML(Jiqladmin.getLanguageProperties().pleaseCreateTable())));

  //  lp.add(centerPanel, new BorderLayoutData( true));
    lp.add(centerPanel, new BorderLayoutData(Region.EAST,
        0.75, true));
	mainP.add(lp);
	//mainP.layout();

}

Jiqladmin.getConentPanel().clear();
Jiqladmin.getConentPanel().add(mainP,new BoxLayoutData(FillStyle.BOTH));
Jiqladmin.getConentPanel().layout();

ShowTablesCallback cb = new ShowTablesCallback(selT);
 		 Jiqladmin.getService().showTables(cb);

}

public static void describeTable(){
	Set<java.lang.Integer> srs = dataTable.getSelectedRows();
	Integer st = (Integer)srs.toArray()[0] ;
	//MessageBox.alert(Window.getTitle(), st.toString());
	StatusMgr.show();
	StatusMgr.appendln(Jiqladmin.getLanguageProperties().fetchingDetails());
	DescribeTableCallback cb = new DescribeTableCallback();
 		 Jiqladmin.getService().describeTable(st,cb);


}

public static String getSelectedTable(){
		if (dataTable == null)
			return null;
	Set<java.lang.Integer> srs = dataTable.getSelectedRows();
	if (srs == null || srs.toArray().length < 1)return null;
	Integer st = (Integer)srs.toArray()[0] ;
	return dataTable.getText(st.intValue(), 0);



}

public static void postDrop(){
	StatusMgr.hide();
	 show();


}

public static void Drop(String t){
StatusMgr.show();
	StatusMgr.appendln(Jiqladmin.getLanguageProperties().droppingTable() + " : " + t);
	DropTableCallback cb = new DropTableCallback();
 		 Jiqladmin.getService().dropTable(t,cb);

}
public static void describeTable(List<QMap> qm){
	//StatusMgr.hide();
	//MessageBox.alert(Window.getTitle(), qm.toString());
    /*
    descols.add("Field");
	descols.add("Type");
	descols.add("Null");
	descols.add("Key");
	descols.add("Default");
	*/
	try{

    FixedWidthGrid dTable = new FixedWidthGrid();
    FixedWidthFlexTable hTable = new FixedWidthFlexTable();
    hTable.setHTML(0, 0, "Field");
	hTable.setHTML(0, 1, "Type");
    hTable.setHTML(0, 2, "Null");
	hTable.setHTML(0, 3, "Key");
	hTable.setHTML(0, 4, "Default");

	String t = null;
	int beforeRow = 0;
	dTable.resizeColumns(5);
	QMap qmp = null;
	for (int ct = 0;ct < qm.size();ct ++)
	{
		qmp = qm.get(ct);
		beforeRow = dTable.insertRow(ct);
		dTable.setText(beforeRow, 0, qmp.get("Field"));
		dTable.setText(beforeRow, 1, qmp.get("Type"));
		dTable.setText(beforeRow, 2, qmp.get("Null"));
		dTable.setText(beforeRow, 3, qmp.get("Key"));
		dTable.setText(beforeRow, 4, qmp.get("Default"));

	}

		ScrollTable2 scrollTable = new ScrollTable2(dTable, hTable);
	 scrollTable.setMinimumColumnWidth(0, 50);
    scrollTable.setPreferredColumnWidth(0, 100);
    scrollTable.setColumnTruncatable(0, false);

	 scrollTable.setMinimumColumnWidth(1, 50);
    scrollTable.setPreferredColumnWidth(1, 100);
    scrollTable.setColumnTruncatable(1, false);

	 scrollTable.setMinimumColumnWidth(2, 50);
    scrollTable.setPreferredColumnWidth(2, 100);
    scrollTable.setColumnTruncatable(2, false);

	 scrollTable.setMinimumColumnWidth(3, 50);
    scrollTable.setPreferredColumnWidth(3, 100);
    scrollTable.setColumnTruncatable(3, false);

	 scrollTable.setMinimumColumnWidth(4, 50);
    scrollTable.setPreferredColumnWidth(4, 100);
    scrollTable.setColumnTruncatable(4, false);




    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    scrollTable.setHeight((qm.size()*(30 + 5)) + "px");


    ScrollPanel sp = new ScrollPanel();

    sp.setHeight("750px");
       sp.setWidth("415px");
	sp.add(scrollTable);

   	centerPanel.clear();
    centerPanel.add(sp);
    centerPanel.layout();
	StatusMgr.hide();

	}catch (Exception e)
	{
	StatusMgr.hide();
	MessageBox.alert(Window.getTitle(), e.toString());

	}

}
static  FixedWidthGrid dataTable = null;
public static void showTables(List<String> l,String selT){
	try{

	westPanel.clear();
	if (l.size() < 1)
	    westPanel.add(new Label(Jiqladmin.getLanguageProperties().noTablesFound()));

    else{

    dataTable = new FixedWidthGrid();
    dataTable.addRowSelectionHandler(new TableSelectHandler());
    FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
    headerTable.setHTML(0, 0, "");

	String t = null;
	int beforeRow = 0;
	dataTable.resizeColumns(1);
	int seltRow = 0;
	for (int ct = 0;ct < l.size();ct ++)
	{
		t = l.get(ct);
		beforeRow = dataTable.insertRow(ct);
		dataTable.setText(beforeRow, 0, t);
		    if (selT != null && selT.equals(t))
    			seltRow = ct;

	}
	ScrollTable2 scrollTable = new ScrollTable2(dataTable, headerTable);
	 scrollTable.setMinimumColumnWidth(0, 50);
    scrollTable.setPreferredColumnWidth(0, 100);
    scrollTable.setColumnTruncatable(0, false);

    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    scrollTable.setHeight(((l.size()*20) + 50) + "px");


    ScrollPanel sp = new ScrollPanel();

    sp.setHeight("350px");
       sp.setWidth("115px");
	sp.add(scrollTable);
    westPanel.add(sp);
    //if (selT == null)
    dataTable.selectRow(seltRow,true);
    }
    westPanel.layout();
	}catch (Exception e){
			 StatusMgr.hide();

		MessageBox.alert(Window.getTitle(), e.toString());
	}
	 StatusMgr.hide();

}
/*public static void show(){

if (mainP == null){
//	mainP = buildButtonBar1Panel();
  // mainP = new LayoutPanel(new BoxLayout(
    //    Orientation.HORIZONTAL));
     HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
    hSplit.ensureDebugId("cwHorizontalSplitPanel");
    hSplit.setSize("500px", "350px");
    hSplit.setSplitPosition("30%");


    final CaptionLayoutPanel westPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().tables());
    final ImageButton collapseBtn3 = new ImageButton(
        Caption.IMAGES.toolCollapseLeft());
    westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
    westPanel.add(new Label("Width: 20%"));




  final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().details());
    //centerPanel.getHeader().add(Showcase.IMAGES.gwtLogoThumb().createImage());
    centerPanel.add(new WidgetWrapper(new HTML("<h1>GWT Mosaic</h1>")));



    hSplit.setRightWidget(centerPanel);
    hSplit.setLeftWidget(westPanel);

    // Wrap the split panel in a decorator panel
    mainP = new DecoratorPanel();
    mainP.setWidget(hSplit);
	//mainP.add(decPanel,new BoxLayoutData(FillStyle.BOTH));

}

Jiqladmin.getConentPanel().clear();
Jiqladmin.getConentPanel().add(mainP);

}*/







/*
 *public static void show(){
 *if (mainP == null){
//	mainP = buildButtonBar1Panel();
   mainP = new LayoutPanel(new BoxLayout(
        Orientation.HORIZONTAL));
  final LayoutPanel lp = new LayoutPanel(new BorderLayout());
    lp.setPadding(0);


    final CaptionLayoutPanel northPanel = new CaptionLayoutPanel("");
   // final ImageButton collapseBtn1 = new ImageButton(
     //   Caption.IMAGES.toolCollapseUp());
    //northPanel.getHeader().add(collapseBtn1, CaptionRegion.RIGHT);
    northPanel.add(new Label("Height: 20%"));



    lp.add(northPanel, new BorderLayoutData(Region.NORTH,
        0.20, true));
    lp.setCollapsed(northPanel, true);



    final CaptionLayoutPanel southPanel = new CaptionLayoutPanel("South");
    final ImageButton collapseBtn2 = new ImageButton(
        Caption.IMAGES.toolCollapseDown());
    southPanel.getHeader().add(collapseBtn2, CaptionRegion.RIGHT);
    southPanel.add(new Label("Height: 20%"));



    lp.add(southPanel, new BorderLayoutData(Region.SOUTH,
        0.20, true));
    lp.setCollapsed(southPanel, true);




    final CaptionLayoutPanel westPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().tables());
    final ImageButton collapseBtn3 = new ImageButton(
        Caption.IMAGES.toolCollapseLeft());
    westPanel.getHeader().add(collapseBtn3, CaptionRegion.RIGHT);
    westPanel.add(new Label("Width: 20%"));

    collapseBtn3.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        lp.setCollapsed(westPanel, !lp.isCollapsed(westPanel));
        lp.layout();
      }
    });

    lp.add(westPanel, new BorderLayoutData(Region.WEST,
        0.2, true));


    final CaptionLayoutPanel eastPanel = new CaptionLayoutPanel("East");
    final ImageButton collapseBtn4 = new ImageButton(
        Caption.IMAGES.toolCollapseRight());
    eastPanel.getHeader().add(collapseBtn4, CaptionRegion.RIGHT);
    eastPanel.add(new Label("Width: 20%"));


    lp.add(eastPanel, new BorderLayoutData(Region.EAST,
        0.2, true));
    lp.setCollapsed(eastPanel, true);





   final CaptionLayoutPanel centerPanel = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().details());
    //centerPanel.getHeader().add(Showcase.IMAGES.gwtLogoThumb().createImage());
    centerPanel.add(new WidgetWrapper(new HTML("<h1>GWT Mosaic</h1>")));

    lp.add(centerPanel, new BorderLayoutData( true));

	mainP.add(lp,new BoxLayoutData(FillStyle.BOTH));

}

Jiqladmin.getConentPanel().clear();
Jiqladmin.getConentPanel().add(mainP,new BoxLayoutData(FillStyle.BOTH));

}*/

  /*private static LayoutPanel buildButtonBar1Panel() {
    final LayoutPanel buttonBar = new LayoutPanel(new FormLayout(
        "0:grow, p, 4px, p", "p"));
    buttonBar.setPadding(0);
    buttonBar.add(new Button(Jiqladmin.getLanguageProperties().submit()), new CellConstraints(4, 1));
    return wrap(buttonBar,
        Jiqladmin.getLanguageProperties().copyPaste());
  }


  private static LayoutPanel wrap(Widget buttonBar, String text) {
    TextArea textArea = new TextArea();
    textArea.setText(text);
    // textArea.setMargin(new Insets(6, 10, 4, 6));
    // Non-editable but shall use the editable background.
    //textArea.setReadOnly(true);
    // textArea.putClientProperty("JTextArea.infoBackground", Boolean.TRUE);
    // Component textPane = new JScrollPane(textArea);

    FormLayout layout = new FormLayout("fill:100dlu:grow",
        "fill:56dlu:grow, 4dlu, p");
    LayoutPanel panel = new LayoutPanel(layout);
    // panel.setBorder(Borders.DIALOG_BORDER);
    panel.setPadding(0);
    panel.add(textArea, CellConstraints.xy(1, 1));
    panel.add(buttonBar, CellConstraints.xy(1, 3));
    return panel;
  }*/



}
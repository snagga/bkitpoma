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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RadioButton;
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
import org.gwt.mosaic.ui.client.WindowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.appcloem.gwt.client.ClientUtil;
import com.google.gwt.gen2.table.override.client.FlexTable.FlexCellFormatter;



public class CreateTableMgr {

//Create new table on database
//Name:
//Number of fields

static LayoutPanel spl = null;
static WindowPanel modal = null;
static ScrollPanel sp = null;
static ListBox lb = null;
static TextBox tb = null;
static String table = null;

public static String getTable(){
	return table;
}
public static void clear(){
	//ta.setText("");
	spl.clear();
}


public static void show(){

if (modal == null){
	modal = new WindowPanel(Jiqladmin.getLanguageProperties().create());
    modal.setResizable(false);
    modal.setAnimationEnabled(true);
       LayoutPanel mainP = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
  mainP.setPadding(5);
    mainP.setWidgetSpacing(10);


    mainP.add(new HTML("<br/>"));


     sp = new ScrollPanel();
           sp.setHeight("450px");
       sp.setWidth("820px");

       spl = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
   spl.setHeight("2000px");
   spl.setWidth("1000px");
	sp.add(spl);
    //sp.add(ta);
	mainP.add(sp,new BoxLayoutData(FillStyle.HORIZONTAL ,true));
//    Button b = new Button(Jiqladmin.getLanguageProperties().submit());

  //LayoutPanel grpl = new LayoutPanel(new BorderLayout());
  mainP.add(new HTML("<br/><br/>"));
 //   mainP.add(new WidgetWrapper(b), new BoxLayoutData(
   //     FillStyle.HORIZONTAL, true));


modal.setWidget(mainP);


}
clear();

CaptionLayoutPanel	    grd = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().createNewTable());

    Grid grid = new Grid(2, 2);

        grid.setWidget(0, 0, new Label(Jiqladmin.getLanguageProperties().name()));
         grid.setWidget(1, 0, new Label(Jiqladmin.getLanguageProperties().numberOfFields()));
tb = new TextBox();
grid.setWidget(0, 1, tb);
 lb = new ListBox();
  for (int ct = 1;ct <= 100;ct++)
 	lb.addItem(String.valueOf(ct));
         grid.setWidget(1, 1, lb);

VerticalPanel vp = new VerticalPanel();
grd.add(vp);
vp.add(grid);
Button b = new Button(Jiqladmin.getLanguageProperties().Continue(),new ContinueCreateButtonHandler());
vp.add(b);
spl.add(grd);
modal.showModal();

}
public static void postSave(){
	StatusMgr.hide();
	 hide();
	 TablesMgr.show(table);


}
public static void Save(){
	try{
		String creatT = CreateField.parse();

	StatusMgr.show();
	StatusMgr.appendln(Jiqladmin.getLanguageProperties().saving());
	//String creatT = CreateField.parse();
	StatusMgr.appendln(creatT);
	SaveTableCallback cb = new SaveTableCallback();
 	Jiqladmin.getService().createTable(creatT,cb);

	}catch (Exception e){
	 //	hide();
	//StatusMgr.hide();

 	MessageBox.alert(Window.getTitle(), "SAVE:" + e.toString());

  	return;
	}

}
public static void Continue(){


clear();

CaptionLayoutPanel	    grd = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().createNewTable());

    Grid grid = new Grid(2, 2);

        grid.setWidget(0, 0, new Label(Jiqladmin.getLanguageProperties().name()));
         grid.setWidget(1, 0, new Label(Jiqladmin.getLanguageProperties().numberOfFields()));
        grid.setWidget(0, 1, new TextBox());

  table = tb.getText();
  if (!ClientUtil.validString(table)){
 	hide();
 	MessageBox.alert(Window.getTitle(), Jiqladmin.getLanguageProperties().invalidName());

  	return;
  }
        grid.setWidget(0, 1, new Label(table));
int ctf = lb.getSelectedIndex();
String fieldno = lb.getValue(ctf);

         grid.setWidget(1, 1,  new Label(fieldno));

VerticalPanel vp = new VerticalPanel();
grd.add(vp);
vp.add(grid);

 ctf = new Integer(fieldno).intValue();



    FixedWidthGrid dTable = new FixedWidthGrid();
    FixedWidthFlexTable hTable = new FixedWidthFlexTable();
    hTable.setHTML(0, 0, "Field");
	hTable.setHTML(0, 1, "Type");
	hTable.setHTML(0, 2, "Length");
    hTable.setHTML(0, 3, "Null");
	hTable.setHTML(0, 4, "Default");
	FlexCellFormatter headerFormatter = hTable.getFlexCellFormatter();
    hTable.setHTML(0, 5, Jiqladmin.getLanguageProperties().attribute());
    headerFormatter.setColSpan(0, 5, 3);


	//hTable.setHTML(0, 5, "PRIMARY");
	//hTable.setHTML(0, 6, "UNIQUE");
	//hTable.setHTML(0, 7, "---");

	String t = null;
	int beforeRow = 0;
	dTable.resizeColumns(8);
	CreateField.clear();
	CreateField crf = null;
	for (int ct = 0;ct < ctf;ct ++)
	{

crf = CreateField.add(ct);
		beforeRow = dTable.insertRow(ct);
		dTable.setWidget(beforeRow, 0, crf.getNameWidget());


		dTable.setWidget(beforeRow, 1, crf.getTypesWidget());

dTable.setWidget(beforeRow, 2, crf.getLengthWidget());


		dTable.setWidget(beforeRow, 3,crf.getNullsWidget());

		dTable.setWidget(beforeRow, 4, crf.getDefaultWidget());
		dTable.setWidget(beforeRow, 5, crf.getPrimaryWidget());
		dTable.setWidget(beforeRow, 6, crf.getUniqueWidget());
		dTable.setWidget(beforeRow, 7, crf.getNothingWidget());

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

	 scrollTable.setMinimumColumnWidth(5, 50);
    scrollTable.setPreferredColumnWidth(5, 100);
    scrollTable.setColumnTruncatable(5, false);

	 scrollTable.setMinimumColumnWidth(6, 50);
    scrollTable.setPreferredColumnWidth(6, 100);
    scrollTable.setColumnTruncatable(6, false);

	 scrollTable.setMinimumColumnWidth(7, 50);
    scrollTable.setPreferredColumnWidth(7, 100);
    scrollTable.setColumnTruncatable(7, false);



    // Setup the formatting
    scrollTable.setCellPadding(3);
    scrollTable.setCellSpacing(0);
    scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);

    scrollTable.setHeight("300px");
    scrollTable.setWidth("770px");


vp.add(scrollTable);









Button b = new Button(Jiqladmin.getLanguageProperties().save(),new SaveTableButtonHandler());
vp.add(b);








spl.add(grd);
//modal.showModal();

}


/*public static void Continue(){


clear();

CaptionLayoutPanel	    grd = new CaptionLayoutPanel(Jiqladmin.getLanguageProperties().createNewTable());

    Grid grid = new Grid(2, 2);

        grid.setWidget(0, 0, new Label(Jiqladmin.getLanguageProperties().name()));
         grid.setWidget(1, 0, new Label(Jiqladmin.getLanguageProperties().numberOfFields()));
        grid.setWidget(0, 1, new TextBox());
 ListBox lb = new ListBox();
 for (int ct = 1;ct <= 100;ct++)
 	lb.addItem(String.valueOf(ct));
         grid.setWidget(1, 1, lb);

VerticalPanel vp = new VerticalPanel();
grd.add(vp);
vp.add(grid);
Button b = new Button(Jiqladmin.getLanguageProperties().Continue());
vp.add(b);
spl.add(grd);
modal.showModal();

}
*/
public static void hide(){
	modal.hide();
}



}
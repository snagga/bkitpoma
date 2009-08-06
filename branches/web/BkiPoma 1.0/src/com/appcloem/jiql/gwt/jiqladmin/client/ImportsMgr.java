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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.appcloem.gwt.client.StatusMgr;
import java.util.List;


public class ImportsMgr {

static LayoutPanel mainP = null;
	public static String getSubmittedText(){
		return ta.getText();
	}
static TextArea ta = null;
static ListBox types = null;
static ListBox tables = null;

public static String getType(){
	    int ctf = types.getSelectedIndex();
String v = types.getValue(ctf);
return v;
}

public static String getTable(){
	    int ctf = tables.getSelectedIndex();
String v = tables.getValue(ctf);
return v;
}
public static void show(){

if (mainP == null){
//	mainP = buildButtonBar1Panel();
   mainP = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
  mainP.setPadding(20);
    mainP.setWidgetSpacing(20);   
       ta = new TextArea();
       ta.setHeight("400px");
    ta.setCharacterWidth(80);
    ta.setVisibleLines(30);
    ta.setText(Jiqladmin.getLanguageProperties().copyPaste());
	
			 types = new ListBox();
 	types.addItem("SQL");
types.addItem("CSV");

    types.addChangeHandler(new ChangeHandler() {
      public void onChange(ChangeEvent event) {
String v = getType();
    		if (v.equals("CSV"))
    		{
    			 StatusMgr.show();
StatusMgr.appendln(Jiqladmin.getLanguageProperties().loading());

    		 Jiqladmin.getService().showTables(
    		 	
    		 	new AsyncCallback() {

	public void onFailure(Throwable caught) {
			 StatusMgr.hide();
			 MessageBox.alert(Window.getTitle(), caught.toString());
			 }

			        public void onSuccess(Object v) {
			 StatusMgr.hide();

						List<String> qr = (List)v;
						if (qr.size() > 0)
						{
						tables.clear();
						for (int ct = 0;ct < qr.size();ct ++)
							tables.addItem(qr.get(ct));
						tables.setVisible(true);
						}
						else
							 MessageBox.alert(Window.getTitle(), Jiqladmin.getLanguageProperties().pleaseCreateTable());




		        }


    }
    		 	
    		 	
    		 	);


    		}else
        	 tables.setVisible(false);

      }
    });



   LayoutPanel tlp = new LayoutPanel(new BoxLayout(
        Orientation.HORIZONTAL));
	tlp.add(types);
	
	 tables = new ListBox();
	 tables.addItem("mytable");
	 tables.setVisible(false);
	tlp.add(tables);

	
	mainP.add(tlp,new BoxLayoutData(FillStyle.HORIZONTAL ,true));
	
	mainP.add(ta,new BoxLayoutData(FillStyle.HORIZONTAL ,true));
    Button b = new Button(Jiqladmin.getLanguageProperties().submit(),new ImportsSubmitHandler());
    Button b2 = new Button(Jiqladmin.getLanguageProperties().upload(),new ImportsUploadHandler());
 final ToolBar toolBar = new ToolBar(); 
  /*LayoutPanel grpl = new LayoutPanel(new BoxLayout(
        Orientation.HORIZONTAL));
  grpl.add(b,new BoxLayoutData(
        FillStyle.HORIZONTAL, true));
   grpl.add(b2,new BoxLayoutData(
        FillStyle.HORIZONTAL, true));*/
 toolBar.add(b);
 toolBar.add(b2);
  mainP.add(new HTML("<br/>"));
   mainP.add(toolBar);
    //mainP.add(new WidgetWrapper(toolBar), new BoxLayoutData(
     //   FillStyle.HORIZONTAL, true));
    
    
    
    /*    LayoutPanel panel = new LayoutPanel(layout);
    // panel.setBorder(Borders.DIALOG_BORDER);
    panel.setPadding(0);
    panel.add(textArea, CellConstraints.xy(1, 1));
    panel.add(buttonBar, CellConstraints.xy(1, 3));*/

        
        
}

Jiqladmin.getConentPanel().clear();
Jiqladmin.getConentPanel().add(mainP,new BoxLayoutData(FillStyle.BOTH));

}

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
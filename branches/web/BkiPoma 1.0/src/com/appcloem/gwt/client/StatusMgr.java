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
package com.appcloem.gwt.client;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.core.client.GWT;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import com.google.gwt.user.client.ui.Image;
import java.util.List;
import com.google.gwt.user.client.ui.ScrollPanel;

public class StatusMgr {
static LayoutPanel spl = null;
static WindowPanel modal = null;
static ScrollPanel sp = null;
static TextArea ta = null;
static int ln = 0;
static int maxLn = 1000;

public static void appendln(List<String> t){
	for (int ct = 0;ct < t.size();ct ++)
		appendln(t.get(ct));

}
public static void clear(){
	//ta.setText("");
	spl.clear();
}
public static void appendln(String t){
appendln(t,"black");
}

public static void appendln(String t,String c){
	if (maxLn < ln){
		maxLn = 0;
		clear();
	}
	//<br/>
	spl.add(new com.google.gwt.user.client.ui.HTML("<br/><font color=\"" + c + "\">" + t + "</font>"));
	ln = ln + 1;
	//ta.setCursorPos(ln);
}


/*public static void appendln(String t){
	if (maxLn < ln){
		maxLn = 0;
		ta.setText("");
	}
	ta.setText(ta.getText() + "\n" + t);
	ln = ln + 1;
	ta.setCursorPos(ln);
}*/
public static void show(){

if (modal == null){
	modal = new WindowPanel("");
    modal.setResizable(false);
    modal.setAnimationEnabled(true);
       LayoutPanel mainP = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
  mainP.setPadding(5);
    mainP.setWidgetSpacing(10);



 /// AppcloemImageBundle myImageBundle = GWT.create(AppcloemImageBundle.class);
 // AbstractImagePrototype imgp = myImageBundle.progress();

  mainP.add(new WidgetWrapper(new Image(GlobalObjects.getPropertiesConstants().moduleName() + "/images/progress.gif")), new BoxLayoutData(
        FillStyle.HORIZONTAL, true));

    mainP.add(new com.google.gwt.user.client.ui.HTML("<br/>"));


       ta = new TextArea();
   //    ta.setHeight("150px");
     //  ta.setWidth("170px");
       ta.setReadOnly(true);
    ta.setCharacterWidth(2000);
    ta.setVisibleLines(1000);
    //ta.setText(Jiqladmin.getLanguageProperties().copyPaste());
     sp = new ScrollPanel();
           sp.setHeight("200px");
       sp.setWidth("220px");

       spl = new LayoutPanel(new BoxLayout(
        Orientation.VERTICAL));
   spl.setHeight("2000px");
   spl.setWidth("1000px");
	sp.add(spl);
    //sp.add(ta);
	mainP.add(sp,new BoxLayoutData(FillStyle.HORIZONTAL ,true));
//    Button b = new Button(Jiqladmin.getLanguageProperties().submit());

  //LayoutPanel grpl = new LayoutPanel(new BorderLayout());
  mainP.add(new com.google.gwt.user.client.ui.HTML("<br/><br/>"));
 //   mainP.add(new WidgetWrapper(b), new BoxLayoutData(
   //     FillStyle.HORIZONTAL, true));


modal.setWidget(mainP);


}
clear();
modal.showModal();
modal.toFront();

}
public static void hide(){
modal.toBack();

	modal.hide();
}

}
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

import com.google.gwt.gen2.table.client.CachedTableModel;
import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;

import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import org.gwt.mosaic.ui.client.table.PagingScrollTable2;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import com.google.gwt.widgetideas.table.client.TableModel;

import com.google.gwt.user.client.ui.Grid;
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


public class TriggeredJobTimerCallback implements AsyncCallback
{

	TriggeredJobTimer jadmin = null;
	public TriggeredJobTimerCallback(TriggeredJobTimer j)
	{
		jadmin = j;
	}
	public void onFailure(Throwable caught) {
			 MessageBox.alert(Window.getTitle(), caught.toString());
			 }

			        public void onSuccess(Object v) {

						TriggeredJob qr = (TriggeredJob)v;
						jadmin.update(qr);



		        }



}
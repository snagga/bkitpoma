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


import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.MessageBox.ConfirmationCallback;
import org.gwt.mosaic.ui.client.MessageBox.MessageBoxType;
import com.google.gwt.user.client.Window;

public class DropTableButtonHandler implements ClickHandler
{
//	public void onClick(ClickEvent event){
public void onClick(ClickEvent event) {
        final String table = TablesMgr.getSelectedTable();
        if (table == null){
   				 MessageBox.alert(Window.getTitle(), Jiqladmin.getLanguageProperties().noTablesFound());
				return;
        }
        MessageBox.confirm(Window.getTitle(),Jiqladmin.getLanguageProperties().dropTable() + ":" + table ,
            
            new ConfirmationCallback() {
              public void onResult(boolean result) {
		if (result)
		TablesMgr.Drop(table);
              }
            });
      }


//	}



}
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


import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;

import org.gwt.mosaic.ui.client.CaptionLayoutPanel;

import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import org.gwt.mosaic.ui.client.table.PagingScrollTable;
import org.gwt.mosaic.ui.client.table.ScrollTable;
import com.google.gwt.widgetideas.table.client.TableModel;
import com.google.gwt.widgetideas.table.client.TableModelHelper;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.Grid;

import com.appcloem.gwt.client.QMap;


import com.google.gwt.core.client.GWT;
import com.google.gwt.gen2.table.client.MutableTableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.widgetideas.table.client.ReadOnlyTableModel;
import com.google.gwt.widgetideas.table.client.TableModel;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

public class QueryTableModel extends MutableTableModel<QMap> {


  Jiqladmin jadmin = null;

  public QueryTableModel(Jiqladmin j){
	  jadmin = j;
  }



  /**
   * The source of the data.
   */
  /*private StudentGenerator data = new StudentGenerator() {
    @Override
    public int getRandomInt(int max) {
      return Random.nextInt(max);
    }
  };*/

  /**
   * The RPC service used to generate data, if RPC mode is enabled.
   */
  //private DataSourceServiceAsync dataService = null;

  /**
   * A boolean indicating that we should throw an error.
   */
  private boolean errorMode = false;

  /**
   * A boolean indicating that we should use an rpc request instead of
   * generating data locally.
   */
  private boolean rpcMode = false;

  /**
   * A boolean indicating that we should return 0 rows in the response.
   */
  private boolean zeroMode = true;

  /**
   * Check if error mode is enabled.
   *
   * @return true if enabled
   */
  public boolean isErrorModeEnabled() {
    return errorMode;
  }

  /**
   * Check if RPC mode is enabled.
   *
   * @return true if enabled
   */
  public boolean isRPCModeEnabled() {
    return rpcMode;
  }

  /**
   * Check if zero mode is enabled.
   *
   * @return true if enabled
   */
  public boolean isZeroModeEnabled() {
    return zeroMode;
  }

  /**
   * Override that can optionally throw an error.
   */
  @Override
  public void requestRows(final Request request,
      final Callback<QMap> callback) {
    if (errorMode) {
      // Return an error
      callback.onFailure(new Exception("An error has occured."));
    } else if (zeroMode) {
      // Return an empty result
      List<QMap> students = new ArrayList<QMap>();
      callback.onRowsReady(request, new SerializableResponse<QMap>(students));
    } else if (rpcMode) {


      // Send RPC request for data
      jadmin.getService().requestRows(request,
          new AsyncCallback<SerializableResponse<QMap>>() {
            public void onFailure(Throwable caught) {
              //callback.onFailure(new Exception("RPC Failure"));
              MessageBox.alert(Window.getTitle(), caught.toString());

            }

            public void onSuccess(SerializableResponse<QMap> result) {
              callback.onRowsReady(request, result);
            }
          });
    } else {


    }
  }

  /**
   * Enable or disable error mode.
   *
   * @param enabled true to enable
   */
  public void setErrorModeEnabled(boolean enabled) {
    this.errorMode = enabled;
  }

  /**
   * Enable or disable rpc mode.
   *
   * @param enabled true to enable
   */
  public void setRPCModeEnabled(boolean enabled) {
    this.rpcMode = enabled;
  }

  /**
   * Enable or disable zero mode.
   *
   * @param enabled true to enable
   */
  public void setZeroModeEnabled(boolean enabled) {
    this.zeroMode = enabled;
  }

  @Override
  protected boolean onRowInserted(int beforeRow) {
    return true;
  }

  @Override
  protected boolean onRowRemoved(int row) {
    return true;
  }

  @Override
  protected boolean onSetRowValue(int row, QMap rowValue) {
    return true;
  }
}
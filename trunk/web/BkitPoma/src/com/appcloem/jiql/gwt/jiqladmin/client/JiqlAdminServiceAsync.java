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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.appcloem.gwt.client.QMap;
//import com.appcloem.jiqladmin.QueryResult;

import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.appcloem.gwt.client.AGWTServiceAsync;
import java.util.List;

public interface JiqlAdminServiceAsync extends AGWTServiceAsync {

  void getLabel(String name, AsyncCallback<String> callback);
  void getQuery(String q, AsyncCallback<QueryResult> callback);
  void getQuery(AsyncCallback<QueryResult> callback);
	void submitScript(String q,String ty,String tb,AsyncCallback<Void> callback);
  void requestRows(Request request,
      AsyncCallback<SerializableResponse<QMap>> callback);

void uploadScript(String ty,String tb,AsyncCallback<Void> callback);
  void showTables(AsyncCallback<List<String>> callback);
 
 void describeTable(Integer st,AsyncCallback<List<QMap>> callback);
	void createTable(String q,AsyncCallback<Void> callback);
	void dropTable(String q,AsyncCallback<Void> callback);



}

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
package com.appcloem.gwt.server;



import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import com.appcloem.jiql.gwt.jiqladmin.client.QueryResult;
import com.appcloem.jiql.gwt.jiqladmin.client.JiqlAdminService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import com.appcloem.gwt.client.QMap;
import java.sql.SQLException;

import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.appcloem.jiqladmin.QueryMgr;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import com.appcloem.gwt.client.AGWTService;
import com.appcloem.gwt.client.TriggeredJob;
/**
 * The implemenation of the RPC service which runs on the server.
 */
public class AGWTServiceImpl extends RemoteServiceServlet implements
    AGWTService {

    public TriggeredJob executeTriggeredJob()throws com.google.gwt.user.client.rpc.SerializableException{
  		//("TriggeredJob1 : ");
  		try{
  			//,getThreadLocalRequest()
  		TriggeredJobsMgr qr = TriggeredJobsMgr.get(getThreadLocalRequest());
  		TriggeredJob tqr = qr.execute(getThreadLocalRequest());
  		  		//("TriggeredJob2 : " + tqr.isComplete());

  		return tqr;
  	}catch (Exception e){
  		e.printStackTrace();
  		throw new com.google.gwt.user.client.rpc.SerializableException(e.toString());
  	}
  	//return null;
  	}


}

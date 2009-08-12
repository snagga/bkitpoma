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
package com.appcloem.jiql.gwt.jiqladmin.server;

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

//import com.appcloem.jiqladmin.QueryMgr;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import com.appcloem.gwt.server.AGWTServiceImpl;
import com.appcloem.gwt.server.UploadMgr;

/**
 * The implemenation of the RPC service which runs on the server.
 */
public class JiqlAdminServiceImpl extends AGWTServiceImpl implements
		JiqlAdminService {

	public SerializableResponse<QMap> requestRows(Request request) {
		// Get the sort info, even though we ignore it
		ColumnSortList sortList = request.getColumnSortList();
		sortList.getPrimaryColumn();
		sortList.isPrimaryAscending();

		// Return the data
		int numRows = request.getNumRows();
		int startRow = request.getStartRow();
		// (startRow + " DSS " + numRows);

		Vector<QMap> rows = (Vector<QMap>) getThreadLocalRequest().getSession()
				.getAttribute("jiqlwebadmin_rows");

		// (startRow + " DSS 2 " + rows.size());

		if ((startRow + numRows) > rows.size())
			numRows = rows.size() - startRow;
		numRows = numRows + startRow;
		// (startRow + " DSS 3 " + numRows);
		ArrayList<QMap> tRows = new ArrayList<QMap>();

		for (int ct = startRow; ct < numRows; ct++)
			tRows.add((rows.elementAt(ct)));
		// (startRow + " DSS 4 " + tRows);
		SerializableResponse<QMap> srm = new SerializableResponse<QMap>(tRows);

		// (sortList + " DSS 5 " + srm);

		return srm;
	}

	public void init(ServletConfig config) throws ServletException {

		String ps = config.getServletContext().getRealPath("/");
		QueryMgr.init(ps);
		super.init(config);
	}

	public String getLabel(String name) {
		// ("getLabel: " + name);

		return name;
	}

	public QueryResult getQuery(String q)
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("getQuery: " + q);
		try {
			// ,getThreadLocalRequest()
			QueryResult qr = QueryMgr
					.getQueryResult(q, getThreadLocalRequest());
			// (q + " getQuery 2 : " + qr.hasResult());
			// if (true)
			// getThreadLocalResponse().sendRedirect("/jiqladmin.html");
			return qr;
			// return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public void submitScript(String q, String ty, String tb)
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("submitScript: " + q);
		try {
			ScriptJobsMgr sjb = new ScriptJobsMgr(q, ty, tb);
			sjb.set(getThreadLocalRequest());
		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public void createTable(String q)
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("createTable: " + q);
		try {
			QueryMgr.createTable(q, getThreadLocalRequest());
		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public void dropTable(String q)
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("dropTable: " + q);
		try {
			QueryMgr.dropTable(q, getThreadLocalRequest());
		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public void uploadScript(String ty, String tb)
			throws com.google.gwt.user.client.rpc.SerializableException {

		String q = null;
		String un = UploadMgr.getName(getThreadLocalRequest());
		if (un == null)
			throw new com.google.gwt.user.client.rpc.SerializableException(
					"no_upload_file");

		if (un != null) {
			byte[] sb = UploadMgr.getItem(getThreadLocalRequest());
			if (sb != null) {
				q = (new String(sb));
			}
		}

		submitScript(q, ty, tb);
		// return null;
	}

	public QueryResult getQuery()
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("getQueryb1 : ");
		try {
			// ,getThreadLocalRequest()
			QueryResult qr = QueryMgr.getQueryResult(getThreadLocalRequest());
			// (" getQuery 2b : " + qr.hasResult());
			// if (true)
			// getThreadLocalResponse().sendRedirect("jiqladmin.html");
			return qr;
			// return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public List<String> showTables()
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("showTables : ");
		try {
			// ,getThreadLocalRequest()
			return QueryMgr.showTables(getThreadLocalRequest());

		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

	public List<QMap> describeTable(Integer st)
			throws com.google.gwt.user.client.rpc.SerializableException {
		// ("showTables : ");
		try {
			// ,getThreadLocalRequest()
			return QueryMgr.describeTable(st, getThreadLocalRequest());

		} catch (Exception e) {
			e.printStackTrace();
			throw new com.google.gwt.user.client.rpc.SerializableException(e
					.toString());
		}
		// return null;
	}

}

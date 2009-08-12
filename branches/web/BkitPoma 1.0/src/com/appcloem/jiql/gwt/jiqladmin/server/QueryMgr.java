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

import java.io.*;
import java.util.*;
import java.sql.*;
import com.appcloem.jiql.gwt.jiqladmin.client.QueryResult;
import com.appcloem.gwt.client.QMap;
import tools.util.NameValuePairs;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//import org.gwt.mosaic.showcase.client.content.tables.QueryResult;
//import com.appcloem.gwt.QMap;

public class QueryMgr {

	static NameValuePairs config = null;
	static Driver driver = null;

	public static void createTable(String sql, HttpServletRequest request)
			throws SQLException {

		System.out.println(sql);
		Connection con = getConnection(request);

		Statement Stmt = con.createStatement();
		Stmt.execute(sql);

		con.close();

	}

	public static void dropTable(String sql, HttpServletRequest request)
			throws SQLException {

		System.out.println(sql);

		Connection con = getConnection(request);

		Statement Stmt = con.createStatement();
		Stmt.execute("drop table " + sql);

		con.close();

	}

	public static Connection getConnection(HttpServletRequest request)
			throws SQLException {

		Properties props = new Properties();
		if (config.get("user") != null)
			props.put("user", config.getString("user"));
		if (config.get("password") != null)
			props.put("password", config.getString("password"));
		if (config.get("date.format") != null)
			props.put("date.format", config.getString("date.format"));

		String url = (String) config.get("url");
		if (url == null)
			url = "jdbc:jiql://local";
		Connection con = driver.connect(url, props);
		return con;
	}

	public static List<String> showTables(HttpServletRequest request)
			throws SQLException {

		Connection con = getConnection(request);

		List<String> v = new ArrayList<String>();

		DatabaseMetaData dbm = con.getMetaData();
		// m_res = dbm.getFunctions(null,null,null);

		String[] types = { "TABLE" };
		ResultSet m_res = dbm.getTables(null, null, "%", types);

		// Get the table names
		while (m_res.next()) {
			// Get the table name
			String tableName = m_res.getString(3);
			v.add(tableName);
		}
		m_res.close();
		con.close();
		request.getSession(true).setAttribute("showTables", v);
		return v;

	}

	public static List<QMap> describeTable(Integer st,
			HttpServletRequest request) throws SQLException {

		Connection con = getConnection(request);

		QMap v = new QMap();
		List<QMap> v2 = new ArrayList<QMap>();

		List<String> l = (List<String>) request.getSession().getAttribute(
				"showTables");
		// (st + " describeTable 1 " + l);
		Statement Stmt = con.createStatement();
		String t = l.get(st.intValue());
		ResultSet m_res = Stmt.executeQuery("describe " + t);

		// Stmt.execute("describe " + t);
		// ResultSet m_res = Stmt.getResultSet();

		// (m_res + " describeTable 1c " + "describe " + t);

		// (st + " describeTable 1b " + l);

		/*
		 * descols.add("Field"); descols.add("Type"); descols.add("Null");
		 * descols.add("Key"); descols.add("Default"); descols.add("Extra");
		 */

		String n = null;
		// String a = null;
		while (m_res.next()) {
			// ("describeTable 2 " + l);
			v = new QMap();
			n = m_res.getString("Field");
			v.put("Field", n);
			n = m_res.getString("Type");
			v.put("Type", n);
			n = m_res.getString("Null");
			if (n != null)
				v.put("Null", n);
			else
				v.put("Null", "");

			n = m_res.getString("Key");
			if (n != null)
				v.put("Key", n);
			else
				v.put("Key", "");

			n = m_res.getString("Default");
			if (n != null)
				v.put("Default", n);
			else
				v.put("Default", "");

			v2.add(v);
		}
		m_res.close();
		con.close();
		// ("describeTable 3 " + l);
		return v2;

	}

	// describeTable
	public static QueryResult getQueryResult(String q,
			HttpServletRequest request) throws SQLException {
		request.getSession(true).removeAttribute("jiqlwebadmin_rows");
		request.getSession(true).removeAttribute("jiqlwebadmin_QueryResult");
		Connection con = null;
		// con =
		// (Connection)request.getSession(true).getAttribute("jiqlConnection");
		if (con == null) {
			con = getConnection(request);
			/*
			 * Properties props = new Properties(); if (config.get("user") !=
			 * null) props.put("user",config.getString("user")); if
			 * (config.get("password") != null)
			 * props.put("password",config.getString("password")); String url =
			 * (String)config.get("url"); if (url == null) url =
			 * "jdbc:jiql://local"; con = driver.connect(url,props);
			 */
			// request. ().setAttribute("jiqlConnection",con);
			// (" YELL2 " + props);
		}
		Statement Stmt = con.createStatement();
		// (" YELL3 " + Stmt);
		Stmt.execute(q);
		// (" YELL5 ");
		ResultSet result = Stmt.getResultSet();
		// (" YELL4 " + result);
		if (result != null) {
			// org.jiql.jdbc.ResultSet result2 =
			// (org.jiql.jdbc.ResultSet)result;
			ResultSetMetaData mres = result.getMetaData();

			int cc = mres.getColumnCount();
			Vector<String> columns = new Vector<String>();
			for (int c = 0; c < cc; c++) {
				columns.add(mres.getColumnName(c + 1));
			}
			// (" YELLO ");

			String n = null;
			Object o = null;
			Vector<QMap> rows = new Vector<QMap>();
			Map<String, String> rm = null;
			while (result.next()) {
				rm = new HashMap<String, String>();

				for (int c = 0; c < cc; c++) {
					n = mres.getColumnName(c + 1);
					o = result.getObject(n);
					if (o != null) {

						// (n + " NV " + o.getClass().getName());
						rm.put(n, o.toString());
					}
				}
				rows.add(new QMap(rm));
			}

			request.getSession(true).setAttribute("jiqlwebadmin_rows", rows);
			QueryResult qr = new QueryResult(true, columns, rows.size());
			request.getSession(true).setAttribute("jiqlwebadmin_QueryResult",
					qr);

			// qr.setRows(rows);
			return qr;
			// return new QueryResult(true,columns,result2.getResults());
		}
		// }catch (Exception e){
		// e.printStackTrace();
		// }
		return new QueryResult(false, new Vector<String>(), 0);
	}

	public static QueryResult getQueryResult(HttpServletRequest request)
			throws SQLException {
		QueryResult qr = (QueryResult) request.getSession(true).getAttribute(
				"jiqlwebadmin_QueryResult");
		if (qr == null)
			qr = new QueryResult();
		return qr;
	}

	public static void init(String sb) {
		try {
			sb = sb + "WEB-INF/jiqladmin.properties";
			if (new File(sb).exists())
				config = new NameValuePairs(sb);
			else
				config = new NameValuePairs();

			Class clazz = Class.forName("org.jiql.jdbc.Driver");
			driver = (Driver) clazz.newInstance();

		} catch (Exception e) {
			tools.util.LogMgr.err("QueryMgr.init " + e.toString());
		}
	}

}

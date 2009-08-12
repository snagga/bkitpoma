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
import tools.util.NameValuePairs;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import org.gwt.mosaic.showcase.client.content.tables.QueryResult;
import com.appcloem.gwt.client.TriggeredJob;
import com.appcloem.gwt.server.TriggeredJobsMgr;
import tools.util.StringUtil;
import org.jiql.db.LoadData;
import org.jiql.util.SQLParser;

public class ScriptJobsMgr extends TriggeredJobsMgr implements java.io.Serializable{

	List<String> scriptL = new ArrayList<String>();
	String type = "SQL";

	public TriggeredJob execute(HttpServletRequest request){
		TriggeredJob tj = null;
			tj = new TriggeredJob();
			//(" scriptL.size() " + scriptL.size());
 		//ScriptJobsMgr sjb = new ScriptJobsMgr(q);
 		//sjb.set(getThreadLocalRequest());


		if (scriptL.size() > 0){
			String s = scriptL.get(0);
			scriptL.remove(0);
			try{

			QueryMgr.getQueryResult(s,request);
			tj.setMsg(s);

			}catch (Exception e){
				tools.util.LogMgr.err(s + " ScriptsMgr.getQueryResult " + e.toString());
				tj.setMsg("ERROR: " + s + ":" + e.toString(),"red");

			}
		set( request);

		}
		else
		{
			tj.setComplete(true);
		}

		return tj;

	}

	 public ScriptJobsMgr(String q,String ty,String tb){
	 	super();
	 	if (ty.equals("CSV"))
	 	parseCSV(q,tb);
	 	else
	 	parseScript(q);

	 }

public  void parseCSV (String f,String tb)
{
	try{
		//LOAD DATA INTO TABLE wp_users INTEXT 
StringBuffer tok = new StringBuffer("LOAD DATA INTO TABLE ").append(tb).append(" INTEXT ").append(f);
LoadData ld = new LoadData(null, tok.toString());
		ld.execute(null,scriptL);
		}catch (Exception e){
		e.printStackTrace();

	}
}



		public  void parseScript (String f)
//	throws Exception
	{
		try{
		if (!f.trim().endsWith(";"))
		f = f + ";";
		boolean tf = true;
		boolean dr = false;
		ByteArrayInputStream bin = new ByteArrayInputStream(f.getBytes());
		InputStreamReader fin = new InputStreamReader(bin);
		try{
		String templ = null;
		LineNumberReader lr = new LineNumberReader(fin);
		String tc = "";
		int isc = -1;
		//("SS 1 ");
		while ((templ = lr.readLine()) != null){
		if (!StringUtil.isRealString(templ) || templ.trim().startsWith("--") || templ.trim().startsWith("#"))continue;
		//int ec = templ.indexOf(" -- ");
		//if (ec > 1){
		//	templ = templ.substring(0,ec);
		//}
		templ = tc + templ;
		//("SS 2 " + templ);

		//isc = templ.indexOf(";");
		//if (isc < 0){
		if (!StringUtil.getTrimmedValue(templ).endsWith(";")){

			tc = templ;
			continue;
		}

		templ = StringUtil.replaceLastSubstring(templ,";","");
		templ = StringUtil.replaceSubstring(templ,"NG_TM_SC",";");
		try{
		tc = "";
		if (dr){
			isc = templ.toLowerCase().indexOf("create table");
			if (isc > -1)
			{
			String dtempl = null;
				try{
						dtempl = templ.substring(isc + "create table".length(),templ.length()).trim();
						isc = dtempl.indexOf("(");
						if (isc < 0)
						 isc = dtempl.indexOf(" ");
						 if (isc > -1){
						dtempl = dtempl.substring(0,isc);
						scriptL.add("drop table " + dtempl);

		//Statement	sstmt = m_con.createStatement ();
		//sstmt.execute(templ.toString());

						}
					}catch (Exception e){
						if (e.toString().indexOf("ORA-00942") < 0)
						tools.util.LogMgr.warn(dtempl + " executeScript DROP " + e.toString());
					}

			}
		}

		scriptL.add(templ);
		//(scriptL.size() + " SPQ: " + templ.toString());

		//Statement	sstmt = m_con.createStatement ();
		//sstmt.execute(templ.toString());
		}catch (Exception e){
			//tools.util.LogMgr.err(templ + " executeScript " + e.toString());

		}
		}
		}finally{
			fin.close();
		}
		}catch (Exception e){
			tools.util.LogMgr.err("parseScript " + e.toString());
			e.printStackTrace();
		}
	}






}

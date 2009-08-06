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
//package com.appcloem.jiql.gwt.jiqladmin.client;
package com.appcloem.jiql.gwt.jiqladmin.client;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Vector;
import java.util.Map;
import com.appcloem.gwt.client.QMap;

public class QueryResult implements IsSerializable ,java.io.Serializable{

  private boolean isresult = false;
  Vector<QMap> rows  = new Vector<QMap>();
  Vector<String> columns  = new Vector<String>();

	int rowsc = 0;
	int PostCachedRowCount = 5;
	int PreCachedRowCount = 5;
	int PageSize = 30;
  public QueryResult() {
  }
	public void setRows(Vector<QMap> r){
		rows = r;
	}
		public Vector<QMap>  getRows(){
		return rows;
	}
  public QueryResult(boolean r,Vector c,int ro) {
  	isresult = r;
  	columns = c;
  	rowsc = ro;
  	PreCachedRowCount = ro;
  	PostCachedRowCount = 0;

  	if (ro < PreCachedRowCount)
  	 PreCachedRowCount = ro;
  	if (ro < PageSize)
  	 PageSize = ro;

   	if (ro < (PreCachedRowCount + PostCachedRowCount))
  	 PostCachedRowCount = 0;


  }

  public boolean hasResult() {
    return isresult;
  }

  public Vector getColumnNames() {
    return columns;
  }

 public int getRowCount() {
    return rowsc;
  }

   public void setPreCachedRowCount(int c){
	   PreCachedRowCount = c;
   }
       public int getPreCachedRowCount(){
		   return PreCachedRowCount;
   }

   public void setPostCachedRowCount(int c){
	   PostCachedRowCount = c;
   }
       public int getPostCachedRowCount(){
		   return PostCachedRowCount;
   }

   public void setPageSize(int c){
	   PageSize = c;
   }
       public int getPageSize(){
		   return PageSize;
   }

}

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


import java.util.List;
import java.util.ArrayList;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.appcloem.gwt.client.ClientUtil;
public class CreateField
{

static List<CreateField> fields = new ArrayList<CreateField>();

TextBox nw = null;
ListBox types = null;
TextBox ltb = null;
ListBox nulls = null;
TextBox defultV = null;
RadioButton primary = null;
RadioButton unique = null;
RadioButton nothing = null;
int fn = 0;
public static void clear(){
	fields = new ArrayList();
}

	public CreateField(int f){
		fn = f;
		nw = new TextBox();

		 types = new ListBox();
 	types.addItem("VARCHAR");
types.addItem("TEXT");
 	types.addItem("BOOL");
types.addItem("INT");
 	types.addItem("TINYINT");
 	types.addItem("BIGINT");
types.addItem("FLOAT");
types.addItem("DOUBLE");
types.addItem("DATE");
types.addItem("TIMESTAMP");

 ltb = new TextBox();
ltb.setWidth("50px");

 nulls = new ListBox();
nulls.addItem("not null");

 	nulls.addItem("null");

defultV = new TextBox();
      primary = new RadioButton("attribute-" + fn, "primary");
       unique = new RadioButton("attribute-" + fn, "unique");
       nothing = new RadioButton("attribute-" + fn, "---");
	nothing.setValue(true);


	}
		public RadioButton getUniqueWidget(){
		return unique;
	}
			public RadioButton getNothingWidget(){
		return nothing;
	}

		public RadioButton getPrimaryWidget(){
		return primary;
	}

		public TextBox getDefaultWidget(){
		return defultV;
	}

	public TextBox getLengthWidget(){
		return ltb;
	}
	public TextBox getNameWidget(){
		return nw;
	}
	public String getName(){
		return nw.getText();
	}
	public boolean isUnique(){
		return unique.getValue();
	}
	public boolean isPrimary(){
		return primary.getValue();
	}
	public String getType(){
int ctf = types.getSelectedIndex();
String v = types.getValue(ctf);
return v;
	}
	public int getLength()throws Exception{
		if (!ClientUtil.isNumeric(ltb.getText()) && getType().equals("VARCHAR"))
			throw new Exception(Jiqladmin.getLanguageProperties().invalidValue());
		String stv = ltb.getText();
		if (stv.length() < 1)stv = "0";
		return new Integer(stv).intValue();
	}

	public boolean notNull(){
int ctf = nulls.getSelectedIndex();
String v = nulls.getValue(ctf);
return v.equals("not null");
	}

	public String getDefaultValue(){
		return defultV.getText();
	}

	public ListBox getTypesWidget(){
		return types;
	}
		public ListBox getNullsWidget(){
		return nulls;
	}

	protected void validate()throws Exception{
		if (!ClientUtil.validString(getName()))
			throw new Exception(Jiqladmin.getLanguageProperties().invalidName());
		if (isUnique() && isPrimary())
			throw new Exception(Jiqladmin.getLanguageProperties().puConflict());

	}
	public static CreateField add(int f){
		CreateField cf = new CreateField(f);
		fields.add(cf);
		return cf;
	}

	public static String parse()throws Exception{

//    create  table  testable  ( name  varchar(18),countf int default 22,yesno  varchar(100) default 'Yes and :) (:where or, No not null primary key' not null )
		String table = CreateTableMgr.getTable();
		String creatT = "create table " + table + " (";
		//CreateField[] cfs = (CreateField[])fields.toArray();
		String primary = "";
		CreateField cfs = null;
		for (int ct = 0;ct < fields.size() ;ct++ )
		{
			cfs = fields.get(ct);
			cfs.validate();
			if (ct > 0)
				creatT = creatT + ",";
			creatT = creatT + cfs.getName();
			creatT = creatT +  " " + cfs.getType();

			if (cfs.getType().equals("VARCHAR"))
			creatT = creatT + "(" + cfs.getLength() + ")";

			if (cfs.getType().indexOf("INT") > -1 && cfs.getLength() > 0)
			creatT = creatT + "(" + cfs.getLength() + ")";

			if (cfs.notNull())
				creatT = creatT + " NOT NULL";
			else
				creatT = creatT + " NULL";

			if (cfs.isUnique())
				creatT = creatT + " UNIQUE";
			if (ClientUtil.validString(cfs.getDefaultValue()))
			{
				if (cfs.getName().equals("VARCHAR"))
				creatT = creatT + " DEFAULT '" + cfs.getDefaultValue() + "'";
				else
				creatT = creatT + " DEFAULT " + cfs.getDefaultValue();

			}
			if (cfs.isPrimary())
			{
				if (primary.length() > 0)
					primary = primary + ",";
				primary = primary + cfs.getName();
			}

		}
		if (primary.length() > 0)
			creatT  = creatT + ",PRIMARY KEY (" + primary + ")";
		creatT = creatT + ");";

		return creatT;
	}


}
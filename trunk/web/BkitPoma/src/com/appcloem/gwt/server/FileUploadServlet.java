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
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.io.InputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileItemIterator;
import tools.util.StreamUtil;

public class FileUploadServlet extends HttpServlet implements Servlet {


public void doPost(HttpServletRequest aReq, HttpServletResponse aResp)
throws ServletException, IOException {

//("FILE UPLOAD");
// Check that we have a file upload request
boolean isMultipart = FileUpload.isMultipartContent(aReq);
//("isMultipart:[" + isMultipart + "]");

// Create a factory for disk-based file items
//DiskFileItemFactory factory = new DiskFileItemFactory();

// Set factory constraints
//factory.setSizeThreshold(1000000);
//factory.setRepository(new File(""));

// Create a new file upload handler
ServletFileUpload upload = new ServletFileUpload();

// Set overall request size constraint
upload.setSizeMax(10000000);

// Parse the request
try {
//List items = upload.parseRequest(aReq);
     FileItemIterator iter = upload.getItemIterator(aReq);


// Process the uploaded items
//Iterator iter = items.iterator();
while (iter.hasNext()) {
       FileItemStream item = iter.next();
        String name = item.getFieldName();
       // InputStream stream = item.openStream();

//("item.getString():[" + name + "]" + item.isFormField());

if (!item.isFormField()) {
// (item.getString() + " item.getFieldName():[" + item.getFieldName() +
//"]");
// (item.isInMemory() + " item.getName():[" + item.getName() + "]");
        InputStream stream = item.openStream();
byte[] b = StreamUtil.readBytes(stream);
//new byte[stream.available()];
//stream.read(b);
UploadMgr.set(name,b,aReq);
}
}


}
catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
}
}

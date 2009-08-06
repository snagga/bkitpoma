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

package com.appcloem.gwt.client;
import com.google.gwt.user.client.Timer;
import java.util.List;
import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.Window;
public class TriggeredJobTimer extends Timer{


	public TriggeredJobTimer(){
		super();
		scheduleRepeating(1000);
		StatusMgr.show();
	}
	
	public void update(TriggeredJob tj){
		if (tj != null){
			String m = tj.getMsg();
			StatusMgr.appendln(m,tj.getColor());
			if (tj.isComplete())
			{
				
				StatusMgr.hide();
				cancel();
			}
			else
			scheduleRepeating(tj.getTimerDelay());
			
		}
	}

      public void run() {

        AGWTServiceAsync aserv =  GlobalObjects.getAGWTServiceAsync();
        TriggeredJobTimerCallback cb = new TriggeredJobTimerCallback(this);

		aserv.executeTriggeredJob(cb);

        
      }


}

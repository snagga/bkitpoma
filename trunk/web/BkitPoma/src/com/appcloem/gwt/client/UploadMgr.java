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

import com.google.gwt.user.client.ui.Widget;
import org.gwt.mosaic.ui.client.layout.BorderLayoutData;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;

import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.core.client.GWT;
import org.gwt.mosaic.ui.client.WindowPanel;
import org.gwt.mosaic.ui.client.WidgetWrapper;
import org.gwt.mosaic.ui.client.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button; 
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import org.gwt.mosaic.ui.client.MessageBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;


public class UploadMgr {

static WindowPanel modal = null;



public static void show(final UploadCompleteAction uca){

if (modal == null){
	modal = new WindowPanel("");
    modal.setResizable(false);
    modal.setAnimationEnabled(true);
      final FormPanel form = new FormPanel();
    form.setAction(GlobalObjects.getPropertiesConstants().uploadURL());

    // Because we're going to add a FileUpload widget, we'll need to set the
    // form to use the POST method, and multipart MIME encoding.
    form.setEncoding(FormPanel.ENCODING_MULTIPART);
    form.setMethod(FormPanel.METHOD_POST);

    // Create a panel to hold all of the form widgets.
    VerticalPanel panel = new VerticalPanel();
    form.setWidget(panel);

 

    // Create a FileUpload widget.
    final FileUpload upload = new FileUpload();
    upload.setName("uploadFormElement");
    panel.add(upload);

    // Add a 'submit' button.
    panel.add(new Button(GlobalObjects.getPropertiesConstants().submit(), new ClickHandler() {
      public void onClick(ClickEvent event) {
        form.submit();
      }
    }));

	  panel.add(new HTML("<br/><br/>"));

    // Add an event handler to the form.
    form.addSubmitHandler(new FormPanel.SubmitHandler() {
      public void onSubmit(SubmitEvent event) {
        // This event is fired just before the form is submitted. We can take
        // this opportunity to perform validation.
               String filename = upload.getFilename();
        if (filename.length() == 0) {
          MessageBox.alert(GlobalObjects.getPropertiesConstants().uploadFile(), GlobalObjects.getPropertiesConstants().uploadFileRequired());
        }

      }
    });
    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
      public void onSubmitComplete(SubmitCompleteEvent event) {
        // When the form submission is successfully completed, this event is
        // fired. Assuming the service returned a response of type text/html,
        // we can get the result text here (see the FormPanel documentation for
        // further explanation).
        //Window.alert(event.getResults());
        modal.hide();
        uca.execute();
      }
    });

 

modal.setWidget(form);


}
modal.showModal();

}
public static void hide(){
	modal.hide();
}

}
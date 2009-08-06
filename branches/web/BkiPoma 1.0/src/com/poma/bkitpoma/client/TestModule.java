package com.poma.bkitpoma.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class TestModule implements EntryPoint {
	
	public void onModuleLoad() {
		
		final TextField tf = new TextField("Text Field") {
			@Override
			public void onBrowserEvent(Event event) {
				// TODO Auto-generated method stub
				super.onBrowserEvent(event);
				if (DOM.eventGetType(event) == Event.ONMOUSEMOVE)
					MessageBox.alert("abc");
			}
		};
		tf.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.UP) {
					tf.setValue("");
				}
			}
		});
		tf.addListener("click", new Function() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub
				MessageBox.alert("clicked");
			}
			
		});
		
		tf.sinkEvents(Event.ONMOUSEMOVE);
		Panel panel = new Panel("Panel") {
			public void onBrowserEvent(Event event) {
				// TODO Auto-generated method stub
				super.onBrowserEvent(event);
				if (DOM.eventGetType(event) == Event.ONMOUSEWHEEL)
					MessageBox.alert("abc");
			}
		};
		panel.setSize(500, 500);
		panel.add(tf);
		panel.sinkEvents(Event.ONMOUSEWHEEL);
		
		RootPanel.get().add(panel);
		tf.sinkEvents(Event.ONMOUSEMOVE);
	}
	
}

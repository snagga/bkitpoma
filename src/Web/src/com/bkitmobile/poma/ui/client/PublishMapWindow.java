package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.home.client.UserSettings;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListener;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;

public class PublishMapWindow extends Window {
	private static PublishMapWindow window = null;
	private Long trackedID;
	private String trackedApi;

	public static final int LABEL_WIDTH = 130;
	private static final int TEXTFIELD_WIDTH = 300;

	private TextArea txtScript;
	private Button btnCopy;
	private Button btnClose;

	private TextField txtGMapApiKey;

	String script = "<embed id=\"PomaMap\" height=\"320px\" width=\"240px\" align=\"middle\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.adobe.com/go/getflashplayer\" allowscriptaccess=\"sameDomain\" name=\"PomaMap\" bgcolor=\"#869ca7\" quality=\"high\" src=\"http://bkitpoma.appspot.com/embedded/PomaMap.swf?uid=__ID__&api=__API__&key=__GMAP_API__\"/>	<noscript> <object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" id=\"PomaMap\" width=\"240px\" height=\"320px\" codebase=\"http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab\"> <param name=\"movie\" value=\"http://bkitpoma.appspot.com/embedded/PomaMap.swf?uid=__ID__&api=__API__&key=__GMAP_API__\" /> <param name=\"quality\" value=\"high\" /> <param name=\"flashVars\" value=\"uid=__ID__&api=__API__&key=__GMAP_API__\" /> <param name=\"bgcolor\" value=\"#869ca7\" /> <param name=\"allowScriptAccess\" value=\"sameDomain\" /> <embed src=\"http://bkitpoma.appspot.com/embedded/PomaMap.swf?uid=__ID__&api=__API__&key=__GMAP_API__\" quality=\"high\" bgcolor=\"#869ca7\" width=\"240px\" height=\"320px\" name=\"PomaMap\" align=\"middle\" play=\"true\" loop=\"false\" quality=\"high\" allowScriptAccess=\"sameDomain\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.adobe.com/go/getflashplayer\"> </embed> </object> </noscript>";

	public static PublishMapWindow getInstance() {
		return window != null ? window : new PublishMapWindow();
	}

	private PublishMapWindow() {
		super();
		window = this;

		init();
		layout();
	}

	private void init() {
		this.setCloseAction(Window.HIDE);
		this.setSize(LABEL_WIDTH + TEXTFIELD_WIDTH + 45, 170);
		this.setButtonAlign(Position.CENTER);
		this.setModal(true);

		txtScript = new TextArea("Script");
		txtScript.setWidth(TEXTFIELD_WIDTH);

		txtGMapApiKey = new TextField("GMap API Key");
		txtGMapApiKey.setWidth(TEXTFIELD_WIDTH);
		txtGMapApiKey.setEmptyText("Enter your GMap Api Key");
		txtGMapApiKey.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				getEmbededMap(newVal.toString());
			}

			@Override
			public void onBlur(Field field) {
				getEmbededMap(txtGMapApiKey.getText());
			}
		});

		btnCopy = new Button("Copy", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				txtScript.selectText();
				copy(txtScript.getText());
			}
		});

		btnCopy.focus();

		btnClose = new Button("Close", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				hide();
			}
		});
	}

	private void getEmbededMap(String apiKey) {
		if (apiKey == null || apiKey.trim().equals("")) {
			MessageBox.alert("You must fill GMap API key to get embeded map");
			return;
		}
		String s = new String(script);
		if (UserSettings.ctracker.getApiKey() == null) {
			MessageBox.alert("You must have API key to get embeded map");
		}
		s = s.replaceAll("__ID__", UserSettings.ctracker.getUsername());
		s = s.replaceAll("__API__", UserSettings.ctracker.getApiKey());
		s = s.replaceAll("__GMAP_API__", apiKey);

		txtScript.focus();
		txtScript.setValue(s);
		txtScript.selectText();
	}

	private void layout() {
		FormPanel form = new FormPanel();
		form.setLabelWidth(LABEL_WIDTH);

		form.add(txtGMapApiKey);
		form
				.add(new HTML(
						"<a href='http://code.google.com/apis/maps/signup.html' target='_blank'>Get Google Map API</a>"));
		form.add(txtScript);
		this.add(form);

		this.addButton(btnCopy);
		this.addButton(btnClose);
	}

	private native void copy(String text) /*-{
		if (window.clipboardData) { // Internet Explorer
		           window.clipboardData.setData("Text", ""+ text); // stupid IE... won't work without the ""+ ?!?!?
		           alert('Copied!');
		   } else if (window.netscape) { // Mozilla
		           try {
		                   netscape.security.PrivilegeManager.enablePrivilege('UniversalXPConnect');
		                   var gClipboardHelper = Components.classes["@mozilla.org/widget/clipboardhelper;1"].getService(Components.interfaces.nsIClipboardHelper);
		                   gClipboardHelper.copyString(text);
		                   alert('Copied!');
		           } catch(e) {
		                   return alert(e +'\n\nPlease type: "about:config" in your address bar.\nThen filter by "signed".\nChange the value of "signed.applets.codebase_principal_support" to true.\nYou should then be able to use this feature.');
		           }
		   } else {
		           return alert("Your browser does not support this feature");
		   }
	}-*/;
}

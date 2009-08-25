package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.captcha.RecaptchaService;
import com.bkitmobile.poma.client.captcha.RecaptchaServiceAsync;
import com.bkitmobile.poma.client.captcha.RecaptchaWidget;
import com.bkitmobile.poma.client.mail.MailService;
import com.bkitmobile.poma.client.mail.MailServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.layout.HorizontalLayout;

public class ContactWindow extends Window {

	private TextField txtEmail;
	private TextField txtTitle;
	private TextArea taContent;
	private Button btnSend;
	private Button btnCancel;
	private final MailServiceAsync emailAsync = GWT
			.create(MailService.class);
	private FieldSet fsMail;
	
	public static ContactWindow contactWindow = null;
	
	public static ContactWindow getInstance() {
		if (contactWindow != null)
			return contactWindow;
		else 
			return new ContactWindow();
	}

	public ContactWindow() {
		contactWindow = this;
		setTitle("Contact");
		init();
		layout();
	}
	
	private void init() {
		
		/*
		 * Email
		 */
		txtEmail = new TextField("Email ");
		txtEmail.setWidth(150);
		txtEmail.setVtype(VType.EMAIL);
		txtEmail.setAllowBlank(false);
		
		
		/*
		 * Title of email
		 */
		txtTitle = new TextField("Title ");
		txtTitle.setWidth(150);
		txtTitle.setAllowBlank(false);
		
		
		/*
		 * Mail content
		 */
		taContent = new TextArea("Content ");
		taContent.setSize(300, 150);
		taContent.setAllowBlank(false);
		
		
		/*
		 * Button
		 */
		btnSend = new Button("Send");
		btnSend.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (!txtEmail.isValid() || !taContent.isValid() || !txtTitle.isValid()) {
					MessageBox.alert("All field must not empty");
					return;
				}
				
				/*
				 * Check captcha
				 */
				CaptchaWindow.getInstance().show();
				CaptchaWindow.getInstance().addListener("validate", new Function() {
					@Override
					public void execute() {
						if (CaptchaWindow.getInstance().getValue() == true) {
							emailAsync.sendEmailToAdmin(txtEmail.getText(),
									txtTitle.getText(), taContent.getText(),
									new AsyncCallback<Boolean>() {

										@Override
										public void onFailure(Throwable caught) {
											MessageBox.alert("Send mail error: " + caught.toString());
											caught.printStackTrace();
										}

										@Override
										public void onSuccess(Boolean result) {
											if (!result) {
												MessageBox.alert("Can't send");
											} else {
												MessageBox.alert("Send succesfully");
												destroy();
												BkitPoma.returnToMap();
											}
										}
									});
						}
						
					}
				});


			}
		});
		btnCancel = new Button("Cancel");
		btnCancel.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				destroy();
				BkitPoma.returnToMap();
			}
		});

	}
	
	private void layout() {
		
		/*
		 * Setting for this window
		 */
		setSize(500, 300);
		setResizable(false);
		setDraggable(false);
		setButtonAlign(Position.CENTER);
		setCloseAction(Window.HIDE);
		
		
		/*
		 * Setting for main panel
		 */
		fsMail = new FieldSet();
		fsMail.setPaddings(5, 5, 15, 5);
		fsMail.setLabelWidth(100);
		fsMail.add(txtEmail);
		fsMail.add(txtTitle);
		fsMail.add(taContent);
		
		
		/*
		 * Add to main panel
		 */
		this.add(fsMail);
		this.addButton(btnSend);
		this.addButton(btnCancel);
		
	}
}

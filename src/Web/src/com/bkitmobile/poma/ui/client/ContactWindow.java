package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.localization.client.ContactWindowConstants;
import com.bkitmobile.poma.mail.client.MailService;
import com.bkitmobile.poma.mail.client.MailServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;

public class ContactWindow extends Window {

	private TextField txtEmail;
	private TextField txtTitle;
	private TextArea taContent;
	private Button btnSend;
	private Button btnCancel;
	
	private final static int TEXTFIELD_WIDTH = 300;

	private FieldSet fsMail;

	// email service
	private final MailServiceAsync emailAsync = GWT.create(MailService.class);
	// static instance
	public static ContactWindow contactWindow = null;
	// localization
	private ContactWindowConstants constants = GWT
			.create(ContactWindowConstants.class);

	/**
	 * 
	 * @return static instance of this contact window
	 */
	public static ContactWindow getInstance() {
		if (contactWindow != null)
			return contactWindow;
		else
			return new ContactWindow();
	}

	/**
	 * Constructor
	 */
	private ContactWindow() {
		contactWindow = this;
		setTitle(constants.title());
		init();
		layout();
	}

	private void init() {

		addListener(new PanelListenerAdapter() {
			@Override
			public void onShow(Component component) {
				txtEmail.setValue("");
				txtTitle.setValue("");
				taContent.setValue("");
				txtEmail.clearInvalid();
				txtTitle.clearInvalid();
				taContent.clearInvalid();
			}
		});

		/*
		 * Email
		 */
		txtEmail = new TextField(constants.email());
		txtEmail.setWidth(TEXTFIELD_WIDTH);
		txtEmail.setVtype(VType.EMAIL);
		txtEmail.setAllowBlank(false);

		/*
		 * Title of email
		 */
		txtTitle = new TextField(constants.title());
		txtTitle.setWidth(TEXTFIELD_WIDTH);
		txtTitle.setAllowBlank(false);

		/*
		 * Mail content
		 */
		taContent = new TextArea(constants.content());
		taContent.setSize(TEXTFIELD_WIDTH, 150);
		taContent.setAllowBlank(false);

		/*
		 * Button
		 */
		btnSend = new Button(constants.send());
		btnSend.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (!txtEmail.isValid() || !taContent.isValid()
						|| !txtTitle.isValid()) {
					MessageBox.alert(constants.notEmpty());
					return;
				}

				/*
				 * Check captcha
				 */
				CaptchaWindow.getInstance().show(new Function() {
					@Override
					public void execute() {
						emailAsync.sendEmailToAdmin1(txtEmail.getText(),
								txtTitle.getText(), taContent.getText(),
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										MessageBox.alert(constants
												.sendMailFailure()
												+ caught.toString());
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(Boolean result) {
										if (!result) {
											MessageBox.alert(constants
													.cantSend());
										} else {
											MessageBox.alert(constants
													.canSend());
											destroy();
											BkitPoma.returnToMap();
										}
									}
								});
					}
				});

			}
		});
		btnCancel = new Button(constants.cancel());
		btnCancel.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				hide();
			}
		});

	}

	private void layout() {

		/*
		 * Setting for this window
		 */
		setSize(485, 300);
		setResizable(false);
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

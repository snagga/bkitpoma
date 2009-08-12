package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.bkitmobile.poma.client.Utils;
import com.bkitmobile.poma.client.captcha.RecaptchaService;
import com.bkitmobile.poma.client.captcha.RecaptchaServiceAsync;
import com.bkitmobile.poma.client.captcha.RecaptchaWidget;
import com.bkitmobile.poma.client.captcha.RecaptchaService.Util;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.Tracked;
import com.bkitmobile.poma.client.localization.LRegisterTrackedForm;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class RegisterTrackedFormWindow extends Window {

	private FormPanel mainPanel;

	private TextField txtName;
	private TextField txtTrackedUN;
	private TextField txtEmail;
	private TextField txtEmailConfirm;
	private DateField dateBirthday;
	private TextField txtMobilePhone;
	private TextField txtAPIKey;

	private ProgressBar prgBarInterval;
	private Button btnPrgBarIncr = null;
	private Button btnPrgBarDecr = null;
	private int valuePrgbar = 5;

	private Button btnGenerateKey;
	private Button btnSchedule = null;
	private Button btnSubmit;

	private RecaptchaWidget reCaptchaWidget;

	private SchedulePopUpWindow sWindow = null;
	private FileUpload fileUploadIconPath = null;
	private Label lblFileUpload = null;

	private float level = (float) 1 / 10;
	boolean ok = true;
	/*
	 * Create instance of Database to manipulate
	 */
	private DatabaseServiceAsync dbService;
	private String trackerUN;

	// private LRegisterTrackedForm local;

	public RegisterTrackedFormWindow(String trackerUN) {
		this.trackerUN = trackerUN;
		this.setSize(500, 600);

		// local = GWT.create(LRegisterTrackedForm.class);
		dbService = DatabaseService.Util.getInstance();
		mainPanel = new FormPanel();
		mainPanel.setPaddings(5, 5, 5, 0);
		mainPanel.setSize(500, 600);
		mainPanel.setAutoScroll(true);

		/**
		 * Username
		 */
		txtTrackedUN = new TextField("tracked", "username");
		txtTrackedUN.setRegex("^[a-zA-Z0-9_]{3,32}$");
		txtTrackedUN.setAllowBlank(false);
		// txtUsername.setInvalidText(local.lbl_invalid_username());
		txtTrackedUN.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				// TODO Auto-generated method stub
				if (!txtTrackedUN.isValid()) {
					return;
				}
				super.onChange(field, newVal, oldVal);
				txtName.setValue(txtTrackedUN.getText());
				dbService.verifyTracked(txtTrackedUN.getText(),
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Boolean result) {
								// TODO Auto-generated method stub
								if (result) {
								} else {
									txtTrackedUN.markInvalid("Dup");
								}
							}
						});
			}
		});

		/**
		 * API Key
		 */
		txtAPIKey = new TextField("API Key: ");
		txtAPIKey.setValue(Utils.getAlphaNumeric(6));
		txtAPIKey.setDisabled(true);

		btnGenerateKey = new Button("Press here to generate Key for Tracked");
		btnGenerateKey.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				txtAPIKey.setValue(Utils.getAlphaNumeric(6));
			}
		});

		txtEmail = new TextField("Email", "email");
		txtEmail.setVtype(VType.EMAIL);
		mainPanel.add(txtEmail);

		txtEmailConfirm = new TextField("Conf email", "email_confirm");
		txtEmailConfirm.setVtype(VType.EMAIL);

		txtName = new TextField("Display name", "name");
		txtName.setAllowBlank(false);

		dateBirthday = new DateField("Birth", "d-M-Y");
		dateBirthday.setReadOnly(true);
		dateBirthday.setValue(new Date());

		txtMobilePhone = new TextField("tel", "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		ToolTip mobileTip = new ToolTip("tel valid");
		mobileTip.applyTo(txtMobilePhone);

		dateBirthday = new DateField();

		lblFileUpload = new Label("File up");
		fileUploadIconPath = new FileUpload();
		fileUploadIconPath.setName("fileUpload");

		prgBarInterval = new ProgressBar();
		prgBarInterval.setWidth(300);
		prgBarInterval.setValue((((float) (valuePrgbar - 1) * 10) / 14) * level);

		/**
		 * Interval GPS
		 */
		btnPrgBarIncr = new Button("Incr", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				btnPrgBarDecr.setDisabled(false);
				valuePrgbar++;
				prgBarInterval.setText(Format.format(
						"Loading item {0} of 15...", (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 15)
					btnPrgBarIncr.setDisabled(true);
			}
		});
		btnPrgBarDecr = new Button("Decr", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				btnPrgBarIncr.setDisabled(false);
				valuePrgbar--;
				prgBarInterval.setText(Format.format(
						"Loading item {0} of 15...", (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 1) {
					btnPrgBarDecr.setDisabled(true);
				}
			}
		});

		/**
		 * Schedule
		 */
		btnSchedule = new Button("Schedule", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				if (sWindow == null) {
					sWindow = new SchedulePopUpWindow();
				}
				sWindow.setAnimateTarget(button.getId());
				sWindow.show();
			}
		});

		reCaptchaWidget = new RecaptchaWidget(
				"6LdakQcAAAAAALX2JUFtsjbPTV0TcAkMhQY8iMkS");

		btnSubmit = new Button("Submit", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				ok = isValidForm();

				if (!ok) {
					MessageBox.alert("Invalid Message...");
					return;
				}

				/*
				 * Check captcha match or not
				 */
				RecaptchaServiceAsync rrsa = RecaptchaService.Util
						.getInstance();
				rrsa.verifyChallenge(reCaptchaWidget.getChallenge(),
						reCaptchaWidget.getResponse(),
						new AsyncCallback<Boolean>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
								MessageBox.alert("Wrong captcha message...");
							}

							public void onSuccess(Boolean result) {
								if (result) {
									Date date = dateBirthday.getValue();
									String strDate = String.valueOf(date
											.getYear())
											+ date.getMonth() + date.getDay();
									dbService
											.insertTracked(RegisterTrackedFormWindow.this.trackerUN,
													new Tracked(
															txtTrackedUN
																	.getText(),
															txtAPIKey.getText(),
															txtName.getText(),
															strDate,
															txtMobilePhone
																	.getText(),
															txtEmail.getText(),
															"0",
															"0",
															"/"
																	+ RegisterTrackedFormWindow.this.trackerUN
																	+ "/"
																	+ fileUploadIconPath
																			.getName(),
															"TRUE",
															"TRUE",
															sWindow
																	.getStringBinarySchedule(),
															String
																	.valueOf(valuePrgbar)),
													new AsyncCallback<Integer>() {

														@Override
														public void onFailure(
																Throwable caught) {
															caught
																	.printStackTrace();
															MessageBox
																	.alert("Kh\u00F4ng th\u1EC3 th\u00EAm th\u00F4ng c\u1EE7a \u0111\u1ED1i t\u01B0\u1EE3ng qu\u1EA3n l\u00FD v\u00E0o CSDL. Xin vui l\u00F2ng th\u1EED l\u1EA1i sau...");
														}

														@Override
														public void onSuccess(
																Integer result) {
															// TODO Forward to
															// other page
															MessageBox
																	.alert("onSuccess");
														}

													});
								} else {
									MessageBox.alert("fail error" + result);
								}
							}

						});
			}
		});

		mainPanel.add(txtTrackedUN);
		mainPanel.add(new HTML("</br>"));
		mainPanel.add(txtAPIKey);
		mainPanel.add(btnGenerateKey);
		mainPanel.add(new HTML("</br>"));
		mainPanel.add(lblFileUpload);
		mainPanel.add(fileUploadIconPath);
		mainPanel.add(new HTML("</br>"));
		mainPanel.add(prgBarInterval);
		mainPanel.add(btnPrgBarDecr);
		mainPanel.add(btnPrgBarIncr);
		mainPanel.add(new HTML("</br>"));
		mainPanel.add(btnSchedule);
		mainPanel.add(new HTML("</br>"));
		mainPanel.add(txtEmail);
		mainPanel.add(txtEmailConfirm);
		mainPanel.add(txtName);
		mainPanel.add(dateBirthday);
		mainPanel.add(txtMobilePhone);
		mainPanel.add(reCaptchaWidget);
		mainPanel.add(btnSubmit);

		mainPanel.setAutoScroll(true);
		mainPanel.setAutoWidth(true);
		mainPanel.setAutoHeight(true);

		this.add(mainPanel);
		// this.add(btnSubmit);

	}

	private boolean isValidForm() {
		if (!txtTrackedUN.isValid())
			return false;
		if (txtTrackedUN.getText().equals("")) {
			txtTrackedUN.markInvalid("null");
			return false;
		}
		if (!txtEmail.isValid())
			return false;
		if (txtEmail.getText().equals("")) {
			txtEmail.markInvalid("email null");
			return false;
		}
		if (!txtEmailConfirm.isValid())
			return false;
		if (txtEmailConfirm.getText().equals("")) {
			txtEmailConfirm.markInvalid("email con null");
			return false;
		}

		/*
		 * Check email and email confirm whether match or not
		 */
		if (!txtEmailConfirm.getText().equals(txtEmail.getText())) {
			txtEmailConfirm.markInvalid("Email not match");
			return false;
		}

		if (!txtMobilePhone.isValid())
			return false;
		return true;
	}

	private void test() {
		txtTrackedUN.setValue("test");
		txtEmail.setValue("abc@gmail.com");
		txtEmailConfirm.setValue("abc@gmail.com");
	}
}

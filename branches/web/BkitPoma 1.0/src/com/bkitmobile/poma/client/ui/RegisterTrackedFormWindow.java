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
import com.bkitmobile.poma.server.DatabaseServiceImpl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.*;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.*;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

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
	private Image imgPrgBarIncr = null;
	private Image imgPrgBarDecr = null;
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

	private LRegisterTrackedForm local;

	public RegisterTrackedFormWindow(String trackerUN) {
		local = GWT.create(LRegisterTrackedForm.class);

		this.trackerUN = trackerUN;

		// local = GWT.create(LRegisterTrackedForm.class);
		dbService = DatabaseService.Util.getInstance();
		mainPanel = new FormPanel();
		mainPanel.setPaddings(5, 5, 5, 0);
		this.setTitle(local.window_title());
		mainPanel.setAutoScroll(true);
		// mainPanel.setLayout(new FitLayout());
		this.setSize(470, 464);

		/**
		 * Username
		 */
		txtTrackedUN = new TextField(local.txtTrackedUN_lbl(), "username");
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
									txtTrackedUN.markInvalid(local
											.txtTrackedUN_dup());
								}
							}
						});
			}
		});
		txtTrackedUN.setDisabled(true);
		dbService.getNewTrackedUN(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable arg0) {
				arg0.printStackTrace();
			}

			public void onSuccess(String arg0) {
				txtTrackedUN.setValue(arg0);
			};
		});

		/**
		 * API Key
		 */
		txtAPIKey = new TextField(local.txtAPIKey());
		txtAPIKey.setValue(Utils.getAlphaNumeric(6));
		txtAPIKey.setDisabled(true);

		btnGenerateKey = new Button(local.btnGenerateKey_lbl());
		btnGenerateKey.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				txtAPIKey.setValue(Utils.getAlphaNumeric(6));
			}
		});

		txtEmail = new TextField(local.txtEmail_lbl(), "email");
		txtEmail.setVtype(VType.EMAIL);
		// mainPanel.add(txtEmail);

		txtEmailConfirm = new TextField(local.txtEmailConfirm_lbl(),
				"email_confirm");
		txtEmailConfirm.setVtype(VType.EMAIL);

		txtName = new TextField(local.txtName_lbl(), "name");
		txtName.setAllowBlank(false);

		dateBirthday = new DateField(local.dateBirth_lbl(), "d-M-Y");
		dateBirthday.setReadOnly(true);
		dateBirthday.setValue(new Date());

		txtMobilePhone = new TextField(local.txtMobilePhone_lbl(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		ToolTip mobileTip = new ToolTip(local.txtMobilePhone_invalid());
		mobileTip.applyTo(txtMobilePhone);

		lblFileUpload = new Label(local.lblFileUpload_lbl());
		fileUploadIconPath = new FileUpload();
		fileUploadIconPath.setName("fileUpload");

		prgBarInterval = new ProgressBar();
		prgBarInterval.setTitle(local.prgBarInterval_lbl());
		prgBarInterval.setText(Format.format(local.prgBarInterval_content(),
				(valuePrgbar) + ""));
		prgBarInterval.setWidth(300);
		prgBarInterval
				.setValue((((float) (valuePrgbar - 2) * 10) / 14) * level);

		/**
		 * Interval GPS
		 */
		imgPrgBarIncr = new Image("images/RegisterTracked/next.png");
		imgPrgBarIncr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (valuePrgbar == 15)
					return;
				imgPrgBarDecr.setUrl("images/RegisterTracked/back.png");
				// imgPrgBarDecr.setDisabled(false);
				valuePrgbar++;
				prgBarInterval.setText(Format.format(local
						.prgBarInterval_content(), (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 15)
					// imgPrgBarIncr.setDisabled(true);
					imgPrgBarIncr.setUrl("images/RegisterTracked/next_dis.png");
			}
		});

		imgPrgBarDecr = new Image("images/RegisterTracked/back.png");
		imgPrgBarDecr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (valuePrgbar == 1)
					return;
				// imgPrgBarIncr.setDisabled(false);
				imgPrgBarIncr.setUrl("images/RegisterTracked/next.png");
				valuePrgbar--;
				prgBarInterval.setText(Format.format(local
						.prgBarInterval_content(), (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 1) {
					// imgPrgBarDecr.setDisabled(true);
					imgPrgBarDecr.setUrl("images/RegisterTracked/back_dis.png");
				}
			}
		});

		/**
		 * Schedule
		 */
		btnSchedule = new Button(local.btnSchedule(),
				new ButtonListenerAdapter() {
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

		btnSubmit = new Button(local.btnSubmit_lbl(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						ok = isValidForm();

						if (!ok) {
							MessageBox.alert(local.msgbox_alert_title(), local
									.msgbox_alert_invalid());
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
										MessageBox.alert(local
												.reCaptchaWidget_invalid());
									}

									public void onSuccess(Boolean result) {
										if (result) {
											Date date = dateBirthday.getValue();
											String strDate = String
													.valueOf(date.getYear())
													+ date.getMonth()
													+ date.getDay();
											dbService
													.insertTracked(
															RegisterTrackedFormWindow.this.trackerUN,
															new Tracked(
																	txtTrackedUN
																			.getText(),
																	txtAPIKey
																			.getText(),
																	txtName
																			.getText(),
																	strDate,
																	txtMobilePhone
																			.getText(),
																	txtEmail
																			.getText(),
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
																			.valueOf(valuePrgbar),
																	RegisterTrackedFormWindow.this.trackerUN),
															new AsyncCallback<String>() {

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
																		String result) {
																	// TODO
																	// Forward
																	// to
																	// other
																	// page
																	MessageBox
																			.alert("onSuccess");
																}

															});
										} else {
											MessageBox
													.alert(
															local
																	.msgbox_alert_title(),
															local
																	.msgbox_alert_insert());
										}
									}

								});
					}
				});

		mainPanel.add(txtTrackedUN);
		mainPanel.add(txtAPIKey);
		mainPanel.add(btnGenerateKey);
		mainPanel.add(txtEmail);
		mainPanel.add(txtEmailConfirm);
		mainPanel.add(txtName);
		mainPanel.add(dateBirthday);
		mainPanel.add(txtMobilePhone);
		mainPanel.add(lblFileUpload);
		mainPanel.add(fileUploadIconPath);
		mainPanel.add(prgBarInterval);
		mainPanel.add(imgPrgBarDecr);
		mainPanel.add(imgPrgBarIncr);
		mainPanel.add(new HTML("</ br>"));
		mainPanel.add(new HTML("</ br>"));
		mainPanel.add(btnSchedule);
		mainPanel.add(new HTML("</ br>"));
		mainPanel.add(reCaptchaWidget);
		mainPanel.add(btnSubmit);

		// this.addListener(new WindowListenerAdapter() {
		// @Override
		// public void onResize(Window source, int width, int height) {
		// // TODO Auto-generated method stub
		// super.onResize(source, width, height);
		// System.out.println(width + " " + height);
		// }
		// });

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

	// private void test() {
	// txtTrackedUN.setValue("test");
	// txtEmail.setValue("abc@gmail.com");
	// txtEmailConfirm.setValue("abc@gmail.com");
	// }

}

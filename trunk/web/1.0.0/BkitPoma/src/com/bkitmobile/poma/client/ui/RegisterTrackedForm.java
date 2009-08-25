package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.Utils;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.localization.LRegisterTrackedForm;
import com.bkitmobile.poma.client.ui.imagechooser.ImageChooser;
import com.bkitmobile.poma.client.ui.imagechooser.ImageChooserCallback;
import com.bkitmobile.poma.client.ui.imagechooser.ImageData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class RegisterTrackedForm extends Panel {
	protected static String UNKNOWN_ICON = "images/RegisterTracked/status_unknown.png";

	protected FieldSet infoPanel;
	protected Panel imagePanel;
	protected Panel intervalPanel;

	protected TextField txtName;
	protected TextField txtTrackedUN;
	protected TextField txtEmail;
	protected TextField txtEmailConfirm;
	protected DateField dateBirthday;
	protected TextField txtMobilePhone;
	protected TextField txtAPIKey;
	protected TextField txtUrl;

	protected ProgressBar prgBarInterval;
	protected Image imgPrgBarIncr = null;
	protected Image imgPrgBarDecr = null;
	protected int valuePrgbar = 5;

	protected Button btnGenerateKey;
	protected Button btnSchedule = null;
	protected Button btnSubmit;
	protected Button btnImgChooser;
	protected Image imgIcon;

	protected ScheduleWindow sWindow = null;

	protected Label lblFileUpload = null;
	protected Radio radioUrlImage;
	protected Radio radioChooseImage;
	// protected FileUpload fileUpload;

	protected float level = (float) 1 / 10;

	protected DatabaseServiceAsync dbService;
	protected String trackerUN;

	protected LRegisterTrackedForm local;

	protected ImageChooser ic;

	protected String urlIcon = UNKNOWN_ICON;

	private Panel progressPanel;

	protected int labelWidth = 120;
	protected int textFieldWidth = 150;
	protected int formWidth = 400;

	protected int progressWidth = 300;
	protected int progressHeight = 100;

	protected boolean firstTime = true;

	public RegisterTrackedForm(String trackerUN) {
		local = GWT.create(LRegisterTrackedForm.class);
		dbService = DatabaseService.Util.getInstance();
		this.setTitle(local.window_title());
		this.trackerUN = trackerUN;

		init();
		layout();

	}

	private void init() {

		/*
		 * Schedule popup window
		 */
		sWindow = new ScheduleWindow(local);

		// /*
		// * File upload
		// */
		// fileUpload = new FileUpload() {
		// @Override
		// public void onBrowserEvent(Event event) {
		// if (DOM.eventGetType(event) == Event.ONCHANGE
		// && radioUploadImage.getValue() == true) {
		// urlIcon = fileUpload.getFilename();
		// // TODO convert filename to url in website
		// Image.prefetch(urlIcon);
		// imgIcon.setUrl(urlIcon);
		// }
		// }
		// };
		// fileUpload.sinkEvents(Event.ONCHANGE)

		/*
		 * Url of image
		 */
		txtUrl = new TextField();
		txtUrl.setVtype(VType.URL);
		txtUrl.setWidth(textFieldWidth);

		/**
		 * Username
		 */
		txtTrackedUN = new TextField(local.txtTrackedUN_lbl(), "username");
		txtTrackedUN.setRegex("^[a-zA-Z0-9_]{3,32}$");
		txtTrackedUN.setAllowBlank(false);
		txtTrackedUN.setWidth(textFieldWidth);
		// txtTrackedUN.setInvalidText(local.lbl_invalid_username());
		txtTrackedUN.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {

				if (!txtTrackedUN.isValid()) {
					return;
				}
				txtName.setValue(txtTrackedUN.getText());

				// dbService.verifyTracked(txtTrackedUN.getText(),
				// new AsyncCallback<Boolean>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// caught.printStackTrace();
				// }
				//
				// @Override
				// public void onSuccess(Boolean result) {
				// if (!result)
				// txtTrackedUN.markInvalid(local.txtTrackedUN_dup());
				// }
				// });
			}
		});

		// dbService.getNewTrackedUN(new AsyncCallback<String>() {
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// public void onSuccess(String result) {
		// txtTrackedUN.setValue(result);
		// };
		// });

		/**
		 * API Key
		 */
		txtAPIKey = new TextField(local.txtAPIKey());
		txtAPIKey.setValue(Utils.getAlphaNumeric(6));
		txtAPIKey.setDisabled(true);
		txtAPIKey.setWidth(textFieldWidth);

		/*
		 * Button generate key
		 */
		btnGenerateKey = new Button(local.btnGenerateKey_lbl());
		btnGenerateKey.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				txtAPIKey.setValue(Utils.getAlphaNumeric(6));
			}
		});

		/*
		 * Email
		 */
		txtEmail = new TextField(local.txtEmail_lbl(), "email");
		txtEmail.setVtype(VType.EMAIL);
		txtEmail.setWidth(textFieldWidth);

		/*
		 * Confirm email
		 */
		txtEmailConfirm = new TextField("Email confirm", "email_confirm");
		txtEmailConfirm.setVtype(VType.EMAIL);
		txtEmailConfirm.setWidth(textFieldWidth);

		/*
		 * Birthday
		 */
		dateBirthday = new DateField("Birthday", "d-M-Y");
		dateBirthday.setReadOnly(true);
		// dateBirthday.setValue(new Date());
		dateBirthday.setWidth(textFieldWidth);

		dateBirthday.addListener(new DatePickerListenerAdapter() {
			@Override
			public void onSelect(DatePicker dataPicker, Date date) {
				if (date.compareTo(new Date()) >= 0) {
					dateBirthday
							.markInvalid("You must enter a smaller day than to day");
					MessageBox.alert("Enter a smaller day than to day");
				}
			}
		});

		/*
		 * Name
		 */
		txtName = new TextField(local.txtName_lbl(), "name");
		txtName.setWidth(textFieldWidth);
		txtName.setAllowBlank(false);

		/*
		 * Mobile phone
		 */
		txtMobilePhone = new TextField(local.txtMobilePhone_lbl(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		txtMobilePhone.setWidth(textFieldWidth);
		ToolTip mobileTip = new ToolTip(local.txtMobilePhone_invalid());
		mobileTip.applyTo(txtMobilePhone);

		/*
		 * Button choose image
		 */
		btnImgChooser = new Button(local.btnImgChooser_lbl(),
				new ButtonListenerAdapter() {
					public void onClick(final Button button, EventObject e) {
						if (ic == null)
							ic = new ImageChooser("Image Chooser", 515, 400);

						ic.show(new ImageChooserCallback() {
							public void onImageSelection(ImageData data) {
								urlIcon = data.getUrl();
								imgIcon.setUrl(urlIcon);
							}
						});
					}
				});
		btnImgChooser.setIconCls("image-icon");
		btnImgChooser.setMinWidth(120);

		/*
		 * Image icon
		 */
		imgIcon = new Image(UNKNOWN_ICON);

		/*
		 * Radio button for upload image
		 */
		radioUrlImage = new Radio("Type an URL", "iconchooser");
		radioUrlImage.setChecked(true);
		radioUrlImage.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				if (checked) {
					btnImgChooser.setDisabled(true);
					// if (!firstTime && txtUrl.getText().trim().equals("")) {
					// MessageBox.alert("You must choose an image url");
					// txtUrl.focus();
					// txtUrl.markInvalid("Enter your url here");
					// } else {
					// urlIcon = txtUrl.getText();
					// Image.prefetch(urlIcon);
					// imgIcon.setUrl(urlIcon);
					// firstTime = false;
					// }
				}
			}

		});

		/*
		 * Radio button for choose image
		 */
		radioChooseImage = new Radio("Choose from existing icon", "iconchooser");
		radioChooseImage.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				if (checked)
					btnImgChooser.setDisabled(false);
			}
		});

		/*
		 * Progress bar
		 */
		prgBarInterval = new ProgressBar(28, ProgressBar.SHOW_TEXT);
		// prgBarInterval.setWidth("300px");
		// prgBarInterval.setHeight("100px");
		// prgBarInterval.setWidth(progressWidth);
		// prgBarInterval.setHeight(progressHeight);
		prgBarInterval.setTitle(local.prgBarInterval_lbl());
		prgBarInterval.setText(Format.format(local.prgBarInterval_content(),
				(valuePrgbar)));
		prgBarInterval
				.setValue((((float) (valuePrgbar - 2) * 10) / 14) * level);

		/*
		 * Interval GPS
		 */
		imgPrgBarIncr = new Image("images/RegisterTracked/next.png");
		imgPrgBarIncr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (valuePrgbar == 15)
					return;
				imgPrgBarDecr.setUrl("images/RegisterTracked/back.png");
				valuePrgbar++;
				prgBarInterval.setText(Format.format(local
						.prgBarInterval_content(), (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 15)
					imgPrgBarIncr.setUrl("images/RegisterTracked/next_dis.png");
			}
		});

		imgPrgBarDecr = new Image("images/RegisterTracked/back.png");
		imgPrgBarDecr.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				if (valuePrgbar == 1)
					return;
				imgPrgBarIncr.setUrl("images/RegisterTracked/next.png");
				valuePrgbar--;
				prgBarInterval.setText(Format.format(local
						.prgBarInterval_content(), (valuePrgbar) + ""));
				prgBarInterval.setValue((float) (valuePrgbar - 1) * 10 / 14
						* level);
				if (valuePrgbar == 1) {
					imgPrgBarDecr.setUrl("images/RegisterTracked/back_dis.png");
				}
			}
		});

		/*
		 * Schedule button handle
		 */
		btnSchedule = new Button(local.btnSchedule(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						sWindow.setAnimateTarget(button.getId());
						sWindow.show();
					}
				});

		/*
		 * Submit button handle
		 */
		btnSubmit = new Button(local.btnSubmit_lbl(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {

						if (!isValidForm()) {
							MessageBox.alert(local.msgbox_alert_title(), local
									.msgbox_alert_invalid());
							return;
						}

						/*
						 * Check captcha match or not
						 */
						CaptchaWindow.getInstance().show();
						CaptchaWindow.getInstance().addListener("validate",
								new Function() {

									@Override
									public void execute() {
										if (CaptchaWindow.getInstance()
												.getValue() == true) {
											// CTracked tracked = new CTracked(
											// txtTrackedUN.getText(),
											// txtAPIKey.getText(),
											// txtName.getText(),
											// txtMobilePhone.getText()
											// );
											// // TODO set tracker name of this
											// tracked
											// tracked.setIconpath(urlIcon);
											// tracked.setBirthday(dateBirthday.
											// getValue());
											//tracked.setEmail(txtEmail.getText(
											// ));
											//tracked.setIntervalgps(valuePrgbar
											// );
											// tracked.setSchedule(sWindow.
											// getStringBinarySchedule());
											// TODO set tracked schedule
											// information

											// dbService.insertTracked(
											// tracked,
											// new AsyncCallback<String>() {
											//
											// @Override
											// public void onFailure(Throwable
											// caught) {
											// System.out.println(
											// "Insert tracked error: " +
											// caught.toString() + "\n");
											// MessageBox.alert(
											// "Insert tracked error: " +
											// caught.toString() + "\n");
											// }
											//
											// @Override
											// public void onSuccess(String
											// result) {
											// System.out.println(
											// "Insert tracked successfully: " +
											// result);
											// MessageBox.alert(
											// "Insert tracked successfully: " +
											// result);
											// }
											//											
											// }
											// );
										} else {
											MessageBox
													.alert(
															local
																	.msgbox_alert_title(),
															local
																	.msgbox_alert_insert());
											MessageBox.alert(local
													.reCaptchaWidget_invalid());
										}

									}

								});

						System.out.println(sWindow.getStringBinarySchedule());
					} // end onClick

				});

	}

	private void layout() {

		this.setButtonAlign(Position.LEFT);
		this.setLayout(new VerticalLayout());
		this.setAutoScroll(true);

		/*
		 * information panel
		 */
		infoPanel = new FieldSet();
		infoPanel.setLabelWidth(150);
		infoPanel.setButtonAlign(Position.LEFT);
		infoPanel.setPaddings(10);
		infoPanel.add(txtTrackedUN);
		infoPanel.add(txtEmail);
		infoPanel.add(txtEmailConfirm);
		infoPanel.add(dateBirthday);
		infoPanel.add(txtName);
		infoPanel.add(txtMobilePhone);
		infoPanel.add(txtAPIKey);
		infoPanel.add(btnGenerateKey);

		/*
		 * image panel
		 */
		imagePanel = new Panel();
		imagePanel.setLayout(new VerticalLayout());
		imagePanel.setPaddings(5);

		Panel uploadImagePanel = new Panel();
		uploadImagePanel.setLayout(new HorizontalLayout(5));
		uploadImagePanel.add(radioUrlImage);
		// uploadImagePanel.add(new Label("Enter your url"));
		uploadImagePanel.add(txtUrl);

		Panel chooseImagePanel = new Panel();
		chooseImagePanel.setLayout(new HorizontalLayout(5));
		chooseImagePanel.add(radioChooseImage);
		chooseImagePanel.add(btnImgChooser);
		chooseImagePanel.add(imgIcon);

		imagePanel.add(new Label("Choose your avatar"));
		imagePanel.add(uploadImagePanel);
		imagePanel.add(chooseImagePanel);

		progressPanel = new Panel();
		progressPanel.setFrame(true);
		progressPanel.setLayout(new HorizontalLayout(5));
		progressPanel.setPaddings(5, 5, 5, 0);
		progressPanel.add(new Label("Interval"));
		progressPanel.add(prgBarInterval);
		progressPanel.add(imgPrgBarDecr);
		progressPanel.add(imgPrgBarIncr);
		progressPanel.add(btnSchedule);

		/*
		 * Add to main panel
		 */
		this.add(infoPanel);
		this.add(imagePanel);
		this.add(progressPanel);
		this.addButton(btnSubmit);
		this.addButton(new Button("Return to map", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				BkitPoma.returnToMap();
			}
		}));
	}

	protected boolean isValidForm() {
		if (!txtTrackedUN.isValid())
			return false;
		if (txtTrackedUN.getText().trim().equals("")) {
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
		if (txtEmailConfirm.getText().trim().equals("")) {
			txtEmailConfirm.markInvalid("Confirm your email");
			return false;
		}

		if (!txtEmailConfirm.getText().equals(txtEmail.getText())) {
			txtEmailConfirm.markInvalid("Email not match");
			return false;
		}

		if (!txtMobilePhone.isValid())
			return false;

		if (radioUrlImage.getValue() && txtUrl.getText().trim().equals(""))
			return false;

		return true;
	}



}

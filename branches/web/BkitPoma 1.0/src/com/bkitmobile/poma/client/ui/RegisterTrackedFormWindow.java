package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.bkitmobile.poma.client.Utils;
import com.bkitmobile.poma.client.captcha.RecaptchaService;
import com.bkitmobile.poma.client.captcha.RecaptchaServiceAsync;
import com.bkitmobile.poma.client.captcha.RecaptchaWidget;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.Tracked;
import com.bkitmobile.poma.client.localization.LRegisterTrackedForm;
import com.bkitmobile.poma.client.ui.imagechooser.ImageChooser;
import com.bkitmobile.poma.client.ui.imagechooser.ImageChooserCallback;
import com.bkitmobile.poma.client.ui.imagechooser.ImageData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.DateFieldDef;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.IntegerFieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ProgressBar;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.RowLayout;
import com.gwtext.client.widgets.layout.RowLayoutData;
import com.gwtext.client.widgets.layout.TableLayout;

public class RegisterTrackedFormWindow extends Window {
	private static String UNKNOWN_ICON = "images/RegisterTracked/status_unknown.png";

	private Panel pnlMain;
	private FormPanel pnlLeft;
	private Panel pnlRight;
	private Panel pnlBelow;
	private Panel pnlAbove;

	private TextField txtName;
	private TextField txtTrackedUN;
	private TextField txtEmail;
	// private TextField txtEmailConfirm;
	// private DateField dateBirthday;
	private TextField txtMobilePhone;
	private TextField txtAPIKey;

	private ProgressBar prgBarInterval;
	private Image imgPrgBarIncr = null;
	private Image imgPrgBarDecr = null;
	private int valuePrgbar = 5;

	private Button btnGenerateKey;
	private Button btnSchedule = null;
	private Button btnSubmit;
	private Button btnImgChooser;
	private Image imgIcon;

	private RecaptchaWidget reCaptchaWidget;

	private SchedulePopUpWindow sWindow = null;

	// private FileUpload fileUploadIconPath = null;
	private TextField txtURL;
	private Label lblFileUpload = null;
	private Radio radioIconURL;
	private Radio radioIconImgChoose;

	private float level = (float) 1 / 10;
	boolean ok = true;
	/*
	 * Create instance of Database to manipulate
	 */
	private DatabaseServiceAsync dbService;
	private String trackerUN;

	private LRegisterTrackedForm local;

	private ImageChooser ic;
	
	private boolean isURLIcon = true;
	private String urlIcon = UNKNOWN_ICON;

	public RegisterTrackedFormWindow(String trackerUN) {
		local = GWT.create(LRegisterTrackedForm.class);
		dbService = DatabaseService.Util.getInstance();
		this.setTitle(local.window_title());
		this.trackerUN = trackerUN;
		this.setButtonAlign(Position.CENTER);
		// this.setLayout(new RowLayout());
		this.setSize(660, 340);

		initPanel();
		initForm();
		createForm();

		// this.addListener(new WindowListenerAdapter() {
		// @Override
		// public void onResize(Window source, int width, int height) {
		// // TODO Auto-generated method stub
		// super.onResize(source, width, height);
		// System.out.println(width + " " + height);
		// }
		// });

		// this.add(pnlLeft);
		// this.add(btnSubmit);

	}

	public void initPanel() {
		pnlMain = new Panel();
		pnlMain.setPaddings(5, 5, 5, 0);
		pnlMain.setLayout(new RowLayout());
		pnlMain.setPaddings(15);
		pnlMain.setBodyStyle("border-style:dotted;border-color:blue;");

		pnlLeft = new FormPanel();
		pnlLeft.setPaddings(5, 5, 5, 0);
		pnlLeft.setAutoScroll(false);
		// mainPanel.setLayout(new FitLayout());

		pnlRight = new Panel();
		pnlRight.setPaddings(5, 5, 5, 0);
		pnlRight.setAutoScroll(false);
		pnlRight.setButtonAlign(Position.CENTER);
		// pnlRight.setLayout(new BorderLayout());

		pnlBelow = new Panel();
		pnlBelow.setLayout(new HorizontalLayout(5));
		pnlBelow.setPaddings(5, 5, 5, 0);
		pnlBelow.setAutoScroll(false);

		pnlAbove = new Panel();
		pnlAbove.setLayout(new TableLayout(2));
		pnlAbove.setPaddings(5, 5, 5, 0);
		pnlAbove.setAutoScroll(false);
	}

	public void initForm() {
		sWindow = new SchedulePopUpWindow(local);
		
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

		// txtEmailConfirm = new TextField(local.txtEmailConfirm_lbl(),
		// "email_confirm");
		// txtEmailConfirm.setVtype(VType.EMAIL);

		txtName = new TextField(local.txtName_lbl(), "name");
		txtName.setAllowBlank(false);

		// dateBirthday = new DateField(local.dateBirth_lbl(), "d-M-Y");
		// dateBirthday.setReadOnly(true);
		// dateBirthday.setValue(new Date());

		txtMobilePhone = new TextField(local.txtMobilePhone_lbl(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		ToolTip mobileTip = new ToolTip(local.txtMobilePhone_invalid());
		mobileTip.applyTo(txtMobilePhone);

		lblFileUpload = new Label(local.lblFileUpload_lbl());
		txtURL = new TextField("URL");
		txtURL.setWidth(120);
		txtURL.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				// TODO Auto-generated method stub
				super.onChange(field, newVal, oldVal);
				//				
				if(txtURL.getText().equals("")){
					urlIcon = UNKNOWN_ICON;
				}else{
					urlIcon = txtURL.getText();
				}
				
				Image.prefetch(txtURL.getText());
				imgIcon.setUrl(urlIcon);
			}
		});
		// fileUploadIconPath = new FileUpload();
		// fileUploadIconPath.setName("fileUpload");

		MemoryProxy dataProxy = new MemoryProxy(getData());
		RecordDef recordDef = new RecordDef(new FieldDef[] {
				new StringFieldDef("name"), new IntegerFieldDef("size"),
				new DateFieldDef("lastmod", "timestamp"),
				new StringFieldDef("url") });
		ArrayReader reader = new ArrayReader(recordDef);
		final Store store = new Store(dataProxy, reader, true);
		store.load();

		btnImgChooser = new Button(local.btnImgChooser_lbl(),
				new ButtonListenerAdapter() {
					public void onClick(final Button button, EventObject e) {
						if (ic == null) {
							ic = new ImageChooser("Image Chooser", 515, 400,
									store);
						}

						ic.show(new ImageChooserCallback() {
							public void onImageSelection(ImageData data) {
								// Element el = DomHelper.append("images",
								// Format.format(
								// "<img src='{0}' style='margin:20px;visibility:hidden;'/>"
								// , data.getUrl()));
								// ExtElement extEl = new ExtElement(el);
								// extEl.show(true).frame();
								// button.focus();
								urlIcon = data.getUrl();
								imgIcon.setUrl(urlIcon);
							}
						});
					}
				});
		btnImgChooser.setIconCls("image-icon");
		btnImgChooser.setMinWidth(120);
		imgIcon = new Image(UNKNOWN_ICON);

		radioIconURL = new Radio("", "iconchooser");
		radioIconImgChoose = new Radio("", "iconchooser");
		radioIconURL.setChecked(true);
		radioIconURL.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				// TODO Auto-generated method stub
				super.onCheck(field, checked);
				if (checked) {
					isURLIcon = true;
					btnImgChooser.setDisabled(true);
					txtURL.setDisabled(false);
					if (txtURL.getText().equals(""))
						urlIcon = UNKNOWN_ICON;
					else
						urlIcon = txtURL.getText();
					imgIcon.setUrl(urlIcon);
				}

			}
		});
		radioIconImgChoose.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
				// TODO Auto-generated method stub
				super.onCheck(field, checked);
				if (checked) {
					isURLIcon = false;
					txtURL.setDisabled(true);
					btnImgChooser.setDisabled(false);
					urlIcon = UNKNOWN_ICON;
				}
			}
		});

		prgBarInterval = new ProgressBar();
		prgBarInterval.setWidth(300);
		prgBarInterval.setTitle(local.prgBarInterval_lbl());
		prgBarInterval.setText(Format.format(local.prgBarInterval_content(),
				(valuePrgbar)));
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
//						if (sWindow == null) {
//							sWindow = new SchedulePopUpWindow(local);
//						}
						// sWindow.setAnimateTarget(button.getId());
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
											// Date date =
											// dateBirthday.getValue();
											Date date = new Date();
											String strDate = String
													.valueOf(date.getYear())
													+ date.getMonth()
													+ date.getDay();
											String urlIcon = "";
											System.out.println("strDate = " + strDate);
												
// String username,String apikey,String name,String birthday,String tel,String email,String state,String gpsState,String iconPath,String showInMap,String eb,String schedule,String intervalgps,String owner
											dbService
													.insertTracked(
															RegisterTrackedFormWindow.this.trackerUN,
															new Tracked(
																	txtTrackedUN.getText(),
																	txtAPIKey.getText(),
																	txtName.getText(),
																	strDate,
																	txtMobilePhone.getText(),
																	txtEmail.getText(),
																	"0",
																	"0","7",urlIcon,"TRUE","TRUE",sWindow.getStringBinarySchedule(),String.valueOf(valuePrgbar),RegisterTrackedFormWindow.this.trackerUN),
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

						System.out.println(sWindow.getStringBinarySchedule());
					}// end onClick

				});

	}

	public void createForm() {
		pnlLeft.add(txtTrackedUN);
		pnlLeft.add(txtAPIKey);
		pnlLeft.add(btnGenerateKey);
		pnlLeft.add(txtEmail);
		// pnlForm.add(txtEmailConfirm);
		pnlLeft.add(txtName);
		// pnlForm.add(dateBirthday);
		pnlLeft.add(txtMobilePhone);

		pnlBelow.add(prgBarInterval);
		pnlBelow.add(imgPrgBarDecr);
		pnlBelow.add(imgPrgBarIncr);
		pnlBelow.add(btnSchedule);

		pnlRight.add(lblFileUpload);
		// pnlRight.add(fileUploadIconPath);
		pnlRight.add(new HTML("</br>"));

		Panel pnlURL = new Panel();
		pnlURL.setLayout(new HorizontalLayout(5));
		pnlURL.add(radioIconURL);
		pnlURL.add(txtURL);
		pnlRight.add(pnlURL);

		Panel pnlImgChooser = new Panel();
		pnlImgChooser.setLayout(new HorizontalLayout(5));
		pnlImgChooser.add(radioIconImgChoose);
		pnlImgChooser.add(btnImgChooser);
		pnlRight.add(pnlImgChooser);

		pnlRight.add(imgIcon);

		pnlRight.add(new HTML("</br>"));
		pnlRight.add(reCaptchaWidget);

		pnlAbove.add(pnlLeft);
		pnlAbove.add(pnlRight);

		pnlMain.add(pnlAbove, new RowLayoutData(210));
		pnlMain.add(pnlBelow, new RowLayoutData(55));

		this.add(pnlMain, new AnchorLayoutData("100% 100%"));
		this.addButton(btnSubmit);
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
		// if (!txtEmailConfirm.isValid())
		// return false;
		// if (txtEmailConfirm.getText().equals("")) {
		// txtEmailConfirm.markInvalid("email con null");
		// return false;
		// }

		/*
		 * Check email and email confirm whether match or not
		 */
		// if (!txtEmailConfirm.getText().equals(txtEmail.getText())) {
		// txtEmailConfirm.markInvalid("Email not match");
		// return false;
		// }
		if (!txtMobilePhone.isValid())
			return false;
		return true;
	}

	private Object[][] getData() {
		return new Object[][] {
				new Object[] { "Pirates of the Caribbean", new Integer(2120),
						new Long(1180231870000l), "images/view/carribean.jpg" },
				new Object[] { "Resident Evil", new Integer(2120),
						new Long(1180231870000l),
						"images/view/resident_evil.jpg" },
				new Object[] { "Blood Diamond", new Integer(2120),
						new Long(1180231870000l),
						"images/view/blood_diamond.jpg" },
				new Object[] { "No Reservations", new Integer(2120),
						new Long(1180231870000l),
						"images/view/no_reservations.jpg" },
				new Object[] { "Casino Royale", new Integer(2120),
						new Long(1180231870000l),
						"images/view/casino_royale.jpg" },
				new Object[] { "Good Shepherd", new Integer(2120),
						new Long(1180231870000l),
						"images/view/good_shepherd.jpg" },
				new Object[] { "Ghost Rider", new Integer(2120),
						new Long(1180231870000l), "images/view/ghost_rider.jpg" },
				new Object[] { "Batman Begins", new Integer(2120),
						new Long(1180231870000l),
						"images/view/batman_begins.jpg" },
				new Object[] { "Last Samurai", new Integer(2120),
						new Long(1180231870000l),
						"images/view/last_samurai.jpg" },
				new Object[] { "Italian Job", new Integer(2120),
						new Long(1180231870000l), "images/view/italian_job.jpg" },
				new Object[] { "Mission Impossible III", new Integer(2120),
						new Long(1180231870000l), "images/view/mi3.jpg" },
				new Object[] { "Mr & Mrs Smith", new Integer(2120),
						new Long(1180231870000l), "images/view/smith.jpg" },
				new Object[] { "Inside Man", new Integer(2120),
						new Long(1180231870000l), "images/view/inside_man.jpg" },
				new Object[] { "The Island", new Integer(2120),
						new Long(1180231870000l), "images/view/island.jpg" } };
	}

	// private void test() {
	// txtTrackedUN.setValue("test");
	// txtEmail.setValue("abc@gmail.com");
	// txtEmailConfirm.setValue("abc@gmail.com");
	// }

}

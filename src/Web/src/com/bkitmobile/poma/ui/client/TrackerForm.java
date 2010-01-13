package com.bkitmobile.poma.ui.client;

import java.util.Date;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.home.client.WebsiteConfig;
import com.bkitmobile.poma.localization.client.TrackerFormConstants;
import com.bkitmobile.poma.mail.client.TrackerService;
import com.bkitmobile.poma.mail.client.TrackerServiceAsync;
import com.bkitmobile.poma.util.client.Task;
import com.bkitmobile.poma.util.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.BoxComponentListener;
import com.gwtext.client.widgets.event.BoxComponentListenerAdapter;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

/**
 * Base class for tracker form
 * @author Hieu Rocker
 */
public class TrackerForm extends FormPanel {

	protected TextField txtUsername;
	protected TextField txtPassword;
	protected TextField txtPasswordConfirm;
	protected TextField txtName;
	protected DateField dateBirthday;
	protected TextField txtMobilePhone;
	protected TextField txtAddress;
	protected TextField txtEmail;
	protected TextField txtEmailConfirm;

	protected ComboBox cbLanguage;
	protected ComboBox cbCountry;
	protected ComboBox cbTimeZone;

	protected Label lbActive;
	protected Label lbEnable;

	protected TextField txtApiKey;
	protected Button btnGenerateApiKey;

	protected Button btnSubmit;
	protected Button btnReset;
	protected Button btnReturnToMap;

	protected DatabaseServiceAsync dbService = DatabaseService.Util
			.getInstance();
	protected TrackerServiceAsync trackerService = TrackerService.Util
			.getInstance();

	protected final int TEXTFIELD_WIDTH = 350;
	protected final int LABEL_WIDTH = 150;
	protected final int COMBO_WIDTH = TEXTFIELD_WIDTH-17;
	protected final int FORM_WIDTH = 800;

	protected FieldSet fsRequireInfo;
	protected FieldSet fsOptionalInfo;

	protected CTracker submitTracker;
	
	protected TrackerFormConstants constants = GWT.create(TrackerFormConstants.class);

	/**
	 * Constructor
	 */
	public TrackerForm() {
		init();
		layout();
		resetForm();
	}
	
	/**
	 * validate all form
	 * @return string that inform where field is invalid, null if all are OK
	 */
	protected String validate() {
		if (!txtName.isValid()) {
			return txtName.getInvalidText();
		}

		if (!txtUsername.isValid()) {
			return txtUsername.getInvalidText();
		}

		if (!txtPassword.isValid()) {
			return txtPassword.getInvalidText();
		}

		if (!txtEmail.isValid()) {
			return txtEmail.getInvalidText();
		}

		if (!dateBirthday.isValid()) {
			return dateBirthday.getInvalidText();
		}

		if (!txtMobilePhone.isValid()) {
			return txtMobilePhone.getInvalidText();
		}

		if (!txtAddress.isValid()) {
			return txtAddress.getInvalidText();
		}

		return postValidate();
	}

	/**
	 * Do other thing in tracker form sub-class
	 * @return
	 */
	protected String postValidate() {
		return null;
	}

	/**
	 * initialize
	 */
	protected void init() {
		/*
		 * Username
		 */
		txtUsername = new TextField(constants.txtUsername(), "username");
		txtUsername.setRegex("^[a-zA-Z][a-zA-Z0-9_]{3,32}$");
		txtUsername.setAllowBlank(false);
		txtUsername.setWidth(TEXTFIELD_WIDTH);
		txtUsername.setInvalidText(constants.txtUsername_setInvalidText());
		txtUsername.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (!txtUsername.isValid()) {
					return;
				}
				dbService.getTracker(txtUsername.getText(),
						new AsyncCallback<ServiceResult<CTracker>>() {
							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ServiceResult<CTracker> result) {
								if (result.getResult() != null)
									txtUsername.markInvalid(constants.txtUsername_markInvalid_dup());
							}
						});
			}
		});

		/*
		 * Password
		 */
		txtPassword = new TextField(constants.txtPassword(), "password");
		txtPassword.setAllowBlank(false);
		txtPassword.setWidth(TEXTFIELD_WIDTH);
		txtPassword.setPassword(true);

		/**
		 * Confirm Password
		 */
		txtPasswordConfirm = new TextField(constants.txtPasswordConfirm(),
				"password_confirm");
		txtPasswordConfirm.setAllowBlank(false);
		txtPasswordConfirm.setWidth(TEXTFIELD_WIDTH);
		txtPasswordConfirm.setPassword(true);

		/*
		 * Name of tracker
		 */
		txtName = new TextField(constants.txtName(), "name");
		txtName.setAllowBlank(false);
		txtName.setWidth(TEXTFIELD_WIDTH);

		/*
		 * Birthday
		 */
		dateBirthday = new DateField(constants.dateBirthday(), "d-m-Y");
		dateBirthday.setWidth(COMBO_WIDTH);
		dateBirthday.setMaxValue(new Date());
		dateBirthday.setEmptyText(constants.dateBirthday_setEmptyText());

		/*
		 * Mobile phone
		 */
		txtMobilePhone = new TextField(constants.txtMobilePhone(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		txtMobilePhone.setWidth(TEXTFIELD_WIDTH);
		new ToolTip(constants.txtMobilePhone_tooltip()).applyTo(txtMobilePhone);

		/*
		 * Address of tracker
		 */
		txtAddress = new TextArea(constants.txtAddress(), "address");
		txtAddress.setMaxLength(400);
		txtAddress.setWidth(TEXTFIELD_WIDTH);

		/*
		 * Email
		 */
		txtEmail = new TextField(constants.txtEmail(), "email");
		txtEmail.setVtype(VType.EMAIL);
		txtEmail.setWidth(TEXTFIELD_WIDTH);

		/*
		 * Confirm email
		 */
		txtEmailConfirm = new TextField(constants.txtEmailConfirm(),
				"email_confirm");
		txtEmailConfirm.setWidth(TEXTFIELD_WIDTH);
		txtEmailConfirm.setVtype(VType.EMAIL);

		cbLanguage = new ComboBox(constants.cbLanguage());
		Store languageStore = new SimpleStore(new String[] { "locales",
				"languages" }, WebsiteConfig.LANGUAGE);
		languageStore.load();
		cbLanguage.setStore(languageStore);
		cbLanguage.setDisplayField("languages");
		cbLanguage.setValueField("locales");
		cbLanguage.setForceSelection(true);
		cbLanguage.setWidth(COMBO_WIDTH);
		cbLanguage.setListWidth(TEXTFIELD_WIDTH);

		/*
		 * country names
		 */
		cbCountry = new ComboBox(constants.cbCountry());
		Store countryStore = new SimpleStore(new String[] { "country_codes",
				"country_names" }, WebsiteConfig.COUNTRIES_NAME);
		countryStore.load();
		cbCountry.setStore(countryStore);
		cbCountry.setForceSelection(true);
		cbCountry.setDisplayField("country_names");
		cbCountry.setValueField("country_codes");
		cbCountry.setWidth(COMBO_WIDTH);
		cbCountry.setListWidth(TEXTFIELD_WIDTH);

		/*
		 * time zone
		 */
		cbTimeZone = new ComboBox(constants.cbTimeZone());
		Store timeZoneStore = new SimpleStore(
				new String[] { "timezone", "gmt" }, WebsiteConfig.TIME_ZONES);
		timeZoneStore.load();
		cbTimeZone.setStore(timeZoneStore);
		cbTimeZone.setDisplayField("timezone");
		cbTimeZone.setValueField("gmt");
		cbTimeZone.setWidth(COMBO_WIDTH);
		cbTimeZone.setListWidth(COMBO_WIDTH);
		cbTimeZone.setForceSelection(true);

		lbActive = new Label(constants.lbActive());

		lbEnable = new Label(constants.lbEnable());

		/*
		 * Tracker apikey
		 */
		txtApiKey = new TextField(constants.txtApiKey(), "apikey");
		txtApiKey.setWidth(TEXTFIELD_WIDTH);
		txtApiKey.setReadOnly(true);
		txtApiKey.setEmptyText(constants.txtApiKey_setEmptyText());

		btnGenerateApiKey = new Button(constants.btnGenerateApiKey(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						txtApiKey.setValue(Utils.getAlphaNumeric(32));
					}
				});

		// buttons
		btnSubmit = new Button();
		btnSubmit.setText(constants.btnSubmit());
		btnSubmit.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				String s = validate();
				if (s != null) {
					MessageBox.alert(s);
					return;
				}
				CaptchaWindow captcha = CaptchaWindow.getInstance();
				captcha.show(new Function() {
					@Override
					public void execute() {
						submit();
					}
				});
			}
		});

		btnReturnToMap = new Button(constants.btnReturnToMap());
		btnReturnToMap.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				BkitPoma.returnToMap();
			}
		});

		btnReset = new Button(constants.btnReset(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				resetForm();
			}
		});

		fsOptionalInfo = new FieldSet();
		fsRequireInfo = new FieldSet();
		
		this.addListener(new PanelListenerAdapter(){
			@Override
			public void onShow(Component component) {
				resetForm();
//				txtUsername.clearInvalid();
//				txtName.clearInvalid();
//				txtPassword.clearInvalid();
//				txtPasswordConfirm.clearInvalid();
				
				MenuPanel.getInstance().hide();
				setSize(Window.getClientWidth(), Window.getClientHeight());
				BkitPoma.resize();
			}
		});

		postInit();
	}

	/**
	 * After init
	 */
	protected void postInit() {
	}

	/**
	 * Create CTracker and assign field into this CTracker
	 */
	protected void assignTracker() {
		submitTracker = new CTracker();
		submitTracker.setName(txtName.getText());
		submitTracker.setUsername(txtUsername.getText());
		submitTracker.setPassword(txtPassword.getText());
		submitTracker.setEmail(txtEmail.getText());
		submitTracker.setBirthday(dateBirthday.getValue());
		submitTracker.setTel(txtMobilePhone.getText().equals("") ? null
				: txtMobilePhone.getText());
		submitTracker.setAddr(txtAddress.getText().equals("") ? null
				: txtAddress.getText());
		submitTracker.setLang(cbLanguage.getValue());
		submitTracker.setCountry(cbCountry.getValue());
		try {
			submitTracker.setGmt(new Integer(cbTimeZone.getValue()));
		} catch (Exception ex) {
			submitTracker.setGmt(7);
		}
		submitTracker.setApiKey(txtApiKey.getText());
		postAssignTracker();
	}

	/**
	 * After assignCTracker
	 */
	protected void postAssignTracker() {
	}

	/**
	 * When user press Submit
	 */
	private void submit() {
		assignTracker();
		BkitPoma.startLoading(constants.BkitPoma_startLoading_wating());
		postSubmit();
	}

	/**
	 * After submit
	 */
	protected void postSubmit() {
	}

	/**
	 * Reset form with CTracker at UserSettings
	 */
	public void resetForm() {
		// txtUsername.setValue(UserSettings.ctracker.getUsername());
		final CTracker ctracker = UserSettings.ctracker == null ? new CTracker()
				: UserSettings.ctracker;

		txtUsername.setValue(ctracker.getUsername() == null ? "" : ctracker
				.getUsername());
		txtUsername.clearInvalid();

		txtPassword.setValue("");
		txtPassword.clearInvalid();

		txtPasswordConfirm.setValue("");
		txtPasswordConfirm.clearInvalid();

		txtName.setValue(ctracker.getName() == null ? "" : ctracker.getName());
		txtName.clearInvalid();

		if (ctracker.getBirthday() != null) {
			UserSettings.timerTask.getTaskList().add(new Task(2) {
				@Override
				public void execute() {
					dateBirthday.setValue(ctracker.getBirthday());
					finish();
				}
			});
		} else {
			dateBirthday.setValue("");
		}

		dateBirthday.clearInvalid();

		txtMobilePhone.setValue(ctracker.getTel() == null ? "" : ctracker
				.getTel());
		txtMobilePhone.clearInvalid();

		txtAddress.setValue(ctracker.getAddr() == null ? "" : ctracker
				.getAddr());
		txtAddress.clearInvalid();

		txtEmail.setValue(ctracker.getEmail() == null ? "" : ctracker
				.getEmail());
		txtEmail.clearInvalid();

		txtEmailConfirm.setValue(ctracker.getEmail() == null ? "" : ctracker
				.getEmail());
		txtEmailConfirm.clearInvalid();

		cbLanguage.setValue(ctracker.getLang() == null ? "vi" : ctracker
				.getLang());
		cbCountry.setValue(ctracker.getCountry() == null ? "VN" : ctracker
				.getCountry());
		cbTimeZone.setValue(ctracker.getGmt() == null ? "7" : ctracker.getGmt()
				+ "");

		txtApiKey.setValue(ctracker.getApiKey() == null ? "" : ctracker
				.getApiKey());
		postResetForm();
	}

	/**
	 * After resetForm
	 */
	protected void postResetForm() {
	}

	/**
	 * Layout
	 */
	protected void layout() {
		// Setting for main panel
		this.setPaddings(5, 5, 10, 5);
		this.setLabelWidth(LABEL_WIDTH);
		this.setButtonAlign(Position.CENTER);
		this.setAutoScroll(true);

		requireItemLayout();
		optionalItemLayout();

		this.addButton(btnSubmit);
		this.addButton(btnReset);
		this.addButton(btnReturnToMap);
		postLayout();
	}

	/**
	 * After postLayout
	 */
	protected void postLayout() {
	}

	/**
	 * Layout for fsRequire
	 */
	protected void requireItemLayout() {
		fsRequireInfo = new FieldSet();
		fsRequireInfo.setTitle(constants.fsRequireInfo());
//		Panel pnlRequire = new Panel();
//		pnlRequire.setBorder(false);
//		pnlRequire.setLayout(new FitLayout());

		// require information
		fsRequireInfo.add(txtName);
		fsRequireInfo.add(txtUsername);
		fsRequireInfo.add(txtPassword);
		fsRequireInfo.add(txtPasswordConfirm);
		fsRequireInfo.add(txtEmail);
		fsRequireInfo.add(txtEmailConfirm);
		
//		pnlRequire.add(txtName);
//		pnlRequire.add(txtUsername);
//		pnlRequire.add(txtPassword);
//		pnlRequire.add(txtPasswordConfirm);
//		pnlRequire.add(txtEmail);
//		pnlRequire.add(txtEmailConfirm);
//		fsRequireInfo.add(pnlRequire);

		this.add(fsRequireInfo);
	}

	/**
	 * Layout for fsOptionalInfo
	 */
	protected void optionalItemLayout() {
		fsOptionalInfo = new FieldSet(constants.fsOptionalInfo());
		fsOptionalInfo.setCollapsible(true);
		fsOptionalInfo.setButtonAlign(Position.LEFT);

		// optional information
		fsOptionalInfo.add(dateBirthday);
		fsOptionalInfo.add(txtMobilePhone);
		fsOptionalInfo.add(txtAddress);
		fsOptionalInfo.add(cbLanguage);
		fsOptionalInfo.add(cbCountry);
		fsOptionalInfo.add(cbTimeZone);
		fsOptionalInfo.add(txtApiKey);
		fsOptionalInfo.addButton(btnGenerateApiKey);

		this.add(fsOptionalInfo);
	}
}

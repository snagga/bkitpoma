package com.bkitmobile.poma.ui.client;

import java.util.Date;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.home.client.WebsiteConfig;
import com.bkitmobile.poma.localization.client.TrackedFormConstants;
import com.bkitmobile.poma.ui.client.imagechooser.ImageChooser;
import com.bkitmobile.poma.ui.client.imagechooser.ImageChooserCallback;
import com.bkitmobile.poma.ui.client.imagechooser.ImageData;
import com.bkitmobile.poma.ui.client.schedule.Schedule;
import com.bkitmobile.poma.ui.client.schedule.ScheduleCallback;
import com.bkitmobile.poma.ui.client.schedule.ScheduleItem;
import com.bkitmobile.poma.ui.client.schedule.ScheduleWindow;
import com.bkitmobile.poma.util.client.Task;
import com.bkitmobile.poma.util.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.HorizontalLayout;

/**
 * @author Hieu Rocker
 */
public class TrackedForm extends FormPanel {

	protected DatabaseServiceAsync dbService = DatabaseService.Util
			.getInstance();

	protected TextField txtUsername;
	protected TextField txtPassword;
	protected TextField txtPasswordConfirm;
	protected TextField txtName;
	protected DateField dateBirthday;
	protected TextField txtMobilePhone;
	protected TextField txtEmail;
	protected TextField txtEmailConfirm;
	protected ComboBox cbIntervalGPS;

	protected TextField txtIconPath;
	protected Image imgIcon;
	protected ImageChooser iconChooser;
	protected Button btnIconChooser;

	protected ComboBox cbLanguage;
	protected ComboBox cbCountry;
	protected ComboBox cbTimeZone;

	protected Checkbox checkShowInMap;
	protected Checkbox checkActive;
	protected Checkbox checkEmbedded;

	protected TextField txtApiKey;
	protected Button btnGenerateApiKey;

	protected Button btnSubmit;
	protected Button btnReset;
	protected Button btnReturnToMap;

	protected final int TEXTFIELD_WIDTH = 350;
	protected final int LABEL_WIDTH = 150;
	protected final int COMBO_WIDTH = TEXTFIELD_WIDTH-17;
	protected final int FORM_WIDTH = 800;

	protected FieldSet fsRequireInfo;
	protected FieldSet fsOptionalInfo;
	protected FieldSet fsScheduleWindow;

	protected CTracked submitTracked;
	// protected ScheduleWindow scheduleWindow = null;
//	protected Schedule schedule = new Schedule();
	protected float level = 0.1F;

	protected ScheduleWindow scheduleWindow;
	private Button btnSchedule;
	private MemoryProxy proxy;
	protected Store store;
	protected GridPanel gridSchedule;
	
	protected TrackedFormConstants constants = GWT.create(TrackedFormConstants.class);
	
	public static String DEFFAULT_ICON_PATH = "http://maps.google.com/mapfiles/ms/micons/red-dot.png";

	/**
	 * Constructor
	 */
	public TrackedForm() {
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

		if (!cbIntervalGPS.isValid()) {
			return cbIntervalGPS.getInvalidText();
		}

		if (!dateBirthday.isValid()) {
			return dateBirthday.getInvalidText();
		}

		if (!txtMobilePhone.isValid()) {
			return txtMobilePhone.getInvalidText();
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
		txtUsername.setRegex("^[a-zA-Z0-9_]{3,32}$");
		txtUsername.setAllowBlank(false);
		txtUsername.setInvalidText(constants.txtUsername_setInvalidText());
		txtUsername.setWidth(TEXTFIELD_WIDTH);
		
		/*
		 * Name of tracker
		 */
		txtName = new TextField(constants.txtName(), "name");
		txtName.setAllowBlank(false);
		txtName.setWidth(TEXTFIELD_WIDTH);

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
		 * Birthday
		 */
		dateBirthday = new DateField(constants.dateBirthday(), "d-m-Y");
		// dateBirthday.setReadOnly(true);
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

		txtIconPath = new TextField(constants.txtIconPath());
		txtIconPath.setValue(DEFFAULT_ICON_PATH);
		txtIconPath.setWidth(TEXTFIELD_WIDTH);
		txtIconPath.setReadOnly(true);

		imgIcon = new Image();
		imgIcon.setUrl(DEFFAULT_ICON_PATH);
		iconChooser = new ImageChooser();
		iconChooser.setSize(300, 300);
		btnIconChooser = new Button(constants.btnIconChooser());
		btnIconChooser.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				iconChooser.show(new ImageChooserCallback() {
					public void onImageSelection(ImageData data) {
						txtIconPath.setValue(data.getUrl());
						imgIcon.setUrl(data.getUrl());
					}
				});
			}
		});

		cbIntervalGPS = new ComboBox(constants.cbIntervalGPS());
		Store intervalStore = new SimpleStore("intervals", new String[] { "10",
				"30", "60", "300", "1800", "3600" });
		intervalStore.load();
		cbIntervalGPS.setStore(intervalStore);
		cbIntervalGPS.setValueField("intervals");
		cbIntervalGPS.setDisplayField("intervals");
		cbIntervalGPS.setForceSelection(true);
		cbIntervalGPS.setRegex("^[0-9]+$");
		cbIntervalGPS.setWidth(100);
		cbIntervalGPS.setListWidth(100);
		cbIntervalGPS.setValue("10");
		
		cbLanguage = new ComboBox(constants.cbLanguage());
		Store languageStore = new SimpleStore(new String[] { "locales",
				"languages" }, WebsiteConfig.LANGUAGE);
		languageStore.load();
		cbLanguage.setStore(languageStore);
		cbLanguage.setDisplayField("languages");
		cbLanguage.setValueField("locales");
		cbLanguage.setForceSelection(true);
		cbLanguage.setWidth(COMBO_WIDTH);

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
		cbCountry.setListWidth(COMBO_WIDTH);

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

		checkShowInMap = new Checkbox(constants.checkShowInMap());
		checkActive = new Checkbox(constants.checkActive());
		checkEmbedded = new Checkbox(constants.checkEmbedded());

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
				CaptchaWindow.getInstance().show(new Function() {
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
		
//		this.addListener(new PanelListenerAdapter(){
//			@Override
//			public void onShow(Component component) {
////				resetForm();
//				
//				MenuPanel.getInstance().hide();
//				setSize(Window.getClientWidth(), Window.getClientHeight());
//				BkitPoma.resize();
//			}
//		});

		postInit();
	}

	/**
	 * After init
	 */
	protected void postInit() {
	}
	
	protected void resize(){
		MenuPanel.getInstance().hide();
		setSize(Window.getClientWidth(), Window.getClientHeight());
		BkitPoma.resize();
	}

	/**
	 * Create CTracked and assign field into this CTracked
	 */
	protected void assignTracked() {
		submitTracked = new CTracked();
		submitTracked.setName(txtName.getText());
		submitTracked.setPassword(txtPassword.getText());
		submitTracked.setEmail(txtEmail.getText());
		submitTracked.setBirthday(dateBirthday.getValue());
		submitTracked.setTel(txtMobilePhone.getText().equals("") ? null
				: txtMobilePhone.getText());
		submitTracked
				.setIntervalGps(Integer.parseInt(cbIntervalGPS.getValue()));
		submitTracked.setActive(checkActive.getValue());
		submitTracked.setEmbedded(checkEmbedded.getValue());
		submitTracked.setShowInMap(checkShowInMap.getValue());
		submitTracked.setLang(cbLanguage.getValue());
		submitTracked.setCountry(cbCountry.getValue());
		
		submitTracked.setSchedule(scheduleWindow.getSubmitSchedule().toBytes());
		try {
			submitTracked.setGmt(new Integer(cbTimeZone.getValue()));
		} catch (Exception ex) {
			submitTracked.setGmt(7);
		}
		submitTracked.setApiKey(txtApiKey.getText());
		postAssignTracked();
	}

	/**
	 * After assignCTracker
	 */
	protected void postAssignTracked() {
	}

	/**
	 * When user press Submit
	 */
	private void submit() {
		assignTracked();
		BkitPoma.startLoading(constants.BkitPoma_startLoading_wating());
		postSubmit();
	}

	/**
	 * After submit
	 */
	protected void postSubmit() {
	}

	/**
	 * Reset form with ctracked at UserSettings
	 */
	public void resetForm() {
		final CTracked ctracked = UserSettings.ctracked == null ? new CTracked()
				: UserSettings.ctracked;

		txtPassword.setValue("");
		txtPassword.clearInvalid();

		txtPasswordConfirm.setValue("");
		txtPasswordConfirm.clearInvalid();

		txtName.setValue(ctracked.getName() == null ? "" : ctracked.getName());
		txtName.clearInvalid();

		if (ctracked.getBirthday() != null) {
			UserSettings.timerTask.getTaskList().add(new Task(2) {
				@Override
				public void execute() {
					dateBirthday.setValue(ctracked.getBirthday());
					finish();
				}
			});
		} else {
			dateBirthday.setValue("");
		}
		dateBirthday.clearInvalid();
		
		txtUsername.setValue(String.valueOf(ctracked.getUsername()));


		txtMobilePhone.setValue(ctracked.getTel() == null ? "" : ctracked
				.getTel());
		txtMobilePhone.clearInvalid();

		txtEmail.setValue(ctracked.getEmail() == null ? "" : ctracked
				.getEmail());
		txtEmail.clearInvalid();

		txtEmailConfirm.setValue(ctracked.getEmail() == null ? "" : ctracked
				.getEmail());
		txtEmailConfirm.clearInvalid();

		if (ctracked.getIntervalGps() != null) {
			UserSettings.timerTask.getTaskList().add(new Task(2) {
				@Override
				public void execute() {
					cbIntervalGPS.setValue(ctracked.getIntervalGps()+"");
					finish();
				}
			});
		} else {
			cbIntervalGPS.setValue("10");
		}
		cbIntervalGPS.clearInvalid();

		txtIconPath
				.setValue(ctracked.getIconPath() == null ? DEFFAULT_ICON_PATH
						: ctracked.getIconPath());

		imgIcon.setUrl(txtIconPath.getText());

		checkActive.setValue(ctracked.isActive() == null ? false : true);

		checkEmbedded.setValue(ctracked.getEmbedded() == null ? false : true);

		checkShowInMap.setValue(ctracked.getShowInMap() == null ? false : true);

		cbLanguage.setValue(ctracked.getLang() == null ? "vi" : ctracked
				.getLang());
		cbCountry.setValue(ctracked.getCountry() == null ? "VN" : ctracked
				.getCountry());
		cbTimeZone.setValue(ctracked.getGmt() == null ? "7" : ctracked.getGmt()
				+ "");

		txtApiKey.setValue(ctracked.getApiKey() == null ? "" : ctracked
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
		scheduleItemLayout();
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

		// require information
		fsRequireInfo.add(txtName);
		fsRequireInfo.add(txtPassword);
		fsRequireInfo.add(txtPasswordConfirm);
		fsRequireInfo.add(txtEmail);
		fsRequireInfo.add(txtEmailConfirm);
		
		fsRequireInfo.setCollapsible(true);

		FieldSet fsConfig = new FieldSet(constants.fsConfig());
		fsConfig.add(cbIntervalGPS);

		Panel panel = new Panel();
		panel.setLayout(new HorizontalLayout(5));
		Label lblImgURL = new Label(constants.lblImgURL());
		lblImgURL.setCls("gwt-Label");
		lblImgURL.setWidth(LABEL_WIDTH + 3);
		panel.add(lblImgURL);
		panel.add(txtIconPath);
		panel.add(imgIcon);
		panel.add(btnIconChooser);

		fsConfig.add(panel);

		this.add(fsRequireInfo);
		this.add(fsConfig);
	}

	/**
	 * Layout for fsOptionalInfo
	 */
	protected void optionalItemLayout() {
		fsOptionalInfo = new FieldSet(constants.fsOptionalInfo());
		fsOptionalInfo.setButtonAlign(Position.LEFT);

		// optional information
		fsOptionalInfo.add(dateBirthday);
		fsOptionalInfo.add(txtMobilePhone);
		fsOptionalInfo.add(cbLanguage);
		fsOptionalInfo.add(cbCountry);
		fsOptionalInfo.add(cbTimeZone);
		fsOptionalInfo.add(txtApiKey);
		fsOptionalInfo.addButton(btnGenerateApiKey);
		fsOptionalInfo.setCollapsible(true);

		this.add(fsOptionalInfo);
	}

	/**
	 * Layout for fsSchedule
	 */
	protected void scheduleItemLayout() {
		// Panel pnlSchedule = new Panel();
		// pnlSchedule.setBorder(false);
		// pnlSchedule.setLayout(new HorizontalLayout(10));

		// pnlSchedule.add(schedulePanel);

		fsScheduleWindow = new FieldSet(constants.fsScheduleWindow());
		fsScheduleWindow.setLayout(new FitLayout());
		fsScheduleWindow.setButtonAlign(Position.CENTER);
		fsScheduleWindow.setCollapsible(true);

		btnSchedule = new Button(constants.btnSchedule(), new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				scheduleWindow.show(new ScheduleCallback() {

					@Override
					public void onApplyOperation(Schedule schedule) {
						TrackedForm.this.scheduleWindow.setSchedule(schedule);
						
						for (byte b: schedule.toBytes()){
							
						}
						
						Object[][] data = getScheduleData();
						proxy = new MemoryProxy(data);
						store.setDataProxy(proxy);
						store.load();

					}

				});
			}
		});
		
		scheduleWindow = ScheduleWindow.getInstance();

		gridSchedule = new GridPanel();
		gridSchedule.setBorder(false);
		gridSchedule.setEnableHdMenu(false);
		gridSchedule.setHideColumnHeader(true);
		gridSchedule.setAutoScroll(true);
		gridSchedule.setButtonAlign(Position.CENTER);
		gridSchedule.setHeight(150);
		gridSchedule.setWidth("100%");

		RecordDef recordDef = new RecordDef(
				new FieldDef[] { new StringFieldDef("time") });

		Object[][] data = getScheduleData();
		proxy = new MemoryProxy(data);

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);

		ColumnConfig ccTime = new ColumnConfig(constants.ccTime(), "time", 160,
				false, new Renderer() {
					@Override
					public String render(Object value,
							CellMetadata cellMetadata, Record record,
							int rowIndex, int colNum, Store store) {
						if (rowIndex % 2 == 0) {
							cellMetadata.setCssClass("lightyellow");
						} else {
							cellMetadata.setCssClass("lightred");
						}
						return String.valueOf(value);
					}
				}, "time");
		ccTime.setResizable(false);

		ColumnConfig[] columns = new ColumnConfig[] { ccTime };

		ColumnModel columnModel = new ColumnModel(columns);
		columnModel.setColumnWidth("time", 160);

		gridSchedule.setColumnModel(columnModel);
		gridSchedule.setStore(store);
		store.load();

		fsScheduleWindow.add(gridSchedule);
		fsScheduleWindow.add(btnSchedule);

		this.add(fsScheduleWindow);
	}
	
	protected void refreshGridSchedule(){
		Object[][] data = getScheduleData();
		proxy = new MemoryProxy(data);
		store.setDataProxy(proxy);
		store.load();
	}

	/**
	 * Get Schedule to display in the grid panel
	 * @return
	 */
	protected Object[][] getScheduleData() {
		ScheduleItem[] arrScheduleItems = scheduleWindow.getSubmitSchedule().getScheduleItems();
		Object[][] arr = new Object[arrScheduleItems.length][];
		for (int i = 0; i < arrScheduleItems.length; i++) {
			arr[i] = new Object[] { arrScheduleItems[i].toString(true) };
			// 
		}
		return arr;
	}

}

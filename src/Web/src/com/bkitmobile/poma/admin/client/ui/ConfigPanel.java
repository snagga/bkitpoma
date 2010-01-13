package com.bkitmobile.poma.admin.client.ui;

import java.util.HashMap;

import com.bkitmobile.poma.admin.client.StaticMethod;
import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.DefaultConfig;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.home.client.WebsiteConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class ConfigPanel extends FormPanel {

	private final String DEFAULT_URL = "/images/poma/logo-header.gif";
	private final int TEXTFIELD_WIDTH = 350;
	private final int LABEL_WIDTH = 150;
	private final int COMBO_WIDTH = TEXTFIELD_WIDTH - 17;
	private Button btnEdit;
	private TextArea txtTOS;
	private FieldSet fsTOS;
	private TextField txtTitle;
	private TextField txtMailAdmin;
	private TextField txtURLLogo;
	private Checkbox checkBoxConfirmMail;
	private ComboBox cbLanguage;
	private FieldSet fsSetting;
	private Button btnSubmit;
	private Button btnReset;

	private Image imgLogo;

	private HashMap<String, String> hashMapChange;
	private HashMap<String, String> hashMapRecordDefault;

	private DatabaseServiceAsync dbAsync = GWT.create(DatabaseService.class);

	public ConfigPanel() {
		super();
		init();
		layout();

		loadDefaultRecord();
	}

	private void init() {
		// Setting for main panel
		this.setPaddings(5, 5, 10, 5);
		this.setLabelWidth(LABEL_WIDTH);
		this.setButtonAlign(Position.CENTER);
		this.setAutoScroll(true);
		this.setBorder(false);

		hashMapChange = new HashMap<String, String>();
		hashMapRecordDefault = new HashMap<String, String>();

		btnSubmit = new Button("Submit", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				if (!txtTitle.getText().equals(
						hashMapRecordDefault.get("title")))
					hashMapChange.put("title", txtTitle.getText());

				if (!cbLanguage.getValue().equals(
						hashMapRecordDefault.get("lang")))
					hashMapChange.put("lang", cbLanguage.getValue());

				if (!txtMailAdmin.getText().equals(
						hashMapRecordDefault.get("mail")))
					hashMapChange.put("mail", txtMailAdmin.getText());

				if (!checkBoxConfirmMail.getValueAsString().equals(
						hashMapRecordDefault.get("confirm")))
					hashMapChange.put("confirm", checkBoxConfirmMail
							.getValueAsString());

				if (!txtTOS.getText().equals(hashMapRecordDefault.get("tos")))
					hashMapChange.put("tos", txtTOS.getText());

				dbAsync.setAllRecords(hashMapChange,
						new AsyncCallback<ServiceResult<Boolean>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(ServiceResult<Boolean> result) {
								if (!result.isOK()) {
									MessageBox.alert(result.getMessage());
									hashMapChange.clear();
									loadData();
								} else {
									MessageBox.alert("Update Success");
									hashMapRecordDefault.putAll(hashMapChange);
									loadData();
								}
							}

						});
			}
		});

		btnReset = new Button("Reset", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				hashMapChange.clear();
				loadData();
			}
		});

		settingFieldSet();
		termOfServiceFieldSet();

	}

	private void termOfServiceFieldSet() {
		fsTOS = new FieldSet("Term of Services");
		fsTOS.setCheckboxToggle(true);

		txtTOS = new TextArea("Term of Services");
		txtTOS.setWidth(TEXTFIELD_WIDTH);
		txtTOS.setHeight(150);
		txtTOS.setReadOnly(true);

		btnEdit = new Button("Edit", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				btnEdit.setDisabled(true);
				txtTOS.setReadOnly(false);
			}
		});

		fsTOS.add(txtTOS);
		fsTOS.add(btnEdit);
	}

	private void settingFieldSet() {
		fsSetting = new FieldSet("Settings");

		txtTitle = new TextField("Title");
		txtTitle.setWidth(TEXTFIELD_WIDTH);

		cbLanguage = new ComboBox("Default Language");
		Store languageStore = new SimpleStore(new String[] { "locales",
				"languages" }, StaticMethod.LANGUAGE);
		languageStore.load();
		cbLanguage.setStore(languageStore);
		cbLanguage.setDisplayField("languages");
		cbLanguage.setValueField("locales");
		cbLanguage.setForceSelection(true);
		cbLanguage.setWidth(COMBO_WIDTH);

		txtMailAdmin = new TextField("Mail admin");
		txtMailAdmin.setWidth(TEXTFIELD_WIDTH);

		txtURLLogo = new TextField("Logo Header");
		txtURLLogo.setWidth(TEXTFIELD_WIDTH);
		txtURLLogo.setValue(DEFAULT_URL);
		txtURLLogo.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				super.onChange(field, newVal, oldVal);

			}
		});

		imgLogo = new Image(DEFAULT_URL);

		checkBoxConfirmMail = new Checkbox(
				"Send email for tracker user when registering");

		fsSetting.setLabelWidth(LABEL_WIDTH);
		fsSetting.add(txtTitle);
		fsSetting.add(cbLanguage);
		fsSetting.add(txtMailAdmin);
		fsSetting.add(txtURLLogo);

		// Panel pnlLogo = new Panel();
		// pnlLogo.setBorder(false);
		// pnlLogo.setWidth(LABEL_WIDTH + TEXTFIELD_WIDTH);
		// pnlLogo.setLayout(new BorderLayout());
		// pnlLogo.add(imgLogo,new BorderLayoutData(RegionPosition.CENTER));
		//		
		// fsSetting.add(pnlLogo);

		fsSetting.add(imgLogo);
		fsSetting.add(checkBoxConfirmMail);

		// fsSetting.add()
	}

	private void layout() {
		this.add(fsSetting);
		this.add(fsTOS);

		this.setButtonAlign(Position.CENTER);

		this.addButton(btnReset);
		this.addButton(btnSubmit);
	}

	private void loadDefaultRecord() {
		dbAsync
				.getAllRecords(new AsyncCallback<ServiceResult<HashMap<String, String>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<HashMap<String, String>> result) {
						if (result.isOK()) {
							hashMapRecordDefault = result.getResult();
							if (hashMapRecordDefault.size() == 0) {
								hashMapRecordDefault.putAll(DefaultConfig
										.getHashMapConfig());
								dbAsync
										.setAllRecords(
												hashMapRecordDefault,
												new AsyncCallback<ServiceResult<Boolean>>() {

													@Override
													public void onFailure(
															Throwable caught) {
														caught
																.printStackTrace();
													}

													@Override
													public void onSuccess(
															ServiceResult<Boolean> result) {
														if (!result.isOK()) {
															MessageBox
																	.alert(result
																			.getMessage());
														}
													}

												});
							}
							loadData();
						} else {
							MessageBox.alert(result.getMessage());
						}
					}
				});
	}

	private void loadData() {
		HashMap<String, String> hashMap = DefaultConfig.getHashMapConfig();
		String title = hashMapRecordDefault.get("title") == null ? hashMap
				.get("title") : hashMapRecordDefault.get("title");
		String lang = hashMapRecordDefault.get("lang") == null ? hashMap
				.get("lang") : hashMapRecordDefault.get("lang");
		String mail = hashMapRecordDefault.get("mail") == null ? hashMap
				.get("mail") : hashMapRecordDefault.get("mail");
		boolean confirm = hashMapRecordDefault.get("confirm") == null ? Boolean
				.parseBoolean(hashMap.get("confirm")) : Boolean
				.parseBoolean(hashMapRecordDefault.get("confirm"));
		String tos = hashMapRecordDefault.get("tos") == null ? hashMap
				.get("tos") : hashMapRecordDefault.get("tos");
		txtTitle.setValue(title);
		cbLanguage.setValue(lang);
		txtMailAdmin.setValue(mail);
		checkBoxConfirmMail.setChecked(confirm);
		txtTOS.setValue(tos);
	}

}

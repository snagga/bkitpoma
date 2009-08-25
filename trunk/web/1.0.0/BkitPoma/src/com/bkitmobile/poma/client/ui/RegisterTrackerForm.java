package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.*;
import com.bkitmobile.poma.client.localization.LRegisterFormWindow;
import com.bkitmobile.poma.client.mail.TrackerService;
import com.bkitmobile.poma.client.mail.TrackerServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class RegisterTrackerForm extends FormPanel {

	protected TextField txtName;
	protected TextField txtUsername;
	protected TextField txtEmail;
	protected TextField txtEmailConfirm;
	protected TextField txtPassword;
	protected TextField txtPasswordConfirm;
	protected TextField txtMobilePhone;
	
	protected DateField dateBirthday;
	
	protected TextArea taAddress;
	
	protected ComboBox cbCountry;
	protected ComboBox cbTimeZone;
	
	protected LRegisterFormWindow local = GWT.create(LRegisterFormWindow.class);
	protected DatabaseServiceAsync dbService = DatabaseService.Util.getInstance();
	protected TrackerServiceAsync trackerService = null;//TrackerService.Util.getInstance();
	
	int selectedIndexTimeZone;
	int selectedIndexCountry;
	
	protected FieldSet fsRequireInfo;
	protected FieldSet fsOptionalInfo;
	
	protected Button btnSubmit;
	protected Button btnReturn;
	
	protected int labelWidth = 120;
	protected int comboWidth = 250;
	protected int textareaWidth = 300;
	protected int textFieldWidth = 150;
	protected int formWidth = 500;
	
	
	public RegisterTrackerForm() {
		init();
		layout();
	}
	
	private void init() {
		
		/*
		 * Username
		 */
		txtUsername = new TextField(local.lbl_username(), "username");
		txtUsername.setRegex("^[a-zA-Z0-9_]{3,32}$");
		txtUsername.setAllowBlank(false);
		txtUsername.setInvalidText(local.lbl_invalid_username());
		txtUsername.setWidth(textFieldWidth);

		txtUsername.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				if (!txtUsername.isValid()) {
					return;
				}
				dbService.getTracker(txtUsername.getText(), new AsyncCallback<ServiceResult<CTracker>>(){

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						MessageBox.alert("Verify tracker error", caught.getMessage());
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result.getResult()==null) 
							txtUsername.markInvalid(local.lbl_duplicate_username());
					}
					
				});
			}
		});

		
		/*
		 * Email
		 */
		txtEmail = new TextField(local.lbl_email(), "email");
		txtEmail.setVtype(VType.EMAIL);
		txtEmail.setWidth(textFieldWidth);
		// TODO verify tracker's email
		

		/*
		 * Confirm email
		 */
		txtEmailConfirm = new TextField(local.lbl_confirm_email(), "email_confirm");
		txtEmail.setWidth(textFieldWidth);
		txtEmailConfirm.setVtype(VType.EMAIL);
		

		/*
		 * Password
		 */
		txtPassword = new TextField(local.lbl_password(), "password");
		txtPassword.setWidth(textFieldWidth);
		txtPassword.setPassword(true);
		

		/*
		 * Confirm password
		 */
		txtPasswordConfirm = new TextField(local.lbl_confirm_password(),
				"password_confirm");
		txtPassword.setWidth(textFieldWidth);
		txtPasswordConfirm.setPassword(true);
		

		/*
		 * Name of tracker
		 */
		txtName = new TextField(local.lbl_display_name(), "name");
		txtName.setAllowBlank(false);
		txtName.setWidth(textFieldWidth);
		
		
		/*
		 * Birthday
		 */
		dateBirthday = new DateField(local.lbl_birth_day(), "d-M-Y");
		dateBirthday.setReadOnly(true);
//		dateBirthday.setValue(new Date());
//		dateBirthday.setWidth("50%");
		dateBirthday.setWidth(textFieldWidth);
		
		dateBirthday.addListener(new DatePickerListenerAdapter() {
			@Override
			public void onSelect(DatePicker dataPicker, Date date) {
				if (date.compareTo(new Date()) >= 0) {
					dateBirthday.markInvalid("You must enter a smaller day than to day");
					MessageBox.alert("Enter a smaller day than to day");
				}
			}
		});
		
		
		/*
		 * Mobile phone
		 */
		txtMobilePhone = new TextField(local.lbl_mobile_phone(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		txtMobilePhone.setWidth(textFieldWidth);
		ToolTip mobileTip = new ToolTip(local.lbl_invalid_mobile_phone());
		mobileTip.applyTo(txtMobilePhone);
		
		
		/*
		 * Address of tracker
		 */
		taAddress = new TextArea(local.lbl_address(), "address");
		taAddress.setWidth(textareaWidth);

		
		/*
		 *  country names
		 */
		cbCountry = new ComboBox(local.lbl_country());
		Store countryStore = new SimpleStore("country_names", getCountryNames());
		countryStore.load();
		cbCountry.setStore(countryStore);
		cbCountry.setDisplayField("country_names");
		cbCountry.setReadOnly(true);
		cbCountry.setWidth(comboWidth);
		cbCountry.setListWidth(comboWidth);
		
		cbCountry.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				selectedIndexCountry = index;
			}
		});

		
		/*
		 *  time zone
		 */
		cbTimeZone = new ComboBox(local.lbl_time_zone());
		Store timeZoneStore = new SimpleStore(
				new String[] { "timezone", "id" }, getTimeZone());
		timeZoneStore.load();
		cbTimeZone.setStore(timeZoneStore);
		cbTimeZone.setDisplayField("timezone");
		cbTimeZone.setReadOnly(true);
		cbTimeZone.setWidth(comboWidth);
		cbTimeZone.setListWidth(comboWidth);
		
		cbTimeZone.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				selectedIndexTimeZone = index;
			}
		});
		
		
		/*
		 * Submit button
		 */
		btnSubmit = new Button(local.button_submit(),
				new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {

				if (!isValidForm()) {
					MessageBox.alert("Some fields are invalid!!!");
					return;
				}

				/*
				 * Check captcha match or not
				 */
				CaptchaWindow.getInstance().show();
				CaptchaWindow.getInstance().addListener("validate", new Function() {

					@Override
					public void execute() {
						if (CaptchaWindow.getInstance().getValue() == true) {
							
							CTracker cTracker = new CTracker(
									txtUsername.getText(),
									txtPassword.getText(),
									txtName.getText(),dateBirthday.getValue(),txtMobilePhone.getText(),taAddress.getText(),txtEmail.getText(),
									0,selectedIndexTimeZone,"vi",cbCountry.getText(),true,false
								);
							dbService.insertTracker(cTracker,
									new AsyncCallback<ServiceResult<CTracker>>() {

										@Override
										public void onFailure(Throwable caught) {
											MessageBox.alert("Insert tracker error", caught.toString());
											caught.printStackTrace();
										}

										@Override
										public void onSuccess(ServiceResult<CTracker> result) {
											System.out.println(result.getMessage());
											if (result.getResult() != null){
												MessageBox.alert("Insert tracker successfully");
											}else{
												MessageBox.alert("Insert tracker error", "FAILURE");
											}
										}

									}
							);
							
							trackerService.validateEmailNewTracker(cTracker, new AsyncCallback<Boolean>(){

								@Override
								public void onFailure(Throwable caught) {
									MessageBox.alert("onFailure: " +caught.getMessage());
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result){
										MessageBox.alert("Send email successfully");
									}else{
										MessageBox.alert("Send email fail");
									}
									
								}
								
							});
						} else {
							MessageBox.alert("Captcha not validated");
						}
						
					}
					
				});
			}
		});

		/*
		 * Button return to map
		 */
		btnReturn = new Button("Return to map",new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				BkitPoma.returnToMap();
			}
			
		});
		
	}
	
	private void layout() {
		
		/*
		 * Setting for main panel
		 */
		this.setTitle("Register a new tracker");
		this.setFrame(true);
		this.setPaddings(5, 5, 10, 5);
		this.setLabelWidth(labelWidth);
		this.setButtonAlign(Position.CENTER);
		this.setAutoScroll(true);
		this.setWidth(formWidth);
		
		/*
		 * Require information 
		 */
		fsRequireInfo = new FieldSet(local.lbl_require_information());
//		fsRequireInfo.setFrame(false);
//		fsRequireInfo.setAutoScroll(true);
//		requireInfo.setCollapsible(true);
		
		fsRequireInfo.add(txtUsername);
		fsRequireInfo.add(txtEmail);
		fsRequireInfo.add(txtEmailConfirm);
		fsRequireInfo.add(txtPassword);
		fsRequireInfo.add(txtPasswordConfirm);
		
		
		/*
		 * Optional information
		 */
		fsOptionalInfo = new FieldSet(local.lbl_optional_information());
//		fsOptionalInfo.setAutoScroll(true);
		fsOptionalInfo.setCheckboxToggle(true);
		fsOptionalInfo.setCollapsed(true);
//		fsOptionalInfo.setAutoHeight(true);
//		fsOptionalInfo.setAutoWidth(true);
		
		fsOptionalInfo.add(txtName);
		fsOptionalInfo.add(dateBirthday);
		fsOptionalInfo.add(txtMobilePhone);
		fsOptionalInfo.add(taAddress);
		fsOptionalInfo.add(cbCountry);
		fsOptionalInfo.add(cbTimeZone);
		
		
		/*
		 * Add to main form
		 */
		this.add(fsRequireInfo);
		this.add(fsOptionalInfo);
		this.addButton(btnSubmit);
		this.addButton(btnReturn);
		
	}

	protected boolean isValidForm() {
		if (!txtUsername.isValid())
			return false;
		if (txtUsername.getText().equals("")) {
			txtUsername.markInvalid(local.err_username_null());
			return false;
		}
		if (!txtEmail.isValid())
			return false;
		if (txtEmail.getText().equals("")) {
			txtEmail.markInvalid(local.err_email_null());
			return false;
		}
		if (!txtEmailConfirm.isValid())
			return false;
		if (txtEmailConfirm.getText().equals("")) {
			txtEmailConfirm.markInvalid(local.err_email_confirm_null());
			return false;
		}
		if (!txtPassword.isValid())
			return false;
		if (txtPassword.getText().equals("")) {
			txtPassword.markInvalid(local.err_password_null());
			return false;
		}

		if (!txtPasswordConfirm.isValid())
			return false;
		if (txtPasswordConfirm.getText().equals("")) {
			txtPasswordConfirm.markInvalid(local.err_password_confirm_null());
			return false;
		}

		/*
		 * Check email and email confirm whether match or not
		 */
		if (!txtEmailConfirm.getText().equals(txtEmail.getText())) {
			txtEmailConfirm.markInvalid(local.lbl_invalid_email());
			return false;
		}

		/*
		 * Check password and password confirm whether match or not
		 */
		if (!txtPasswordConfirm.getText().equals(txtPassword.getText())) {
			txtPasswordConfirm
					.markInvalid(local.lbl_invalid_confirm_password());
			return false;
		}

		if (!txtMobilePhone.isValid())
			return false;
		
		if (!dateBirthday.isValid()) 
			return false;
		
		return true;
	}

	private static String[] getCountryNames() {
		return new String[] { "AF|Afghanistan", "AL|Albania", "DZ|Algeria",
				"AS|American Samoa", "AD|Andorra", "AO|Angola", "AI|Anguilla",
				"AQ|Antarctica", "AG|Antigua And Barbuda", "AR|Argentina",
				"AM|Armenia", "AW|Aruba", "AU|Australia", "AT|Austria",
				"AZ|Azerbaijan", "BS|Bahamas", "BH|Bahrain", "BD|Bangladesh",
				"BB|Barbados", "BY|Belarus", "BE|Belgium", "BZ|Belize",
				"BJ|Benin", "BM|Bermuda", "BT|Bhutan", "BO|Bolivia",
				"BA|Bosnia And Herzegovina", "BW|Botswana", "BV|Bouvet Island",
				"BR|Brazil", "IO|British Indian Ocean Territory",
				"BN|Brunei Darussalam", "BG|Bulgaria", "BF|Burkina Faso",
				"BI|Burundi", "KH|Cambodia", "CM|Cameroon", "CA|Canada",
				"CV|Cape Verde", "KY|Cayman Islands",
				"CF|Central African Republic", "TD|Chad", "CL|Chile",
				"CN|China", "CX|Christmas Island",
				"CC|Cocos (keeling) Islands", "CO|Colombia", "KM|Comoros",
				"CG|Congo", "CD|Congo, The Democratic Republic Of The",
				"CK|Cook Islands", "CR|Costa Rica", "CI|Cote D'ivoire",
				"HR|Croatia", "CU|Cuba", "CY|Cyprus", "CZ|Czech Republic",
				"DK|Denmark", "DJ|Djibouti", "DM|Dominica",
				"DO|Dominican Republic", "TP|East Timor", "EC|Ecuador",
				"EG|Egypt", "SV|El Salvador", "GQ|Equatorial Guinea",
				"ER|Eritrea", "EE|Estonia", "ET|Ethiopia",
				"FK|Falkland Islands (malvinas)", "FO|Faroe Islands",
				"FJ|Fiji", "FI|Finland", "FR|France", "GF|French Guiana",
				"PF|French Polynesia", "TF|French Southern Territories",
				"GA|Gabon", "GM|Gambia", "GE|Georgia", "DE|Germany",
				"GH|Ghana", "GI|Gibraltar", "GR|Greece", "GL|Greenland",
				"GD|Grenada", "GP|Guadeloupe", "GU|Guam", "GT|Guatemala",
				"GN|Guinea", "GW|Guinea-bissau", "GY|Guyana", "HT|Haiti",
				"HM|Heard Island And Mcdonald Islands",
				"VA|Holy See (vatican City State)", "HN|Honduras",
				"HK|Hong Kong", "HU|Hungary", "IS|Iceland", "IN|India",
				"ID|Indonesia", "IR|Iran, Islamic Republic Of", "IQ|Iraq",
				"IE|Ireland", "IL|Israel", "IT|Italy", "JM|Jamaica",
				"JP|Japan", "JO|Jordan", "KZ|Kazakstan", "KE|Kenya",
				"KI|Kiribati", "KP|Korea, Democratic People's Republic Of",
				"KR|Korea, Republic Of", "KV|Kosovo", "KW|Kuwait",
				"KG|Kyrgyzstan", "LA|Lao People's Democratic Republic",
				"LV|Latvia", "LB|Lebanon", "LS|Lesotho", "LR|Liberia",
				"LY|Libyan Arab Jamahiriya", "LI|Liechtenstein",
				"LT|Lithuania", "LU|Luxembourg", "MO|Macau",
				"MK|Macedonia, The Former Yugoslav Republic Of",
				"MG|Madagascar", "MW|Malawi", "MY|Malaysia", "MV|Maldives",
				"ML|Mali", "MT|Malta", "MH|Marshall Islands", "MQ|Martinique",
				"MR|Mauritania", "MU|Mauritius", "YT|Mayotte", "MX|Mexico",
				"FM|Micronesia, Federated States Of",
				"MD|Moldova, Republic Of", "MC|Monaco", "MN|Mongolia",
				"MS|Montserrat", "ME|Montenegro", "MA|Morocco",
				"MZ|Mozambique", "MM|Myanmar", "NA|Namibia", "NR|Nauru",
				"NP|Nepal", "NL|Netherlands", "AN|Netherlands Antilles",
				"NC|New Caledonia", "NZ|New Zealand", "NI|Nicaragua",
				"NE|Niger", "NG|Nigeria", "NU|Niue", "NF|Norfolk Island",
				"MP|Northern Mariana Islands", "NO|Norway", "OM|Oman",
				"PK|Pakistan", "PW|Palau",
				"PS|Palestinian Territory, Occupied", "PA|Panama",
				"PG|Papua New Guinea", "PY|Paraguay", "PE|Peru",
				"PH|Philippines", "PN|Pitcairn", "PL|Poland", "PT|Portugal",
				"PR|Puerto Rico", "QA|Qatar", "RE|Reunion", "RO|Romania",
				"RU|Russian Federation", "RW|Rwanda", "SH|Saint Helena",
				"KN|Saint Kitts And Nevis", "LC|Saint Lucia",
				"PM|Saint Pierre And Miquelon",
				"VC|Saint Vincent And The Grenadines", "WS|Samoa",
				"SM|San Marino", "ST|Sao Tome And Principe", "SA|Saudi Arabia",
				"SN|Senegal", "RS|Serbia", "SC|Seychelles", "SL|Sierra Leone",
				"SG|Singapore", "SK|Slovakia", "SI|Slovenia",
				"SB|Solomon Islands", "SO|Somalia", "ZA|South Africa",
				"GS|South Georgia And The South Sandwich Islands", "ES|Spain",
				"LK|Sri Lanka", "SD|Sudan", "SR|Suriname",
				"SJ|Svalbard And Jan Mayen", "SZ|Swaziland", "SE|Sweden",
				"CH|Switzerland", "SY|Syrian Arab Republic",
				"TW|Taiwan, Province Of China", "TJ|Tajikistan",
				"TZ|Tanzania, United Republic Of", "TH|Thailand", "TG|Togo",
				"TK|Tokelau", "TO|Tonga", "TT|Trinidad And Tobago",
				"TN|Tunisia", "TR|Turkey", "TM|Turkmenistan",
				"TC|Turks And Caicos Islands", "TV|Tuvalu", "UG|Uganda",
				"UA|Ukraine", "AE|United Arab Emirates", "GB|United Kingdom",
				"US|United States", "UM|United States Minor Outlying Islands",
				"UY|Uruguay", "UZ|Uzbekistan", "VU|Vanuatu", "VE|Venezuela",
				"VN|Viet Nam", "VG|Virgin Islands, British",
				"VI|Virgin Islands, U.s.", "WF|Wallis And Futuna",
				"EH|Western Sahara", "YE|Yemen", "ZM|Zambia", "ZW|Zimbabwe" };
	}

	protected static String[][] getTimeZone() {
		return new String[][] {
				{ "(UTC-11 01:00)Apia, Pago Pago", "-11" },
				{ "(UTC-10 02:00)Honolulu, Papeete", "-10" },
				{ "(UTC-9 03:00)Anchorage, Juneau", "-9" },
				{
						"(UTC-8 04:00)Los Angeles, San Francisco, Las Vegas, Vancouver",
						"-8" },
				{ "(UTC-7 05:00)Calgary, Denver", "-7" },
				{ "(UTC-6 06:00)Chicago, Mexico City", "-6" },
				{
						"(UTC-5 07:00)Toronto, New York City, Havana, Bogot\u0102\u0192\u00C2\u00A1, Lima",
						"-5" },
				{
						"(UTC-4 08:00)Asunci\u0102\u0192\u00C2\u00B3n, Halifax, Santiago",
						"-4" },
				{
						"(UTC-3 09:00)Buenos Aires, Montevideo, Rio de Janeiro, S\u0102\u0192\u00C2\u00A3o Paulo",
						"-3" },
				{
						"(UTC-2 10:00)Fernando de Noronha, South Georgia and the South Sandwich Islands",
						"-2" },
				{ "(UTC-1 11:00)Azores, Cape Verde", "-1" },
				{
						"(UTC+0 12:00)Dakar, Casablanca, London, Lisbon, Reykjav\u0102\u0192\u00C2\u00ADk, Tenerife",
						"0" },
				{
						"(UTC+1 13:00)Algiers, Berlin, Kinshasa, Lagos, Paris, Rome, Tunis",
						"1" },
				{
						"(UTC+2 14:00)Istanbul,Athens ,Cairo, Cape Town, Helsinki, Jerusalem",
						"2" },
				{
						"(UTC+3 15:00)Addis Ababa, Baghdad, Moscow, Nairobi, Saint Petersburg",
						"3" },
				{ "(UTC+4 16:00)Baku, Dubai, Mauritius, Samara, Tbilisi ", "4" },
				{ "(UTC+5 17:00)Karachi, Maldives, Tashkent, Yekaterinburg",
						"5" },
				{ "(UTC+6 18:00)Almaty, Dhaka, Omsk 	", "6" },
				{ "(UTC+7 19:00)Bangkok, Jakarta, Hanoi, Krasnoyarsk", "7" },
				{
						"(UTC+8 20:00)Beijing, Hong Kong, Irkutsk, Kuala Lumpur, Manila, Perth",
						"8" },
				{ "(UTC+9 21:00)Pyongyang, Seoul, Tokyo, Yakutsk", "9" },
				{ "(UTC+10 22:00)Melbourne, Sydney, Vladivostok", "10" },
				{ "(UTC+11 23:00)Magadan, Noum\u0102\u0192\u00C2\u00A9a", "11" },
				{
						"(UTC+12 00:00 (the following day))Auckland, Petropavlovsk-Kamchatsky, Suva",
						"12" },
				{
						"(UTC+13 01:00 (the following day))Nuku\u0102\u00C2\u00BBalofa",
						"13" } };
	}
}

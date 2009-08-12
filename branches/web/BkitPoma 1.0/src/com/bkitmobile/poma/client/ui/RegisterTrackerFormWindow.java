package com.bkitmobile.poma.client.ui;

import java.util.Date;

import com.bkitmobile.poma.client.captcha.RecaptchaService;
import com.bkitmobile.poma.client.captcha.RecaptchaServiceAsync;
import com.bkitmobile.poma.client.captcha.RecaptchaWidget;
import com.bkitmobile.poma.client.captcha.RecaptchaService.Util;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.Tracker;
import com.bkitmobile.poma.client.localization.LRegisterForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class RegisterTrackerFormWindow extends Window {

	private TextField txtName;
	private TextField txtUsername;
	private TextField txtEmail;
	private TextField txtEmailConfirm;
	private TextField txtPassword;
	private TextField txtPasswordConfirm;
	private DateField dateBirthday;
	private TextField txtMobilePhone;
	private TextArea taAddress;
	private RecaptchaWidget rw;
	private ComboBox cbCountry;
	private ComboBox cbTimeZone;
	private LRegisterForm local;
	boolean ok = true;
	int selectedIndexTimeZone;
	int selectedIndexCountry;

	private FormPanel formPanel = null;
	/*
	 * Create instance of Database to manipulate
	 */
	private DatabaseServiceAsync dbService;

	public RegisterTrackerFormWindow() {
		local = GWT.create(LRegisterForm.class);
		dbService = DatabaseService.Util.getInstance();
		formPanel = new FormPanel();

		formPanel.setPaddings(5, 5, 5, 0);
		formPanel.setLabelWidth(100);
		formPanel.setSize(500, 600);
		formPanel.setAutoScroll(true);

		FieldSet requireInfo = new FieldSet(local.lbl_require_information());
		requireInfo.setCollapsible(true);

		/**
		 * Username
		 */
		txtUsername = new TextField(local.lbl_username(), "username");
		txtUsername.setRegex("^[a-zA-Z0-9_]{3,32}$");
		txtUsername.setAllowBlank(false);
		txtUsername.setInvalidText(local.lbl_invalid_username());
		requireInfo.add(txtUsername);

		txtUsername.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onChange(Field field, Object newVal, Object oldVal) {
				// TODO Auto-generated method stub
				if (!txtUsername.isValid()) {
					return;
				}
				super.onChange(field, newVal, oldVal);
				dbService.verifyTracker(txtUsername.getText(),
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(Boolean result) {
								// TODO Auto-generated method stub
								System.out.println(result);
								if (result) {
								} else {
									txtUsername.markInvalid(local
											.lbl_duplicate_username());
								}
							}

						});

			}
		});

		/**
		 * Email
		 */
		txtEmail = new TextField(local.lbl_email(), "email");
		txtEmail.setVtype(VType.EMAIL);
		requireInfo.add(txtEmail);

		/**
		 * Confirm email
		 */
		txtEmailConfirm = new TextField(local.lbl_confirm_email(),
				"email_confirm");
		txtEmailConfirm.setVtype(VType.EMAIL);
		requireInfo.add(txtEmailConfirm);

		/**
		 * Password
		 */
		txtPassword = new TextField(local.lbl_password(), "password");
		txtPassword.setPassword(true);
		requireInfo.add(txtPassword);

		/**
		 * Confirm password
		 */
		txtPasswordConfirm = new TextField(local.lbl_confirm_password(),
				"password_confirm");
		txtPasswordConfirm.setPassword(true);
		requireInfo.add(txtPasswordConfirm);

		/**
		 * Captcha
		 */
		rw = new RecaptchaWidget("6LdakQcAAAAAALX2JUFtsjbPTV0TcAkMhQY8iMkS");
		requireInfo.add(rw);

		/**
		 * Optional field
		 */
		FieldSet optionalInfo = new FieldSet(local.lbl_optional_information());
		optionalInfo.setCheckboxToggle(true);
		optionalInfo.setCollapsed(true);
		optionalInfo.setAutoHeight(true);
		optionalInfo.setAutoWidth(true);

		txtName = new TextField(local.lbl_display_name(), "name");
		txtName.setAllowBlank(false);
		dateBirthday = new DateField(local.lbl_birth_day(), "d-M-Y");
		dateBirthday.setReadOnly(true);
		dateBirthday.setValue(new Date());
		txtMobilePhone = new TextField(local.lbl_mobile_phone(), "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		ToolTip mobileTip = new ToolTip(local.lbl_invalid_mobile_phone());
		mobileTip.applyTo(txtMobilePhone);
		taAddress = new TextArea(local.lbl_address(), "address");
		taAddress.setWidth(350);
		// country names
		cbCountry = new ComboBox(local.lbl_country());
		Store countryStore = new SimpleStore("country_names", getCountryNames());
		countryStore.load();
		cbCountry.setStore(countryStore);
		cbCountry.setDisplayField("country_names");
		cbCountry.setReadOnly(true);
		// time zone
		cbTimeZone = new ComboBox(local.lbl_time_zone());
		Store timeZoneStore = new SimpleStore(
				new String[] { "timezone", "id" }, getTimeZone());
		timeZoneStore.load();
		cbTimeZone.setStore(timeZoneStore);
		cbTimeZone.setDisplayField("timezone");
		cbTimeZone.setReadOnly(true);
		// add to optional form
		optionalInfo.add(txtName);
		optionalInfo.add(dateBirthday);
		optionalInfo.add(txtMobilePhone);
		optionalInfo.add(taAddress);
		optionalInfo.add(cbCountry);
		optionalInfo.add(cbTimeZone);

		/**
		 * Add to main form
		 */
		formPanel.add(requireInfo);
		formPanel.add(optionalInfo);

		cbTimeZone.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				// TODO Auto-generated method stub
				super.onSelect(comboBox, record, index);
				selectedIndexTimeZone = index;
			}
		});

		cbCountry.addListener(new ComboBoxListenerAdapter() {
			@Override
			public void onSelect(ComboBox comboBox, Record record, int index) {
				// TODO Auto-generated method stub
				super.onSelect(comboBox, record, index);
				selectedIndexCountry = index;
			}
		});

		/*
		 * Handle Submit button
		 */
		formPanel.add(new Button(local.button_submit(),
				new ButtonListenerAdapter() {
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
						rrsa.verifyChallenge(rw.getChallenge(), rw
								.getResponse(), new AsyncCallback<Boolean>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
								MessageBox.alert("Wrong captcha message...");
							}

							public void onSuccess(Boolean result) {
								if (result) {
									// (String username,String password,String
									// name,String
									// birthday,String tel,String addr,String
									// email, String
									// typeCus,String state,String gmt,String
									// lang,String
									// country){//
									Date date = dateBirthday.getValue();
									String strDate = String.valueOf(date
											.getYear())
											+ date.getMonth() + date.getDay();
									dbService
											.insertTracker(
													new Tracker(
															txtUsername
																	.getText(),
															txtPassword
																	.getText(),
															txtName.getText(),
															strDate,
															txtMobilePhone
																	.getText(),
															taAddress.getText(),
															txtEmail.getText(),
															getTimeZone()[selectedIndexTimeZone][1],
															"VI",
															getCountryNames()[selectedIndexCountry]),
													new AsyncCallback<Integer>() {

														@Override
														public void onFailure(
																Throwable caught) {
															caught
																	.printStackTrace();
															MessageBox
																	.alert("Cannot insert account to database");
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
									MessageBox.alert("Insert success");
								} else {
									MessageBox.alert("fail");
								}
							}

						});
					}
				}));

		formPanel.add(new Button("Insert field", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				super.onClick(button, e);
				test();
			}
		}));

		this.add(formPanel);
	}

	private void test() {
		txtUsername.setValue("vomintah");
		txtEmail.setValue("abc@gmail.com");
		txtEmailConfirm.setValue("abc@gmail.com");
		txtPassword.setValue("123456");
		txtPasswordConfirm.setValue("123456");
	}

	private boolean isValidForm() {
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

	private static String[][] getTimeZone() {
		return new String[][] {
				{ "Apia, Pago Pago 	UTC-11 	01:00", "-11" },
				{ "Honolulu, Papeete 	UTC-10 	02:00", "-10" },
				{ "Anchorage, Juneau 	UTC-9 	03:00", "-9" },
				{
						"Los Angeles, San Francisco, Las Vegas, Vancouver 	UTC-8 	04:00",
						"-8" },
				{ "Calgary, Denver 	UTC-7 	05:00", "-7" },
				{ "Chicago, Mexico City 	UTC-6 	06:00", "-6" },
				{
						"Toronto, New York City, Havana, Bogot\u0102\u0192\u00C2\u00A1, Lima 	UTC-5 	07:00",
						"-5" },
				{
						"Asunci\u0102\u0192\u00C2\u00B3n, Halifax, Santiago 	UTC-4 	08:00",
						"-4" },
				{
						"Buenos Aires, Montevideo, Rio de Janeiro, S\u0102\u0192\u00C2\u00A3o Paulo 	UTC-3 	09:00",
						"-3" },
				{
						"Fernando de Noronha, South Georgia and the South Sandwich Islands 	UTC-2 	10:00",
						"-2" },
				{ "Azores, Cape Verde 	UTC-1 	11:00", "-1" },
				{
						"Dakar, Casablanca, London, Lisbon, Reykjav\u0102\u0192\u00C2\u00ADk, Tenerife 	UTC (UTC\u0102\u201A\u00C2\u00B10) 	12:00",
						"0" },
				{
						"Algiers, Berlin, Kinshasa, Lagos, Paris, Rome, Tunis 	UTC+1 	13:00",
						"1" },
				{
						"Istanbul,Athens ,Cairo, Cape Town, Helsinki, Jerusalem 	UTC+2 	14:00",
						"2" },
				{
						"Addis Ababa, Baghdad, Moscow, Nairobi, Saint Petersburg 	UTC+3 	15:00",
						"3" },
				{ "Baku, Dubai, Mauritius, Samara, Tbilisi 	UTC+4 	16:00", "4" },
				{ "Karachi, Maldives, Tashkent, Yekaterinburg 	UTC+5 	17:00",
						"5" },
				{ "Almaty, Dhaka, Omsk 	UTC+6 	18:00", "6" },
				{ "Bangkok, Jakarta, Hanoi, Krasnoyarsk 	UTC+7 	19:00", "7" },
				{
						"Beijing, Hong Kong, Irkutsk, Kuala Lumpur, Manila, Perth 	UTC+8 	20:00",
						"8" },
				{ "Pyongyang, Seoul, Tokyo, Yakutsk 	UTC+9 	21:00", "9" },
				{ "Melbourne, Sydney, Vladivostok 	UTC+10 	22:00", "10" },
				{ "Magadan, Noum\u0102\u0192\u00C2\u00A9a 	UTC+11 	23:00", "11" },
				{
						"Auckland, Petropavlovsk-Kamchatsky, Suva 	UTC+12 	00:00 (the following day)",
						"12" },
				{
						"Nuku\u0102\u00C2\u00BBalofa 	UTC+13 	01:00 (the following day)",
						"13" } };
	}

}
package com.poma.bkitpoma.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

public class RegisterForm extends FormPanel {

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

	public RegisterForm() {

		this.setPaddings(5, 5, 5, 0);
		this.setLabelWidth(100);
		this.setSize(500, 600);
		this.setAutoScroll(true);

		FieldSet requireInfo = new FieldSet("Require information");
		requireInfo.setCollapsible(true);

		/**
		 * Username
		 */
		txtUsername = new TextField("Username (*)", "username");
		txtUsername.setRegex("^[a-zA-Z0-9_]{3,50}$");
		txtUsername.setInvalidText("Kh\u00F4ng h\u1EE3p l\u1EC7");
		requireInfo.add(txtUsername);

		/**
		 * Email
		 */
		txtEmail = new TextField("Email", "email");
		txtEmail.setVtype(VType.EMAIL);
		requireInfo.add(txtEmail);

		/**
		 * Confirm email
		 */
		txtEmailConfirm = new TextField("Confirm email", "email_confirm");
		txtEmailConfirm.setVtype(VType.EMAIL);
		requireInfo.add(txtEmailConfirm);

		/**
		 * Password
		 */
		txtPassword = new TextField("Password", "password");
		txtPassword.setPassword(true);
		requireInfo.add(txtPassword);

		/**
		 * Confirm password
		 */
		txtPasswordConfirm = new TextField("Confirm Password",
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
		FieldSet optionalInfo = new FieldSet("Optional information");
		optionalInfo.setCheckboxToggle(true);
		optionalInfo.setCollapsed(true);
		optionalInfo.setAutoHeight(true);
		optionalInfo.setAutoWidth(true);

		txtName = new TextField("Display name", "name");
		txtName.setAllowBlank(false);
		dateBirthday = new DateField("Birth day", "d-M-Y");
		dateBirthday.setReadOnly(true);
		txtMobilePhone = new TextField("Mobile phone", "phone");
		txtMobilePhone.setRegex("^[0-9]{8,11}$");
		ToolTip mobileTip = new ToolTip("Phone number must have 8 - 11 digits");
		mobileTip.applyTo(txtMobilePhone);
		taAddress = new TextArea("Address", "address");
		taAddress.setWidth(350);
		// country names
		cbCountry = new ComboBox("Country");
		Store countryStore = new SimpleStore(new String[] { "country_names" },
				getCountryNames());
		countryStore.load();
		cbCountry.setStore(countryStore);
		cbCountry.setDisplayField("country_names");
		cbCountry.setReadOnly(true);
		// time zone
		cbTimeZone = new ComboBox("Time zone");
		Store timeZoneStore = new SimpleStore(new String[] { "timezone" },
				getTimeZone());
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
		this.add(requireInfo);
		this.add(optionalInfo);

		/*
		 * Create instance of Database to manipulate
		 */
		final DatabaseServiceAsync dbService = DatabaseService.Util
				.getInstance();

		/*
		 * Handle Submit button
		 */
		this.add(new Button("Submit", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO Auto-generated method stub
				/*
				 * Check email and email confirm whether match or not
				 */
				if (txtEmailConfirm.getText().equals(txtEmail.getText())) {
					txtEmailConfirm.markInvalid("Not match!");
				}

				/*
				 * Check password and password confirm whether match or not
				 */
				if (txtPasswordConfirm.getText().equals(txtPassword.getText())) {
					txtPasswordConfirm.markInvalid("Not match!");
				}

				/*
				 * Check captcha match or not
				 */
				RecaptchaServiceAsync rrsa = RecaptchaService.Util
						.getInstance();
				rrsa.verifyChallenge(rw.getChallenge(), rw.getResponse(),
						new AsyncCallback<Boolean>() {

							public void onFailure(Throwable caught) {
								MessageBox.alert("ERROR");
							}

							public void onSuccess(Boolean result) {
								MessageBox.alert(result.toString());
							}

						});

				/*
				 * Verify username of tracker whether duplicate or not
				 */
				dbService.verifyTracker(txtUsername.getText(),
						new AsyncCallback<Boolean>() {

							public void onFailure(Throwable caught) {
								MessageBox.alert("ERROR");
							}

							public void onSuccess(Boolean result) {
								MessageBox.alert(result.toString());
							}

						});

			}
		}));
	}

	private static String[][] getCountryNames() {
		return new String[][] {
				new String[] { "AF|Afghanistan" },
				new String[] { "AL|Albania" },
				new String[] { "DZ|Algeria" },
				new String[] { "AS|American Samoa" },
				new String[] { "AD|Andorra" },
				new String[] { "AO|Angola" },
				new String[] { "AI|Anguilla" },
				new String[] { "AQ|Antarctica" },
				new String[] { "AG|Antigua And Barbuda" },
				new String[] { "AR|Argentina" },
				new String[] { "AM|Armenia" },
				new String[] { "AW|Aruba" },
				new String[] { "AU|Australia" },
				new String[] { "AT|Austria" },
				new String[] { "AZ|Azerbaijan" },
				new String[] { "BS|Bahamas" },
				new String[] { "BH|Bahrain" },
				new String[] { "BD|Bangladesh" },
				new String[] { "BB|Barbados" },
				new String[] { "BY|Belarus" },
				new String[] { "BE|Belgium" },
				new String[] { "BZ|Belize" },
				new String[] { "BJ|Benin" },
				new String[] { "BM|Bermuda" },
				new String[] { "BT|Bhutan" },
				new String[] { "BO|Bolivia" },
				new String[] { "BA|Bosnia And Herzegovina" },
				new String[] { "BW|Botswana" },
				new String[] { "BV|Bouvet Island" },
				new String[] { "BR|Brazil" },
				new String[] { "IO|British Indian Ocean Territory" },
				new String[] { "BN|Brunei Darussalam" },
				new String[] { "BG|Bulgaria" },
				new String[] { "BF|Burkina Faso" },
				new String[] { "BI|Burundi" },
				new String[] { "KH|Cambodia" },
				new String[] { "CM|Cameroon" },
				new String[] { "CA|Canada" },
				new String[] { "CV|Cape Verde" },
				new String[] { "KY|Cayman Islands" },
				new String[] { "CF|Central African Republic" },
				new String[] { "TD|Chad" },
				new String[] { "CL|Chile" },
				new String[] { "CN|China" },
				new String[] { "CX|Christmas Island" },
				new String[] { "CC|Cocos (keeling) Islands" },
				new String[] { "CO|Colombia" },
				new String[] { "KM|Comoros" },
				new String[] { "CG|Congo" },
				new String[] { "CD|Congo, The Democratic Republic Of The" },
				new String[] { "CK|Cook Islands" },
				new String[] { "CR|Costa Rica" },
				new String[] { "CI|Cote D'ivoire" },
				new String[] { "HR|Croatia" },
				new String[] { "CU|Cuba" },
				new String[] { "CY|Cyprus" },
				new String[] { "CZ|Czech Republic" },
				new String[] { "DK|Denmark" },
				new String[] { "DJ|Djibouti" },
				new String[] { "DM|Dominica" },
				new String[] { "DO|Dominican Republic" },
				new String[] { "TP|East Timor" },
				new String[] { "EC|Ecuador" },
				new String[] { "EG|Egypt" },
				new String[] { "SV|El Salvador" },
				new String[] { "GQ|Equatorial Guinea" },
				new String[] { "ER|Eritrea" },
				new String[] { "EE|Estonia" },
				new String[] { "ET|Ethiopia" },
				new String[] { "FK|Falkland Islands (malvinas)" },
				new String[] { "FO|Faroe Islands" },
				new String[] { "FJ|Fiji" },
				new String[] { "FI|Finland" },
				new String[] { "FR|France" },
				new String[] { "GF|French Guiana" },
				new String[] { "PF|French Polynesia" },
				new String[] { "TF|French Southern Territories" },
				new String[] { "GA|Gabon" },
				new String[] { "GM|Gambia" },
				new String[] { "GE|Georgia" },
				new String[] { "DE|Germany" },
				new String[] { "GH|Ghana" },
				new String[] { "GI|Gibraltar" },
				new String[] { "GR|Greece" },
				new String[] { "GL|Greenland" },
				new String[] { "GD|Grenada" },
				new String[] { "GP|Guadeloupe" },
				new String[] { "GU|Guam" },
				new String[] { "GT|Guatemala" },
				new String[] { "GN|Guinea" },
				new String[] { "GW|Guinea-bissau" },
				new String[] { "GY|Guyana" },
				new String[] { "HT|Haiti" },
				new String[] { "HM|Heard Island And Mcdonald Islands" },
				new String[] { "VA|Holy See (vatican City State)" },
				new String[] { "HN|Honduras" },
				new String[] { "HK|Hong Kong" },
				new String[] { "HU|Hungary" },
				new String[] { "IS|Iceland" },
				new String[] { "IN|India" },
				new String[] { "ID|Indonesia" },
				new String[] { "IR|Iran, Islamic Republic Of" },
				new String[] { "IQ|Iraq" },
				new String[] { "IE|Ireland" },
				new String[] { "IL|Israel" },
				new String[] { "IT|Italy" },
				new String[] { "JM|Jamaica" },
				new String[] { "JP|Japan" },
				new String[] { "JO|Jordan" },
				new String[] { "KZ|Kazakstan" },
				new String[] { "KE|Kenya" },
				new String[] { "KI|Kiribati" },
				new String[] { "KP|Korea, Democratic People's Republic Of" },
				new String[] { "KR|Korea, Republic Of" },
				new String[] { "KV|Kosovo" },
				new String[] { "KW|Kuwait" },
				new String[] { "KG|Kyrgyzstan" },
				new String[] { "LA|Lao People's Democratic Republic" },
				new String[] { "LV|Latvia" },
				new String[] { "LB|Lebanon" },
				new String[] { "LS|Lesotho" },
				new String[] { "LR|Liberia" },
				new String[] { "LY|Libyan Arab Jamahiriya" },
				new String[] { "LI|Liechtenstein" },
				new String[] { "LT|Lithuania" },
				new String[] { "LU|Luxembourg" },
				new String[] { "MO|Macau" },
				new String[] { "MK|Macedonia, The Former Yugoslav Republic Of" },
				new String[] { "MG|Madagascar" },
				new String[] { "MW|Malawi" },
				new String[] { "MY|Malaysia" },
				new String[] { "MV|Maldives" },
				new String[] { "ML|Mali" },
				new String[] { "MT|Malta" },
				new String[] { "MH|Marshall Islands" },
				new String[] { "MQ|Martinique" },
				new String[] { "MR|Mauritania" },
				new String[] { "MU|Mauritius" },
				new String[] { "YT|Mayotte" },
				new String[] { "MX|Mexico" },
				new String[] { "FM|Micronesia, Federated States Of" },
				new String[] { "MD|Moldova, Republic Of" },
				new String[] { "MC|Monaco" },
				new String[] { "MN|Mongolia" },
				new String[] { "MS|Montserrat" },
				new String[] { "ME|Montenegro" },
				new String[] { "MA|Morocco" },
				new String[] { "MZ|Mozambique" },
				new String[] { "MM|Myanmar" },
				new String[] { "NA|Namibia" },
				new String[] { "NR|Nauru" },
				new String[] { "NP|Nepal" },
				new String[] { "NL|Netherlands" },
				new String[] { "AN|Netherlands Antilles" },
				new String[] { "NC|New Caledonia" },
				new String[] { "NZ|New Zealand" },
				new String[] { "NI|Nicaragua" },
				new String[] { "NE|Niger" },
				new String[] { "NG|Nigeria" },
				new String[] { "NU|Niue" },
				new String[] { "NF|Norfolk Island" },
				new String[] { "MP|Northern Mariana Islands" },
				new String[] { "NO|Norway" },
				new String[] { "OM|Oman" },
				new String[] { "PK|Pakistan" },
				new String[] { "PW|Palau" },
				new String[] { "PS|Palestinian Territory, Occupied" },
				new String[] { "PA|Panama" },
				new String[] { "PG|Papua New Guinea" },
				new String[] { "PY|Paraguay" },
				new String[] { "PE|Peru" },
				new String[] { "PH|Philippines" },
				new String[] { "PN|Pitcairn" },
				new String[] { "PL|Poland" },
				new String[] { "PT|Portugal" },
				new String[] { "PR|Puerto Rico" },
				new String[] { "QA|Qatar" },
				new String[] { "RE|Reunion" },
				new String[] { "RO|Romania" },
				new String[] { "RU|Russian Federation" },
				new String[] { "RW|Rwanda" },
				new String[] { "SH|Saint Helena" },
				new String[] { "KN|Saint Kitts And Nevis" },
				new String[] { "LC|Saint Lucia" },
				new String[] { "PM|Saint Pierre And Miquelon" },
				new String[] { "VC|Saint Vincent And The Grenadines" },
				new String[] { "WS|Samoa" },
				new String[] { "SM|San Marino" },
				new String[] { "ST|Sao Tome And Principe" },
				new String[] { "SA|Saudi Arabia" },
				new String[] { "SN|Senegal" },
				new String[] { "RS|Serbia" },
				new String[] { "SC|Seychelles" },
				new String[] { "SL|Sierra Leone" },
				new String[] { "SG|Singapore" },
				new String[] { "SK|Slovakia" },
				new String[] { "SI|Slovenia" },
				new String[] { "SB|Solomon Islands" },
				new String[] { "SO|Somalia" },
				new String[] { "ZA|South Africa" },
				new String[] { "GS|South Georgia And The South Sandwich Islands" },
				new String[] { "ES|Spain" }, new String[] { "LK|Sri Lanka" },
				new String[] { "SD|Sudan" }, new String[] { "SR|Suriname" },
				new String[] { "SJ|Svalbard And Jan Mayen" },
				new String[] { "SZ|Swaziland" }, new String[] { "SE|Sweden" },
				new String[] { "CH|Switzerland" },
				new String[] { "SY|Syrian Arab Republic" },
				new String[] { "TW|Taiwan, Province Of China" },
				new String[] { "TJ|Tajikistan" },
				new String[] { "TZ|Tanzania, United Republic Of" },
				new String[] { "TH|Thailand" }, new String[] { "TG|Togo" },
				new String[] { "TK|Tokelau" }, new String[] { "TO|Tonga" },
				new String[] { "TT|Trinidad And Tobago" },
				new String[] { "TN|Tunisia" }, new String[] { "TR|Turkey" },
				new String[] { "TM|Turkmenistan" },
				new String[] { "TC|Turks And Caicos Islands" },
				new String[] { "TV|Tuvalu" }, new String[] { "UG|Uganda" },
				new String[] { "UA|Ukraine" },
				new String[] { "AE|United Arab Emirates" },
				new String[] { "GB|United Kingdom" },
				new String[] { "US|United States" },
				new String[] { "UM|United States Minor Outlying Islands" },
				new String[] { "UY|Uruguay" },
				new String[] { "UZ|Uzbekistan" },
				new String[] { "VU|Vanuatu" }, new String[] { "VE|Venezuela" },
				new String[] { "VN|Viet Nam" },
				new String[] { "VG|Virgin Islands, British" },
				new String[] { "VI|Virgin Islands, U.s." },
				new String[] { "WF|Wallis And Futuna" },
				new String[] { "EH|Western Sahara" },
				new String[] { "YE|Yemen" }, new String[] { "ZM|Zambia" },
				new String[] { "ZW|Zimbabwe" }, };
	}

	private static String[][] getTimeZone() {
		return new String[][] {
				new String[] { "Apia, Pago Pago 	UTC-11 	01:00" },
				new String[] { "Honolulu, Papeete 	UTC-10 	02:00" },
				new String[] { "Anchorage, Juneau 	UTC-9 	03:00" },
				new String[] { "Los Angeles, San Francisco, Las Vegas, Vancouver 	UTC-8 	04:00" },
				new String[] { "Calgary, Denver 	UTC-7 	05:00" },
				new String[] { "Chicago, Mexico City 	UTC-6 	06:00" },
				new String[] { "Toronto, New York City, Havana, Bogot\u0102\u0192\u00C2\u00A1, Lima 	UTC-5 	07:00" },
				new String[] { "Caracas 	UTC-4:30 	07:30" },
				new String[] { "Asunci\u0102\u0192\u00C2\u00B3n, Halifax, Santiago 	UTC-4 	08:00" },
				new String[] { "Buenos Aires, Montevideo, Rio de Janeiro, S\u0102\u0192\u00C2\u00A3o Paulo 	UTC-3 	09:00" },
				new String[] { "Fernando de Noronha, South Georgia and the South Sandwich Islands 	UTC-2 	10:00" },
				new String[] { "Azores, Cape Verde 	UTC-1 	11:00" },
				new String[] { "Dakar, Casablanca, London, Lisbon, Reykjav\u0102\u0192\u00C2\u00ADk, Tenerife 	UTC (UTC\u0102\u201A\u00C2\u00B10) 	12:00" },
				new String[] { "Algiers, Berlin, Kinshasa, Lagos, Paris, Rome, Tunis 	UTC+1 	13:00" },
				new String[] { "Istanbul,Athens ,Cairo, Cape Town, Helsinki, Jerusalem 	UTC+2 	14:00" },
				new String[] { "Addis Ababa, Baghdad, Moscow, Nairobi, Saint Petersburg 	UTC+3 	15:00" },
				new String[] { "Tehran 	UTC+3:30 	15:30" },
				new String[] { "Baku, Dubai, Mauritius, Samara, Tbilisi 	UTC+4 	16:00" },
				new String[] { "Karachi, Maldives, Tashkent, Yekaterinburg 	UTC+5 	17:00" },
				new String[] { "Colombo, Delhi, Mumbai 	UTC+5:30 	17:30" },
				new String[] { "Kathmandu 	UTC+5:45 	17:45" },
				new String[] { "Almaty, Dhaka, Omsk 	UTC+6 	18:00" },
				new String[] { "Cocos Islands, Yangon 	UTC+6:30 	18:30" },
				new String[] { "Bangkok, Jakarta, Hanoi, Krasnoyarsk 	UTC+7 	19:00" },
				new String[] { "Beijing, Hong Kong, Irkutsk, Kuala Lumpur, Manila, Perth 	UTC+8 	20:00" },
				new String[] { "Pyongyang, Seoul, Tokyo, Yakutsk 	UTC+9 	21:00" },
				new String[] { "Adelaide, Darwin 	UTC+9:30 	21:30" },
				new String[] { "Melbourne, Sydney, Vladivostok 	UTC+10 	22:00" },
				new String[] { "Magadan, Noum\u0102\u0192\u00C2\u00A9a 	UTC+11 	23:00" },
				new String[] { "Auckland, Petropavlovsk-Kamchatsky, Suva 	UTC+12 	00:00 (the following day)" },
				new String[] { "Nuku\u0102\u00C2\u00BBalofa 	UTC+13 	01:00 (the following day)" }, };
	}
}
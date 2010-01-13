package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.util.client.Task;
import com.bkitmobile.poma.util.client.Utils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextArea;

public class RegisterTrackerForm extends TrackerForm {

	private Checkbox checkboxTOS;
	private FieldSet fsTOS;

	private boolean confirm = false;
	private TextArea txtTOS;

	/**
	 * After initialize
	 */
	@Override
	protected void postInit() {
		super.postInit();

		cbLanguage
				.setValue(UserSettings.hashMapConfig.get("lang") == null ? "vi"
						: UserSettings.hashMapConfig.get("lang"));

	}

	@Override
	public void resetForm() {
		// super.resetForm();
		txtApiKey.setValue(Utils.getAlphaNumeric(32));

		txtUsername.setValue("");
		txtUsername.clearInvalid();

		txtPassword.setValue("");
		txtPassword.clearInvalid();

		txtPasswordConfirm.setValue("");
		txtPasswordConfirm.clearInvalid();

		txtName.setValue("");
		txtName.clearInvalid();

			dateBirthday.setValue("");
		dateBirthday.clearInvalid();

		txtMobilePhone.setValue("");
		txtMobilePhone.clearInvalid();

		txtAddress.setValue("");
		txtAddress.clearInvalid();

		txtEmail.setValue("");
		txtEmail.clearInvalid();

		txtEmailConfirm.setValue("");
		txtEmailConfirm.clearInvalid();

		cbLanguage.setValue("vi");
		cbCountry.setValue("VN");
		cbTimeZone.setValue("7");

	}

	/**
	 * After validate
	 */
	@Override
	protected String postValidate() {
		if (!txtEmail.getText().equals(txtEmailConfirm.getText())) {
			return "email_not_match";
		}

		if (!checkboxTOS.getValue()) {
			return constants.tos_error();
		}
		return null;
	}

	/**
	 * After submit method, call database to insert tracker into database
	 */
	@Override
	protected void postSubmit() {
		confirm = Boolean.parseBoolean(UserSettings.hashMapConfig
				.get("confirm") == null ? "true" : UserSettings.hashMapConfig
				.get("confirm"));

		if (!confirm)
			submitTracker.setActived(true);
		else
			submitTracker.setActived(false);

		dbService.insertTracker(submitTracker,
				new AsyncCallback<ServiceResult<CTracker>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result.isOK()) {

							if (confirm) {
								trackerService.validateEmailNewTracker(
										submitTracker,
										new AsyncCallback<Boolean>() {

											@Override
											public void onFailure(
													Throwable caught) {
												MessageBox.alert(caught
														.getMessage());
												caught.printStackTrace();
											}

											@Override
											public void onSuccess(Boolean result) {
												BkitPoma.stopLoading();
												if (result) {
													MessageBox
															.alert(
																	"",
																	constants
																			.validateEmailNewTracker_success(),
																	new AlertCallback() {

																		@Override
																		public void execute() {
																			BkitPoma
																					.returnToMap();
																		}

																	});

												} else {
													MessageBox
															.alert(constants
																	.validateEmailNewTracker_error());
												}
												return;
											}

										});
							} else {
								BkitPoma.stopLoading();
								MessageBox.alert("", constants
										.insertTracker_success(),
										new AlertCallback() {

											@Override
											public void execute() {
												BkitPoma.returnToMap();
											}

										});
							}
						} else {
							BkitPoma.stopLoading();
							if (result.getResult().getType() == -1) {
								MessageBox.alert(constants
										.insertTracker_usernameexisted());
								txtUsername.markInvalid(constants
										.insertTracker_usernameexisted());
							} else {
								MessageBox.alert(constants
										.insertTracker_mailexisted());
								txtEmail.markInvalid(constants
										.insertTracker_mailexisted());
							}
						}
					}

				});
	}

	/**
	 * Re-Layout for fsOptional
	 */
	@Override
	protected void optionalItemLayout() {
		super.optionalItemLayout();

		fsTOS = new FieldSet(constants.fsTOS());
		fsTOS.setCollapsible(true);

		txtTOS = new TextArea(constants.txtTOS());
		txtTOS.setReadOnly(true);
		txtTOS.setWidth(TEXTFIELD_WIDTH);
		txtTOS.setHeight(150);
		txtTOS.setValue(UserSettings.hashMapConfig.get("tos") == null ? ""
				: UserSettings.hashMapConfig.get("tos"));

		checkboxTOS = new Checkbox(constants.checkboxTOS());
		checkboxTOS.setChecked(false);

		fsTOS.add(txtTOS);
		fsTOS.add(checkboxTOS);

		this.add(fsTOS);
	}
}

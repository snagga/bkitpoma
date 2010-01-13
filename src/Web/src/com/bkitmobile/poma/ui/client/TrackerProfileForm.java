package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;

public class TrackerProfileForm extends TrackerForm {
	FieldSet fsChangePassword;
	TextField txtOldPassword;

	private boolean updatePassword = false;

	/**
	 * Constructor
	 */
	public TrackerProfileForm() {
		super();
	}

	/**
	 * Do after init in base class
	 */
	@Override
	protected void postInit() {
		txtOldPassword = new TextField(constants.txtOldPassword());
		txtOldPassword.setAllowBlank(true);
		txtOldPassword.setWidth(TEXTFIELD_WIDTH);
		txtOldPassword.setPassword(true);

		txtPassword.setLabel(constants.txtPassword_newpassword());
		txtPassword.setAllowBlank(true);

		txtPasswordConfirm.setLabel(constants.txtPasswordConfirm_newpassword());
		txtPasswordConfirm.setAllowBlank(true);

		txtUsername.setDisabled(true);

		btnSubmit.setText(constants.btnSubmit_update());

	}

	/**
	 * Other validate with validate in base class
	 */
	@Override
	protected String postValidate() {
		if (!txtOldPassword.isValid()) {
			return txtOldPassword.getInvalidText();
		}

		if (!txtOldPassword.getText().equals("")
				&& txtPassword.getText().equals("")) {
			return "new_password_not_enter";
		}

		if (txtOldPassword.getText().equals("")
				&& !txtPassword.getText().equals("")) {
			return "old_password_not_enter";
		}

		if (!txtPassword.getText().equals(txtPasswordConfirm.getText())) {
			txtPassword.markInvalid("password_not_equal");
			txtPasswordConfirm.markInvalid("password_not_equal");
			return "password_not_equal";
		}

		// if (!txtEmail.getText().equals(txtEmailConfirm.getText())) {
		// txtEmail.markInvalid("email_not_equal");
		// txtEmailConfirm.markInvalid("email_not_equal");
		// return "email_not_equal";
		// }
		return null;
	}

	/**
	 * After assign tracker
	 */
	@Override
	protected void postAssignTracker() {
		if (txtOldPassword.getText().equals("")) {
			submitTracker.setPassword(null);
		}
	}

	/**
	 * After submit
	 */
	@Override
	protected void postSubmit() {
		if (submitTracker.getPassword() != null) {
			changePassword();
		} else {
			updateProfile();
		}
	}

	/**
	 * After call resetForm
	 */
	@Override
	protected void postResetForm() {
		txtOldPassword.setValue("");
	}

	/**
	 * Call when user want to change password
	 */
	private void changePassword() {
		updatePassword = true;
		BkitPoma.startLoading(constants.BkitPoma_startLoading_loading());
		DatabaseService.Util.getInstance().updateTrackerPassword(
				submitTracker.getUsername(), txtOldPassword.getText(),
				submitTracker.getPassword(),
				new AsyncCallback<ServiceResult<CTracker>>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert(constants.msb_changePassword_fail());
						BkitPoma.stopLoading();
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result == null || result.getResult() == null) {
							MessageBox.alert(constants.msb_changePassword_fail());
							BkitPoma.stopLoading();
							return;
						}

						UserSettings.ctracker = result.getResult();
						TrackerProfileForm.this.resetForm();
						updateProfile();
					}
				});
	}

	/**
	 * Call when user want to change password or profile
	 */
	private void updateProfile() {
		BkitPoma.startLoading(constants.BkitPoma_startLoading_loading());
		DatabaseService.Util.getInstance().updateTracker(submitTracker,
				new AsyncCallback<ServiceResult<CTracker>>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert(constants.msb_updateProfile_fail());
						BkitPoma.stopLoading();
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result == null || result.getResult() == null) {
							MessageBox.alert(constants.msb_updateProfile_fail());
							BkitPoma.stopLoading();
							return;
						}
						UserSettings.ctracker = result.getResult();
						if (updatePassword)
							MessageBox.alert(constants.msb_changePassword_succecss());
						else
							MessageBox.alert(constants.msb_updateProfile_success());
						updatePassword = false;
						BkitPoma.stopLoading();
					}
				});
	}

	/**
	 * Re-layout for fsRequire
	 */
	@Override
	protected void requireItemLayout() {
		fsRequireInfo = new FieldSet(constants.fsRequireInfo());

		// require information
		fsRequireInfo.add(txtName);
		fsRequireInfo.add(txtUsername);
		fsRequireInfo.add(txtEmail);

		fsChangePassword = new FieldSet(
				constants.fsChangePassword());
		fsChangePassword.add(txtOldPassword);
		fsChangePassword.add(txtPassword);
		fsChangePassword.add(txtPasswordConfirm);
		fsChangePassword.setCollapsible(true);
		fsChangePassword.setCollapsed(true);

		this.add(fsRequireInfo);

	}

	/**
	 * Call to check whether user login with what account
	 */
	public void doAfterLogin() {
		if (UserSettings.ctracker != null
				&& UserSettings.ctracker.getType() == 0)
			this.add(fsChangePassword);
	}

}

package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.util.client.Utils;
import com.gwtext.client.widgets.form.FieldSet;

public class RegisterTrackerOpenID extends RegisterTrackerForm {
	/**
	 * After init
	 */
	@Override
	protected void postInit() {
		super.postInit();
		txtUsername.setRegex(null);
		txtUsername.setDisabled(true);
		txtPassword.setRegex(null);
		txtPassword.setAllowBlank(true);
		txtPasswordConfirm.setRegex(null);
		txtPasswordConfirm.setAllowBlank(true);
		btnReturnToMap.setVisible(false);
	}
	
	/**
	 * Re-layout for fsRequire
	 */
	@Override
	protected void requireItemLayout() {
		fsRequireInfo = new FieldSet();
		fsRequireInfo.setTitle(constants.fsRequireInfo());

		// require information
		fsRequireInfo.add(txtName);
		fsRequireInfo.add(txtUsername);
		fsRequireInfo.add(txtEmail);
		fsRequireInfo.add(txtEmailConfirm);

		this.add(fsRequireInfo);
	}
	
	/**
	 * After assign field into CTracker 
	 */
	@Override
	protected void postAssignTracker() {
		submitTracker.setPassword(Utils.getAlphaNumeric(10));
		submitTracker.setActived(true);
	}
}

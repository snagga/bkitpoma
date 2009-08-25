package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class TrackerProfileForm extends RegisterTrackerForm {

	public TrackerProfileForm() {
		super();
		setTitle("Show tracker profile");

		btnSubmit.setText("Change profile");
		displayTrackerInfo();
		btnSubmit.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {

				if (!isValidForm()) {
					MessageBox.alert("Some fields are invalid!!!");
					return;
				}

				CaptchaWindow.getInstance().show();
				CaptchaWindow.getInstance().addListener("validate", new Function() {
					@Override
					public void execute() {
						if (CaptchaWindow.getInstance().getValue() == true) {

							CTracker cTracker = new CTracker(txtUsername
									.getText(), txtPassword.getText(), txtName
									.getText(), dateBirthday.getValue(),
									txtMobilePhone.getText(), taAddress
											.getText(), txtEmail.getText(), 0,
									selectedIndexTimeZone, "vi", cbCountry
											.getText(), true, false);

							dbService
									.updateTracker(
											cTracker,
											new AsyncCallback<ServiceResult<CTracker>>() {
												@Override
												public void onFailure(
														Throwable caught) {
													MessageBox
															.alert(
																	"Insert tracker error",
																	caught
																			.toString());
													caught.printStackTrace();
												}

												@Override
												public void onSuccess(
														ServiceResult<CTracker> result) {
													if (result.getResult() != null) {
														MessageBox
																.alert("Insert tracker successfully");

														UserSettings.ctracker = result
																.getResult();
													} else {
														MessageBox
																.alert(
																		"Insert tracker error",
																		"FAILURE");
													}
												}
											});
						}
					}
				});
			}

		});
	}

	public void displayTrackerInfo() {
		CTracker cTracker = UserSettings.ctracker;
		if (cTracker == null) return;
		txtEmail.setValue(cTracker.getEmail());
		txtMobilePhone.setValue(cTracker.getTel());
		txtName.setValue(cTracker.getName());
		txtUsername.setValue(cTracker.getUsername());
		txtUsername.disable();
		if (cTracker.getBirthday() != null)
			dateBirthday.setValue(cTracker.getBirthday());
		taAddress.setValue(cTracker.getAddr());
		if (cTracker.getGmt() != null)
			cbTimeZone.setValue(getTimeZone()[cTracker.getGmt()][0]);
		if (cTracker.getCountry() != null)
			cbCountry.setValue(cTracker.getCountry());
	}

	class ButtonSubmitUpdateTrackerListenerAdapter extends
			ButtonListenerAdapter {
		@Override
		public void onClick(Button button, EventObject e) {
			// TODO Auto-generated method stub
			super.onClick(button, e);
		}
	}

}

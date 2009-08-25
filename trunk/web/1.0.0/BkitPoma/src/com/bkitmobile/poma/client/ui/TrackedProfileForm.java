package com.bkitmobile.poma.client.ui;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.Utils;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracked;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Function;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class TrackedProfileForm extends RegisterTrackedForm {

	public TrackedProfileForm() {
		super(UNKNOWN_ICON);
		btnSubmit.setText("Change profile");
		btnSubmit.addListener(
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {

						if (!isValidForm()) {
							MessageBox.alert(local.msgbox_alert_title(), local.msgbox_alert_invalid());
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
									CTracked tracked = new CTracked(
											"",//txtPassword.getText(),
											txtName.getText(),
											dateBirthday.getValue(),
											txtMobilePhone.getText(),
											txtEmail.getText(),
											true,
											UserSettings.ctracker.getGmt(),
											UserSettings.ctracker.getLang(),
											UserSettings.ctracker.getCountry(),
											urlIcon,
											true,
											true,
											sWindow.getStringBinarySchedule(),
											valuePrgbar,
											Utils.getAlphaNumeric(32)
										);
									
									dbService.updateTracked(tracked, new AsyncCallback<ServiceResult<CTracked>>() {

										@Override
										public void onFailure(Throwable arg0) {
											arg0.printStackTrace();
										}

										@Override
										public void onSuccess(ServiceResult<CTracked> arg0) {
											if (arg0.isOK()) 
												MessageBox.alert(arg0.getMessage());
											else
												MessageBox.alert("Update tracked profile failed");
										}
										
									});
									
								} else {
									MessageBox.alert(local.msgbox_alert_title(), local.msgbox_alert_insert());
									MessageBox.alert(local.reCaptchaWidget_invalid());
								}
								
							}
						});

					} // end onClick

				});
		
	}
	
	public void displayProfile() {
		
	}

}

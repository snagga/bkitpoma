package com.bkitmobile.poma.ui.client;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.bkitmobile.poma.util.client.Task;
import com.bkitmobile.poma.util.client.Utils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class RegisterTrackedForm extends TrackedForm {
	private static RegisterTrackedForm registerTrackedForm;

	/**
	 * 
	 * @return instance of class
	 */
	public static RegisterTrackedForm getInstance() {
		return registerTrackedForm == null ? new RegisterTrackedForm()
				: registerTrackedForm;
	}

	/**
	 * Constructor
	 */
	public RegisterTrackedForm() {
		super();
		registerTrackedForm = this;
	}

	/**
	 * After init method
	 */
	@Override
	protected void postInit() {
		super.postInit();
		
		this.addListener(new PanelListenerAdapter(){
			@Override
			public void onShow(Component component) {
				resetForm();
				
				resize();
			}
		});

		cbLanguage
				.setValue(UserSettings.hashMapConfig.get("lang") == null ? "vi"
						: UserSettings.hashMapConfig.get("lang"));

		txtApiKey.setValue(Utils.getAlphaNumeric(32));
	}

	@Override
	public void resetForm() {
		txtPassword.setValue("");
		txtPassword.clearInvalid();

		txtPasswordConfirm.setValue("");
		txtPasswordConfirm.clearInvalid();

		txtUsername.setValue("");
		txtUsername.clearInvalid();

		txtName.setValue("");
		txtName.clearInvalid();

		dateBirthday.setValue("");
		dateBirthday.clearInvalid();

		txtUsername.setValue("");

		txtMobilePhone.setValue("");
		txtMobilePhone.clearInvalid();

		txtEmail.setValue("");
		txtEmail.clearInvalid();

		txtEmailConfirm.setValue("");
		txtEmailConfirm.clearInvalid();

		cbIntervalGPS.setValue("10");
		cbIntervalGPS.clearInvalid();

		txtIconPath.setValue(DEFFAULT_ICON_PATH);

		imgIcon.setUrl(txtIconPath.getText());

		checkActive.setValue(true);

		checkEmbedded.setValue(false);

		checkShowInMap.setValue(false);

		cbLanguage.setValue("vi");
		cbCountry.setValue("VN");
		cbTimeZone.setValue("7");

		txtApiKey.setValue(Utils.getAlphaNumeric(32));
	}

	/**
	 * Do other things, not in layout method of base class
	 */
	protected void postLayout() {
		txtUsername.setAllowBlank(true);
		txtUsername.setRegex(null);
		txtUsername.hide();
	}

	/**
	 * Do other things, not in validate method of base class
	 */
	@Override
	protected String postValidate() {
		if (!txtPasswordConfirm.isValid()) {
			return txtPasswordConfirm.getInvalidText();
		}

		if (!txtEmailConfirm.isValid()) {
			return txtEmailConfirm.getInvalidText();
		}

		if (!txtPassword.getText().equals(txtPasswordConfirm.getText())) {
			txtPassword.markInvalid("password_not_equal");
			txtPasswordConfirm.markInvalid("password_not_equal");
			return "password_not_equal";
		}

		if (!txtEmail.getText().equals(txtEmailConfirm.getText())) {
			txtEmail.markInvalid("Email_not_equal");
			txtEmailConfirm.markInvalid("Email_not_equal");
			return "Email_not_equal";
		}

		return null;
	}

	/**
	 * Do other things, not in submit method of base class: After submit method,
	 * call database to insert tracked into database
	 */
	@Override
	protected void postSubmit() {
		// submitTracked.setSchedule(schedule == null ? (new Schedule())
		// .getBytesScheduleTime() : schedule.getBytesScheduleTime());
		BkitPoma.startLoading(constants.BkitPoma_startLoading_loading());
		dbService.insertTracked(submitTracked, UserSettings.ctracker
				.getUsername(), true,
				new AsyncCallback<ServiceResult<CTracked>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						MessageBox.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(ServiceResult<CTracked> result) {
						if (result.isOK()) {
							CTracked ctracked = result.getResult();
							boolean isShowInMap = ctracked.getShowInMap() == null ? false
									: ctracked.getShowInMap();
							TrackedPanel.getInstance().addNewTracked(ctracked);
							// TrackedPanel
							// .getInstance()
							// .getTrackedTable()
							// .addRecord(
							// new String[] {
							// ctracked.getUsername()
							// .toString(),
							// ctracked.getName(),
							// "false", "" + isShowInMap });
							// UserSettings.ctrackedList.put(ctracked
							// .getUsername(), ctracked);
							// TrackedPanel.getInstance().setCurrentRow(-1);
							MessageBox.alert("",
									Format.format(constants
											.insertTracked_success(), String
											.valueOf(result.getResult()
													.getUsername())),
									new AlertCallback() {

										@Override
										public void execute() {
											BkitPoma.returnToMap();
										}

									});
						} else
							MessageBox.alert(result.getMessage());
						BkitPoma.stopLoading();
					}

				});
	}

	@Override
	protected void postAssignTracked() {
		super.postAssignTracked();
		submitTracked.setIconPath(txtIconPath.getValueAsString());
	}

}

package com.bkitmobile.poma.ui.client;

import java.util.ArrayList;

import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CManage;
import com.bkitmobile.poma.database.client.entity.CStaff;
import com.bkitmobile.poma.database.client.entity.CTracked;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.home.client.UserSettings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.MessageBox.AlertCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class TrackedProfileForm extends TrackedForm {

	private static TrackedProfileForm trackedProfileForm;

	public static ArrayList<String> arrStaff = null;
	public static ArrayList<String> arrManage = null;

	private TextField txtOldPassword;
	private FieldSet fsChangePassword;
	protected static String UNKNOWN_ICON = "http://maps.google.com/mapfiles/ms/micons/red-dot.png";

	private FieldSet fsShareTracked;

	private Label lblStaff;
	private Label lblManage;
	private Button btnShare;

	private ShareTrackedWindow permitTrackedWindow;

	/**
	 * Get instance
	 * 
	 * @return Instance of this window
	 */
	public static TrackedProfileForm getInstance() {
		return trackedProfileForm == null ? new TrackedProfileForm()
				: trackedProfileForm;
	}

	/**
	 * Constructor
	 */
	public TrackedProfileForm() {
		super();
		trackedProfileForm = this;
	}

	/**
	 * After call init
	 */
	@Override
	protected void postInit() {

		this.addListener(new PanelListenerAdapter() {
			@Override
			public void onShow(Component component) {
				resize();
			}
		});

		txtOldPassword = new TextField(constants.txtOldPassword());
		txtOldPassword.setAllowBlank(true);
		txtOldPassword.setWidth(TEXTFIELD_WIDTH);
		txtOldPassword.setPassword(true);

		txtPassword.setLabel(constants.txtPassword_newpassword());
		txtPassword.setAllowBlank(true);

		txtPasswordConfirm.setLabel(constants.txtPasswordConfirm_newpassword());
		txtPasswordConfirm.setAllowBlank(true);

		btnSubmit.setText(constants.btnSubmit_update());

		/*
		 * Username
		 */
		txtUsername.setDisabled(true);

		lblStaff = new Label();
		lblStaff.setCls("gwt-Label");
		lblManage = new Label();
		lblManage.setCls("gwt-Label");

		arrStaff = new ArrayList<String>();
		arrManage = new ArrayList<String>();

		btnShare = new Button(constants.btnShare(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);
						
						permitTrackedWindow = ShareTrackedWindow.getInstance();
						permitTrackedWindow.setTrackedID(UserSettings.ctracked
								.getUsername(), TrackedProfileForm.arrManage,
								TrackedProfileForm.arrStaff);
						permitTrackedWindow.show(new ShareTrackedCallback() {

							@Override
							public void onApplyOperation(
									ArrayList<String> arrListManage,
									ArrayList<String> arrListStaff) {
								arrManage.clear();
								arrManage.addAll(arrListManage);
								refreshManageLabel();

								arrStaff.clear();
								arrStaff.addAll(arrListStaff);
								refreshStaffLabel();

								if (!arrManage.contains(UserSettings.ctracker
										.getUsername())) {
									btnShare.setDisabled(true);
								} else {
									btnShare.setDisabled(false);
								}
							}

						});
					}
				});
	}

	/**
	 * Re-layout for fsRequireInfo
	 */
	@Override
	protected void requireItemLayout() {
		// fsConfig
		FieldSet fsConfig = new FieldSet(constants.fsConfig());
		fsConfig.add(cbIntervalGPS);

		Panel panel = new Panel();
		panel.setLayout(new HorizontalLayout(5));
		Label lblImgURL = new Label(constants.lblImgURL());
		lblImgURL.setCls("gwt-Label");
		lblImgURL.setWidth(LABEL_WIDTH + 3);
		panel.add(lblImgURL);
		panel.add(txtIconPath);
		panel.add(imgIcon);
		panel.add(btnIconChooser);

		fsConfig.add(panel);

		// require information
		fsRequireInfo = new FieldSet(constants.fsRequireInfo());
		fsRequireInfo.add(txtName);
		fsRequireInfo.add(txtUsername);
		fsRequireInfo.add(txtEmail);

		fsChangePassword = new FieldSet(constants.fsChangePassword());
		fsChangePassword.setCollapsible(true);
		fsChangePassword.add(txtOldPassword);
		fsChangePassword.add(txtPassword);
		fsChangePassword.add(txtPasswordConfirm);

		this.add(fsRequireInfo);
		this.add(fsConfig);
		this.add(fsChangePassword);
	}

	/**
	 * Re-layout for optional part, call base class and add fsShareTracked
	 */
	@Override
	protected void optionalItemLayout() {
		super.optionalItemLayout();

		fsShareTracked = new FieldSet(constants.fsShareTracked());
		fsShareTracked.setLayout(new VerticalLayout());
		fsShareTracked.setCollapsible(true);

		fsShareTracked.add(lblManage);
		fsShareTracked.add(lblStaff);
		fsShareTracked.add(btnShare);

		this.add(fsShareTracked);
	}

	/**
	 * After call validate
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

		return null;
	}

	/**
	 * After call assignTracked
	 */
	@Override
	protected void postAssignTracked() {
		submitTracked.setUsername(Long.parseLong(txtUsername.getText()));
		submitTracked
				.setIconPath(txtIconPath.getValueAsString().trim() == "" ? DEFFAULT_ICON_PATH
						: txtIconPath.getValueAsString());
		if (txtOldPassword.getText().equals("")) {
			submitTracked.setPassword(null);
		}
	}

	/**
	 * After call submit method
	 */
	@Override
	protected void postSubmit() {
		// submitTracked.setSchedule(schedule == null ? (new Schedule())
		// .getBytesScheduleTime() : schedule.getBytesScheduleTime());
		BkitPoma.startLoading(constants.BkitPoma_startLoading_loading());
		dbService.updateTracked(submitTracked,
				new AsyncCallback<ServiceResult<CTracked>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						MessageBox.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(ServiceResult<CTracked> result) {
						if (result.isOK()) {
							UserSettings.ctracked = result.getResult();
							MessageBox.alert("", constants
									.msb_updateProfile_success(),
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

	/**
	 * Load staff list in the database
	 */
	private void loadStaffData() {
		arrStaff.clear();
		
		dbService.getStaffsByTracked(UserSettings.ctracked.getUsername(),
				new AsyncCallback<ServiceResult<ArrayList<CStaff>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CStaff>> result) {
						if (result.isOK()) {
							for (int i = 0; i < result.getResult().size(); i++) {
								arrStaff.add(result.getResult().get(i)
										.getTrackerUN());
							}

							refreshStaffLabel();
						}
					}

				});
	}

	/**
	 * Load manage list in the database
	 */
	private void loadManageData() {
		arrManage.clear();
		dbService.getManagesByTracked(UserSettings.ctracked.getUsername(),
				new AsyncCallback<ServiceResult<ArrayList<CManage>>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<ArrayList<CManage>> result) {
						if (result.isOK()) {
							for (int i = 0; i < result.getResult().size(); i++) {
								arrManage.add(result.getResult().get(i)
										.getTrackerUN());
							}
							refreshManageLabel();
						}
					}

				});
	}

	/**
	 * Call whenever staff list change
	 */
	private void refreshStaffLabel() {
		
		if (arrStaff.size() == 0) {
			// fsShareTracked.remove(lblStaff.getId());
			lblStaff.setVisible(false);
		} else {
			lblStaff.setVisible(true);
			// if (fsShareTracked.getComponent(lblStaff.getId()) == null) {
			// fsShareTracked.add(lblStaff);
			// }
			
			String staffLabel = constants.staffLabel();
			for (int i = 0; i < arrStaff.size(); i++) {
				if (i == 0)
					staffLabel += " " + arrStaff.get(i);
				else
					staffLabel += ", " + arrStaff.get(i);
			}

			lblStaff.setText(staffLabel);
		}
	}

	/**
	 * Call whenever manage list change
	 */
	private void refreshManageLabel() {
		if (arrManage.size() == 0) {
			// lblManage.setVisible(false);
			// fsShareTracked.remove(lblManage.getId());
		} else {
			// if (fsShareTracked.getComponent(lblManage.getId()) == null) {
			// fsShareTracked.add(lblManage);
			// }
			String manageLabel = constants.manageLabel();
			for (int i = 0; i < arrManage.size(); i++) {
				if (i == 0)
					manageLabel += " " + arrManage.get(i);
				else
					manageLabel += ", " + arrManage.get(i);
			}

			lblManage.setText(manageLabel);
		}
	}

	/**
	 * Change data after user change staff list and manage list
	 * 
	 * @param arrManage
	 * @param arrStaff
	 */
	public void refresh(ArrayList<String> arrManage, ArrayList<String> arrStaff) {
		TrackedProfileForm.arrManage.clear();
		TrackedProfileForm.arrManage.addAll(arrManage);
		refreshManageLabel();

		TrackedProfileForm.arrStaff.clear();
		TrackedProfileForm.arrStaff.addAll(arrStaff);
		refreshStaffLabel();

	}

	/**
	 * Reset form, call base class with exception, call loadStaffData and
	 * loadManageData
	 */
	@Override
	public void postResetForm() {
		if (UserSettings.ctracked == null)
			return;

		boolean[] defaultScheduleBytes = new boolean[] { true, true, true,
				true, false, true, false, true, true, false, true, false, true,
				true, false, true, false, true, true, false, true, false, true,
				true };
		if (UserSettings.ctracked.getSchedule() == null) {
			scheduleWindow.getSubmitSchedule()
					.setSchedule(defaultScheduleBytes);
		} else {
			scheduleWindow.getSubmitSchedule().setSchedule(
					UserSettings.ctracked.getSchedule());
		}
		// scheduleWindow
		// .setSchedule(UserSettings.ctracked.getSchedule() == null ? new
		// Schedule()
		// : new Schedule(UserSettings.ctracked.getSchedule()));
		scheduleWindow.reset();

		refreshGridSchedule();
		loadStaffData();
		loadManageData();

		if (UserSettings.ctracked.isUnderManage()) {
			btnShare.setDisabled(false);
		} else
			btnShare.setDisabled(true);
	}
}

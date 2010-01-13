package com.bkitmobile.poma.admin.client.ui;

import java.util.HashMap;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.DefaultConfig;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CAdminConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

public class LoginAdminWindow extends Window {
	private String username;
	private String password;

	private final int TEXTFIELD_WIDTH = 200;
	private final int LABEL_WIDTH = 150;

	private TextField txtUsername;
	private TextField txtPassword;
	private Button btnLogin;
	private Button btnCancel;
	private TextField txtCurrentPassword;
	private TextField txtNewPassword;
	private TextField txtConfirmNewPassword;
	private FieldSet fsLogin;
	private FieldSet fsChangePassword;

	private boolean isLogin = true;

	private static LoginAdminWindow window = null;

	private DatabaseServiceAsync dbAsync = GWT.create(DatabaseService.class);

	public static LoginAdminWindow getInstance() {
		if (window != null)
			return window;
		else
			return new LoginAdminWindow();
	}

	public LoginAdminWindow() {
		super();
		window = this;
		setSize(430, 230);
		setResizable(false);
		setDraggable(false);
		setButtonAlign(Position.CENTER);
		setCloseAction(Window.HIDE);
		setTitle("Login into administrator page");

		dbAsync.getRecord("username",
				new AsyncCallback<ServiceResult<CAdminConfig>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ServiceResult<CAdminConfig> result) {
						if (result.isOK()) {
							if (result.getResult() == null) {
								HashMap<String, String> hashMapConfig = DefaultConfig
										.getHashMapConfig();
								username = hashMapConfig.get("username");
								dbAsync
										.setAllRecords(
												hashMapConfig,
												new AsyncCallback<ServiceResult<Boolean>>() {

													@Override
													public void onFailure(
															Throwable caught) {
														caught
																.printStackTrace();
													}

													@Override
													public void onSuccess(
															ServiceResult<Boolean> result) {
														if (!result.isOK()) {
															MessageBox
																	.alert(result
																			.getMessage());
														}else{
															getPassword();
														}
													}

												});
							} else {
								username = result.getResult().getValue();
								getPassword();
							}
							
						} else {
							
							MessageBox.alert(result.getMessage());
//							init();
//							layout();
						}
					}

				});
	}
	
	private void getPassword(){
		dbAsync
		.getRecord(
				"password",
				new AsyncCallback<ServiceResult<CAdminConfig>>() {

					@Override
					public void onFailure(
							Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<CAdminConfig> result) {
						if (result.isOK()) {
							password = result
									.getResult()
									.getValue();
							init();
							layout();
						} else {
							
						}
					}

				});
	}

	private void init() {
		txtUsername = new TextField("Username");
		txtUsername.setWidth(TEXTFIELD_WIDTH);

		txtPassword = new TextField("Password");
		txtPassword.setWidth(TEXTFIELD_WIDTH);
		txtPassword.setPassword(true);
		txtPassword.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				super.onSpecialKey(field, e);
				if (e.getCharCode() == 13) {
					adminLogin();
				}
			}
		});

		txtCurrentPassword = new TextField("Current Password");
		txtCurrentPassword.setWidth(TEXTFIELD_WIDTH);
		txtCurrentPassword.setPassword(true);

		txtNewPassword = new TextField("New password");
		txtNewPassword.setWidth(TEXTFIELD_WIDTH);
		txtNewPassword.setPassword(true);

		txtConfirmNewPassword = new TextField("Confirm new password");
		txtConfirmNewPassword.setWidth(TEXTFIELD_WIDTH);
		txtConfirmNewPassword.setPassword(true);
		txtConfirmNewPassword.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				super.onSpecialKey(field, e);
				if (e.getCharCode() == 13) {
					changePassword();
				}
			}
		});

		btnLogin = new Button("Login", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				if (isLogin) {
					adminLogin();
				} else {
					changePassword();
				}
			}
		});

		btnCancel = new Button("Cancel", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				hide();
			}
		});

	}

	private void adminLogin() {
		if (txtUsername.getText().equals(username)
				&& txtPassword.getText().equals(password)) {
			// Login successfully
			hide();
			new Viewport(new ManagerPanel());
		} else {
			txtUsername.selectText();
			MessageBox.alert("Login fail");
			return;
		}
	}

	private void changePassword() {
		if (txtCurrentPassword.getText().equals(password)) {
			if (txtNewPassword.getText()
					.equals(txtConfirmNewPassword.getText())) {
				dbAsync.addRecord("password", txtNewPassword.getText(),
						new AsyncCallback<ServiceResult<CAdminConfig>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ServiceResult<CAdminConfig> result) {
								if (result.isOK()) {
									txtCurrentPassword.setValue("");
									txtNewPassword.setValue("");
									txtConfirmNewPassword.setValue("");
									MessageBox
											.alert("Change password successfully");
									password = result.getResult().getValue();
								} else {
									MessageBox.alert("Change password fail");
								}
							}

						});
			} else {
				MessageBox
						.alert("New password and Confirm new password is not match");
			}
		} else {
			MessageBox.alert("Current password is not exact");
		}
	}

	private void layout() {
		
		fsLogin = new FieldSet();
		fsLogin.setTitle("Account information");
		fsLogin.setLabelWidth(LABEL_WIDTH);
		fsLogin.setCollapsible(false);
		fsLogin.setFrame(true);
		fsLogin.setPaddings(10, 10, 20, 10);

		fsLogin.add(txtUsername);
		fsLogin.add(txtPassword);

		fsChangePassword = new FieldSet();
		fsChangePassword.setLabelWidth(LABEL_WIDTH);
		fsChangePassword.setFrame(true);
		fsChangePassword.setPaddings(5);
		fsChangePassword.collapse();
		fsChangePassword.addListener(new PanelListenerAdapter() {
			@Override
			public void onExpand(Panel panel) {
				fsLogin.collapse();
				btnLogin.setText("Change");
				isLogin = false;
			}

			@Override
			public void onCollapse(Panel panel) {
				fsLogin.expand();
				btnLogin.setText("Login");
				isLogin = true;
			}
		});
		fsChangePassword.setTitle("Change password");
		fsChangePassword.setFrame(true);
		fsChangePassword.setCollapsible(true);
		fsChangePassword.collapse();
		fsChangePassword.setPaddings(10);

		fsChangePassword.add(txtCurrentPassword);
		fsChangePassword.add(txtNewPassword);
		fsChangePassword.add(txtConfirmNewPassword);

		this.add(fsLogin);
		this.add(fsChangePassword);

		this.addButton(btnLogin);
		this.addButton(btnCancel);

		Button btnReset = new Button("Reset", new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				dbAsync.addRecord("username", "admin",
						new AsyncCallback<ServiceResult<CAdminConfig>>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(
									ServiceResult<CAdminConfig> result) {
								if (result.isOK()) {
									username = result.getResult().getValue();
									dbAsync
											.addRecord(
													"password",
													"123456",
													new AsyncCallback<ServiceResult<CAdminConfig>>() {

														@Override
														public void onFailure(
																Throwable caught) {
															caught
																	.printStackTrace();
														}

														@Override
														public void onSuccess(
																ServiceResult<CAdminConfig> result) {
															if (result.isOK()) {
																MessageBox
																		.alert("Reset successfully");
																password = result
																		.getResult()
																		.getValue();
															} else {
																MessageBox
																		.alert("Reset fail");
															}
														}

													});
								} else {
									MessageBox.alert(result.getMessage());
								}
							}

						});

			}
		});
		// this.addButton(btnReset);

		show();
	}
}

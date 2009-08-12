package com.bkitmobile.poma.client.ui;

import java.util.Vector;

import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.localization.LLoginWindow;
import com.bkitmobile.poma.client.ui.openid.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.ComponentListener;
import com.gwtext.client.widgets.event.ComponentListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListener;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.FitLayout;

public class LoginWindow extends Window {
	private TextField txtUsername;
	private TextField txtPassword;
	private FieldSet fieldSetOpenID;
	private Vector<Provider> oidProviders;
	private Panel pnlURL;
	private Label lbl1stURL;
	private Label lbl2ndURL;
	private int valueChoose = 0;
	private Button btnSubmit;
	private DatabaseServiceAsync dbService;
	private LLoginWindow local;

	private FormPanel pnlMain;

	public LoginWindow() {
		dbService = DatabaseService.Util.getInstance();
		local = GWT.create(LLoginWindow.class);
		this.setTitle(local.window_title());
		this.setClosable(true);
		this.setSize(400, 200);
		this.setPlain(true);
		this.setLayout(new FitLayout());
		this.setCloseAction(Window.HIDE);

		txtUsername = new TextField();
		txtUsername.setAllowBlank(false);
		txtUsername.setEmptyText(local.txtUsername_setEmptyText());
		txtUsername.setMaxLength(16);

		txtPassword = new TextField(local.lblPassword());
		txtPassword.setPassword(true);
		txtPassword.setAllowBlank(false);
		txtPassword.setEmptyText(local.txtPassword_setEmptyText());
		txtPassword.setMaxLength(16);

		lbl1stURL = new Label();
		lbl2ndURL = new Label();

		pnlURL = new Panel();
		pnlURL.add(lbl1stURL);
		pnlURL.add(txtUsername);
		pnlURL.add(lbl2ndURL);

		fieldSetOpenID = new FieldSet("OpenID");
		fieldSetOpenID.setCheckboxToggle(true);
		fieldSetOpenID.setCollapsed(false);
		fieldSetOpenID.setFrame(true);
		fieldSetOpenID.addListener(new PanelListenerAdapter() {
			@Override
			public void onCollapse(Panel panel) {
				// TODO Auto-generated method stub
				super.onCollapse(panel);
				txtPassword.setDisabled(false);
				txtPassword.setValidateOnBlur(false);
				lbl1stURL.setText(local.lbl1stURL_username());
			}

			@Override
			public void onExpand(Panel panel) {
				// TODO Auto-generated method stub
				super.onExpand(panel);
				txtPassword.setDisabled(true);
				txtPassword.setValidateOnBlur(false);
				lbl1stURL.setText("");

			}
		});

		oidProviders = new Vector<Provider>();
		oidProviders.add(new GoogleProvider());
		oidProviders.add(new YahooProvider());
		oidProviders.add(new AOLProvider());
		oidProviders.add(new MyOpenIDProvider());
		
		oidProviders.add(new BloggerProvider());
		oidProviders.add(new ClaimIDProvider());
		oidProviders.add(new FlickrProvider());
		oidProviders.add(new LiveJournalProvider());
		oidProviders.add(new TechnoratiProvider());
		oidProviders.add(new VerisignProvider());
		oidProviders.add(new VidoopProvider());
		oidProviders.add(new WordpressProvider());
		oidProviders.add(new OpenIDProvider());

		for (final Provider provider : oidProviders) {
			provider.getLogo().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					// TODO Auto-generated method stub
					valueChoose = 12;
					lbl1stURL.setText(provider.getFirstURL());
					lbl2ndURL.setText(provider.getLastURL());
					txtPassword.setValue("");
					txtPassword.setDisabled(true);
					txtPassword.setValidateOnBlur(true);
				}
			});

			fieldSetOpenID.add(provider.getLogo());
		}

		pnlMain = new FormPanel();
		pnlMain.add(pnlURL);
		pnlMain.add(txtPassword);
		pnlMain.add(fieldSetOpenID);

		btnSubmit = new Button(local.btnSubmit_text(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						super.onClick(button, e);
						if (!txtUsername.isValid()) {
							MessageBox.alert(local.msgbox_title_err(), local
									.msgbox_text_user_invalid());
							return;
						}
						if (!txtPassword.isValid()) {
							MessageBox.alert(local.msgbox_title_err(), local
									.msgbox_text_pass_invalid());
							return;
						}

						dbService.loginTracker(txtUsername.getText(),
								txtPassword.getText(),
								new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable arg0) {
										// TODO Auto-generated method stub
										MessageBox.alert(local
												.msgbox_title_err(), local
												.msgbox_text_cannotlogin());
									}

									@Override
									public void onSuccess(Boolean arg0) {
										if (arg0) {
											System.out.println("Login Success");
										} else {
											MessageBox
													.alert(
															local
																	.msgbox_title_incor(),
															local
																	.msgbox_text_userpass_invalid());
										}

									}

								});
					}
				});

		this.add(pnlMain);
		pnlMain.add(btnSubmit);
	}
}

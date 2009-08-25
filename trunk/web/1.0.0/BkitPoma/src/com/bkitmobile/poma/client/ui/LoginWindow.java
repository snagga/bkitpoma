package com.bkitmobile.poma.client.ui;

import java.util.Vector;

import com.bkitmobile.poma.client.BkitPoma;
import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.database.ServiceResult;
import com.bkitmobile.poma.client.database.entity.CTracker;
import com.bkitmobile.poma.client.localization.LLoginWindow;
import com.bkitmobile.poma.client.mail.TrackerService;
import com.bkitmobile.poma.client.mail.TrackerServiceAsync;
import com.bkitmobile.poma.client.openid.provider.AOLProvider;
import com.bkitmobile.poma.client.openid.provider.BloggerProvider;
import com.bkitmobile.poma.client.openid.provider.ClaimIDProvider;
import com.bkitmobile.poma.client.openid.provider.FlickrProvider;
import com.bkitmobile.poma.client.openid.provider.GoogleProvider;
import com.bkitmobile.poma.client.openid.provider.LiveJournalProvider;
import com.bkitmobile.poma.client.openid.provider.MyOpenIDProvider;
import com.bkitmobile.poma.client.openid.provider.OpenIDProvider;
import com.bkitmobile.poma.client.openid.provider.Provider;
import com.bkitmobile.poma.client.openid.provider.TechnoratiProvider;
import com.bkitmobile.poma.client.openid.provider.VerisignProvider;
import com.bkitmobile.poma.client.openid.provider.VidoopProvider;
import com.bkitmobile.poma.client.openid.provider.WordpressProvider;
import com.bkitmobile.poma.client.openid.provider.YahooProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.ContainerListener;
import com.gwtext.client.widgets.event.ContainerListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class LoginWindow extends Window {
	private TextField txtPomaUsername;
	private TextField txtOpenIdUsername;
	private TextField txtPassword;
	private FieldSet fsOpenID;
	private Vector<Provider> oidProviders;
	private Label lblFirstUrl;
	private Label lblSecondUrl;
	private Button btnSubmit;
	private LLoginWindow local;
	private Provider preProvider;
	private FieldSet pomaPanel;
	private Panel openIdPanel;
	private boolean isPomaLogin = true;
	private FormPanel mainPanel;
	private Button btnRegisterTracker;
	private Button btnForgotPass;
	private DatabaseServiceAsync dbService;
	private TrackerServiceAsync trackerService;

	public static LoginWindow loginWindow = null;
	
	public static LoginWindow getInstance() {
		if (loginWindow != null) 
			return loginWindow;
		else
			return new LoginWindow();
	}

	public LoginWindow() {
		loginWindow = this;
		init();
		layout();
	}

	/**
	 * Initialize variables
	 */
	private void init() {
		/*
		 * Database service and localization
		 */
		dbService = DatabaseService.Util.getInstance();
		trackerService = null;// TrackerService.Util.getInstance();
		local = GWT.create(LLoginWindow.class);

		/*
		 * Username
		 */
		txtPomaUsername = new TextField();
		txtPomaUsername.setAllowBlank(false);
		txtPomaUsername.setEmptyText(local.txtUsername_setEmptyText());
		txtPomaUsername.setMaxLength(16);
		txtPomaUsername.setSelectOnFocus(true);
		txtPomaUsername.focus();

		/*
		 * OpenId username
		 */
		txtOpenIdUsername = new TextField();
		// txtOpenIdUsername.setEmptyText("OpenID username");
		txtOpenIdUsername.setAllowBlank(false);
		// txtOpenIdUsername.setMaxLength(16);

		/*
		 * Password
		 */
		txtPassword = new TextField(local.lblPassword());
		txtPassword.setPassword(true);
		// txtPassword.setEmptyText(local.txtPassword_setEmptyText());
		txtPassword.setMaxLength(16);
		txtPassword.setAllowBlank(false);
		txtPassword.addListener(new TextFieldListenerAdapter() {
			@Override
			public void onSpecialKey(Field field, EventObject e) {
				// TODO Auto-generated method stub
				super.onSpecialKey(field, e);
				// Catch ENTER event
				if (e.getCharCode() == 13) {
					// System.out.println("charcode: " + e.getCharCode());
					 System.out.println("getKey: " + e.getKey());
					login();
				}
			}
		});
		/*
		 * Labels
		 */
		lblFirstUrl = new Label();
		// lblFirstUrl.setWidth(width)
		lblSecondUrl = new Label();

		/*
		 * OpenId field set
		 */
		fsOpenID = new FieldSet("OpenID");
		fsOpenID.setFrame(true);

		// OpenId providers
		oidProviders = new Vector<Provider>();
		oidProviders.add(new GoogleProvider());
		oidProviders.add(new YahooProvider());
		oidProviders.add(new AOLProvider());
		oidProviders.add(new MyOpenIDProvider());
		oidProviders.add(new FlickrProvider());

		oidProviders.add(new BloggerProvider());
		oidProviders.add(new ClaimIDProvider());
		oidProviders.add(new LiveJournalProvider());
		oidProviders.add(new TechnoratiProvider());
		oidProviders.add(new VerisignProvider());
		oidProviders.add(new VidoopProvider());
		oidProviders.add(new WordpressProvider());
		oidProviders.add(new OpenIDProvider());

		for (final Provider provider : oidProviders) {
			Image img = provider.getLogo();
			fsOpenID.add(provider.getLogo());
			img.setStyleName("openid");
			img.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent mouseOutEvent) {
					if (provider != preProvider) {
						provider.getLogo().removeStyleName("openid-hover");
					}
				}
			});

			img.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent mouseOverEvent) {
					if (provider != preProvider) {
						provider.getLogo().addStyleName("openid-hover");
					}
				}
			});

			img.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent clickEvent) {

					if (preProvider != null) {
						preProvider.getLogo().removeStyleName("openid-select");
						preProvider.getLogo().removeStyleName("openid-hover");
					}
					preProvider = provider;
					provider.getLogo().addStyleName("openid-select");

					lblFirstUrl.setText(provider.getFirstURL());
					lblSecondUrl.setText(provider.getLastURL());

					if (provider.requireUsernameInUrl()) {
						txtOpenIdUsername.show();
						txtOpenIdUsername.focus();
						txtOpenIdUsername.selectText();
					} else {
						txtOpenIdUsername.hide();
					}
				}
			});
		}

		/*
		 * Submit button
		 */
		btnSubmit = new Button("Login");
		btnSubmit.addListener(new ButtonSubmitListenerAdapter());

		btnRegisterTracker = new Button("Register");
		btnRegisterTracker.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO forward to register tracker panel

			}
		});

		/*
		 * Forgot password button
		 */
		btnForgotPass = new Button("Forgot password");
		btnForgotPass.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (txtPomaUsername.getText().equals("")) {
					MessageBox.alert("Please enter username");
					return;
				}
				MessageBox.alert("Please check your email ");
				trackerService.validateEmailForgotPassword(txtPomaUsername
						.getText(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert(caught.getMessage());
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							MessageBox
									.alert("Send emial to tracker user successfully");
						} else {
							MessageBox.alert("Send emial to tracker user fail");
						}
					}

				});
			}
		});
	}

	/**
	 * Initialize UI
	 */
	private void layout() {
		/*
		 * Setting for the main panel
		 */
		setTitle(local.window_title());
		setClosable(true);
		setWidth(500);
		setResizable(false);
		setPlain(true);
		setButtonAlign(Position.CENTER);
		setCloseAction(Window.HIDE);
		setModal(true);
		setDraggable(false);

		/*
		 * Poma login panel
		 */
		pomaPanel = new FieldSet();
		pomaPanel.setTitle("Poma login");
		pomaPanel.setLabelWidth(100);
		pomaPanel.setCollapsible(false);
		pomaPanel.setFrame(true);
		pomaPanel.setPaddings(10, 10, 20, 10);
		txtPomaUsername.setFieldLabel("Poma username");
		txtPassword.setFieldLabel("Password");
		pomaPanel.add(txtPomaUsername);
		pomaPanel.add(txtPassword);

		/*
		 * OpenId login panel
		 */
		openIdPanel = new Panel();
		openIdPanel.setLayout(new VerticalLayout(5));
		openIdPanel.setFrame(true);
		openIdPanel.setPaddings(5);
		openIdPanel.collapse();
		openIdPanel.addListener(new PanelListenerAdapter() {
			@Override
			public void onExpand(Panel panel) {
				pomaPanel.collapse();
				isPomaLogin = false;
				if (preProvider != null && preProvider.requireUsernameInUrl())
					txtOpenIdUsername.show();
			}

			@Override
			public void onCollapse(Panel panel) {
				pomaPanel.expand();
				isPomaLogin = true;
			}
		});
		openIdPanel.setTitle("OpenId login");
		openIdPanel.setFrame(true);
		openIdPanel.setCollapsible(true);
		openIdPanel.collapse();
		openIdPanel.setPaddings(10);

		// panel openid user name
		Panel temp = new Panel();
		temp.setLayout(new HorizontalLayout(5));
		temp.add(new Label("OpenID:   "));
		temp.add(lblFirstUrl);
		temp.add(txtOpenIdUsername);
		temp.add(lblSecondUrl);

		openIdPanel.add(temp);
		openIdPanel.add(fsOpenID);

		/*
		 * The main panel
		 */
		mainPanel = new FormPanel();
		mainPanel.setPaddings(5);
		mainPanel.setButtonAlign(Position.CENTER);
		mainPanel.add(pomaPanel);
		mainPanel.add(openIdPanel);
		mainPanel.addButton(btnSubmit);
		mainPanel.addButton(btnRegisterTracker);
		mainPanel.addButton(btnForgotPass);

		/*
		 * Add to this window
		 */
		this.add(mainPanel);
	}

	/**
	 * Login to system by OpenID user
	 * 
	 * @param provider
	 *            <code>provider</code> contains info about OpenID provider whom
	 *            user belong to
	 */
	private void openIdLogin(Provider provider) {
		Location.replace("/login.jsp?openid="
				+ provider.getURL(txtOpenIdUsername.getText()));
	}

	/**
	 * Login to system by POMA user
	 */
	private void pomaLogin() {
		dbService.loginTracker(txtPomaUsername.getText(),
				txtPassword.getText(),
				new AsyncCallback<ServiceResult<CTracker>>() {
					@Override
					public void onFailure(Throwable caught) {
						MessageBox.alert(local.msgbox_title_err(), local
								.msgbox_text_cannotlogin());
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result.isOK()) {
							BkitPoma.loginSuccessfully();
						} else
							MessageBox.alert(local.msgbox_title_incor(), local
									.msgbox_text_userpass_invalid());
					}
				});
	}

	/**
	 * Determine login type and do login
	 * 
	 * @author Hieu Rocker
	 */
	class ButtonSubmitListenerAdapter extends ButtonListenerAdapter {
		@Override
		public void onClick(Button button, EventObject e) {
			super.onClick(button, e);
			login();
		}
	}

	private void login() {
		this.setVisible(false);
		if (isPomaLogin) {
			// POMA user login mode
			if (!txtPomaUsername.isValid()) {
				MessageBox.alert(local.msgbox_title_err(), local
						.msgbox_text_user_invalid());
			}
			if (!txtPassword.isValid()) {
				MessageBox.alert(local.msgbox_title_err(), local
						.msgbox_text_pass_invalid());
			}
			pomaLogin();
			return;
		}
		// OpenID login mode
		openIdLogin(preProvider);
	}
}

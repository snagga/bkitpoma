package com.bkitmobile.poma.ui.client;

import java.util.Vector;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.home.client.BkitPoma;
import com.bkitmobile.poma.localization.client.LLoginWindow;
import com.bkitmobile.poma.mail.client.TrackerService;
import com.bkitmobile.poma.mail.client.TrackerServiceAsync;
import com.bkitmobile.poma.openid.client.provider.AOLProvider;
import com.bkitmobile.poma.openid.client.provider.BloggerProvider;
import com.bkitmobile.poma.openid.client.provider.ClaimIDProvider;
import com.bkitmobile.poma.openid.client.provider.FlickrProvider;
import com.bkitmobile.poma.openid.client.provider.GoogleProvider;
import com.bkitmobile.poma.openid.client.provider.LiveJournalProvider;
import com.bkitmobile.poma.openid.client.provider.MyOpenIDProvider;
import com.bkitmobile.poma.openid.client.provider.OpenIDProvider;
import com.bkitmobile.poma.openid.client.provider.Provider;
import com.bkitmobile.poma.openid.client.provider.TechnoratiProvider;
import com.bkitmobile.poma.openid.client.provider.VerisignProvider;
import com.bkitmobile.poma.openid.client.provider.VidoopProvider;
import com.bkitmobile.poma.openid.client.provider.WordpressProvider;
import com.bkitmobile.poma.openid.client.provider.YahooProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Checkbox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.CheckboxListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.HorizontalLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class LoginWindow extends Window {
	private TextField txtPomaUsername;
	private TextField txtOpenIdUsername;
	private TextField txtPassword;
	private Vector<Provider> oidProviders;
	private Label lblFirstUrl;
	private Label lblSecondUrl;
	private Button btnSubmit;
	private LLoginWindow local;
	private Provider preProvider;
	private FieldSet fsPoma;
	private FieldSet fsOpenID;
	private Panel openIdPanel;
	private boolean isPomaLogin = true;
	private FormPanel mainPanel;
	private Button btnRegisterTracker;
	private Button btnForgotPass;
	private DatabaseServiceAsync dbService;
	private TrackerServiceAsync trackerService;

	private Checkbox cbRemember1;
	private Checkbox cbRemember2;

	public static LoginWindow loginWindow = null;

	public static LoginWindow getInstance() {
		if (loginWindow != null)
			return loginWindow;
		else
			return new LoginWindow();
	}

	private LoginWindow() {
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
		trackerService = GWT.create(TrackerService.class);
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
				super.onSpecialKey(field, e);
				// Catch ENTER event
				if (e.getCharCode() == 13) {
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
		btnSubmit = new Button(local.btnLogin());
		btnSubmit.addListener(new ButtonSubmitListenerAdapter());

		btnRegisterTracker = new Button(local.btnRegister());
		btnRegisterTracker.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				// TODO forward to register tracker panel
				hide();
				BkitPoma.displayItemID(RegisterTrackedForm.getInstance().getId());
			}
		});

		/*
		 * Forgot password button
		 */
		btnForgotPass = new Button(local.btnForgotPass());
		btnForgotPass.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (txtPomaUsername.getText().equals("")) {
					MessageBox.alert(local.usernameFill());
					return;
				}
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
							MessageBox.alert(local.sendMailSuccess());
						} else {
							MessageBox.alert(local.sendMailFail());
						}
					}

				});
			}
		});

		cbRemember1 = new Checkbox("Remember");
		cbRemember1.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
			}
		});

		cbRemember2 = new Checkbox("Remember");
		cbRemember2.addListener(new CheckboxListenerAdapter() {
			@Override
			public void onCheck(Checkbox field, boolean checked) {
			}
		});

		this.addListener(new WindowListenerAdapter() {
			@Override
			public void onClose(Panel panel) {
				LoginWindow.this.setVisible(false);
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
		setWidth(550);
		setResizable(false);
		setPlain(true);
		setButtonAlign(Position.CENTER);
		setCloseAction(Window.HIDE);
		setModal(true);
		setDraggable(false);
		// setAutoWidth(true);

		/*
		 * Poma login panel
		 */
		fsPoma = new FieldSet();
		fsPoma.setTitle(local.pomaLogin());
		fsPoma.setLabelWidth(100);
		fsPoma.setCollapsible(false);
		fsPoma.setFrame(true);
		fsPoma.setPaddings(10, 10, 20, 10);
		txtPomaUsername.setFieldLabel(local.pomaUsername());
		txtPassword.setFieldLabel(local.pomaPass());
		fsPoma.add(txtPomaUsername);
		fsPoma.add(txtPassword);
		fsPoma.add(cbRemember1);

		fsPoma
				.add(new HTML(
						"<a href=\"#\" "
								+ "onclick=\"FB.Connect.requireSession(reload); return false;\" > "
								+ "<img id=\"fb_login_image\" "
								+ "src=\"http://static.ak.fbcdn.net/images/fbconnect/login-buttons/connect_light_medium_long.gif\" "
								+ "alt=\"Connect\"/> </a>"));

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
				fsPoma.collapse();
				isPomaLogin = false;
				if (preProvider != null && preProvider.requireUsernameInUrl())
					txtOpenIdUsername.show();
			}

			@Override
			public void onCollapse(Panel panel) {
				fsPoma.expand();
				isPomaLogin = true;
			}
		});
		openIdPanel.setTitle(local.openIdLogin());
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
		openIdPanel.add(cbRemember2);

		/*
		 * The main panel
		 */
		mainPanel = new FormPanel();
		mainPanel.setPaddings(5);
		mainPanel.setButtonAlign(Position.CENTER);
		mainPanel.add(fsPoma);
		mainPanel.add(openIdPanel);
		mainPanel.addButton(btnSubmit);
		mainPanel.addButton(btnRegisterTracker);
		mainPanel.addButton(btnForgotPass);

		/*
		 * Add to this window
		 */
		this.add(mainPanel);
	}

	private native void FacebookConnectLogin() /*-{
		FB.Connect.requireSession(reload);
	}-*/;

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
						// TODO
						BkitPoma.stopLoading();
					}

					@Override
					public void onSuccess(ServiceResult<CTracker> result) {
						if (result.isOK()) {
							BkitPoma.reloadPage();
						} else {
							MessageBox.alert(local.msgbox_title_incor(), local
									.msgbox_text_userpass_invalid());
							BkitPoma.stopLoading();
						}
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
		BkitPoma.startLoading(local.pomaLoginMessage());
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
	
	@Override
	public void show() {
		txtPomaUsername.clearInvalid();
		txtPomaUsername.setValue("");
		
		super.show();
	}
}

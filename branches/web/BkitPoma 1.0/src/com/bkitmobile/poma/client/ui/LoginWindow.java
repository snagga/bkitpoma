package com.bkitmobile.poma.client.ui;

import java.util.Vector;

import com.bkitmobile.poma.client.UserSettings;
import com.bkitmobile.poma.client.database.DatabaseService;
import com.bkitmobile.poma.client.database.DatabaseServiceAsync;
import com.bkitmobile.poma.client.localization.LLoginWindow;
import com.bkitmobile.poma.client.openid.provider.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
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
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class LoginWindow extends Window {
	private TextField txtUsername;
	private TextField txtPassword;
	private FieldSet fsOpenID;
	private Vector<Provider> oidProviders;
	private Panel pnlURL;
	private Label lbl1stURL;
	private Label lbl2ndURL;
	private Button btnSubmit;
	private DatabaseServiceAsync dbService;
	private LLoginWindow local;
	private Provider preProvider;

	private FormPanel pnlMain;

	public LoginWindow() {
		dbService = DatabaseService.Util.getInstance();
		local = GWT.create(LLoginWindow.class);
		this.setTitle(local.window_title());
		this.setClosable(true);
		this.setSize(400, 200);
		// this.setResizable(false);
		this.setPaddings(5);
		this.setPlain(true);
		this.setLayout(new FitLayout());
		this.setButtonAlign(Position.CENTER);
		this.setCloseAction(Window.CLOSE);

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

		fsOpenID = new FieldSet("OpenID");
		fsOpenID.setCheckboxToggle(true);
		fsOpenID.setCollapsed(true);
		fsOpenID.setFrame(true);
		fsOpenID.addListener(new PanelListenerAdapter() {
			@Override
			public void onCollapse(Panel panel) {
				txtPassword.setDisabled(false);
				txtPassword.setValidateOnBlur(false);
				lbl1stURL.setText(local.lbl1stURL_username());
			}

			@Override
			public void onExpand(Panel panel) {
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
			img.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent arg0) {
					if (preProvider != null) {
						preProvider.getLogo().removeStyleName("openid-select");
						preProvider.getLogo().removeStyleName("openid-hover");
					}
					preProvider = provider;
					provider.getLogo().addStyleName("openid-select");
					lbl1stURL.setText(provider.getFirstURL());
					lbl2ndURL.setText(provider.getLastURL());
					txtPassword.setValue("");
					txtPassword.setDisabled(true);
					txtPassword.setValidateOnBlur(true);
					txtUsername.focus();
					txtUsername.selectText();
				}
			});

			fsOpenID.add(provider.getLogo());
			img.setStyleName("openid");
			// img.addStyleName("openid:hover");
			img.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent arg0) {
					if (provider != preProvider) {
						provider.getLogo().removeStyleName("openid-hover");
					}
				}
			});

			img.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent arg0) {
					if (provider != preProvider) {
						provider.getLogo().addStyleName("openid-hover");
					}
				}
			});
		}

		pnlURL = new Panel();
		pnlURL.add(lbl1stURL);
		pnlURL.add(txtUsername);
		pnlURL.add(lbl2ndURL);

		pnlMain = new FormPanel();
		pnlMain.add(pnlURL, new AnchorLayoutData("100%"));
		pnlMain.add(txtPassword);
		pnlMain.add(fsOpenID, new AnchorLayoutData("100%"));

		btnSubmit = new Button(local.btnSubmit_text());
		this.add(pnlMain, new AnchorLayoutData("100% -53"));
		this.addButton(btnSubmit);

		btnSubmit.addListener(new ButtonSubmitListenerAdapter());
	}

	private void openIDLogin(Provider provider) {
		Window win = new Window();
		win.addListener(new WindowListenerAdapter() {
			@Override
			public void onClose(Panel panel) {
				// TODO Auto-generated method stub
			}
		});
		win.setSize(400, 400);
		Frame f = new Frame("login.jsp?openid=" + provider.getURL(txtUsername.getText()));
		f.setSize("100%", "100%");
		win.add(f);
		win.setAutoScroll(true);
		win.setMaximizable(true);
		win.show();
	}

	private void pomaLogin() {
		dbService.loginTracker(txtUsername.getText(), txtPassword.getText(),
				new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						MessageBox.alert(local.msgbox_title_err(), local
								.msgbox_text_cannotlogin());
					}

					@Override
					public void onSuccess(Boolean arg0) {
						if (arg0) {
							System.out.println("Login Success");
						} else {
							MessageBox.alert(local.msgbox_title_incor(), local
									.msgbox_text_userpass_invalid());
						}

					}
				});
	}

	class ButtonSubmitListenerAdapter extends ButtonListenerAdapter {
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
			if (fsOpenID.isCollapsed()) {
				pomaLogin();
			} else {
				openIDLogin(preProvider);
			}
		}
	}
}

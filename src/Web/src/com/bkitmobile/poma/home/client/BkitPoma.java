package com.bkitmobile.poma.home.client;

import java.util.HashMap;

import javax.servlet.http.Cookie;

import com.bkitmobile.poma.database.client.DatabaseService;
import com.bkitmobile.poma.database.client.DatabaseServiceAsync;
import com.bkitmobile.poma.database.client.DefaultConfig;
import com.bkitmobile.poma.database.client.ServiceResult;
import com.bkitmobile.poma.database.client.entity.CTracker;
import com.bkitmobile.poma.localization.client.BkitPomaConstants;
import com.bkitmobile.poma.mail.client.TrackerService;
import com.bkitmobile.poma.mail.client.TrackerServiceAsync;
import com.bkitmobile.poma.ui.client.AuthorWindow;
import com.bkitmobile.poma.ui.client.ContactWindow;
import com.bkitmobile.poma.ui.client.DownLoadDeviceWindow;
import com.bkitmobile.poma.ui.client.IntroductionWindow;
import com.bkitmobile.poma.ui.client.LoadingPanel;
import com.bkitmobile.poma.ui.client.LoginWindow;
import com.bkitmobile.poma.ui.client.MapPanel;
import com.bkitmobile.poma.ui.client.MenuPanel;
import com.bkitmobile.poma.ui.client.PublishMapWindow;
import com.bkitmobile.poma.ui.client.RegisterTrackedForm;
import com.bkitmobile.poma.ui.client.RegisterTrackerForm;
import com.bkitmobile.poma.ui.client.RegisterTrackerOpenID;
import com.bkitmobile.poma.ui.client.TrackedPanel;
import com.bkitmobile.poma.ui.client.TrackedProfileForm;
import com.bkitmobile.poma.ui.client.TrackerProfileForm;
import com.bkitmobile.poma.util.client.Task;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.Margins;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarTextItem;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.CardLayout;
import com.gwtext.client.widgets.layout.VerticalLayout;

public class BkitPoma implements EntryPoint {

	private static MapPanel mapPanel;
	private MenuPanel menuPanel;
	private static LoadingPanel mainPanel;
	private RegisterTrackerForm registerTrackerForm;
	private static RegisterTrackedForm registerTrackedForm;
	private TrackerProfileForm trackerProfileForm;
	private static TrackedProfileForm trackedProfileForm;
	private static RegisterTrackerOpenID registerTrackerOpenID;
	
	private Panel pnlHelp;

	private static Panel cardPanel;

	private ToolbarButton btnVie;
	private ToolbarButton btnLogin;
	private ToolbarButton btnEng;
	private ToolbarButton btnNewTracker;
	private ToolbarButton btnEmail;
	private ToolbarButton btnProfile;
	private ToolbarButton btnAuthor;
	private ToolbarButton btnDownloadDevice;
	private ToolbarButton btnPublish;
	private ToolbarTextItem txtItemTracker;
	
	private ToolbarButton btnHelp;
	private static Toolbar toolbar;

	private DownLoadDeviceWindow wdDownloadDevice;

	// localization
	private static BkitPomaConstants constants = GWT
			.create(BkitPomaConstants.class);
	// database serive
	private DatabaseServiceAsync dbService = DatabaseService.Util
			.getInstance();
	protected TrackerServiceAsync trackerService = TrackerService.Util
			.getInstance();
	// check tracker login or not
	private static boolean isLogin = false;

	private static int mapWidth;
	private static int mapHeight;
	private int toolbarHeight = 36;

	public void onModuleLoad() {
		
		dbService
				.getAllRecords(new AsyncCallback<ServiceResult<HashMap<String, String>>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(
							ServiceResult<HashMap<String, String>> result) {
						if (result.isOK()) {
							UserSettings.hashMapConfig = result.getResult();
							if (UserSettings.hashMapConfig.size() == 0) {
								UserSettings.hashMapConfig.putAll(DefaultConfig
										.getHashMapConfig());
								dbService
										.setAllRecords(
												UserSettings.hashMapConfig,
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
														}
													}

												});
							}
						} else {
							MessageBox.alert(result.getMessage());
						}
						initForm();
						// Determine tracker login or not?
						isLogin();
					}
				});
	}

	private void initForm() {
		
		wdDownloadDevice = new DownLoadDeviceWindow();
		Window
				.setTitle(UserSettings.hashMapConfig.get("title") == null ? "POMA - Position Manager"
						: UserSettings.hashMapConfig.get("title"));
		/*
		 * Main panel
		 */
		mainPanel = new LoadingPanel();
		mainPanel.setLayout(new BorderLayout());

		/*
		 * Toolbar of main panel
		 */
		toolbar = new Toolbar();
		toolbar.setHeight(toolbarHeight);

		// Vietnamese mode
		btnVie = new ToolbarButton();
		btnVie.setIcon("/images/flags/vn.ico");
		ToolTip vieTip = new ToolTip(constants.vieTip());
		vieTip.applyTo(btnVie);
		btnVie.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				if (Cookies.getCookie("tracker_language") != null
						&& Cookies.getCookie("tracker_language").equals("vi"))
					return;
				Cookies.setCookie("tracker_language", "vi");
				if (!isLogin || UserSettings.ctracker == null
						|| UserSettings.ctracker.getUsername() == null) {
					reloadPage();
					return;
				}
				CTracker ctracker = new CTracker();
				ctracker.setUsername(UserSettings.ctracker.getUsername());
				ctracker.setLang("vi");
				dbService.updateTracker(ctracker,
						new AsyncCallback<ServiceResult<CTracker>>() {
							@Override
							public void onFailure(Throwable caught) {
								reloadPage();
							}

							@Override
							public void onSuccess(ServiceResult<CTracker> result) {
								reloadPage();
							}
						});
			}
		});

		// English mode
		btnEng = new ToolbarButton();
		ToolTip enTip = new ToolTip(constants.enTip());
		enTip.applyTo(btnEng);
		btnEng.setIcon("/images/flags/en.ico");
		btnEng.addListener(new ButtonListenerAdapter() {

			@Override
			public void onClick(Button button, EventObject e) {
				if (Cookies.getCookie("tracker_language") != null
						&& Cookies.getCookie("tracker_language").equals("en"))
					return;
				Cookies.setCookie("tracker_language", "en");
				if (!isLogin || UserSettings.ctracker == null
						|| UserSettings.ctracker.getUsername() == null) {
					reloadPage();
					return;
				}
				CTracker ctracker = new CTracker();
				ctracker.setUsername(UserSettings.ctracker.getUsername());
				ctracker.setLang("en");
				dbService.updateTracker(ctracker,
						new AsyncCallback<ServiceResult<CTracker>>() {
							@Override
							public void onFailure(Throwable caught) {
								reloadPage();
							}

							@Override
							public void onSuccess(ServiceResult<CTracker> result) {
								reloadPage();
							}
						});
			}
		});

		// Login button
		btnLogin = new ToolbarButton(constants.loginButton());

		btnLogin.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				btnLogin_clicked();
			}
		});

		// Poma logo
		HTML logo = new HTML("<img src='images/poma/logo-header.gif' />");

		// Button register new tracker
		btnNewTracker = new ToolbarButton(constants.registerButton(),
				new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {
						cardPanel.setActiveItemID(registerTrackerForm.getId());
					}

				});

		// Button email to admin
		btnEmail = new ToolbarButton(constants.contactButton());
		btnEmail.setIcon("/images/silk/email.png");
		btnEmail.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				ContactWindow.getInstance().show();
			}
		});

		// txtItemTracker
		txtItemTracker = new ToolbarTextItem("");
		ToolTip usernameTip = new ToolTip(constants.usernameTip());
		usernameTip.applyTo(txtItemTracker.getElement());

		// Button author
		btnAuthor = new ToolbarButton(constants.author(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						AuthorWindow.getInstance().show();
					}
				});

		btnDownloadDevice = new ToolbarButton("Download",
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						wdDownloadDevice.show();
					}
				});

		btnDownloadDevice.setIcon("/images/mobile.png");
		
		btnPublish = new ToolbarButton("Publish",new ButtonListenerAdapter(){
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				
				PublishMapWindow window = PublishMapWindow.getInstance();
				window.show();
			}
		});
		btnPublish.setTooltip("Publish embeded map");
		btnPublish.setIcon("/images/flash.png");
		
		// Button tracker's profile
		btnProfile = new ToolbarButton(constants.profileButton(),
				new ButtonListenerAdapter() {
					@Override
					public void onClick(Button button, EventObject e) {
						cardPanel.setActiveItemID(trackerProfileForm.getId());
						trackerProfileForm.resetForm();
					}
				});
		ToolTip profileTip = new ToolTip(constants.profileTip());
		profileTip.applyTo(btnProfile);
		
		/********************************/
		btnHelp = new ToolbarButton(constants.btnHelp(),new ButtonListenerAdapter(){
			@Override
			public void onClick(Button button, EventObject e) {
				super.onClick(button, e);
				cardPanel.setActiveItemID(pnlHelp.getId());
			}
		});
		btnHelp.setIcon("/images/help.png");

		initHelpPanel();
		/*******************/
		
		// Add components to toolbar
		toolbar.addElement(logo.getElement());
		toolbar.addElement(new HTML(
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
				.getElement());

		toolbar.addButton(btnEmail);
		toolbar.addButton(btnAuthor);
		toolbar.addButton(btnDownloadDevice);
		toolbar.addButton(btnPublish);
		toolbar.addButton(btnHelp);
		
		toolbar.addFill();
		toolbar.addItem(txtItemTracker);
		toolbar.addButton(btnProfile);
		toolbar.addButton(btnNewTracker);
		toolbar.addButton(btnLogin);
		toolbar.addElement(new HTML(
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
				.getElement());
		toolbar.addButton(btnVie);
		toolbar.addButton(btnEng);

		/*
		 * Menu
		 */
		menuPanel = MenuPanel.getInstance();
		menuPanel.setBorder(false);
		menuPanel.setBodyBorder(false);

		/*
		 * Register new tracker form
		 */
		registerTrackerForm = new RegisterTrackerForm();

		/*
		 * Register new tracked form
		 */
		registerTrackedForm = new RegisterTrackedForm();

		/*
		 * Tracker profile panel
		 */
		trackerProfileForm = new TrackerProfileForm();

		/*
		 * Tracked profile form
		 */
		trackedProfileForm = new TrackedProfileForm();

		/*
		 * Map Panel
		 */
		mapPanel = new MapPanel();

		mapPanel.getMaxButton().addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(Button button, EventObject e) {
				maximizeMap();
			}
		});

		registerTrackerOpenID = new RegisterTrackerOpenID();

		/*
		 * Card panel
		 */
		cardPanel = new Panel();
		cardPanel.setBorder(false);
		cardPanel.setBodyBorder(false);
		cardPanel.setLayout(new CardLayout());

		cardPanel.add(mapPanel);
		cardPanel.add(registerTrackerForm);
		cardPanel.add(registerTrackedForm);
		cardPanel.add(trackerProfileForm);
		cardPanel.add(trackedProfileForm);
		cardPanel.add(registerTrackerOpenID);
		cardPanel.add(pnlHelp);
		

		cardPanel.setActiveItem(0);

		mainPanel.add(toolbar, new BorderLayoutData(RegionPosition.NORTH));
		mainPanel.add(cardPanel, new BorderLayoutData(RegionPosition.CENTER));
	}

	private void btnLogin_clicked() {
		if (isLogin) {
			// logout
			MessageBox.confirm(constants.confirmLogoutTitle(), constants
					.confirmLogoutContent(), new MessageBox.ConfirmCallback() {
				@Override
				public void execute(String btnID) {
					if (btnID.equals("yes")) {
						dbService
								.logoutTracker(new AsyncCallback<ServiceResult<CTracker>>() {
									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
										MessageBox.alert(caught.getMessage());
									}

									@Override
									public void onSuccess(
											ServiceResult<CTracker> result) {
										if (result.isOK()) {
											isLogin = false;
											btnLogin.setText(constants
													.loginButton());
											Cookies
													.removeCookie("tracker_username");
											Cookies
													.removeCookie("tracker_type");
											Cookies
													.removeCookie("tracker_language");
											for (String name : Cookies
													.getCookieNames()) {
												if (name
														.startsWith("a8d694517496758f43e57022b77b01c0")
														|| name
																.startsWith("fb_sig")) {
													Cookies.removeCookie(name);
												}
											}
											faceBookLogout();
											reloadPage();
										} else {
											MessageBox.alert(result
													.getMessage());
										}
									}

								});
					}
				}

			});
		} else {
			LoginWindow.getInstance().show();
		}
	}
	
	private void initHelpPanel(){
		pnlHelp = new Panel();
		pnlHelp.setBorder(false);
		pnlHelp.setLayout(new VerticalLayout());

		Hyperlink linkIntro = new Hyperlink(constants.wikiIntroduction(),true,"");
		linkIntro.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/Introduction", "_blank", "");
			}
		});
		
		Hyperlink linkUserGuide = new Hyperlink(constants.wikiUserGuide(),true,"");
		linkUserGuide.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/User_Guide", "_blank", "");
			}
		});
		
		Hyperlink linkPomaTest = new Hyperlink(constants.wikiPomaTestGuide(),true,"");
		linkPomaTest.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/PomaTest_Guide", "_blank", "");
			}
		});
		
		Hyperlink linkInstallMobile = new Hyperlink(constants.wikiPomaMobile(),true,"");
		linkInstallMobile.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/Download_install_Poma_mobile", "_blank", "");
			}
		});
		
		Hyperlink linkDevProject = new Hyperlink(constants.wikiCompileProject(),true,"");
		linkDevProject.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/Compile_Deploy_Project", "_blank", "");
			}
		});
		
		Hyperlink linkDevMobile = new Hyperlink(constants.wikiCompileMobile(),true,"");
		linkDevMobile.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				Window.open("http://code.google.com/p/bkitpoma/wiki/Develop_Poma_Mobile", "_blank", "");
			}
		});
		
		Hyperlink linkReturn= new Hyperlink(constants.returnToMapContent(),true,"");
		linkReturn.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				cardPanel.setActiveItem(0);
			}
		});
		
		FieldSet fsUser = new FieldSet(constants.user());
		fsUser.add(linkIntro);
		fsUser.add(linkUserGuide);
		fsUser.add(linkInstallMobile);
		fsUser.add(linkPomaTest);
		
		FieldSet fsDeveloper = new FieldSet(constants.developer());
		fsDeveloper.setCollapsible(true);
		fsDeveloper.setCollapsed(true);
		fsDeveloper.add(linkDevProject);
		fsDeveloper.add(linkDevMobile);
		
		pnlHelp.add(fsUser);
		pnlHelp.add(fsDeveloper);
		pnlHelp.add(new HTML("<br />"));
		pnlHelp.add(new HTML("<br />"));
		pnlHelp.add(new HTML("<br />"));
		pnlHelp.add(linkReturn);
	}
	
	private void isLogin() {
		// Is login
		dbService.isLogined(new AsyncCallback<ServiceResult<CTracker>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				finish();
			}

			@Override
			public void onSuccess(ServiceResult<CTracker> result) {

				if (result != null && result.getResult() instanceof CTracker) {
					final CTracker cTracker = result.getResult();

					if (cTracker.getPassword() == null) {
						MessageBox.alert("You must fill all information");
						// Use openID to log in
						btnProfile.hide();
						btnPublish.hide();
						UserSettings.ctracker = cTracker;
						cardPanel
								.setActiveItemID(registerTrackerOpenID.getId());
						UserSettings.timerTask.getTaskList().add(new Task(1) {
							@Override
							public void execute() {
								registerTrackerOpenID.resetForm();
								finish();
							}
						});
					} else {
						if (cTracker.isActived() == false) {
							trackerDeactivedWhenLogin();
						} else if (cTracker.isEnabled() == false) {
							trackerDisabledWhenLogin(cTracker);
						} else {
							UserSettings.ctracker = result.getResult();
							trackerLoginSuccess(result.getResult());
							trackerProfileForm.doAfterLogin();
						}
					}
					isLogin = true;
					btnLogin.setText(constants.logoutButton());
					btnNewTracker.hide();
				} else {
					trackerNotLogin();
				}

				new Viewport(mainPanel);

				mapPanel.init();
				if (!isLogin) {
					if ( Cookies.getCookie("introduction_window") == null ) {
						if (Ext.isFirebug()) {
							MessageBox.alert("Firebug can make the work run slowly.");
						}
						IntroductionWindow.getInstance().show();
						Cookies.setCookie("introduction_window", "done");
					}
				}
				finish();
			}
		});
	}

	private void trackerDeactivedWhenLogin() {
		btnProfile.hide();
		btnPublish.hide();
		MessageBox.alert("Information", "Your account is blocked."
				+ "\nPlease contact with administrator for more information.");
	}

	private void trackerDisabledWhenLogin(final CTracker cTracker) {
		btnProfile.hide();
		btnPublish.hide();
		MessageBox
				.confirm(
						"Information",
						"Please check email to confirm your registration. Or click Yes to resend email for confirmation.",
						new MessageBox.ConfirmCallback() {
							public void execute(String btnID) {
								if (btnID.toLowerCase().equals("yes")) {
									trackerService.validateEmailNewTracker(
											cTracker,
											new AsyncCallback<Boolean>() {

												@Override
												public void onFailure(
														Throwable caught) {
													caught.printStackTrace();
													MessageBox.alert(caught
															.getMessage());
												}

												@Override
												public void onSuccess(
														Boolean result) {
													if (result) {
														MessageBox
																.alert(constants
																		.lbl_send_mail_successfully());
													} else {
														MessageBox
																.alert(constants
																		.err_send_mail());
													}
												}

											});
								}
							}
						});
	}

	private void trackerLoginSuccess(final CTracker ctracker) {
		/*
		 * Add to the main panel
		 */
		final BorderLayoutData menuData = new BorderLayoutData(
				RegionPosition.EAST);
		//Prevent user to resize MenuPanel
		//menuData.setSplit(true);
		menuData.setMargins(new Margins(0, 0, 0, 0));
		menuData.setFloatable(true);

		mainPanel.add(menuPanel, menuData);
		TrackedPanel.getInstance().loadTrackeds();

		btnLogin.setText(constants.logoutButton());
		isLogin = true;
		if (UserSettings.ctracker.getName() != null)
			txtItemTracker.setText(UserSettings.ctracker.getName());
		else
			txtItemTracker.setText(UserSettings.ctracker.getUsername());

		mapPanel.setToolbarVisible();
		btnNewTracker.hide();
	}

	private void trackerNotLogin() {
		mapWidth = mapPanel.getWidth();
		mapHeight = mapPanel.getHeight();
		mapPanel.setSize(com.google.gwt.user.client.Window.getClientWidth(),
				com.google.gwt.user.client.Window.getClientHeight());
		mapPanel.getMaxButton().setIcon("/images/MapToolbar/min.gif");

		resize();

		btnProfile.hide();
		btnPublish.hide();
	}

	private void maximizeMap() {
		if (!UserSettings.MAP_MAXIMIZED) {
			toolbar.hide();
			if (isLogin)
				menuPanel.hide();

			mapWidth = mapPanel.getWidth();
			mapHeight = mapPanel.getHeight();
			mapPanel.setSize(
					com.google.gwt.user.client.Window.getClientWidth(),
					com.google.gwt.user.client.Window.getClientHeight());
			mapPanel.getMaxButton().setIcon("/images/MapToolbar/min.gif");

		} else {
			mapPanel.setSize(mapWidth, mapHeight);
			toolbar.show();
			if (isLogin)
				menuPanel.show();
			mapPanel.getMaxButton().setIcon("/images/MapToolbar/max.gif");
		}
		resize();

		UserSettings.MAP_MAXIMIZED = !UserSettings.MAP_MAXIMIZED;
	}

	public static void displayItemID(String id) {
		cardPanel.setActiveItemID(id);
	}

	/**
	 * Return to map from other panel
	 */
	public static void returnToMap() {
		MessageBox.confirm(constants.returnToMapTitle(), constants
				.returnToMapContent(), new MessageBox.ConfirmCallback() {
			@Override
			public void execute(String btnID) {
				// TODO Auto-generated method stub
				if (btnID.endsWith("yes")) {
					cardPanel.setActiveItem(0);
					if (!isLogin) 
						mapPanel.setSize(Window.getClientWidth(), Window.getClientHeight());
					else
						mapPanel.setSize(mapWidth, mapHeight);
					toolbar.show();
					mapPanel.getMaxButton().setIcon(
							"/images/MapToolbar/max.gif");
					if (isLogin) {
						MenuPanel.getInstance().show();
					}
					resize();
				}
			}
		});
	}

	/**
	 * Show map
	 */
	public static void showMap() {
		cardPanel.setActiveItem(0);
	}

	/**
	 * Loading page
	 */
	public static void reloadPage() {
		mainPanel.startLoading(constants.loadingMessage());
		_reloadPage();
	}

	/**
	 * Stop loading page
	 */
	public static void stopLoading() {
		mainPanel.stopLoading();
	}

	public static void resize() {
		mainPanel.fireEvent("resize");
	}

	/**
	 * A native function to reload page
	 */
	private static native void _reloadPage() /*-{
		$wnd.location.reload();
	}-*/;

	/**
	 * Start loading message
	 * 
	 * @param loadingMessage
	 */
	public static void startLoading(String loadingMessage) {
		mainPanel.startLoading(loadingMessage);
	}

	/**
	 * New tracked form
	 */
	public static void newTracked() {
		cardPanel.setActiveItemID(registerTrackedForm.getId());
	}

	/**
	 * Call after map loaded
	 */
	private native void finish() /*-{
		$wnd.Ext.get('loading').fadeOut( {
				remove :true,
				duration :.25
			});
	}-*/;
	
	/**
	 * Call after map loaded
	 */
	private native void faceBookLogout() /*-{
		if ($wnd.FB == undefined || $wnd.FB.Connect == undefined)
			return;
		$wnd.FB.Connect.logout();
	}-*/;
}

package com.bkitmobile.poma.mobile.ui;

import com.bkitmobile.poma.mobile.api.connection.TrackedSession;
import com.bkitmobile.poma.mobile.api.geometry.Geocode;
import com.bkitmobile.poma.mobile.api.operation.ChangePassOperation;
import com.bkitmobile.poma.mobile.api.operation.LoginOperation;
import com.bkitmobile.poma.mobile.api.operation.NewTrackOperation;
import com.bkitmobile.poma.mobile.api.operation.Operation;
import com.bkitmobile.poma.mobile.api.operation.WaypointOperation;
import com.bkitmobile.poma.mobile.api.util.MobileUtils;
import com.bkitmobile.poma.mobile.location.LocationChangedListener;
import com.bkitmobile.poma.mobile.location.LocationProvider;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import org.netbeans.microedition.lcdui.LoginScreen;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;

/**
 * @author hieu.hua
 */
public class PomaMobile extends MIDlet implements CommandListener, LocationChangedListener {

    private boolean midletPaused = false;
    private TrackedSession session = new TrackedSession();
    private LocationProvider locationProvider = null;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private SplashScreen splashScreenPOMA;
    private LoginScreen loginScreen;
    private WaitScreen waitScreen;
    private Alert alertLoginSuccessful;
    private Alert alertLoginFailed;
    private SplashScreen splashScreenBkitMobile;
    private List list;
    private WaitScreen waitScreen1;
    private TextBox textBoxChangePass;
    private Alert alertChangePassFailed;
    private Alert alertChangePassSuccessful;
    private Command itemCommand;
    private Command exitCommand;
    private Command cmdLogout;
    private Command cmdExit;
    private Command cmdStart;
    private Command cmdStop;
    private Command cmdNewTrack;
    private Command cmdChangePass;
    private Command cmdPause;
    private Command cmdLogin;
    private Command okCommand;
    private Command cancelCommand;
    private Command exitCommand1;
    private SimpleCancellableTask task;
    private Image image;
    private Image image1;
    private Ticker ticker;
    private SimpleCancellableTask task1;
    //</editor-fold>//GEN-END:|fields|0|

    private void log(String s) {
        getList().append(s, null);
        getList().setSelectedIndex(getList().size()-1, true);
    }

    /**
     * The PomaMobile constructor.
     */
    public PomaMobile() {
        locationProvider = new LocationProvider(5000);
        locationProvider.setLocationChangedListener(this);
        Operation.SERVICE_URL = "http://bkitpoma.appspot.com/api/mobile";
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getSplashScreenPOMA());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == list) {//GEN-BEGIN:|7-commandAction|1|58-preAction
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|58-preAction
                // write pre-action user code here
                listAction();//GEN-LINE:|7-commandAction|2|58-postAction
                // write post-action user code here
            } else if (command == cmdChangePass) {//GEN-LINE:|7-commandAction|3|68-preAction
                // write pre-action user code here
                switchDisplayable(null, getTextBoxChangePass());//GEN-LINE:|7-commandAction|4|68-postAction
                // write post-action user code here
            } else if (command == cmdExit) {//GEN-LINE:|7-commandAction|5|61-preAction
                // write pre-action user code here
                log(session.logout().message);
                exitMIDlet();//GEN-LINE:|7-commandAction|6|61-postAction
                // write post-action user code here
            } else if (command == cmdLogin) {//GEN-LINE:|7-commandAction|7|76-preAction
                // write pre-action user code here
                getLoginScreen().setUsername("");
                getLoginScreen().setPassword("");
                switchDisplayable(null, getLoginScreen());
//GEN-LINE:|7-commandAction|8|76-postAction
                // write post-action user code here
            } else if (command == cmdLogout) {//GEN-LINE:|7-commandAction|9|64-preAction
                // write pre-action user code here
                log(session.logout().message);
//GEN-LINE:|7-commandAction|10|64-postAction
                // write post-action user code here
            } else if (command == cmdNewTrack) {//GEN-LINE:|7-commandAction|11|66-preAction
                // write pre-action user code here
                NewTrackOperation result = session.newTrack();
                log(result.message);
//GEN-LINE:|7-commandAction|12|66-postAction
                // write post-action user code here
            } else if (command == cmdStart) {//GEN-LINE:|7-commandAction|13|70-preAction
                // write pre-action user code here
                NewTrackOperation result = session.newTrack();
                log(result.message);
                locationProvider.start();
//GEN-LINE:|7-commandAction|14|70-postAction
                // write post-action user code here
            } else if (command == cmdStop) {//GEN-LINE:|7-commandAction|15|72-preAction
                // write pre-action user code here
                log("Stop tracking");
                locationProvider.stop();
//GEN-LINE:|7-commandAction|16|72-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|25-preAction
        } else if (displayable == loginScreen) {
            if (command == LoginScreen.LOGIN_COMMAND) {//GEN-END:|7-commandAction|17|25-preAction
                // write pre-action user code here
                switchDisplayable(null, getWaitScreen());//GEN-LINE:|7-commandAction|18|25-postAction
                // write post-action user code here
            } else if (command == exitCommand1) {//GEN-LINE:|7-commandAction|19|97-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|20|97-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|21|42-preAction
        } else if (displayable == splashScreenBkitMobile) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|21|42-preAction
                // write pre-action user code here
                switchDisplayable(null, getLoginScreen());//GEN-LINE:|7-commandAction|22|42-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|23|20-preAction
        } else if (displayable == splashScreenPOMA) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|23|20-preAction
                // write pre-action user code here
                switchDisplayable(null, getSplashScreenBkitMobile());//GEN-LINE:|7-commandAction|24|20-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|25|87-preAction
        } else if (displayable == textBoxChangePass) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|25|87-preAction
                // write pre-action user code here
                textBoxChangePass.setString("");
                switchDisplayable(null, getList());//GEN-LINE:|7-commandAction|26|87-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|27|85-preAction
                // write pre-action user code here
                switchDisplayable(null, getWaitScreen1());//GEN-LINE:|7-commandAction|28|85-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|29|31-preAction
        } else if (displayable == waitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|29|31-preAction
                // write pre-action user code here
                switchDisplayable(getAlertLoginFailed(), getLoginScreen());//GEN-LINE:|7-commandAction|30|31-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|31|30-preAction
                // write pre-action user code here
                switchDisplayable(getAlertLoginSuccessful(), getList());//GEN-LINE:|7-commandAction|32|30-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|33|80-preAction
        } else if (displayable == waitScreen1) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|33|80-preAction
                // write pre-action user code here
                switchDisplayable(getAlertChangePassFailed(), getTextBoxChangePass());//GEN-LINE:|7-commandAction|34|80-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|35|79-preAction
                // write pre-action user code here
                switchDisplayable(getAlertChangePassSuccessful(), getList());//GEN-LINE:|7-commandAction|36|79-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|37|7-postCommandAction
        }//GEN-END:|7-commandAction|37|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|38|
    //</editor-fold>//GEN-END:|7-commandAction|38|

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreenPOMA ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of splashScreenPOMA component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreenPOMA() {
        if (splashScreenPOMA == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            splashScreenPOMA = new SplashScreen(getDisplay());//GEN-BEGIN:|18-getter|1|18-postInit
            splashScreenPOMA.setTitle("POMA (c) BkitMobile");
            splashScreenPOMA.setTicker(getTicker());
            splashScreenPOMA.setCommandListener(this);
            splashScreenPOMA.setImage(getImage());
            splashScreenPOMA.setTimeout(3000);//GEN-END:|18-getter|1|18-postInit
            splashScreenPOMA.setText(MobileUtils.getCellId() + "," + MobileUtils.getLAC() + "," + MobileUtils.getMCC() + "," + MobileUtils.getMNC());
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return splashScreenPOMA;
    }
    //</editor-fold>//GEN-END:|18-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: loginScreen ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of loginScreen component.
     * @return the initialized component instance
     */
    public LoginScreen getLoginScreen() {
        if (loginScreen == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            loginScreen = new LoginScreen(getDisplay());//GEN-BEGIN:|23-getter|1|23-postInit
            loginScreen.setLabelTexts("Username", "Password");
            loginScreen.setTitle("Login to POMA");
            loginScreen.setTicker(getTicker());
            loginScreen.addCommand(LoginScreen.LOGIN_COMMAND);
            loginScreen.addCommand(getExitCommand1());
            loginScreen.setCommandListener(this);
            loginScreen.setBGColor(-3355444);
            loginScreen.setFGColor(0);
            loginScreen.setPassword("");
            loginScreen.setUsername("");
            loginScreen.setLoginTitle("Login into POMA");
            loginScreen.setUseLoginButton(true);
            loginScreen.setLoginButtonText("Login");//GEN-END:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return loginScreen;
    }
    //</editor-fold>//GEN-END:|23-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitScreen ">//GEN-BEGIN:|27-getter|0|27-preInit
    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen() {
        if (waitScreen == null) {//GEN-END:|27-getter|0|27-preInit
            // write pre-init user code here
            waitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|27-getter|1|27-postInit
            waitScreen.setTitle("waitScreen");
            waitScreen.setTicker(getTicker());
            waitScreen.setCommandListener(this);
            waitScreen.setImage(getImage());
            waitScreen.setText("Waiting...");
            waitScreen.setTask(getTask());//GEN-END:|27-getter|1|27-postInit
            // write post-init user code here
        }//GEN-BEGIN:|27-getter|2|
        return waitScreen;
    }
    //</editor-fold>//GEN-END:|27-getter|2|
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: alertLoginSuccessful ">//GEN-BEGIN:|33-getter|0|33-preInit
    /**
     * Returns an initiliazed instance of alertLoginSuccessful component.
     * @return the initialized component instance
     */
    public Alert getAlertLoginSuccessful() {
        if (alertLoginSuccessful == null) {//GEN-END:|33-getter|0|33-preInit
            // write pre-init user code here
            alertLoginSuccessful = new Alert("Login successful", "Login successful", getImage(), null);//GEN-BEGIN:|33-getter|1|33-postInit
            alertLoginSuccessful.setTicker(getTicker());
            alertLoginSuccessful.setTimeout(Alert.FOREVER);//GEN-END:|33-getter|1|33-postInit
            // write post-init user code here
        }//GEN-BEGIN:|33-getter|2|
        return alertLoginSuccessful;
    }
    //</editor-fold>//GEN-END:|33-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: alertLoginFailed ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initiliazed instance of alertLoginFailed component.
     * @return the initialized component instance
     */
    public Alert getAlertLoginFailed() {
        if (alertLoginFailed == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            alertLoginFailed = new Alert("Login Failed", "Login Failed", getImage(), null);//GEN-BEGIN:|39-getter|1|39-postInit
            alertLoginFailed.setTicker(getTicker());
            alertLoginFailed.setTimeout(Alert.FOREVER);//GEN-END:|39-getter|1|39-postInit
            // write post-init user code here
        }//GEN-BEGIN:|39-getter|2|
        return alertLoginFailed;
    }
    //</editor-fold>//GEN-END:|39-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: task ">//GEN-BEGIN:|32-getter|0|32-preInit
    /**
     * Returns an initiliazed instance of task component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTask() {
        if (task == null) {//GEN-END:|32-getter|0|32-preInit
            // write pre-init user code here
            task = new SimpleCancellableTask();//GEN-BEGIN:|32-getter|1|32-execute
            task.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|32-getter|1|32-execute
                    // write task-execution user code here
                    session.trackedID = Long.parseLong(loginScreen.getUsername());
                    session.password = loginScreen.getPassword();

                    LoginOperation result = session.login();
                    log(result.message);
                    if (!result.ok) {
                        throw new Exception("Login failed: " + result.message);
                    }
                }//GEN-BEGIN:|32-getter|2|32-postInit
            });//GEN-END:|32-getter|2|32-postInit
            // write post-init user code here
        }//GEN-BEGIN:|32-getter|3|
        return task;
    }
    //</editor-fold>//GEN-END:|32-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: image ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initiliazed instance of image component.
     * @return the initialized component instance
     */
    public Image getImage() {
        if (image == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|40-getter|1|40-@java.io.IOException
                image = Image.createImage("/resource/logo-medium.png");
            } catch (java.io.IOException e) {//GEN-END:|40-getter|1|40-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|40-getter|2|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|3|
        return image;
    }
    //</editor-fold>//GEN-END:|40-getter|3|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: splashScreenBkitMobile ">//GEN-BEGIN:|41-getter|0|41-preInit
    /**
     * Returns an initiliazed instance of splashScreenBkitMobile component.
     * @return the initialized component instance
     */
    public SplashScreen getSplashScreenBkitMobile() {
        if (splashScreenBkitMobile == null) {//GEN-END:|41-getter|0|41-preInit
            // write pre-init user code here
            splashScreenBkitMobile = new SplashScreen(getDisplay());//GEN-BEGIN:|41-getter|1|41-postInit
            splashScreenBkitMobile.setTitle("POMA (c) BkitMobile");
            splashScreenBkitMobile.setTicker(getTicker());
            splashScreenBkitMobile.setCommandListener(this);
            splashScreenBkitMobile.setImage(getImage1());
            splashScreenBkitMobile.setTimeout(3000);//GEN-END:|41-getter|1|41-postInit
            // write post-init user code here
        }//GEN-BEGIN:|41-getter|2|
        return splashScreenBkitMobile;
    }
    //</editor-fold>//GEN-END:|41-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: image1 ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initiliazed instance of image1 component.
     * @return the initialized component instance
     */
    public Image getImage1() {
        if (image1 == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            try {//GEN-BEGIN:|44-getter|1|44-@java.io.IOException
                image1 = Image.createImage("/resource/BkitMobile_logo_43x150.png");
            } catch (java.io.IOException e) {//GEN-END:|44-getter|1|44-@java.io.IOException
                e.printStackTrace();
            }//GEN-LINE:|44-getter|2|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|3|
        return image1;
    }
    //</editor-fold>//GEN-END:|44-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand ">//GEN-BEGIN:|46-getter|0|46-preInit
    /**
     * Returns an initiliazed instance of itemCommand component.
     * @return the initialized component instance
     */
    public Command getItemCommand() {
        if (itemCommand == null) {//GEN-END:|46-getter|0|46-preInit
            // write pre-init user code here
            itemCommand = new Command("Item", Command.ITEM, 0);//GEN-LINE:|46-getter|1|46-postInit
            // write post-init user code here
        }//GEN-BEGIN:|46-getter|2|
        return itemCommand;
    }
    //</editor-fold>//GEN-END:|46-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: ticker ">//GEN-BEGIN:|48-getter|0|48-preInit
    /**
     * Returns an initiliazed instance of ticker component.
     * @return the initialized component instance
     */
    public Ticker getTicker() {
        if (ticker == null) {//GEN-END:|48-getter|0|48-preInit
            // write pre-init user code here
            ticker = new Ticker("POMA (c) 2009 BkitMobile");//GEN-LINE:|48-getter|1|48-postInit
            // write post-init user code here
        }//GEN-BEGIN:|48-getter|2|
        return ticker;
    }
    //</editor-fold>//GEN-END:|48-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|49-getter|0|49-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|49-getter|0|49-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|49-getter|1|49-postInit
            // write post-init user code here
        }//GEN-BEGIN:|49-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|49-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: list ">//GEN-BEGIN:|56-getter|0|56-preInit
    /**
     * Returns an initiliazed instance of list component.
     * @return the initialized component instance
     */
    public List getList() {
        if (list == null) {//GEN-END:|56-getter|0|56-preInit
            // write pre-init user code here
            list = new List("list", Choice.IMPLICIT);//GEN-BEGIN:|56-getter|1|56-postInit
            list.addCommand(getCmdLogin());
            list.addCommand(getCmdLogout());
            list.addCommand(getCmdNewTrack());
            list.addCommand(getCmdChangePass());
            list.addCommand(getCmdStart());
            list.addCommand(getCmdStop());
            list.addCommand(getCmdExit());
            list.setCommandListener(this);//GEN-END:|56-getter|1|56-postInit
            // write post-init user code here
        }//GEN-BEGIN:|56-getter|2|
        return list;
    }
    //</editor-fold>//GEN-END:|56-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: listAction ">//GEN-BEGIN:|56-action|0|56-preAction
    /**
     * Performs an action assigned to the selected list element in the list component.
     */
    public void listAction() {//GEN-END:|56-action|0|56-preAction
        // enter pre-action user code here
        String __selectedString = getList().getString(getList().getSelectedIndex());//GEN-LINE:|56-action|1|56-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|56-action|2|
    //</editor-fold>//GEN-END:|56-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdExit ">//GEN-BEGIN:|60-getter|0|60-preInit
    /**
     * Returns an initiliazed instance of cmdExit component.
     * @return the initialized component instance
     */
    public Command getCmdExit() {
        if (cmdExit == null) {//GEN-END:|60-getter|0|60-preInit
            // write pre-init user code here
            cmdExit = new Command("Exit", Command.EXIT, 99);//GEN-LINE:|60-getter|1|60-postInit
            // write post-init user code here
        }//GEN-BEGIN:|60-getter|2|
        return cmdExit;
    }
    //</editor-fold>//GEN-END:|60-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdLogout ">//GEN-BEGIN:|63-getter|0|63-preInit
    /**
     * Returns an initiliazed instance of cmdLogout component.
     * @return the initialized component instance
     */
    public Command getCmdLogout() {
        if (cmdLogout == null) {//GEN-END:|63-getter|0|63-preInit
            // write pre-init user code here
            cmdLogout = new Command("Logout", Command.ITEM, 1);//GEN-LINE:|63-getter|1|63-postInit
            // write post-init user code here
        }//GEN-BEGIN:|63-getter|2|
        return cmdLogout;
    }
    //</editor-fold>//GEN-END:|63-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdNewTrack ">//GEN-BEGIN:|65-getter|0|65-preInit
    /**
     * Returns an initiliazed instance of cmdNewTrack component.
     * @return the initialized component instance
     */
    public Command getCmdNewTrack() {
        if (cmdNewTrack == null) {//GEN-END:|65-getter|0|65-preInit
            // write pre-init user code here
            cmdNewTrack = new Command("New track", Command.ITEM, 11);//GEN-LINE:|65-getter|1|65-postInit
            // write post-init user code here
        }//GEN-BEGIN:|65-getter|2|
        return cmdNewTrack;
    }
    //</editor-fold>//GEN-END:|65-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdChangePass ">//GEN-BEGIN:|67-getter|0|67-preInit
    /**
     * Returns an initiliazed instance of cmdChangePass component.
     * @return the initialized component instance
     */
    public Command getCmdChangePass() {
        if (cmdChangePass == null) {//GEN-END:|67-getter|0|67-preInit
            // write pre-init user code here
            cmdChangePass = new Command("Change pass", Command.ITEM, 12);//GEN-LINE:|67-getter|1|67-postInit
            // write post-init user code here
        }//GEN-BEGIN:|67-getter|2|
        return cmdChangePass;
    }
    //</editor-fold>//GEN-END:|67-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdStart ">//GEN-BEGIN:|69-getter|0|69-preInit
    /**
     * Returns an initiliazed instance of cmdStart component.
     * @return the initialized component instance
     */
    public Command getCmdStart() {
        if (cmdStart == null) {//GEN-END:|69-getter|0|69-preInit
            // write pre-init user code here
            cmdStart = new Command("Start", Command.ITEM, 21);//GEN-LINE:|69-getter|1|69-postInit
            // write post-init user code here
        }//GEN-BEGIN:|69-getter|2|
        return cmdStart;
    }
    //</editor-fold>//GEN-END:|69-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdStop ">//GEN-BEGIN:|71-getter|0|71-preInit
    /**
     * Returns an initiliazed instance of cmdStop component.
     * @return the initialized component instance
     */
    public Command getCmdStop() {
        if (cmdStop == null) {//GEN-END:|71-getter|0|71-preInit
            // write pre-init user code here
            cmdStop = new Command("Stop", Command.ITEM, 22);//GEN-LINE:|71-getter|1|71-postInit
            // write post-init user code here
        }//GEN-BEGIN:|71-getter|2|
        return cmdStop;
    }
    //</editor-fold>//GEN-END:|71-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdPause ">//GEN-BEGIN:|73-getter|0|73-preInit
    /**
     * Returns an initiliazed instance of cmdPause component.
     * @return the initialized component instance
     */
    public Command getCmdPause() {
        if (cmdPause == null) {//GEN-END:|73-getter|0|73-preInit
            // write pre-init user code here
            cmdPause = new Command("Pause", Command.ITEM, 23);//GEN-LINE:|73-getter|1|73-postInit
            // write post-init user code here
        }//GEN-BEGIN:|73-getter|2|
        return cmdPause;
    }
    //</editor-fold>//GEN-END:|73-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cmdLogin ">//GEN-BEGIN:|75-getter|0|75-preInit
    /**
     * Returns an initiliazed instance of cmdLogin component.
     * @return the initialized component instance
     */
    public Command getCmdLogin() {
        if (cmdLogin == null) {//GEN-END:|75-getter|0|75-preInit
            // write pre-init user code here
            cmdLogin = new Command("Login", Command.ITEM, 0);//GEN-LINE:|75-getter|1|75-postInit
            // write post-init user code here
        }//GEN-BEGIN:|75-getter|2|
        return cmdLogin;
    }
    //</editor-fold>//GEN-END:|75-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitScreen1 ">//GEN-BEGIN:|78-getter|0|78-preInit
    /**
     * Returns an initiliazed instance of waitScreen1 component.
     * @return the initialized component instance
     */
    public WaitScreen getWaitScreen1() {
        if (waitScreen1 == null) {//GEN-END:|78-getter|0|78-preInit
            // write pre-init user code here
            waitScreen1 = new WaitScreen(getDisplay());//GEN-BEGIN:|78-getter|1|78-postInit
            waitScreen1.setTitle("waitScreen1");
            waitScreen1.setCommandListener(this);
            waitScreen1.setTask(getTask1());//GEN-END:|78-getter|1|78-postInit
            // write post-init user code here
        }//GEN-BEGIN:|78-getter|2|
        return waitScreen1;
    }
    //</editor-fold>//GEN-END:|78-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: textBoxChangePass ">//GEN-BEGIN:|82-getter|0|82-preInit
    /**
     * Returns an initiliazed instance of textBoxChangePass component.
     * @return the initialized component instance
     */
    public TextBox getTextBoxChangePass() {
        if (textBoxChangePass == null) {//GEN-END:|82-getter|0|82-preInit
            // write pre-init user code here
            textBoxChangePass = new TextBox("Enter your new password", null, 100, TextField.ANY | TextField.PASSWORD);//GEN-BEGIN:|82-getter|1|82-postInit
            textBoxChangePass.addCommand(getOkCommand());
            textBoxChangePass.addCommand(getCancelCommand());
            textBoxChangePass.setCommandListener(this);//GEN-END:|82-getter|1|82-postInit
            // write post-init user code here
        }//GEN-BEGIN:|82-getter|2|
        return textBoxChangePass;
    }
    //</editor-fold>//GEN-END:|82-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: task1 ">//GEN-BEGIN:|81-getter|0|81-preInit
    /**
     * Returns an initiliazed instance of task1 component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getTask1() {
        if (task1 == null) {//GEN-END:|81-getter|0|81-preInit
            // write pre-init user code here
            task1 = new SimpleCancellableTask();//GEN-BEGIN:|81-getter|1|81-execute
            task1.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|81-getter|1|81-execute
                    // write task-execution user code here
                    ChangePassOperation result = session.changePass(textBoxChangePass.getString());
                    textBoxChangePass.setString("");
                    log(result.message);
                    if (!result.ok) {
                        throw new Exception(result.message);
                    }
                }//GEN-BEGIN:|81-getter|2|81-postInit
            });//GEN-END:|81-getter|2|81-postInit
            // write post-init user code here
        }//GEN-BEGIN:|81-getter|3|
        return task1;
    }
    //</editor-fold>//GEN-END:|81-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|84-getter|0|84-preInit
    /**
     * Returns an initiliazed instance of okCommand component.
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|84-getter|0|84-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|84-getter|1|84-postInit
            // write post-init user code here
        }//GEN-BEGIN:|84-getter|2|
        return okCommand;
    }
    //</editor-fold>//GEN-END:|84-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand ">//GEN-BEGIN:|86-getter|0|86-preInit
    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {//GEN-END:|86-getter|0|86-preInit
            // write pre-init user code here
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|86-getter|1|86-postInit
            // write post-init user code here
        }//GEN-BEGIN:|86-getter|2|
        return cancelCommand;
    }
    //</editor-fold>//GEN-END:|86-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: alertChangePassSuccessful ">//GEN-BEGIN:|92-getter|0|92-preInit
    /**
     * Returns an initiliazed instance of alertChangePassSuccessful component.
     * @return the initialized component instance
     */
    public Alert getAlertChangePassSuccessful() {
        if (alertChangePassSuccessful == null) {//GEN-END:|92-getter|0|92-preInit
            // write pre-init user code here
            alertChangePassSuccessful = new Alert("Successful", "Your password has been changed!", getImage(), null);//GEN-BEGIN:|92-getter|1|92-postInit
            alertChangePassSuccessful.setTimeout(Alert.FOREVER);//GEN-END:|92-getter|1|92-postInit
            // write post-init user code here
        }//GEN-BEGIN:|92-getter|2|
        return alertChangePassSuccessful;
    }
    //</editor-fold>//GEN-END:|92-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: alertChangePassFailed ">//GEN-BEGIN:|93-getter|0|93-preInit
    /**
     * Returns an initiliazed instance of alertChangePassFailed component.
     * @return the initialized component instance
     */
    public Alert getAlertChangePassFailed() {
        if (alertChangePassFailed == null) {//GEN-END:|93-getter|0|93-preInit
            // write pre-init user code here
            alertChangePassFailed = new Alert("Failed", "Your password was not changed!", getImage(), null);//GEN-BEGIN:|93-getter|1|93-postInit
            alertChangePassFailed.setTimeout(Alert.FOREVER);//GEN-END:|93-getter|1|93-postInit
            // write post-init user code here
        }//GEN-BEGIN:|93-getter|2|
        return alertChangePassFailed;
    }
    //</editor-fold>//GEN-END:|93-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand1 ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of exitCommand1 component.
     * @return the initialized component instance
     */
    public Command getExitCommand1() {
        if (exitCommand1 == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            exitCommand1 = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|96-getter|1|96-postInit
            // write post-init user code here
        }//GEN-BEGIN:|96-getter|2|
        return exitCommand1;
    }
    //</editor-fold>//GEN-END:|96-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    public synchronized void onLocationChanged(Geocode oldLocation, Geocode newLocation) {
        WaypointOperation result = session.waypoint(newLocation);
        log(result.message);
    }
}

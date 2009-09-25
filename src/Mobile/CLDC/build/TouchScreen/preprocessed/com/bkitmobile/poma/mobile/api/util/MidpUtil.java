package com.bkitmobile.poma.mobile.api.util;

/**
 * This class will help you to check any system properties and class available or not...
 * @author hieu.hua
 */
public class MidpUtil {

    /**
     * Determine a property exists or not
     * @param inPropName
     * @return
     */
    public static boolean checkProperties(String inPropName) {
        boolean res = false;
        if (System.getProperty(inPropName) != null) {
            res = true;
        }
        return res;
    }

    /**
     * Determine a class is available or not
     * @param inClassName
     * @return
     */
    public static boolean checkClassAvailable(String inClassName) {
        boolean res = false;
        try {
            Class.forName(inClassName);
            res = true;
        } catch (Throwable ex) {
        }
        return res;
    }

    /**
     * Determine this phone is BlackBerry RIM OS or not
     * @return
     */
    public static boolean isRIM() {
        return System.getProperty("microedition.platform").startsWith("RIM");
    }
    public static boolean locationSupport = false;
    public static boolean bluetoothSupport = false;
    public static boolean fileSupport = false;
    public static boolean isMIDP2 = false;
    public static boolean obexSupport = false;
    public static boolean isCLDC1_1 = false;
    public static boolean isTouchScreen = false;
    public static boolean isRim = false;

    /**
     * Checking this MIDP
     */
    public static void checkMIDP() {
        locationSupport = checkProperties("microedition.location.version");
        bluetoothSupport = checkClassAvailable("javax.bluetooth.LocalDevice");
        obexSupport = checkClassAvailable("javax.obex.ServerRequestHandler");
        fileSupport = checkClassAvailable("javax.microedition.io.file.FileConnection");
        isMIDP2 = checkClassAvailable("javax.microedition.lcdui.game.GameCanvas");
        isCLDC1_1 = checkClassAvailable("java.lang.Float");
        isRim = isRIM();
        System.out.println("Location support: " + locationSupport);
        System.out.println("Bluetooth support: " + bluetoothSupport);
        System.out.println("OBEX support: " + obexSupport);
        System.out.println("File support: " + fileSupport);
        System.out.println("MIDP 2.0: " + isMIDP2);
        System.out.println("CLDC 1.1: " + isCLDC1_1);
    }
}

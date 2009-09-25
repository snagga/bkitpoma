package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtil;

/**
 * Represents an operation used to Logout
 * @author Hieu Rocker
 */
public class LogoutOperation extends Operation {

    /**
     * URL used to logout
     */
    public static String DEVICE_LOGOUT = Operation.SERVICE_URL + "/device/logout";

    /**
     * Create an instance of LogoutOperation
     * @param connection
     */
    public LogoutOperation(HttpSessionConnection connection) {
        super(connection);
        parse(execute());
    }

    protected String execute() {
        try {
            return connection.httpGet(DEVICE_LOGOUT);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    protected void parseResult() {
        // OK,username
        try {
            String[] s = StringUtil.split(rawMessage, ',');
            ok = s[0].equals("OK");
            result = s[1];
        } catch (Exception ex) {
            ok = false;
        }
        message = !ok ? "Logout:failed" : "Logout:successed:" + result;
    }
}

package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtil;

/**
 * Represents an operation used to Login
 * @author hieu.hua
 */
public class LoginOperation extends Operation {

    /**
     * URL used to Login
     */
    public static String DEVICE_LOGIN = Operation.SERVICE_URL + "/";

    /**
     * Device's ID
     */
    public long deviceID;

    /**
     * Device's password
     */
    public String password;

    public LoginOperation(HttpSessionConnection connection, long deviceID,
            String password) {
        super(connection);
        this.deviceID = deviceID;
        this.password = password;
        parse(execute());
    }

    protected String execute() {
        try {
            String params = "id=__id__&pwd=__pwd__";
            params = StringUtil.strReplace(params, "__id__", deviceID);
            params = StringUtil.strReplace(params, "__pwd__", password);
            return connection.httpPost(DEVICE_LOGIN, params);
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
        message = !ok ? "Login:failed" : "Login:successed:" + result;
    }
}

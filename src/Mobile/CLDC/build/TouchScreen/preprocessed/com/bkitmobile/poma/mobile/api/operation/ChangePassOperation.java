package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtil;

/**
 * Represents an operation used to change device's password
 * @author hieu.hua
 */
public class ChangePassOperation extends Operation {

    /**
     * URL used to change password
     */
    public static String DEVICE_CHANGEPASS = Operation.SERVICE_URL + "/device/changepass/__newpass__";

    /**
     * New password
     */
    public String newPassword;

    /**
     * Create an instance of ChangePassOperation
     * @param connection
     * @param newPassword
     */
    public ChangePassOperation(HttpSessionConnection connection,
            String newPassword) {
        super(connection);
        this.newPassword = newPassword;
        parse(execute());
    }

    protected String execute() {
        try {
            String url = StringUtil.strReplace(DEVICE_CHANGEPASS, "__newpass__", newPassword);
            return connection.httpGet(url);
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
        message = !ok ? "ChangePass:failed" : "ChangePass:successed";
    }
}

package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class LogoutOperation extends Operation {

    public static String TRACKED_LOGOUT = Operation.SERVICE_URL + "/tracked/logout";

    public LogoutOperation(HttpSessionConnection connection) {
        super(connection);
        parse(execute());
    }

    protected String execute() {
        try {
            return connection.httpGet(TRACKED_LOGOUT);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    protected void parseResult() {
        // OK,username
        try {
            String[] s = StringUtils.split(rawMessage, ',');
            ok = s[0].equals("OK");
            result = s[1];
        } catch (Exception ex) {
            ok = false;
        }
        message = !ok ? "Logout:failed" : "Logout:successed:" + result;
    }
}

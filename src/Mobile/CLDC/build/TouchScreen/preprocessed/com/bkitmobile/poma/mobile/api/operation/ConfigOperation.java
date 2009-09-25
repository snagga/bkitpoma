package com.bkitmobile.poma.mobile.api.operation;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtil;
import java.io.IOException;

/**
 * Represents an operation used to get config
 * @author Hieu Rocker
 */
public class ConfigOperation extends Operation {

     /**
     * URL used to get config
     */
    public static String DEVICE_CONFIG = Operation.SERVICE_URL + "/device/config";

    /**
     * Interval that device send location to POMA
     */
    public long interval;

    /**
     * Schedule that device send location to POMA
     */
    public boolean[] schedule;

    /**
     * Create an instance of ConfigOperation
     * @param connection
     */
    public ConfigOperation(HttpSessionConnection connection) {
        super(connection);
        parse(execute());
    }

    protected String execute() {
        try {
            return connection.httpGet(DEVICE_CONFIG);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    protected void parseResult() {
        // OK,interval,1.1.1.1.0.1..
        long[] res = new long[25];
        try {
            String[] s = StringUtil.split(StringUtil.strReplace(rawMessage,"\r\n", ""), ',');
            ok = s[0].equals("OK");
            res[24] = Long.parseLong(s[1]);
            interval = res[24];
            s = StringUtil.split(s[2], '.');
            for (int i=0;i<24;i++) {
                res[i] = Long.parseLong(s[i]);
                schedule[i] = res[i] != 0;
            }
            result = res;
        } catch (Exception ex) {
            ok = false;
        }
        message = !ok ? "Config:failed" : "Config:successed:" + res[24];
    }
}

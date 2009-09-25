package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class NewTrackOperation extends Operation {

    public static String TRACK_NEWTRACK = Operation.SERVICE_URL + "/track/newtrack";

    public NewTrackOperation(HttpSessionConnection connection) {
        super(connection);
        parse(execute());
    }

    protected String execute() {
        try {
            return connection.httpGet(TRACK_NEWTRACK);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    protected void parseResult() {
        // OK,trackID
        try {
            String[] s = StringUtils.split(rawMessage, ',');
            ok = s[0].equals("OK");
            result = new Long(Long.parseLong(s[1]));
        } catch (Exception ex) {
            ok = false;
        }
        message = !ok ? "NewTrack:failed" : "NewTrack:successed:" + result;
    }
}

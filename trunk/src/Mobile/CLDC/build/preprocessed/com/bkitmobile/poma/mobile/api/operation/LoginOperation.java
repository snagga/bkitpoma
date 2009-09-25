package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class LoginOperation extends Operation {

	public static String TRACKED_LOGIN = Operation.SERVICE_URL + "/";

	public long trackedID;
	public String password;

	public LoginOperation(HttpSessionConnection connection, long trackedID,
			String password) {
		super(connection);
		this.trackedID = trackedID;
		this.password = password;
		parse(execute());
	}

	protected String execute() {
		try {
			String params = "id=__id__&pwd=__pwd__";
			params = StringUtils.strReplace(params, "__id__", trackedID);
			params = StringUtils.strReplace(params, "__pwd__", password);
			return connection.httpPost(TRACKED_LOGIN, params);
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
                message = !ok ? "Login:failed" : "Login:successed:" + result;
	}
}

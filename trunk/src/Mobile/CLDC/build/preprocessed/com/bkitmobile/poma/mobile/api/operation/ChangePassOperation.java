package com.bkitmobile.poma.mobile.api.operation;

import java.io.IOException;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;
import com.bkitmobile.poma.mobile.api.util.StringUtils;

public class ChangePassOperation extends Operation {

	public static String TRACKED_CHANGEPASS = Operation.SERVICE_URL
			+ "/tracked/changepass/__newpass__";

	public String newPassword;

	public ChangePassOperation(HttpSessionConnection connection,
			String newPassword) {
		super(connection);
		this.newPassword = newPassword;
		parse(execute());
	}

	protected String execute() {
		try {
			String url = StringUtils.strReplace(TRACKED_CHANGEPASS, "__newpass__", newPassword);
			return connection.httpGet(url);
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
                message = !ok ? "ChangePass:failed" : "ChangePass:successed";
	}
}

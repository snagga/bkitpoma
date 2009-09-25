package com.bkitmobile.poma.mobile.api.operation;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;

public abstract class Operation {

    public static String SERVICE_URL = "http://localhost:8080/api/mobile";
    public HttpSessionConnection connection;
    public String rawMessage;
    public String message;
    public boolean ok;
    public Object result;

    protected abstract String execute();

    protected abstract void parseResult();

    protected void parse(String input) {
        System.out.println(input);
        rawMessage = input;
        ok = false;
        message = rawMessage;
        if (rawMessage != null) {
            ok = rawMessage.startsWith("OK");
            parseResult();
        }
    }

    public Operation(HttpSessionConnection connection) {
        this.connection = connection;
    }
}

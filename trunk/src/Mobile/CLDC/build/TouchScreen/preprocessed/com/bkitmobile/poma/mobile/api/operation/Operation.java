package com.bkitmobile.poma.mobile.api.operation;

import com.bkitmobile.poma.mobile.api.connection.HttpSessionConnection;

/**
 * Represents an operation from mobile using rest api
 * @author Hieu Rocker
 */
public abstract class Operation {

    /**
     * Service's URL
     */
    public static String SERVICE_URL = "http://bkitpoma.appspot.com/api/mobile";

    /**
     * HttpSessionConnect used to execute this operation 
     */
    public HttpSessionConnection connection;

    /**
     * Raw message server return to client
     */
    public String rawMessage;

    /**
     * Message responsed from server
     */
    public String message;

    /**
     * Determine this operator success or not
     */
    public boolean ok;

    /**
     * Object associated with response. Usually is object that client request.
     */
    public Object result;

    /**
     * Execute this operation 
     * @return
     */
    protected abstract String execute();

    /**
     * Parse the response from server
     */
    protected abstract void parseResult();

    /**
     * Parse raw message from server
     * @param input raw message
     */
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

    /**
     *
     * @param connection
     */
    public Operation(HttpSessionConnection connection) {
        this.connection = connection;
    }
}

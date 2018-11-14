package com.hotelbeds.supplierintegrations.hackertest.detector;

import java.time.LocalDateTime;

public class LogEntry {

    public enum LoginAction {
        SIGNIN_SUCCESS,
        SIGNIN_FAILURE
    }

    private final String ipAddress;
    private final LocalDateTime dateTime;
    private final LoginAction loginAction;
    private final String userName;

    public LogEntry(String ipAddress, LocalDateTime dateTime, LoginAction loginAction, String userName) {
        this.ipAddress = ipAddress;
        this.dateTime = dateTime;
        this.loginAction = loginAction;
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LoginAction getLoginAction() {
        return loginAction;
    }

    public String getUserName() {
        return userName;
    }
}
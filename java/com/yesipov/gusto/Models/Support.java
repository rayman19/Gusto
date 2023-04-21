package com.yesipov.gusto.Models;

public class Support {
    private String message, userID, header;

    public Support() {}

    public Support(String message, String userID, String header) {
        this.message = message;
        this.userID = userID;
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

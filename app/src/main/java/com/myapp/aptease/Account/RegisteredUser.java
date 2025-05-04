package com.myapp.aptease.Account;

public class RegisteredUser {
    String userId, userName;

    public RegisteredUser(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public RegisteredUser(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}

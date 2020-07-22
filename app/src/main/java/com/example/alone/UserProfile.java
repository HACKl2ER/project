package com.example.alone;

public class UserProfile {
    public String userLastName;
    public String userEmail;
    public String userName;
    public String userSexName;

    public UserProfile(){
    }

    public UserProfile(String userEmail, String userName, String userSexName, String userLastName) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSexName = userSexName;
        this.userLastName = userLastName;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserSexName() {
        return userSexName;
    }

    public void setUserSexName(String userSexName) {
        this.userSexName = userSexName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }





}


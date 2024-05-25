package com.iot232.ssis.data;

public class UserInfo {
    public String name, email, username, password, gender, dob, isLogged;

    public UserInfo() {
        this.isLogged = " ";
        this.name = " ";
        this.email = " ";
        this.username = " ";
        this.password = " ";
        this.gender = " ";
        this.dob = " ";
    }

    public UserInfo(String name, String email, String username, String password, String gender, String dob, String isLogged) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.isLogged = isLogged;
    }
}

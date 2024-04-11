package com.iot232.ssis.data;

public class AdaInfo {
    public String clientID, username, password;

    public AdaInfo(){
        this.clientID = " ";
        this.username = " ";
        this.password = " ";
    }

    public AdaInfo(String clientID, String username, String password){
        this.clientID = clientID;
        this.username = username;
        this.password = password;
    }
}

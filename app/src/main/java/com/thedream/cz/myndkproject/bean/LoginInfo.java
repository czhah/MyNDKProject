package com.thedream.cz.myndkproject.bean;

/**
 * Created by Administrator on 2017/11/28.
 */

public class LoginInfo {

    private String uid;
    private String token;
    private String username;
    private String belongTo;

    @Override
    public String toString() {
        return "LoginInfo{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", belongTo='" + belongTo + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }
}

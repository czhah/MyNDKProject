package com.thedream.cz.myndkproject.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by cz on 2017/11/29.
 *      用户登录返回信息
 */
@Entity
public class LoginInfo {

    @NonNull
    @PrimaryKey
    private String uid;
    private String token;
    private String username;
    private String belongTo;

    public LoginInfo() {
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", belongTo='" + belongTo + '\'' +
                '}';
    }
}

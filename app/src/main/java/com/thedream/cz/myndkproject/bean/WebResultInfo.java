package com.thedream.cz.myndkproject.bean;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WebResultInfo<T> {
    private int statusCode;
    private String message;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WebResultInfo{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

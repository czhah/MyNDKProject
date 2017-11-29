package com.thedream.cz.myndkproject.listener;

/**
 * Created by cz on 2017/11/29.
 * 请求返回结果
 */
public interface OnResultListener<T> {

    void onSuccess(T t);

    void onFailed(int code);
}

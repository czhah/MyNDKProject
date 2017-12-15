package com.thedream.cz.myndkproject.listener;

/**
 * Created by Administrator on 2017/12/15.
 */

public interface IProgressListener {

    void onProgress(long progress, long total, boolean isDone);
}

package com.thedream.cz.myndkproject.ndk;

/**
 * Created by cz on 2017/10/15.
 */

public class AutoPlayer {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avformat-56");
        System.loadLibrary("avutil-54");
        System.loadLibrary("postproc-53");
        System.loadLibrary("swresample-1");
        System.loadLibrary("swscale-3");
    }

    public native void sound();

    public native void stop();
}

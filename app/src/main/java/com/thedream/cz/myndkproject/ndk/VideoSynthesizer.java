package com.thedream.cz.myndkproject.ndk;

/**
 * Created by chenzhuang on 2017/10/17.
 * Describe: 视频合成器
 */

public class VideoSynthesizer {

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


    public native void compound(String input_one, String input_two, String output_one, String output_two);

}

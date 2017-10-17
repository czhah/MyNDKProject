//
// Created by Administrator on 2017/10/17.
//
#include <jni.h>
#include <string>
#include <android/log.h>

#ifndef MYNDKPROJECT_FFMPEGVIDEO_H
#define MYNDKPROJECT_FFMPEGVIDEO_H

#endif //MYNDKPROJECT_FFMPEGVIDEO_H

extern "C" {
//编码
#include "libavcodec/avcodec.h"
//封装格式处理
#include "libavformat/avformat.h"
//像素处理
#include "libswscale/swscale.h"
//视频显示
#include <android/native_window_jni.h>
#include <unistd.h>
//冲采样
#include "libswresample/swresample.h"
}
#define LOGI(FORMAT, ...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT, ...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);

void compound(const char *input_one, const char *output);

void yuv2mp4(const char *input, const char *output);
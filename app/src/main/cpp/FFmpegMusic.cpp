//
// Created by cz on 2017/10/15.
//
#include "FFmpegMusic.h"

int audio_stream_idx = -1;
int createFFmpeg(int *rate, int *channel) {
    av_register_all();

    //  本地音频文件
    char *input = "/sdcard/input.mp3";
    AVFormatContext *pFormatCtx = avformat_alloc_context();
    //  判断文件的有效性
    if(avformat_open_input(&pFormatCtx, input, NULL, NULL) != 0) {
        LOGE("%s", "打开音频文件失败");
        return -1;
    }

    if(avformat_find_stream_info(pFormatCtx, NULL) < 0) {
        LOGE("%s", "获取视频信息失败");
        return -1;
    }

    for(int i=0; i < pFormatCtx->nb_streams; i++) {
        if(pFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_AUDIO) {
            audio_stream_idx = i;
            break;
        }
    }

    if(audio_stream_idx == -1) {
        LOGE("%s", "不包含录音信息");
        return -1;
    }

    AVCodecContext *pCodecCtx = pFormatCtx->streams[audio_stream_idx]->codec;
    LOGI("获取视频编码器上下文  %p", pCodecCtx);
    AVCodec *pCodec = avcodec_find_decoder(pCodecCtx->codec->id);
    if(avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {

    }

}

void getPCM(void **outBuffer, size_t *size){

}


void realase(){

}


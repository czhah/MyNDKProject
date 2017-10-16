//
// Created by cz on 2017/10/15.
//
#include "FFmpegMusic.h"

AVFormatContext *pFormatCtx;
AVCodecContext *pCodecCtx;
AVCodec *pCodec;
AVFrame *avFrame;
AVPacket *packet;
SwrContext *swrContext;
uint8_t  *out_buffer;
int audio_stream_idx = -1;
int createFFmpeg(int *rate, int *channel) {
    av_register_all();

    //  本地音频文件
    char *input = "/sdcard/autoinput.mp3";
    pFormatCtx = avformat_alloc_context();
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

    //  获取音频编解码器
    pCodecCtx = pFormatCtx->streams[audio_stream_idx]->codec;
    LOGI("获取视频编码器上下文  %p", pCodecCtx);
    pCodec = avcodec_find_decoder(pCodecCtx->codec->id);
    if(avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {

    }

    avFrame = av_frame_alloc();

    packet = (AVPacket *) av_malloc(sizeof(AVPacket));

    //  mp3 编码格式 转换为 pcm
    swrContext = swr_alloc();

    /**
     * swr_alloc_set_opts(struct SwrContext *s,
      int64_t out_ch_layout, enum AVSampleFormat out_sample_fmt, int out_sample_rate,
      int64_t  in_ch_layout, enum AVSampleFormat  in_sample_fmt, int  in_sample_rate,
      int log_offset, void *log_ctx);
     */
    swr_alloc_set_opts(swrContext, AV_CH_LAYOUT_STEREO, AV_SAMPLE_FMT_S16, pCodecCtx->sample_rate,
    pCodecCtx->channel_layout, pCodecCtx->sample_fmt, pCodecCtx->sample_rate, 0, 0);

    swr_init(swrContext);

    *rate = pCodecCtx->sample_rate;
    *channel = pCodecCtx->channels;

    out_buffer = (uint8_t *) av_malloc(44100 * 2);
    LOGI("初始化FFmpeg完成");
    return 0;
}

void getPCM(void **outBuffer, size_t *size){
    int got_frame;
    int out_channer_nb = av_get_channel_layout_nb_channels(AV_CH_LAYOUT_STEREO);
    while(av_read_frame(pFormatCtx, packet) >= 0) {
        LOGI("stream_index &d", packet->stream_index);
        if(packet->stream_index == audio_stream_idx) {
            avcodec_decode_audio4(pCodecCtx, avFrame, &got_frame, packet);
            if(got_frame) {
                swr_convert(swrContext, &out_buffer, 44100 * 2, (const uint8_t **) avFrame->data, avFrame->nb_samples);
                //  通道数
                int out_buffer_size = av_samples_get_buffer_size(NULL, out_channer_nb, avFrame->nb_samples, AV_SAMPLE_FMT_S16, 1);
                *outBuffer = out_buffer;
                *size = out_buffer_size;
                break;
            }
        }
    }
}


void realase(){
    av_free_packet(packet);
    av_frame_free(&avFrame);
    av_free(out_buffer);
    swr_free(&swrContext);
    avcodec_close(pCodecCtx);
    avformat_close_input(&pFormatCtx);
}


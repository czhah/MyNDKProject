//
// Created by Administrator on 2017/10/17.
//
#include "FFmpegVideo.h"

void compound(const char *inputStr, const char *outStr){
    //  1、注册各大组件
    av_register_all();

    //  2、初始化上下文
    AVFormatContext *pContext = avformat_alloc_context();

    //  3、判断输出文件有效性
    if(avformat_open_input(&pContext, inputStr, NULL, NULL) < 0) {
        LOGE("打开失败  %s:", inputStr);
        return;
    }
    if(avformat_find_stream_info(pContext, NULL) < 0) {
        LOGE("获取信息失败")
        return;
    }

    int vedio_stream_idex = -1;
    for(int i=0; i<pContext->nb_streams; ++i) {
        LOGE("循环类型  %d", pContext->streams[i]->codec->codec_type);
        //  获取视频流的下标
        if(pContext->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            vedio_stream_idex = i;
        }
    }
    //  获取解码的上下文
    AVCodecContext *pCodecCtx = pContext->streams[vedio_stream_idex]->codec;
    LOGE("获取视频编码器上下文 %p  ",pCodecCtx);
    //  获取解码器
    AVCodec *pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    LOGE("获取视频编码 %p",pCodec);
    //  判断是否可以解码
    if(avcodec_open2(pCodecCtx,pCodec,NULL)<0)
    {
        LOGE("解码失败");
        return;
    }

    //  分配内存
    AVPacket *packet = (AVPacket *) av_malloc(sizeof(AVPacket));
    //  初始化结构体
//    av_init_packet(packet);
    //  还是不够？
    AVFrame *avFrame = av_frame_alloc();
    //  生命一个yuvFrame
    AVFrame *yuvFrame = av_frame_alloc();
    //  开辟一块指定大小指定yuv420p格式的内存，并返回指向该内存的指针
    uint8_t *out_buffer = (uint8_t *) av_malloc(avpicture_get_size(AV_PIX_FMT_YUV420P, pCodecCtx->width, pCodecCtx->height));

    int re = avpicture_fill((AVPicture *) yuvFrame, out_buffer, AV_PIX_FMT_YUV420P, pCodecCtx->width, pCodecCtx->height);
    LOGE("申请内存  %d", re);
    LOGE("宽 %d 高 %d frameNum %d", pCodecCtx->width, pCodecCtx->height, pCodecCtx->frame_number);
    //  mp4  的上下文，可操作mp4格式的数据
    SwsContext *swsContext = sws_getContext(pCodecCtx->width, pCodecCtx->height, pCodecCtx->pix_fmt,
                                            pCodecCtx->width, pCodecCtx->height, AV_PIX_FMT_YUV420P,
                                            SWS_BILINEAR, NULL, NULL, NULL);

    //  wb:写入二进制文件
    FILE *fp_yuv = fopen(outStr, "wb");

    //  packet入参 出参  转换上下文
    int got_frame;
    //  循环读取二进制文件
    while(av_read_frame(pContext, packet) >= 0) {
        //  根据frame 进行原生绘制，解码一帧的画面并放入avFrame中
        if(avcodec_decode_video2(pCodecCtx, avFrame, &got_frame, packet) < 0) {
            LOGE("绘制失败");
            return ;
        }
        LOGI("解码%d  ",got_frame);
        if(got_frame) {
            //  将avFrame上的数据写入yuvFrame中
            sws_scale(swsContext, (const uint8_t *const *) avFrame->data, avFrame->linesize, 0, pCodecCtx->height,
                      yuvFrame->data, yuvFrame->linesize);

            int y_size = pCodecCtx->width * pCodecCtx->height;
            fwrite(yuvFrame->data[0], 1, y_size, fp_yuv);  //  y
            fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv);  //  u
            fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv);  //  v
        }
        //  释放packet??
        av_free_packet(packet);
    }
    LOGI("写入完成");
    fclose(fp_yuv);
    av_frame_free(&avFrame);
    av_frame_free(&yuvFrame);
    avcodec_close(pCodecCtx);
    avformat_free_context(pContext);
}


void yuv2mp4(const char *inputStr, const char *output){
    //  1、注册各大组件
    av_register_all();

    //  2、初始化上下文
    AVFormatContext *pContext = avformat_alloc_context();

    FILE *in_file = fopen(inputStr, "rb");
    if(!in_file) {
        LOGE("读取源文件失败");
        return ;
    }

    LOGI("源文件  %p", in_file);

    //宽 480 高 208
    int in_w = 480;
    int in_h = 208;
    int frameNum = 50;
    const char* out_file = "result_one.mp4";

    AVFormatContext *pFormatCtx=NULL;
    //  初始化AVFormatContext结构体
    avformat_alloc_output_context2(&pFormatCtx, NULL, NULL, out_file);
    AVOutputFormat *fmt = pFormatCtx->oformat;

    if(avio_open(&pFormatCtx->pb, out_file, AVIO_FLAG_READ_WRITE)) {
        LOGI("文件打开失败  out_file %s", out_file);
        return ;
    }
    //  初始化视频码流
    AVStream *video_st = avformat_new_stream(pFormatCtx, 0);
    if(video_st == NULL) {
        LOGE("获取视频码流失败");
        return ;
    }
    LOGI("视频码流对象  %p", video_st);
    video_st->time_base.num = 1;
    video_st->time_base.den = 25;


    //  编码器Context设置参数
    AVCodecContext *pCodecCtx = video_st->codec;

    pCodecCtx->codec_id = fmt->video_codec;
    pCodecCtx->codec_type = AVMEDIA_TYPE_VIDEO;
    pCodecCtx->pix_fmt = AV_PIX_FMT_YUV420P;
    pCodecCtx->width = in_w;
    pCodecCtx->height = in_h;
    pCodecCtx->time_base.num = 1;
    pCodecCtx->time_base.den = 25;
    pCodecCtx->bit_rate = 400000;
    pCodecCtx->gop_size = 12;

    if(pCodecCtx->codec_id == AV_CODEC_ID_H264) {
        pCodecCtx->qmin = 10;
        pCodecCtx->qmax = 51;
        pCodecCtx->qcompress = 0.6;
    }

    if(pCodecCtx->codec_id == AV_CODEC_ID_MPEG2VIDEO) {
        //  编码格式是mpeg2
        pCodecCtx->max_b_frames = 2;
    }
    if(pCodecCtx->codec_id = AV_CODEC_ID_MPEG1VIDEO) {
        pCodecCtx->mb_decision = 2;
    }

    AVCodec *pCodec = avcodec_find_encoder(pCodecCtx->codec_id);
    if(!pCodec) {
        LOGI("解码器未找到!");
        return ;
    }
    LOGI("解码器: %p", pCodec);
    if(avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGE("打开文件失败");
        return ;
    }
    //  输出格式信息
    av_dump_format(pFormatCtx, 0, out_file, 1);




}
//
// Created by Administrator on 2017/10/17.
//
#include "FFmpegVideo.h"

AVFormatContext *pContext = NULL;

int flush_encoder(AVFormatContext *pContext, unsigned int stream_index);

void mp42yuv(const char *inputStr, const char *out_one, const char *out_two){
    //  1、注册各大组件
    av_register_all();

    //  2、初始化上下文
    pContext = avformat_alloc_context();

    //  3、判断输出文件有效性
    if(avformat_open_input(&pContext, inputStr, NULL, NULL) < 0) {
        LOGE("打开失败  %s:", inputStr);
        return;
    }
    if(avformat_find_stream_info(pContext, NULL) < 0) {
        LOGE("获取信息失败")
        return;
    }
    AVIOContext *pb = pContext->pb;
    unsigned int nb_streams = pContext->nb_streams;
    AVStream **streams = pContext->streams;
    int64_t duration = pContext->duration;
    int bit_rate = pContext->bit_rate;
    LOGI("AVIOContext  %p -- nb_streams %d  --  streams %p  --  duration %d  --  bit_rate %d ", pb, nb_streams, *streams, duration, bit_rate);

    //  元数据
    AVDictionary *metadata = pContext->metadata;
    AVDictionaryEntry *entry = NULL;
//    entry = av_dict_get(metadata, "author", entry, 0);
//    LOGI("author key %s  value %s", entry->key, entry->value);
//
//    entry = av_dict_get(metadata, "copyright", entry, 0);
//    LOGI("copyright key %s  value %s", entry->key, entry->value);
//
//    entry = av_dict_get(metadata, "description", entry, 0);
//    LOGI("description key %s  value %s", entry->key, entry->value);

    while(entry=av_dict_get(metadata, "", entry, AV_DICT_IGNORE_SUFFIX)) {
        //  没有值
        LOGI("key %s value %s", entry->key, entry->value);
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
    FILE *fp_yuv_one = fopen(out_one, "wb");
    FILE *fp_yuv_two = fopen(out_two, "wb");

    //  packet入参 出参  转换上下文
    int got_frame;
    //  循环读取二进制文件
    while(av_read_frame(pContext, packet) >= 0) {
        //  根据frame 进行原生绘制，解码一帧的画面并放入avFrame中
        if(avcodec_decode_video2(pCodecCtx, avFrame, &got_frame, packet) < 0) {
        }
        LOGI("解码%d  ",got_frame);
        if(got_frame) {
            //  将avFrame上的数据写入yuvFrame中
            sws_scale(swsContext, (const uint8_t *const *) avFrame->data, avFrame->linesize, 0, pCodecCtx->height,
                      yuvFrame->data, yuvFrame->linesize);

            int y_size = pCodecCtx->width * pCodecCtx->height;
            fwrite(yuvFrame->data[0], 1, y_size, fp_yuv_one);  //  y
            fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv_one);  //  u
            fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv_one);  //  v

            fwrite(yuvFrame->data[0], 1, y_size, fp_yuv_two);  //  y
            fwrite(yuvFrame->data[1], 1, y_size / 4, fp_yuv_two);  //  u
            fwrite(yuvFrame->data[2], 1, y_size / 4, fp_yuv_two);  //  v
        }
        //  释放packet
        av_free_packet(packet);
    }
    LOGI("写入完成");
    fclose(fp_yuv_one);
    fclose(fp_yuv_two);
    av_frame_free(&avFrame);
    av_frame_free(&yuvFrame);
    avcodec_close(pCodecCtx);

}

void compound(const char *yuv_one, const char *yuv_two, const char *output){


}

void yuv2mp4(const char *inputStr){

    //  1、注册各大组件
    av_register_all();

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
    int result = avformat_alloc_output_context2(&pFormatCtx, NULL, NULL, out_file);
    if(result) {
        /**
         * 编译ffmpeg的时候未将所需要的muxer给包含进来，这需要重新编译ffmpeg。
         * 如需要MP4类型的muxer则在编译时加上 --enable-muxer= MP4包含MP4，
         * 或者 --enable-muxers 包含所有ffmpeg支持的类型（这样编译出来的avformat会比较大），
         * 然后重新编译ffmpeg就OK了
         */
        LOGI("初始化文件失败  out_file %s", out_file);
        return ;
    }
    AVOutputFormat *fmt = pFormatCtx->oformat;

    if(avio_open(&pFormatCtx->pb, out_file, AVIO_FLAG_READ_WRITE)) {
        LOGI("文件打开失败  out_file %s", out_file);
        return;
    }
    //  初始化视频码流
    AVStream *video_st = avformat_new_stream(pFormatCtx, 0);
    if(video_st == NULL) {
        LOGE("获取视频码流失败");
        return;
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
        return;
    }
    LOGI("解码器: %p", pCodec);
    if(avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        LOGE("打开文件失败");
        return;
    }
    //  输出格式信息
    av_dump_format(pFormatCtx, 0, out_file, 1);

    //  初始化帧
    AVFrame *frame = av_frame_alloc();
    frame->width = pCodecCtx->width;
    frame->height = pCodecCtx->height;
    frame->format = pCodecCtx->pix_fmt;
    uint8_t  *frame_buffer = (uint8_t *) av_malloc(avpicture_get_size(pCodecCtx->pix_fmt, pCodecCtx->width, pCodecCtx->height));

    avpicture_fill((AVPicture *) frame, frame_buffer, pCodecCtx->pix_fmt, pCodecCtx->width, pCodecCtx->height);

    //  写入头文件
    avformat_write_header(pFormatCtx, NULL);

    AVPacket packet;
    int y_size = pCodecCtx->width * pCodecCtx->height;
    //  创建一个数据包
    av_new_packet(&packet, y_size);
    if(-2) {
        LOGI("-2是true")
    }else {
        LOGI("-2是false")
    }
    //  循环编码每一帧
    for(int i=0; i<frameNum; i++) {
        if(fread(frame_buffer, 1, y_size * 3 / 2, in_file) < 0) {
            LOGE("读取每一帧失败");
            return ;
        }else if(feof(in_file)){
            LOGE("到达文件末尾跳出循环");
            break;
        }
        frame->data[0] = frame_buffer;
        frame->data[1] = frame_buffer + y_size;
        frame->data[2] = frame_buffer + y_size * 5 / 4;
        frame->pts = i;
        int got_frame = 0;

        int ret = avcodec_encode_audio2(pCodecCtx, &packet, frame, &got_frame);

        if(ret < 0) {
            LOGE("编码失败");
            return; ;
        }

        if(got_frame == 1) {
            LOGI("编码成功");

            packet.stream_index = video_st->index;
            //av_packet_rescale_ts(AVPacket *pkt, AVRational tb_src, AVRational tb_dst);
            av_packet_rescale_ts(&packet, pCodecCtx->time_base, video_st->time_base);
            packet.pos = -1;
            av_interleaved_write_frame(pFormatCtx, &packet);

            av_free_packet(&packet);
        }

    }

    int ret = flush_encoder(pFormatCtx, 0);
    if(ret < 0) {
        LOGE("flushing encoder failed ");
        return ;
    }

    av_write_trailer(pFormatCtx);
    if(video_st) {
        avcodec_close(video_st->codec);
        av_free(frame);
        av_free(frame_buffer);
    }
    if(pFormatCtx != NULL) {
        avio_close(pFormatCtx->pb);
        avformat_free_context(pFormatCtx);
    }

    fclose(in_file);
}

int flush_encoder(AVFormatContext *pContext, unsigned int stream_index) {
    int ret;
    int got_frame;
    AVPacket packet;
    AVCodecContext *pCodecCtx = pContext->streams[stream_index]->codec;
    if(!(pCodecCtx->codec->capabilities & CODEC_CAP_DELAY)) {
        return 0;
    }
    while(1) {
        LOGI("flushing stream %d endcode", stream_index);
        packet.data = NULL;
        packet.size = 0;
        av_init_packet(&packet);
        /**
         * avcodec_encode_video2(AVCodecContext *avctx, AVPacket *avpkt,
                          const AVFrame *frame, int *got_packet_ptr);
         */
        ret = avcodec_encode_video2(pCodecCtx, &packet, NULL, &got_frame);
        av_frame_free(NULL);
        if(ret < 0) {
            break;
        }
        if(!got_frame) {
            ret = 0;
            break;
        }
        LOGI("编码成功");

        packet.stream_index = stream_index;
        // av_packet_rescale_ts(AVPacket *pkt, AVRational tb_src, AVRational tb_dst)
        av_packet_rescale_ts(&packet, pCodecCtx->time_base, pCodecCtx->time_base);
        //  将数据包写入frame中
        ret = av_interleaved_write_frame(pContext, &packet);
        if(ret < 0) {
            break;
        }

    }
    return ret;
}

void release_resource(){
    avformat_free_context(pContext);
}
#include <jni.h>
#include <string>
#include <android/log.h>
#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <assert.h>
#include "FFmpegMusic.h"
#include "FFmpegVideo.h"

#define LOGI(FORMAT,...) __android_log_print(ANDROID_LOG_INFO,"jason",FORMAT,##__VA_ARGS__);
#define LOGE(FORMAT,...) __android_log_print(ANDROID_LOG_ERROR,"jason",FORMAT,##__VA_ARGS__);

extern "C" {
//  编码
#include "libavcodec/avcodec.h"
//  封装格式处理
#include "libavformat/avformat.h"
#include "libswresample/swresample.h"
//  像素处理
#include "libswscale/swscale.h"
#include <android/native_window_jni.h>
#include <unistd.h>
}

//  创建audio 结构体
SLObjectItf engineObject = NULL;
//  音频引擎
SLEngineItf engineEngine = NULL;
//  混音器
SLObjectItf  outputMixObject = NULL;
//  混音器接口
SLEnvironmentalReverbItf outputMixEnvironmentReverb = NULL;
//  音频播放器
SLObjectItf bqPlayerObject = NULL;
//  播放器接口
SLPlayItf bqPlayerPlay;
//  缓冲队列接口
SLAndroidSimpleBufferQueueItf bqPlayerBufferQueue;

//  声音
SLVolumeItf  bqPlayerVolume;

const SLEnvironmentalReverbSettings settings = SL_I3DL2_ENVIRONMENT_PRESET_DEFAULT;
size_t bufferSize;
void *buffer;
//  回调
void bqPlayerCallback(SLAndroidSimpleBufferQueueItf bq, void *context) {
    bufferSize = 0;
    getPCM(&buffer, &bufferSize);
    if(NULL != buffer && 0 != bufferSize) {
        SLresult lresult;
        lresult = (*bq)->Enqueue(bq, buffer, bufferSize);
        assert(SL_RESULT_SUCCESS == lresult);
        LOGI("bqPlayerCallBack %d", lresult);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_thedream_cz_myndkproject_ndk_AutoPlayer_sound(JNIEnv *env, jobject instance) {
    SLresult sLresult;
    slCreateEngine(&engineObject, 0, NULL, 0, NULL, NULL);

    //  初始化audio引擎，同步初始化
    (*engineObject)->Realize(engineObject, SL_BOOLEAN_FALSE);

    //  获取SLEngine接口对象，后续的操作将使用这个对象
    (*engineObject)->GetInterface(engineObject, SL_IID_ENGINE, &engineEngine);
    LOGI("音频引擎 %p", engineEngine);
    //  用音频引擎调用函数，创建混音器
    (*engineEngine)->CreateOutputMix(engineEngine, &outputMixObject, 0, 0, 0);
    //  初始化混音器outputMixObject
    (*outputMixObject)->Realize(outputMixObject, SL_BOOLEAN_FALSE);
//    //  获取混音器接口
    sLresult = (*outputMixObject)->GetInterface(outputMixObject, SL_IID_ENVIRONMENTALREVERB, &outputMixEnvironmentReverb);
    LOGI("获取混音器  %d", sLresult);
    if(SL_RESULT_SUCCESS == sLresult) {
        (*outputMixEnvironmentReverb)->SetEnvironmentalReverbProperties(outputMixEnvironmentReverb, &settings);
    }

    int rate;
    int channels;
//    //   这个方法是将mp3格式音频转化为pcm格式，并获取音频参数(帧率，通道数等)
    createFFmpeg(&rate, &channels);
    LOGI("比特率 &d  通道数 &d", rate, channels);
    //  配置信息设置
    SLDataLocator_AndroidBufferQueue android_queue = {SL_DATALOCATOR_ANDROIDSIMPLEBUFFERQUEUE, 2};

    /**
     * typedef struct SLDataFormat_PCM_ {
	SLuint32 		formatType;  音频类型
	SLuint32 		numChannels;  音频通道数
	SLuint32 		samplesPerSec;  采样率
	SLuint32 		bitsPerSample;  音频格式(采样位数)  16位、24位、
	SLuint32 		containerSize;  包含位数
	SLuint32 		channelMask;  通道类型(立体声)
	SLuint32		endianness;  end标志位
} SLDataFormat_PCM;
     */
    //  pcm格式, 双通道，xxx，16位，左前、右前方向，xxx
    SLDataFormat_PCM pcm = {SL_DATAFORMAT_PCM, channels,
                            SL_SAMPLINGRATE_44_1, SL_PCMSAMPLEFORMAT_FIXED_16,
                            SL_PCMSAMPLEFORMAT_FIXED_16, SL_SPEAKER_FRONT_LEFT | SL_SPEAKER_FRONT_RIGHT,
                            SL_BYTEORDER_LITTLEENDIAN};

    //  新建一个数据源 将上述配置信息放到这个数据源中
    /*
     * typedef struct SLDataSource_ {
	    void *pLocator;
	    void *pFormat;
        } SLDataSource;
     */
    SLDataSource slDataSource = {&android_queue, &pcm};

    // 设置混音器
    SLDataLocator_OutputMix outputMix = {SL_DATALOCATOR_OUTPUTMIX, outputMixObject};

    SLDataSink audioSnk = {&outputMix, NULL};

    //  创建Recorder需要RECORD_AUDIO 权限
    const SLInterfaceID ids[3] = {SL_IID_BUFFERQUEUE, SL_IID_EFFECTSEND, SL_IID_VOLUME};

    const SLboolean req[3] = {SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE, SL_BOOLEAN_TRUE};

    //  创建音频播放器
    /**
     * 	SLresult (*CreateAudioPlayer) (
		SLEngineItf self,  引擎
		SLObjectItf * pPlayer,   播放对象
		SLDataSource *pAudioSrc,  数据源
		SLDataSink *pAudioSnk,   xxx
		SLuint32 numInterfaces,    xxx数量
		const SLInterfaceID * pInterfaceIds,
		const SLboolean * pInterfaceRequired
	);
     */
    sLresult = (*engineEngine)->CreateAudioPlayer(engineEngine, &bqPlayerObject,
                                                  &slDataSource, &audioSnk,
                                                  3, ids, req);
    LOGI("创建播放器  %p  结果 %d", engineEngine, sLresult);

    //  初始化播放器
    sLresult = (*bqPlayerObject)->Realize(bqPlayerObject, SL_BOOLEAN_FALSE);
    LOGI("初始化播放器  %p  结果 %d", bqPlayerObject, sLresult);

    //  获取player接口
    sLresult = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_PLAY, &bqPlayerPlay);
    LOGI("获取player接口 结果 %d", sLresult);

    //  获取缓冲队列接口
    sLresult = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_BUFFERQUEUE, &bqPlayerBufferQueue);
    LOGI("获取缓冲队列接口  结果 %d", sLresult);

    //  注册回调缓冲区
    (*bqPlayerBufferQueue)->RegisterCallback(bqPlayerBufferQueue, bqPlayerCallback, NULL);

    //  获取音量接口
    sLresult = (*bqPlayerObject)->GetInterface(bqPlayerObject, SL_IID_VOLUME, &bqPlayerVolume);
    LOGI("获取音量接口  结果 %d", sLresult);

    //  获取播放状态
    sLresult = (*bqPlayerPlay)->SetPlayState(bqPlayerPlay, SL_PLAYSTATE_PLAYING);

    bqPlayerCallback(bqPlayerBufferQueue, NULL);
}

void shutdown() {

}

extern "C"
JNIEXPORT void JNICALL
Java_com_thedream_cz_myndkproject_ndk_AutoPlayer_stop(JNIEnv *env, jobject instance) {

    if(bqPlayerObject != NULL) {
        (*bqPlayerObject)->Destroy(bqPlayerObject);
        bqPlayerObject = NULL;
        bqPlayerPlay = NULL;
        bqPlayerBufferQueue = NULL;
        bqPlayerVolume = NULL;
    }
    if(outputMixObject != NULL) {
        (*outputMixObject)->Destroy(outputMixObject);
        outputMixObject = NULL;
        outputMixEnvironmentReverb = NULL;
    }
    if(engineObject != NULL) {
        (*engineObject)->Destroy(engineObject);
        engineObject = NULL;
        engineEngine = NULL;
    }
    realase();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_thedream_cz_myndkproject_ndk_VideoSynthesizer_compound(JNIEnv *env, jobject instance,
                                                                jstring input_one_,
                                                                jstring input_two_,
                                                                jstring output_one_,
                                                                jstring output_two_) {
    const char *input_one = env->GetStringUTFChars(input_one_, 0);
    const char *input_two = env->GetStringUTFChars(input_two_, 0);
    const char *output_one = env->GetStringUTFChars(output_one_, 0);
    const char *output_two = env->GetStringUTFChars(output_two_, 0);

    mp42yuv(input_one, output_one, output_two);
//    yuv2mp4(output);  //  有问题啊！！！
    release_resource();
    env->ReleaseStringUTFChars(input_one_, input_one);
    env->ReleaseStringUTFChars(input_two_, input_two);
    env->ReleaseStringUTFChars(output_one_, output_two);
}
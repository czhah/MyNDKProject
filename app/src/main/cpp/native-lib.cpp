#include <jni.h>
#include <string>

JNIEXPORT void JNICALL
Java_com_thedream_cz_myndkproject_ndk_AutoPlayer_sound(JNIEnv *env, jobject instance) {


}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_thedream_cz_myndkproject_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

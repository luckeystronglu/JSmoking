#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_yzh_smartsmoking_activity_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ 2017建军90周年军演";
    return env->NewStringUTF(hello.c_str());
}

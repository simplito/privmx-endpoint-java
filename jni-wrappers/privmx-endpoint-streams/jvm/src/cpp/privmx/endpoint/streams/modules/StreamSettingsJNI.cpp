#include "privmx/endpoint/wrapper/parsers/model_native_initializers.h"
#include "privmx/endpoint/wrapper/utils/jniUtils.h"
//#include "../utils.hpp"
//#include "StreamApi.h"
//#include "../jniUtils.h"

namespace privmx::wrapper::streams {

    StreamSettingsJNI::StreamSettingsJNI(
            JNIEnv *env,
            jobject jstreamSettings
    ) {
        jclass jstreamJoinSettingsClass = env->FindClass("java/lang/IllegalArgumentException");
        javaVM = nullptr;
        this->jstreamSettings = nullptr;
        if (!env->IsInstanceOf(jstreamSettings, jstreamJoinSettingsClass)) {
            env->ThrowNew(
                    env->FindClass("java/lang/IllegalArgumentException"),
                    "ERROR");
            return;
        }
        env->GetJavaVM(&this->javaVM);
        this->jstreamSettings = env->NewGlobalRef(jstreamSettings);


//        this->OnFrame = [this](
//                int64_t a,
//                int64_t b,
//                const std::shared_ptr<privmx::endpoint::stream::Frame> &frame,
//                const std::string &c
//        ) {
//            JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
//                    javaVM,
//                    privmx::wrapper::jni::getPrivmxCallbackThreadName());
//            JniContextUtils ctx(env);
//        };
//
//        this->OnVideo = [this](const std::string &a) {
//            JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
//                    javaVM,
//                    privmx::wrapper::jni::getPrivmxCallbackThreadName());
//            JniContextUtils ctx(env);
//        };
//
//        this->OnVideoRemove = [this](const std::string &a) {
//            JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
//                    javaVM,
//                    privmx::wrapper::jni::getPrivmxCallbackThreadName());
//            JniContextUtils ctx(env);
//        };
    }

//    void StreamSettingsJNI::StreamSettingsJNI::onVideo2(
//            JNIEnv *env,
//            jobject a
//    ) {
//        jclass jstreamSettingsClass = env->GetObjectClass(jstreamSettings);
//        jmethodID jverifyMID = env->GetMethodID(
//                jstreamSettingsClass,
//                "OnVideo",
//                "("
//                "Ljava/lang/String;"
//                ")V"
//                );
//
//         env->CallObjectMethod(
//                 jstreamSettings,
//                jverifyMID,
//                a
//        );
    }
//
//void StreamSettingsJNI::onFrame2(
//        JNIEnv *env,
//        jobject a,
//        jobject b,
//        jobject c,
//        jstring d
//) {
//    JniContextUtils ctx(env);
//
//    jfieldID onFrameFID = ctx->GetFieldID(
//            this->cls,
//            "OnFrame",
//            "Lcom/simplito/java/privmx_endpoint/model/OnFrameCallback;"
//    );
//
//    jobject onFrameField = ctx->GetObjectField(streamSettings, onFrameFID);
//    jclass onFrameCls = ctx->FindClass(
//            "com/simplito/java/privmx_endpoint/model/OnFrameCallback");
//    jmethodID onFrameMID = ctx->GetMethodID(
//            onFrameCls,
//            "run",
//            "("
//            "J"
//            "J"
//            "Lcom/simplito/java/privmx_endpoint/model/OnFrame;"
//            "Ljava/lang/String;"
//            ")V"
//    );
//
//    env->CallVoidMethod(
//            onFrameField,
//            onFrameMID,
//            a, b, c, d
//    );
//}
//
//void StreamSettingsJNI::onVideo2(JNIEnv *env, jobject a) {
//    JniContextUtils ctx(env);
//
//    jfieldID OnVideoFID = ctx->GetFieldID(
//            this->cls,
//            "OnVideoFID",
//            "Ljava/util/function/Consumer;"
//    );
//
//    jobject onVideoField = ctx->GetObjectField(streamSettings, OnVideoFID);
//    jclass onVideoCls = ctx->FindClass("java/util/function/Consumer");
//    jmethodID onVideoMID = ctx->GetMethodID(onVideoCls, "accept", "(Ljava/lang/Object;)V");
//
//    env->CallVoidMethod(
//            onVideoField,
//            onVideoMID,
//            a
//    );
//}
//
//void StreamSettingsJNI::onVideoRemove2(
//        JNIEnv *env,
//        jobject a
//) {
//    JniContextUtils ctx(env);
//
//    jfieldID OnVideoRemoveFID = ctx->GetFieldID(
//            this->cls,
//            "OnVideoRemoveFID",
//            "Ljava/util/function/Consumer;"
//    );
//
//    jobject onVideoField = ctx->GetObjectField(streamSettings, OnVideoRemoveFID);
//    jclass onVideoCls = ctx->FindClass("java/util/function/Consumer");
//    jmethodID onVideoMID = ctx->GetMethodID(onVideoCls, "accept", "(Ljava/lang/Object;)V");
//
//    env->CallVoidMethod(
//            onVideoField,
//            onVideoMID,
//            a
//    );
//}
//
//StreamSettingsJNI::~StreamSettingsJNI() {
//    if (javaVM != nullptr && streamSettings != nullptr) {
//        JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
//                javaVM,
//                privmx::wrapper::jni::getPrivmxCallbackThreadName()
//        );
//
////        if (env != nullptr) env->DeleteGlobalRef(streamSettings);
//        streamSettings = nullptr;
//        javaVM = nullptr;
//    }
//}


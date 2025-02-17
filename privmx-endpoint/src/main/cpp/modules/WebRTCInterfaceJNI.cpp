//
// Created by Dawid Jenczewski on 13/02/2025.
//

#include "WebRTCInterfaceJNI.h"
#include "../model_native_initializers.h"
#include "../utils.hpp"

WebRTCInterfaceJNI::WebRTCInterfaceJNI(JNIEnv *env, jobject jwebRTCInterface) {
    jwebRTCInterfaceClass = env->FindClass(
            "com/simplito/java/privmx_endpoint/modules/stream/WebRTCInterface");
    if (!env->IsInstanceOf(jwebRTCInterface, jwebRTCInterfaceClass)) {
        env->ThrowNew(
                env->FindClass("java/lang/IllegalArgumentException"),
                "WebRTCInterfaceJNI::WebRTCInterfaceJNI object must be instance of WebRTCInterface");
        return;
    }
    env->GetJavaVM(&this->javaVM);
    this->jwebRTCInterface = jwebRTCInterface;
}

std::string WebRTCInterfaceJNI::createOfferAndSetLocalDescription() {
    JNIEnv *env = nullptr;
    javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    JniContextUtils ctx(env);

    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "createOfferAndSetLocalDescription",
            "()Ljava/lang/String;"
    );
    auto result = (jstring) env->CallObjectMethod(jwebRTCInterface, jmethodId);
    if (result == nullptr) {
        env->ThrowNew(
                env->FindClass("java/lang/NullPointerException"),
                "WebRTCInterfaceJni::createOfferAndSetLocalDescription cannot return null"
        );
        return {};
    }
    return ctx.jString2string(result);
}

std::string WebRTCInterfaceJNI::createAnswerAndSetDescriptions(
        const std::string &sdp,
        const std::string &type
) {
    JNIEnv *env = nullptr;
    //TODO: Check which version use
    javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    JniContextUtils ctx(env);

    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "createAnswerAndSetDescriptions",
            "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    auto result = (jstring) env->CallObjectMethod(
            jwebRTCInterface,
            jmethodId,
            env->NewStringUTF(sdp.c_str()),
            env->NewStringUTF(type.c_str()));
    if (result == nullptr) {
        env->ThrowNew(
                env->FindClass("java/lang/NullPointerException"),
                "WebRTCInterfaceJni::createAnswerAndSetDescriptions cannot return null");
        return {};
    }
    return ctx.jString2string(result);
}

void WebRTCInterfaceJNI::setAnswerAndSetRemoteDescription(
        const std::string &sdp,
        const std::string &type
) {
    JNIEnv *env = nullptr;
    javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    JniContextUtils ctx(env);

    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "setAnswerAndSetRemoteDescription",
            "(Ljava/lang/String;Ljava/lang/String;)V"
    );
    env->CallVoidMethod(
            jwebRTCInterface,
            jmethodId,
            env->NewStringUTF(sdp.c_str()),
            env->NewStringUTF(type.c_str()));
}

void WebRTCInterfaceJNI::close() {
    JNIEnv *env = nullptr;
    javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);

    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "close",
            "()V"
    );
    env->CallVoidMethod(jwebRTCInterface, jmethodId);
}

void WebRTCInterfaceJNI::updateKeys(const std::vector<Key> &keys) {
    JNIEnv *env = nullptr;
    javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);
    JniContextUtils ctx(env);

    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "updateKeys",
            "(Ljava/util/List;)V");
    jclass arrayCls = env->FindClass("java/util/ArrayList");
    jmethodID initArrayMID = env->GetMethodID(
            arrayCls,
            "<init>",
            "()V");
    jobject jKeysArray = env->NewObject(arrayCls, initArrayMID);
    jmethodID addToArrayMID = env->GetMethodID(
            arrayCls,
            "add",
            "(Ljava/lang/Object;)Z");
    for (auto &key_c: keys) {
        env->CallBooleanMethod(jKeysArray,
                               addToArrayMID,
                               privmx::wrapper::key2Java(ctx, key_c));
    }
    env->CallVoidMethod(
            jwebRTCInterface,
            jmethodId,
            jKeysArray);
}
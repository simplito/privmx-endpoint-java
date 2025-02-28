//
// Created by Dawid Jenczewski on 13/02/2025.
//

#include "WebRTCInterfaceJNI.h"
#include "../model_native_initializers.h"
#include "../utils.hpp"
#include <thread>

JNIEnv *WebRTCInterfaceJNI::AttachCurrentThreadIfNeeded() {
    JNIEnv *jni = nullptr;
    jint status = javaVM->GetEnv((void **) &jni, JNI_VERSION_1_6);
    //return if current thread is attached
    if (jni != nullptr && status == JNI_OK) return jni;

    std::string name(
            "WebRTCInterfaceJNI - " + std::to_string(
                    std::hash<std::thread::id>{}(std::this_thread::get_id())));
    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6;
    args.name = &name[0];
    args.group = nullptr;
#ifdef _JAVASOFT_JNI_H_  // Oracle's jni.h violates the JNI spec!
    void* env = nullptr;
#else
    JNIEnv *env = nullptr;
#endif
    if (javaVM->AttachCurrentThread(&env, &args) == JNI_OK) {
        return reinterpret_cast<JNIEnv *>(env);
    }
    return nullptr;
}

WebRTCInterfaceJNI::WebRTCInterfaceJNI(JNIEnv *env, jobject jwebRTCInterface) {
    jwebRTCInterfaceClass = env->FindClass(
            "com/simplito/java/privmx_endpoint/modules/stream/WebRTCInterface");
    javaVM = nullptr;
    if (!env->IsInstanceOf(jwebRTCInterface, jwebRTCInterfaceClass)) {
        env->ThrowNew(
                env->FindClass("java/lang/IllegalArgumentException"),
                "WebRTCInterfaceJNI::WebRTCInterfaceJNI object must be instance of WebRTCInterface");
        return;
    }
    env->GetJavaVM(&this->javaVM);
    //TODO: Clean this global ref on close()
    this->jwebRTCInterface = env->NewGlobalRef(jwebRTCInterface);
}

std::string WebRTCInterfaceJNI::createOfferAndSetLocalDescription() {
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    JniContextUtils ctx(env);
    jclass jwebRTCInterfaceClass = env->GetObjectClass(jwebRTCInterface);
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
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    JniContextUtils ctx(env);
    jclass jwebRTCInterfaceClass = env->GetObjectClass(jwebRTCInterface);
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
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    JniContextUtils ctx(env);
    jclass jwebRTCInterfaceClass = env->GetObjectClass(jwebRTCInterface);
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
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    jclass jwebRTCInterfaceClass = env->GetObjectClass(jwebRTCInterface);
    jmethodID jmethodId = env->GetMethodID(
            jwebRTCInterfaceClass,
            "close",
            "()V"
    );
    env->CallVoidMethod(jwebRTCInterface, jmethodId);
}

void WebRTCInterfaceJNI::updateKeys(const std::vector<Key> &keys) {
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    JniContextUtils ctx(env);
    jclass jwebRTCInterfaceClass = env->GetObjectClass(jwebRTCInterface);
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
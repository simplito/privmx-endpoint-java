//
// Created by Dominika on 27/03/2025.
//

#include "UserVerifierInterfaceJNI.h"
#include "../model_native_initializers.h"
#include "../utils.hpp"
#include "../jniUtils.h"
#include <thread>
#include <jni.h>
#include <iostream>
#include <memory>

privmx::wrapper::UserVerifierInterfaceJNI::UserVerifierInterfaceJNI(
        JNIEnv *env,
        jobject juserVerifierInterface
) {
    jclass juserVerifierInterfaceClass = env->FindClass(
            "com/simplito/java/privmx_endpoint/modules/core/UserVerifierInterface");
    javaVM = nullptr;
    if (!env->IsInstanceOf(juserVerifierInterface, juserVerifierInterfaceClass)) {
        env->ThrowNew(
                env->FindClass("java/lang/IllegalArgumentException"),
                "UserVerifierInterfaceJNI::UserVerifierInterfaceJNI object must be instance of UserVerifierInterface");
        return;
    }
    env->GetJavaVM(&this->javaVM);
    this->juserVerifierInterface = env->NewGlobalRef(juserVerifierInterface);
}

std::vector<bool>
privmx::wrapper::UserVerifierInterfaceJNI::verify(
        const std::vector<privmx::endpoint::core::VerificationRequest> &request
) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            javaVM,
            jni::getPrivmxCallbackThreadName());
    JniContextUtils ctx(env);
    ctx.setClassLoaderFromObject(juserVerifierInterface);
    jclass juserVerifierInterfaceClass = env->GetObjectClass(juserVerifierInterface);
    jmethodID jverifyMID = env->GetMethodID(
            juserVerifierInterfaceClass,
            "verify",
            "(Ljava/util/List;)Ljava/util/List;");

    jclass arrayClass = env->FindClass("java/util/ArrayList");
    jmethodID initArrayMID = env->GetMethodID(
            arrayClass,
            "<init>",
            "()V");
    jobject jverificationRequestArray = env->NewObject(arrayClass, initArrayMID);
    jmethodID addToArrayMID = env->GetMethodID(
            arrayClass,
            "add",
            "(Ljava/lang/Object;)Z");


    for (auto &request_c: request) {
        env->CallBooleanMethod(jverificationRequestArray,
                               addToArrayMID,
                               privmx::wrapper::verificationRequest2Java(ctx, request_c));
    }

    auto jresult = ctx.jObject2jArray(
            env->CallObjectMethod(juserVerifierInterface, jverifyMID, jverificationRequestArray));

    std::vector<bool> result_c;
    for (int i = 0; i < ctx->GetArrayLength(jresult); i++) {
        jobject jElement = ctx->GetObjectArrayElement(jresult, i);
        bool element_c = ctx.getObject(jElement).getBooleanValue();

        result_c.push_back(element_c);
    }
    return result_c;
}

privmx::wrapper::UserVerifierInterfaceJNI::~UserVerifierInterfaceJNI() {
    if (javaVM != nullptr && juserVerifierInterface != nullptr) {
        JNIEnv *env = nullptr;
        javaVM->GetEnv((void **) &env, JNI_VERSION_1_6);

        if (env != nullptr) env->DeleteGlobalRef(juserVerifierInterface);
        juserVerifierInterface = nullptr;
    }
}
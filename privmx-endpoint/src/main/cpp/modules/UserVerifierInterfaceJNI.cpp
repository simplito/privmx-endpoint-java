//
// Created by Dominika on 27/03/2025.
//

#include "UserVerifierInterfaceJNI.h"
#include "../model_native_initializers.h"
#include "../utils.hpp"
#include <thread>
#include <jni.h>
#include <iostream>
#include <memory>

privmx::wrapper::UserVerifierInterfaceJNI::UserVerifierInterfaceJNI(JNIEnv *env,
                                                                    jobject juserVerifierInterface) {
    jclass juserVerifierInterfaceClass = env->FindClass(
            "com/simplito/java/privmx_endpoint/model/UserVerifierInterface");
    javaVM = nullptr;
    if (!env->IsInstanceOf(juserVerifierInterface, juserVerifierInterfaceClass)) {
        env->ThrowNew(
                env->FindClass("java/lang/IllegalArgumentException"),
                "UserVerifierInterfaceJNI::UserVerifierInterfaceJNI object must be instance of UserVerifierInterface");
        return;
    }
    env->GetJavaVM(&this->javaVM);
    //TODO: Clean this global ref on close()
    this->juserVerifierInterface = env->NewGlobalRef(juserVerifierInterface);
}

JNIEnv *privmx::wrapper::UserVerifierInterfaceJNI::AttachCurrentThreadIfNeeded() {
    JNIEnv *jni = nullptr;
    jint status = javaVM->GetEnv((void **) &jni, JNI_VERSION_1_6);

    if (jni != nullptr && status == JNI_OK) return jni;

    std::string name("UserVerifierInterfaceJNI - " +
                     std::to_string(std::hash<std::thread::id>{}(std::this_thread::get_id())));

    JavaVMAttachArgs args;
    args.version = JNI_VERSION_1_6;
    args.name = &name[0];
    args.group = nullptr;

#ifdef _JAVASOFT_JNI_H_  // Oracle's jni.h violates the JNI spec!
    void* env = nullptr;
#else
    JNIEnv *env = nullptr;
#endif

    //TODO: Attached thread should be also detached
    if (javaVM->AttachCurrentThread(&env, &args) == JNI_OK) {
        return reinterpret_cast<JNIEnv *>(env);
    }
    return nullptr;
}


/*
    std::vector<privmx::endpoint::core::UserWithPubKey>
    usersToVector(JniContextUtils &ctx, jobjectArray users) {

    std::vector<privmx::endpoint::core::UserWithPubKey> users_c;
    for (int i = 0; i < ctx->GetArrayLength(users); i++) {

        jobject arrayElement = ctx->GetObjectArrayElement(users, i);
        jclass arrayElementCls = ctx->GetObjectClass(arrayElement);

        jfieldID pubKeyFID = ctx->GetFieldID(arrayElementCls, "pubKey", "Ljava/lang/String;");
        jfieldID userIdFID = ctx->GetFieldID(arrayElementCls, "userId", "Ljava/lang/String;");
        privmx::endpoint::core::UserWithPubKey user = privmx::endpoint::core::UserWithPubKey();
        user.userId = ctx.jString2string(
                (jstring) ctx->GetObjectField(arrayElement, userIdFID));
        user.pubKey = ctx.jString2string(
                (jstring) ctx->GetObjectField(arrayElement, pubKeyFID));

        users_c.push_back(user);
    }
    return users_c;

 */

std::vector<bool>
privmx::wrapper::UserVerifierInterfaceJNI::verify(
        const std::vector<privmx::endpoint::core::VerificationRequest> &request) {
    JNIEnv *env = AttachCurrentThreadIfNeeded();
    JniContextUtils ctx(env);
    jclass juserVerifierInterfaceClass = env->GetObjectClass(juserVerifierInterface);
    jmethodID jmethodId = env->GetMethodID(juserVerifierInterfaceClass, "verify",
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

    ctx.setClassLoaderFromObject(juserVerifierInterface);

    for (auto &request_c: request) {
        env->CallBooleanMethod(jverificationRequestArray,
                               addToArrayMID,
                               privmx::wrapper::verificationRequest2Java(ctx, request_c));
    }

    auto jresult = ctx.jObject2jArray(
            env->CallObjectMethod(juserVerifierInterface, jmethodId, jverificationRequestArray));

    std::vector<bool> result_c;
    for (int i = 0; i < ctx->GetArrayLength(jresult); i++) {
        jobject arrayElement = ctx->GetObjectArrayElement(jresult, i);
        bool vectorElement = ctx.getObject(arrayElement).getBooleanValue();

        result_c.push_back(vectorElement);
    }
    return result_c;
}
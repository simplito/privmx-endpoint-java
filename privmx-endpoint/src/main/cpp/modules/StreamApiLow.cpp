#include <jni.h>

//
// Created by Dawid Jenczewski on 14/02/2025.
//
#include "WebRTCInterfaceJNI.h"
#include "../utils.hpp"
#include "../exceptions.h"
#include "Connection.h"
#include "../parser.h"
#include <privmx/endpoint/stream/WebRTCInterface.hpp>
#include <privmx/endpoint/stream/StreamApiLow.hpp>

using namespace privmx::endpoint::stream;
using namespace privmx::endpoint;

StreamApiLow *getStreamApi(JniContextUtils &ctx, jobject streamApiInstance) {
    jclass cls = ctx->GetObjectClass(streamApiInstance);
    jfieldID apiFID = ctx->GetFieldID(cls, "api", "Ljava/lang/Long;");
    jobject apiLong = ctx->GetObjectField(streamApiInstance, apiFID);
    if (apiLong == nullptr) {
        throw IllegalStateException("ThreadApi cannot be used");
    }
    return (StreamApiLow *) ctx.getObject(apiLong).getLongValue();
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_create(
        JNIEnv *env,
        jclass clazz,
        jobject connection
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(connection, "Connection")) {
        return nullptr;
    }
    try {
        jmethodID initMID = ctx->GetMethodID(clazz, "<init>",
                                             "(Ljava/lang/Long;)V");
        auto connection_c = getConnection(env, connection);
        auto streamApiLow = StreamApiLow::create(*connection_c);
        auto *api = new StreamApiLow();
        *api = streamApiLow;
        jobject result = ctx->NewObject(
                clazz,
                initMID,
                ctx.long2jLong((jlong) api)
        );
        return result;
    } catch (const privmx::endpoint::core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_deinit(
        JNIEnv *env,
        jobject thiz
) {
    try {
        JniContextUtils ctx(env);
        //if null go to catch
        auto api = getStreamApi(ctx, thiz);
        delete api;
        jclass cls = env->GetObjectClass(thiz);
        jfieldID apiFID = env->GetFieldID(cls, "api", "Ljava/lang/Long;");
        env->SetObjectField(thiz, apiFID, (jobject) nullptr);
    } catch (const IllegalStateException &e) {
        env->ThrowNew(
                env->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
        return;
    }
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_createStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring context_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jobject policies
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID") ||
        ctx.nullCheck(users, "Users list") ||
        ctx.nullCheck(managers, "Managers list") ||
        ctx.nullCheck(public_meta, "Public meta") ||
        ctx.nullCheck(private_meta, "Private meta")) {
        return nullptr;
    }

    try {
        std::vector<core::UserWithPubKey> users_c = usersToVector(
                ctx,
                ctx.jObject2jArray(users));
        std::vector<core::UserWithPubKey> managers_c = usersToVector(
                ctx,
                ctx.jObject2jArray(managers));
        auto container_policies_c = std::optional<core::ContainerPolicy>(
                parseContainerPolicy(ctx, policies));
        return ctx->NewStringUTF(
                getStreamApi(ctx, thiz)->createStreamRoom(
                        ctx.jString2string(context_id),
                        users_c,
                        managers_c,
                        core::Buffer::from(ctx.jByteArray2String(public_meta)),
                        core::Buffer::from(ctx.jByteArray2String(private_meta)),
                        container_policies_c
                ).c_str());
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_updateStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jlong version,
        jboolean force,
        jboolean force_generate_new_key,
        jobject policies
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID") ||
        ctx.nullCheck(users, "Users list") ||
        ctx.nullCheck(managers, "Managers list") ||
        ctx.nullCheck(public_meta, "Public meta") ||
        ctx.nullCheck(private_meta, "Private meta")) {
        return;
    }
    try {
        std::vector<core::UserWithPubKey> users_c = usersToVector(
                ctx,
                ctx.jObject2jArray(users));
        std::vector<core::UserWithPubKey> managers_c = usersToVector(
                ctx,
                ctx.jObject2jArray(managers));
        auto container_policies_c = std::optional<core::ContainerPolicy>(
                parseContainerPolicy(ctx, policies));
        getStreamApi(ctx, thiz)->updateStreamRoom(
                ctx.jString2string(stream_room_id),
                users_c,
                managers_c,
                core::Buffer::from(ctx.jByteArray2String(public_meta)),
                core::Buffer::from(ctx.jByteArray2String(private_meta)),
                version,
                force == JNI_TRUE,
                force_generate_new_key == JNI_TRUE,
                container_policies_c
        );
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_listStreamRooms(
        JNIEnv *env,
        jobject thiz,
        jstring context_id,
        jlong skip,
        jlong limit,
        jstring sort_order,
        jstring last_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID") ||
        ctx.nullCheck(sort_order, "Sort order")) {
        return nullptr;
    }

    jclass pagingListCls = env->FindClass(
            "com/simplito/java/privmx_endpoint/model/PagingList");
    jmethodID pagingListInitMID = env->GetMethodID(pagingListCls, "<init>",
                                                   "(Ljava/lang/Long;Ljava/util/List;)V");
    jclass arrayCls = env->FindClass("java/util/ArrayList");
    jmethodID initArrayMID = env->GetMethodID(arrayCls, "<init>", "()V");
    jmethodID addToArrayMID = env->GetMethodID(arrayCls, "add", "(Ljava/lang/Object;)Z");
    auto query = core::PagingQuery();
    query.skip = skip;
    query.limit = limit;
    query.sortOrder = ctx.jString2string(sort_order);
    if (last_id != nullptr) {
        query.lastId = ctx.jString2string(last_id);
    }
    try {
        auto streamRooms_c(
                getStreamApi(ctx, thiz)->listStreamRooms(
                        ctx.jString2string(context_id),
                        query
                )
        );
        jobject array = env->NewObject(arrayCls, initArrayMID);
        for (auto &streamRoom_c: streamRooms_c.readItems) {
            env->CallBooleanMethod(array,
                                   addToArrayMID,
                                   privmx::wrapper::streamRoom2Java(ctx, streamRoom_c)
            );
        }
        return ctx->NewObject(
                pagingListCls,
                pagingListInitMID,
                ctx.long2jLong(streamRooms_c.totalAvailable),
                array
        );
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_getStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return nullptr;
    }
    try {
        return privmx::wrapper::streamRoom2Java(
                ctx,
                getStreamApi(ctx, thiz)->getStreamRoom(
                        ctx.jString2string(stream_room_id)
                )
        );
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_deleteStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return;
    }
    try {
        getStreamApi(ctx, thiz)->deleteStreamRoom(
                ctx.jString2string(stream_room_id)
        );
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_createStream(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jlong local_stream_id,
        jobject web_rtc
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID") ||
        ctx.nullCheck(web_rtc, "webRtc")) {
        return nullptr;
    }
    try {
        auto webrtc = std::make_shared<WebRTCInterfaceJNI>(
                WebRTCInterfaceJNI(env, web_rtc));
        return ctx.long2jLong(
                getStreamApi(ctx, thiz)->createStream(
                        ctx.jString2string(stream_room_id),
                        local_stream_id,
                        webrtc));
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_publishStream(
        JNIEnv *env,
        jobject thiz,
        jlong local_stream_id
) {
    JniContextUtils ctx(env);
    try {
        getStreamApi(ctx, thiz)->publishStream(local_stream_id);
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_joinStream(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject streams_id,
        jlong local_stream_id,
        jobject web_rtc
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID") ||
        ctx.nullCheck(streams_id, "Streams ID") ||
        ctx.nullCheck(web_rtc, "webRtc")) {
        return nullptr;
    }


    try {
        auto webrtc = std::make_shared<WebRTCInterfaceJNI>(
                WebRTCInterfaceJNI(env, web_rtc));
        std::vector<int64_t> streams_id_c;
        jobjectArray streamsIdArr = ctx.jObject2jArray(streams_id);
        for (int i = 0; i < ctx->GetArrayLength(streamsIdArr); i++) {
            jobject arrayElement = ctx->GetObjectArrayElement(streamsIdArr, i);
            streams_id_c.push_back(ctx.getObject(arrayElement).getLongValue());
        }
        return ctx.long2jLong(
                getStreamApi(ctx, thiz)->joinStream(
                        ctx.jString2string(stream_room_id),
                        streams_id_c,
                        streamJoinSettings(),
                        local_stream_id,
                        webrtc));
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_listStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID")) {
        return nullptr;
    }

    jclass arrayCls = env->FindClass("java/util/ArrayList");
    jmethodID initArrayMID = env->GetMethodID(arrayCls, "<init>", "()V");
    jmethodID addToArrayMID = env->GetMethodID(arrayCls, "add", "(Ljava/lang/Object;)Z");
    try {
        auto streams_c = getStreamApi(ctx, thiz)->listStreams(
                ctx.jString2string(stream_room_id));
        jobject array = env->NewObject(arrayCls, initArrayMID);
        for (auto &stream_c: streams_c) {
            env->CallBooleanMethod(
                    array,
                    addToArrayMID,
                    privmx::wrapper::stream2Java(ctx, stream_c));
        }
        return array;
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
    return nullptr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_unpublishStream(
        JNIEnv *env,
        jobject thiz,
        jlong local_stream_id
) {
    JniContextUtils ctx(env);
    try {
        getStreamApi(ctx, thiz)->unpublishStream(local_stream_id);
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_leaveStream(
        JNIEnv *env,
        jobject thiz,
        jlong local_stream_id
) {
    JniContextUtils ctx(env);
    try {
        getStreamApi(ctx, thiz)->leaveStream(local_stream_id);
    } catch (const core::Exception &e) {
        env->Throw(ctx.coreException2jthrowable(e));
    } catch (const IllegalStateException &e) {
        ctx->ThrowNew(
                ctx->FindClass("java/lang/IllegalStateException"),
                e.what()
        );
    } catch (const std::exception &e) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                e.what()
        );
    } catch (...) {
        env->ThrowNew(
                env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/exceptions/NativeException"),
                "Unknown exception"
        );
    }
}
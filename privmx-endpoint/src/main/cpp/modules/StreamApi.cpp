#include "StreamApi.h"

stream::StreamApi *getStreamApi(JniContextUtils &ctx, jobject streamApiInstance) {
    jclass cls = ctx->GetObjectClass(streamApiInstance);
    jfieldID apiFID = ctx->GetFieldID(cls, "api", "Ljava/lang/Long;");
    jobject apiLong = ctx->GetObjectField(streamApiInstance, apiFID);
    if (apiLong == nullptr) {
        throw IllegalStateException("StreamApi cannot be used");
    }
    return (stream::StreamApi *) ctx.getObject(apiLong).getLongValue();
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_init(
        JNIEnv *env,
        jobject thiz,
        jobject connection,
        jobject event_api
) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &env, &connection, &event_api]() {
                auto connection_c = getConnection(env, connection);
                auto event_api_c = getEventApi(ctx, event_api);
                auto streamApi = stream::StreamApi::create(*connection_c, *event_api_c);
                auto streamApi_ptr = new stream::StreamApi();
                *streamApi_ptr = streamApi;
                return ctx.long2jLong((jlong) streamApi_ptr);
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_deinit(
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
    }
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_listStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &stream_room_id]() {
                std::vector<privmx::endpoint::stream::Stream> streams_c = getStreamApi(
                        ctx, thiz)->listStreams(
                        ctx.jString2string(stream_room_id)
                );

                return vectorTojArray(ctx, streams_c,   privmx::wrapper::stream2Java);
            }
    );

    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_createStream(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return nullptr;
    }
    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &stream_room_id]() {
                auto stream_c(
                        getStreamApi(ctx, thiz)->createStream(
                                ctx.jString2string(stream_room_id)
                        )
                );
                return privmx::wrapper::streamHandle2Java(ctx, stream_c);
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_getMediaDevices(
        JNIEnv *env,
        jobject thiz
) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz]() {
                auto media_devices_c(
                        getStreamApi(ctx, thiz)->getMediaDevices()
                );
                return vectorTojArray(ctx, media_devices_c, privmx::wrapper::mediaDevice2Java);
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_addTrack(
        JNIEnv *env, jobject thiz,
        jobject stream_handle,
        jobject track
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle") ||
        ctx.nullCheck(track, "Track")) {
        return;
    }
    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_handle, &track]() {
        getStreamApi(ctx, thiz)->addTrack(
                parseStreamHandle(ctx, stream_handle),
                parseMediaDevice(ctx, track)
        );
    });
}
extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_removeTrack(
        JNIEnv *env,
        jobject thiz,
        jobject stream_handle,
        jobject track
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle") ||
        ctx.nullCheck(track, "Track")) {
        return;
    }
    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_handle, &track]() {
        getStreamApi(ctx, thiz)->removeTrack(
                parseStreamHandle(ctx, stream_handle),
                parseMediaDevice(ctx, track)
        );
    });
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_publishStream(
        JNIEnv *env,
        jobject thiz,
        jobject stream_handle
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &env, &thiz, &stream_handle]() {
                auto id =
                        getStreamApi(ctx, thiz)->publishStream(
                                parseStreamHandle(ctx, stream_handle));
                return privmx::wrapper::remoteStreamId2Java(ctx, id);
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_unpublishStream(
        JNIEnv *env,
        jobject thiz,
        jobject stream_handle
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle")){
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_handle]() {
        getStreamApi(ctx, thiz)->unpublishStream(
                parseStreamHandle(ctx, stream_handle)
        );
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_subscribeToRemoteStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions,
        jobject options
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
        ctx.nullCheck(options, "Options") ||
        ctx.nullCheck(subscriptions, "Subscriptions List")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &subscriptions, &options, &env]() {
        std::vector<privmx::endpoint::stream::StreamSubscription> subscriptions_c =
                jArrayToVector<privmx::endpoint::stream::StreamSubscription>(
                        ctx,
                        ctx.jObject2jArray(
                                subscriptions),
                        parseStreamSubscription
                );
        getStreamApi(ctx, thiz)->subscribeToRemoteStreams(
                ctx.jString2string(stream_room_id),
                subscriptions_c,
                parseStreamSettings(env, options)
        );
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_modifyRemoteStreamsSubscriptions(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions_to_add,
        jobject subscriptions_to_remove,
        jobject options
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
        ctx.nullCheck(options, "Options") ||
        ctx.nullCheck(subscriptions_to_add, "Subscriptions To Add List") ||
        ctx.nullCheck(subscriptions_to_remove, "Subscriptions To Remove List")) {
        return;
    }

    ctx.callVoidEndpointApi(
            [&ctx, &thiz, &stream_room_id, &subscriptions_to_add, &subscriptions_to_remove, &options, &env]() {
                std::vector<privmx::endpoint::stream::StreamSubscription> subscriptions_to_add_c =
                        jArrayToVector<privmx::endpoint::stream::StreamSubscription>(
                                ctx,
                                ctx.jObject2jArray(
                                        subscriptions_to_add),
                                parseStreamSubscription
                        );
                std::vector<privmx::endpoint::stream::StreamSubscription> subscriptions_to_remove_c =
                        jArrayToVector<privmx::endpoint::stream::StreamSubscription>(
                                ctx,
                                ctx.jObject2jArray(
                                        subscriptions_to_remove),
                                parseStreamSubscription
                        );
                getStreamApi(ctx, thiz)->modifyRemoteStreamsSubscriptions(
                        ctx.jString2string(stream_room_id),
                        subscriptions_to_add_c,
                        subscriptions_to_remove_c,
                        parseStreamSettings(env, options)
                );
            });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_unsubscribeFromRemoteStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions_to_remove
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
        ctx.nullCheck(subscriptions_to_remove, "Subscriptions To Remove List")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &subscriptions_to_remove]() {
        std::vector<privmx::endpoint::stream::StreamSubscription> subscriptions_to_remove_c =
                jArrayToVector<privmx::endpoint::stream::StreamSubscription>(
                        ctx,
                        ctx.jObject2jArray(
                                subscriptions_to_remove),
                        parseStreamSubscription
                );
        getStreamApi(ctx, thiz)->unsubscribeFromRemoteStreams(
                ctx.jString2string(stream_room_id),
                subscriptions_to_remove_c
        );
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_dropBrokenFrames(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jboolean enable
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &enable]() {
        getStreamApi(ctx, thiz)->dropBrokenFrames(
                ctx.jString2string(stream_room_id),
                enable == JNI_TRUE
        );
    });
}
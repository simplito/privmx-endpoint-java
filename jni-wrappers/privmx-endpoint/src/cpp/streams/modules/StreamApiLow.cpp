#include <jni.h>

//
// Created by Dawid Jenczewski on 14/02/2025.
//
#include "privmx/endpoint/wrapper/utils/utils.hpp"
#include "privmx/endpoint/wrapper/parsers/parser.h"
#include "privmx/endpoint/wrapper/parsers/model_native_initializers.h"
#include "privmx/endpoint/wrapper/modules/Connection.h"
#include "privmx/endpoint/wrapper/modules/EventApi.h"

#include "privmx/endpoint/wrapper/streams/modules/WebRTCInterfaceJNI.h"
#include "privmx/endpoint/wrapper/streams/parsers/model_native_initializers.h"
#include "privmx/endpoint/wrapper/streams/parsers/parser.h"

//#include <privmx-endpoint/includes/privmx/endpoint/wrapper/parsers/parser.h>

using namespace privmx::endpoint::stream;
using namespace privmx::endpoint;
//using namespace privmx::wrapper;

StreamApiLow *getStreamApi(JniContextUtils &ctx, jobject streamApiInstance) {
    jclass cls = ctx->GetObjectClass(streamApiInstance);
    jfieldID apiFID = ctx->GetFieldID(cls, "api", "Ljava/lang/Long;");
    jobject apiLong = ctx->GetObjectField(streamApiInstance, apiFID);
    if (apiLong == nullptr) {
        throw IllegalStateException("ThreadApi cannot be used");
    }
    return (stream::StreamApiLow *) ctx.getObject(apiLong).getLongValue();
}

//extern "C"
//JNIEXPORT jobject JNICALL
//Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApiLow_init(
//        JNIEnv *env,
//        jobject thiz,
//        jobject connection,
//        jobject eventApi
//) {
//    JniContextUtils ctx(env);
//    jobject result;
//
//    if (ctx.nullCheck(connection, "Connection") ||
//            ctx.nullCheck(eventApi, "EventApi")) {
//        return nullptr;
//    }
//
////    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &env, &thiz, &connection, &eventApi] {
//        auto connection_c = getConnection(env, connection);
//        auto eventApi_c = getEventApi(ctx, eventApi);
//        auto streamApiLow = stream::StreamApiLow::create(
//                *connection_c,
//                *eventApi_c
//                );
//        auto streamApiLow_ptr = new stream::StreamApiLow();
//        *streamApiLow_ptr = streamApiLow;
//        return ctx.long2jLong((jlong) streamApiLow_ptr);
////    });
//    if (ctx->ExceptionCheck()) {
//        return nullptr;
//    }
//    return result;
//}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_create(
        JNIEnv *env,
        jclass clazz,
        jobject connection,
        jobject eventApi,
        jobject stream_encryption_mode
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(connection, "Connection")) {
        return nullptr;
    }

//    jobject result;
//    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &env, &clazz, &connection, &eventApi] {
    jmethodID initMID = ctx->GetMethodID(clazz, "<init>",
            "(Ljava/lang/Long;)V");
    auto connection_c = getConnection(env, connection);
    auto eventApi_c = getEventApi(env, eventApi);
    auto streamApiLow = StreamApiLow::create(
            *connection_c,
            *eventApi_c
    );
    auto *api = new StreamApiLow();
    *api = streamApiLow;

    jobject result = ctx->NewObject(
            clazz,
            initMID,
            ctx.long2jLong((jlong) api)
    );
    return result;

//    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_deinit(
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
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_createStreamRoom(
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

    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [
                    &ctx,
                    &thiz,
                    context_id,
                    &users,
                    &managers,
                    &public_meta,
                    &private_meta,
                    &policies
            ]() {
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
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_updateStreamRoom(
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
    ctx.callVoidEndpointApi(
            [
                    &ctx,
                    &thiz,
                    &stream_room_id,
                    &users,
                    &managers,
                    &public_meta,
                    &private_meta,
                    &version,
                    force,
                    &force_generate_new_key,
                    &policies
            ]() {
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
            }
    );
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_listStreamRooms(
        JNIEnv *env,
        jobject thiz,
        jstring context_id,
        jlong skip,
        jlong limit,
        jstring sort_order,
        jstring last_id,
        jstring sort_by, // todo - use in this impl.
        jstring query_as_json
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID") ||
            ctx.nullCheck(sort_order, "Sort order")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [
                    &ctx,
                    &env,
                    &thiz,
                    &context_id,
                    &skip,
                    &limit,
                    &sort_order,
                    &last_id,
                    &sort_by,
                    &query_as_json
            ]() {
                jclass pagingListCls = env->FindClass(
                        "com/simplito/java/privmx_endpoint/model/PagingList");
                jmethodID pagingListInitMID = env->GetMethodID(
                        pagingListCls, "<init>",
                        "(Ljava/lang/Long;Ljava/util/List;)V");
                jclass arrayCls = env->FindClass("java/util/ArrayList");
                jmethodID initArrayMID = env->GetMethodID(arrayCls,
                        "<init>",
                        "()V");
                jmethodID addToArrayMID = env->GetMethodID(arrayCls,
                        "add",
                        "(Ljava/lang/Object;)Z");
                auto query = core::PagingQuery();
                query.skip = skip;
                query.limit = limit;
                query.sortOrder = ctx.jString2string(sort_order);
                if (last_id != nullptr) {
                    query.lastId = ctx.jString2string(last_id);
                }
                if (sort_by != nullptr) {
                    query.sortBy = ctx.jString2string(sort_by);
                }
                if (query_as_json != nullptr) {
                    query.queryAsJson = ctx.jString2string(query_as_json);
                }

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
                            privmx::wrapper::streams::streamRoom2Java(
                                    ctx,
                                    streamRoom_c
                                    )
                    );
                }
                return ctx->NewObject(
                        pagingListCls,
                        pagingListInitMID,
                        ctx.long2jLong(streamRooms_c.totalAvailable),
                        array
                );
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_getStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return nullptr;
    }
    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &thiz, &stream_room_id] {

        return privmx::wrapper::streams::streamRoom2Java(
                ctx,
                getStreamApi(ctx, thiz)->getStreamRoom(
                        ctx.jString2string(stream_room_id)
                )
        );
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_deleteStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return;
    }
    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id]() {
        getStreamApi(ctx, thiz)->deleteStreamRoom(
                ctx.jString2string(stream_room_id)
        );
    });
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_createStream(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID"))
        return {};

    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &thiz, &stream_room_id] {

        return privmx::wrapper::streams::streamHandle2Java(
                ctx,
                getStreamApi(ctx, thiz)->createStream(
                        ctx.jString2string(stream_room_id)
                )
        );

    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_publishStream(
        JNIEnv *env,
        jobject thiz,
        jobject stream_handle
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle"))
        return {};

    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &thiz, &stream_handle] {

        auto result = getStreamApi(ctx, thiz)->publishStream(
                parseStreamHandle(ctx, stream_handle)
        );
        return nullptr;
        //TODO: Return result
//        return privmx::wrapper::streams::streamPublishResult2Java(
//                ctx,
//                result
//        );
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_joinStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject web_rtc
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID") ||
            ctx.nullCheck(web_rtc, "webRtc")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &env, &thiz, &stream_room_id, &web_rtc]() {
        auto webrtc = std::make_shared<WebRTCInterfaceJNI>(env, web_rtc);
        std::vector<int64_t> streams_id_c;

        getStreamApi(ctx, thiz)->joinStreamRoom(
                ctx.jString2string(stream_room_id),
                webrtc
        );
    });
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_listStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream room ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &thiz, &env, &stream_room_id] {
        jclass arrayCls = env->FindClass("java/util/ArrayList");
        jmethodID initArrayMID = env->GetMethodID(arrayCls, "<init>", "()V");
        jmethodID addToArrayMID = env->GetMethodID(arrayCls, "add", "(Ljava/lang/Object;)Z");

        auto stream_infos_c = getStreamApi(ctx, thiz)->listStreams(
                ctx.jString2string(stream_room_id)
        );
        jobject array = env->NewObject(arrayCls, initArrayMID);
        for (auto &info_c: stream_infos_c) {
            env->CallBooleanMethod(
                    array,
                    addToArrayMID,
                    privmx::wrapper::streams::streamInfo2Java(ctx, info_c)
            );
        }
        return array;
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_unpublishStream(
        JNIEnv *env,
        jobject thiz,
        jobject stream_handle
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_handle, "Stream Handle")) {
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
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_leaveStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id]() {
        getStreamApi(ctx, thiz)->leaveStreamRoom(
                ctx.jString2string(stream_room_id)
        );
    });
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_getTurnCredentials(
        JNIEnv *env,
        jobject thiz
) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &env, &thiz] {
        jclass arrayCls = env->FindClass("java/util/ArrayList");
        jmethodID initArrayMID = env->GetMethodID(arrayCls, "<init>", "()V");
        jmethodID addToArrayMID = env->GetMethodID(arrayCls, "add", "(Ljava/lang/Object;)Z");

        auto turnCredentialsVector = getStreamApi(ctx, thiz)->getTurnCredentials();
        jobject array = env->NewObject(arrayCls, initArrayMID);
        for (
            auto &turnCredentials_c: turnCredentialsVector) {
            env->CallBooleanMethod(
                    array,
                    addToArrayMID,
                    privmx::wrapper::streams::turnCredentials2Java(ctx, turnCredentials_c)
            );
        }

        return array;
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_subscribeFor(
        JNIEnv *env,
        jobject thiz,
        jobject subscription_queries
        // todo - made changes in arguments
) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &env, &thiz, &subscription_queries] {
        jclass arrayListCls = env->FindClass("java/util/ArrayList");
        jmethodID initMID = env->GetMethodID(arrayListCls, "<init>", "()V");
        jmethodID addToListMID = env->GetMethodID(arrayListCls, "add", "(Ljava/lang/Object;)Z");

        auto subscription_queries_arr = ctx.jObject2jArray(subscription_queries);
        auto subscription_queries_c = std::vector<std::string>();

        int length = ctx->GetArrayLength(subscription_queries_arr);
        for (int i = 0; i < length; i++) {
            jobject arrayElement = ctx->GetObjectArrayElement(subscription_queries_arr, i);
            subscription_queries_c.push_back(ctx.jString2string((jstring) arrayElement));
        }

        jobject arrayList = env->NewObject(arrayListCls, initMID);
        auto subscription_ids_c = getStreamApi(ctx, thiz)->subscribeFor(
                subscription_queries_c
        );

        for (auto &id_str: subscription_ids_c) {
            jstring java_id_str = ctx->NewStringUTF(id_str.c_str());
            env->CallBooleanMethod(
                    arrayList,
                    addToListMID,
                    java_id_str
            );
        }
        return arrayList;

    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_unsubscribeFrom(
        JNIEnv *env,
        jobject thiz,
        jobject subscription_ids
        // todo - made changes in arguments
) {
//TODO: Add nullchecks
    JniContextUtils ctx(env);
    if (ctx.nullCheck(subscription_ids, "Subscription ids")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &subscription_ids]() {
        auto subscription_ids_arr = ctx.jObject2jArray(subscription_ids);
        auto subscription_ids_c = std::vector<std::string>();

        int length = ctx->GetArrayLength(subscription_ids_arr);
        for (int i = 0; i < length; i++) {
            jobject arrayElement = ctx->GetObjectArrayElement(subscription_ids_arr, i);
            if (ctx.nullCheck(arrayElement, "Subscription ids array elements")) {
                return;
            }
            subscription_ids_c.push_back(ctx.jString2string((jstring) arrayElement));
        }

        getStreamApi(ctx, thiz)->unsubscribeFrom(subscription_ids_c);
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_keyManagement(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jboolean disable
        // todo - made changes in arguments

) {
//TODO: Add nullchecks
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &disable]() {
        getStreamApi(ctx, thiz)->keyManagement(
                ctx.jString2string(stream_room_id),
                disable == JNI_TRUE
        );

    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_trickle(
        JNIEnv *env,
        jobject thiz,
        jlong session_id,
        jstring candidate_as_json
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(candidate_as_json, "Candidate as JSON")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &session_id, &candidate_as_json]() {
        getStreamApi(ctx, thiz)->trickle(
                session_id,
                ctx.jString2string(candidate_as_json)
        );
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_unsubscribeFromRemoteStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions_to_remove
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
            ctx.nullCheck(subscriptions_to_remove, "Subscriptions to remove")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &subscriptions_to_remove]() {
        auto subscriptions_to_remove_arr = ctx.jObject2jArray(subscriptions_to_remove);
        auto subscriptions_to_remove_c = std::vector<StreamSubscription>();

        int length = ctx->GetArrayLength(subscriptions_to_remove_arr);
        for (int i = 0; i < length; i++) {
            jobject arrayElement = ctx->GetObjectArrayElement(subscriptions_to_remove_arr, i);
            if (ctx.nullCheck(arrayElement, "Subscriptions to remove array elements")) {
                return;
            }
            subscriptions_to_remove_c.push_back(
                    parseStreamSubscription(
                            ctx,
                            arrayElement
                    )
            );
        }

        getStreamApi(ctx, thiz)->unsubscribeFromRemoteStreams(
                ctx.jString2string(stream_room_id),
                subscriptions_to_remove_c
        );
    });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_modifyRemoteStreamsSubscriptions(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions_to_add,
        jobject subscriptions_to_remove,
        jobject options
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
            ctx.nullCheck(subscriptions_to_add, "Subscriptions to add") ||
            ctx.nullCheck(subscriptions_to_add, "Subscriptions to remove") ||
            ctx.nullCheck(options, "Options")) {
        return;
    }

    ctx.callVoidEndpointApi(
            [&ctx, &thiz, &stream_room_id, &subscriptions_to_add, &subscriptions_to_remove, &options]() {
                auto subscriptions_to_add_arr = ctx.jObject2jArray(subscriptions_to_remove);
                auto subscriptions_to_remove_arr = ctx.jObject2jArray(subscriptions_to_remove);
                auto subscriptions_to_add_c = std::vector<StreamSubscription>();
                auto subscriptions_to_remove_c = std::vector<StreamSubscription>();

                int subscriptions_to_add_length = ctx->GetArrayLength(subscriptions_to_add_arr);
                int subscriptions_to_remove_length = ctx->GetArrayLength(
                        subscriptions_to_remove_arr);

                for (int i = 0; i < subscriptions_to_add_length; i++) {
                    jobject arrayElement = ctx->GetObjectArrayElement(subscriptions_to_add_arr, i);
                    if (ctx.nullCheck(arrayElement, "Subscriptions to add array elements")) {
                        return;
                    }
                    subscriptions_to_add_c.push_back(
                            parseStreamSubscription(
                                    ctx,
                                    arrayElement
                            )
                    );
                }
                for (int i = 0; i < subscriptions_to_remove_length; i++) {
                    jobject arrayElement = ctx->GetObjectArrayElement(subscriptions_to_remove_arr,
                            i);
                    if (ctx.nullCheck(arrayElement, "Subscriptions to remove array elements")) {
                        return;
                    }
                    subscriptions_to_remove_c.push_back(
                            parseStreamSubscription(
                                    ctx,
                                    arrayElement
                            )
                    );
                }

                getStreamApi(ctx, thiz)->modifyRemoteStreamsSubscriptions(
                        ctx.jString2string(stream_room_id),
                        subscriptions_to_add_c,
                        subscriptions_to_remove_c,
                        parseSettings(ctx, options)
                );
            });
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_subscribeToRemoteStreams(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jobject subscriptions,
        jobject options
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
            ctx.nullCheck(subscriptions, "Subscriptions") ||
            ctx.nullCheck(options, "Options")) {
        return;
    }
    ctx.callVoidEndpointApi([&ctx, &thiz, &stream_room_id, &subscriptions, &options]() {
        auto subscriptions_arr = ctx.jObject2jArray(subscriptions);
        auto subscriptions_c = std::vector<StreamSubscription>();

        int length = ctx->GetArrayLength(subscriptions_arr);
        for (int i = 0; i < length; i++) {
            jobject arrayElement = ctx->GetObjectArrayElement(subscriptions_arr, i);
            if (ctx.nullCheck(arrayElement, "Subscriptions array elements")) {
                return;
            }
            subscriptions_c.push_back(
                    parseStreamSubscription(
                            ctx,
                            arrayElement
                    )
            );
        }

        getStreamApi(ctx, thiz)->subscribeToRemoteStreams(
                ctx.jString2string(stream_room_id),
                subscriptions_c,
                parseSettings(ctx, options)
        );
    });
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_createStreamRoomEx(
        JNIEnv *env,
        jobject thiz,
        jstring context_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jstring type,
        jobject policies
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID") ||
            ctx.nullCheck(users, "Users list") ||
            ctx.nullCheck(managers, "Managers list") ||
            ctx.nullCheck(public_meta, "Public meta") ||
            ctx.nullCheck(public_meta, "Public meta") ||
            ctx.nullCheck(type, "Type")) {
        return nullptr;
    }

    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [
                    &ctx,
                    &thiz,
                    context_id,
                    &users,
                    &managers,
                    &public_meta,
                    &private_meta,
                    &type,
                    &policies
            ]() {
                std::vector<core::UserWithPubKey> users_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(users));
                std::vector<core::UserWithPubKey> managers_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(managers));
                auto container_policies_c = std::optional<core::ContainerPolicy>(
                        parseContainerPolicy(ctx, policies));
                return ctx->NewStringUTF(
                        getStreamApi(ctx, thiz)->createStreamRoomEx(
                                ctx.jString2string(context_id),
                                users_c,
                                managers_c,
                                core::Buffer::from(ctx.jByteArray2String(public_meta)),
                                core::Buffer::from(ctx.jByteArray2String(private_meta)),
                                ctx.jString2string(type),
                                container_policies_c
                        ).c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_streams_StreamApiLow_getStreamRoomEx(
        JNIEnv *env,
        jobject thiz,
        jstring stream_room_id,
        jstring type
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
            ctx.nullCheck(type, "Type")) {
        return nullptr;
    }
    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &thiz, &stream_room_id, &type] {

        return privmx::wrapper::streams::streamRoom2Java(
                ctx,
                getStreamApi(ctx, thiz)->getStreamRoomEx(
                        ctx.jString2string(stream_room_id),
                        ctx.jString2string(type)
                )
        );
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
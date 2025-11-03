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
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_subscribeFor(
        JNIEnv *env,
        jobject thiz,
        jobject subscription_queries
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(subscription_queries, "Subscription queries")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &subscription_queries]() -> jobject {

                std::vector<std::string> queries = jArrayToVector<std::string>(
                        ctx,
                        ctx.jObject2jArray(subscription_queries),
                        jobject2string
                );

                std::vector<std::string> subscription_ids_c =
                        getStreamApi(ctx, thiz)->subscribeFor(queries);

                return vectorTojArray(
                        ctx,
                        subscription_ids_c,
                        string2jobject
                );
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_buildSubscriptionQuery(
        JNIEnv *env,
        jobject thiz,
        jlong event_type,
        jlong selector_type,
        jstring selector_id
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(selector_id, "SelectorID")) {
        return nullptr;
    }

    jstring result = nullptr;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &event_type, &selector_type, &selector_id]() {
                std::string query_result_c = getStreamApi(ctx, thiz)->buildSubscriptionQuery(
                        static_cast<stream::EventType>(event_type),
                        static_cast<stream::EventSelectorType>(selector_type),
                        ctx.jString2string(selector_id)
                );
                return ctx->NewStringUTF(query_result_c.c_str());
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
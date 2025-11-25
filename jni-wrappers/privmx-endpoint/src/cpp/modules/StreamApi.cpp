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
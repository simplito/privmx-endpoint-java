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
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_createStreamRoom(
        JNIEnv *env,
        jobject thiz,
        jstring context_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jobject container_policies
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
            [&ctx, &thiz, &context_id, &users, &managers, &public_meta, &private_meta, &container_policies]() {
                std::vector<core::UserWithPubKey> managers_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(managers));
                std::vector<core::UserWithPubKey> users_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(users));
                auto container_policies_n = std::optional<core::ContainerPolicy>(
                        parseContainerPolicy(ctx, container_policies));
                return ctx->NewStringUTF(
                        getStreamApi(ctx, thiz)->createStreamRoom(
                                ctx.jString2string(context_id),
                                users_c,
                                managers_c,
                                core::Buffer::from(ctx.jByteArray2String(public_meta)),
                                core::Buffer::from(ctx.jByteArray2String(private_meta)),
                                container_policies_n
                        ).c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_stream_StreamApi_updateStreamRoom(
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
        jobject container_policies
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(stream_room_id, "Stream Room ID") ||
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
                    &users, &managers,
                    &public_meta,
                    &private_meta,
                    &version,
                    &force,
                    &force_generate_new_key,
                    &container_policies
            ]() {
                std::vector<core::UserWithPubKey> users_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(users));
                std::vector<core::UserWithPubKey> managers_c = usersToVector(
                        ctx,
                        ctx.jObject2jArray(managers));
                auto container_policies_n = std::optional<core::ContainerPolicy>(
                        parseContainerPolicy(ctx, container_policies));

                getStreamApi(ctx, thiz)->updateStreamRoom(
                        ctx.jString2string(stream_room_id),
                        users_c,
                        managers_c,
                        core::Buffer::from(ctx.jByteArray2String(public_meta)),
                        core::Buffer::from(ctx.jByteArray2String(private_meta)),
                        version,
                        force == JNI_TRUE,
                        force_generate_new_key == JNI_TRUE,
                        container_policies_n
                );
            });
}
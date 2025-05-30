//
// Created by Dominika on 09/04/2025.
//

#include "KvdbApi.h"
#include "../utils.hpp"
#include "../parser.h"
#include "Connection.h"
#include <privmx/endpoint/kvdb/KvdbApi.hpp>
#include <jni.h>
#include "../model_native_initializers.h"

using namespace privmx::endpoint;

kvdb::KvdbApi *getKvdbApi(JniContextUtils &ctx, jobject kvdbApiInstance) {
    jclass cls = ctx->GetObjectClass(kvdbApiInstance);
    jfieldID apiFID = ctx->GetFieldID(cls, "api", "Ljava/lang/Long;");
    jobject apiLong = ctx->GetObjectField(kvdbApiInstance, apiFID);
    if (apiLong == nullptr) {
        throw IllegalStateException("KvdbApi cannot be used");
    }
    return (kvdb::KvdbApi *) ctx.getObject(apiLong).getLongValue();
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_init(
        JNIEnv *env,
        jobject thiz,
        jobject connection) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(&result, [&ctx, &env, &connection] {
        auto connection_c = getConnection(env, connection);
        auto kvdbApi = kvdb::KvdbApi::create(*connection_c);
        auto kvdbApi_ptr = new kvdb::KvdbApi();
        *kvdbApi_ptr = kvdbApi;

        return ctx.long2jLong((jlong) kvdbApi_ptr);
    });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_deinit(JNIEnv *env, jobject thiz) {
    try {
        JniContextUtils ctx(env);
        auto api = getKvdbApi(ctx, thiz);
        delete api;
        jclass cls = env->GetObjectClass(thiz);
        jfieldID apiFID = env->GetFieldID(cls, "api", "Ljava/lang/Long;");
        env->SetObjectField(thiz, apiFID, (jobject) nullptr);
    } catch (const IllegalStateException &e) {
        env->ThrowNew(env->FindClass("java/lang/IllegalStateException"), e.what());
    }
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_createKvdb(
        JNIEnv *env, jobject thiz,
        jstring context_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jobject policies) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID") || ctx.nullCheck(users, "Users list") ||
        ctx.nullCheck(managers, "Managers list") || ctx.nullCheck(public_meta, "Public meta") ||
        ctx.nullCheck(private_meta, "Private meta")) {
        return nullptr;
    }

    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [
                    &ctx,
                    &thiz,
                    &context_id,
                    &users,
                    &managers,
                    &public_meta,
                    &private_meta,
                    &policies]() {
                auto container_policies_n = std::optional<core::ContainerPolicy>(
                        parseContainerPolicy(ctx, policies));

                std::vector<core::UserWithPubKey> users_c = usersToVector(
                        ctx, ctx.jObject2jArray(users));
                std::vector<core::UserWithPubKey> managers_c = usersToVector(
                        ctx, ctx.jObject2jArray(managers));

                return ctx->NewStringUTF(
                        getKvdbApi(ctx, thiz)->createKvdb(
                                ctx.jString2string(context_id), users_c,
                                managers_c, core::Buffer::from(
                                        ctx.jByteArray2String(
                                                public_meta)),
                                core::Buffer::from(ctx.jByteArray2String(
                                        private_meta)),
                                container_policies_n).c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_updateKvdb(
        JNIEnv *env, jobject thiz,
        jstring kvdb_id,
        jobject users,
        jobject managers,
        jbyteArray public_meta,
        jbyteArray private_meta,
        jlong version,
        jboolean force,
        jboolean force_generate_new_key,
        jobject policies) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID") ||
        ctx.nullCheck(users, "Users list") ||
        ctx.nullCheck(managers, "Managers list") ||
        ctx.nullCheck(public_meta, "Public meta") ||
        ctx.nullCheck(private_meta, "Private meta")) {
        return;
    }

    ctx.callVoidEndpointApi([
                                    &ctx,
                                    &thiz,
                                    &kvdb_id,
                                    &users,
                                    &managers,
                                    &public_meta,
                                    &private_meta,
                                    &version,
                                    &force,
                                    &force_generate_new_key,
                                    &policies]() {
        auto container_policies_n = std::optional<core::ContainerPolicy>(
                parseContainerPolicy(ctx, policies));

        std::vector<core::UserWithPubKey> users_c = usersToVector(
                ctx, ctx.jObject2jArray(users));
        std::vector<core::UserWithPubKey> managers_c = usersToVector(
                ctx, ctx.jObject2jArray(managers));

        getKvdbApi(ctx, thiz)->updateKvdb(
                ctx.jString2string(kvdb_id),
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
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_deleteKvdb(
        JNIEnv *env,
        jobject thiz,
        jstring kvdb_id) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &kvdb_id]() {
        getKvdbApi(ctx, thiz)->deleteKvdb(
                ctx.jString2string(kvdb_id)
        );
    });
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_getKvdb(JNIEnv *env, jobject thiz,
                                                                     jstring kvdb_id) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &kvdb_id]() {
                return privmx::wrapper::kvdb2Java(
                        ctx,
                        getKvdbApi(ctx, thiz)->getKvdb(
                                ctx.jString2string(kvdb_id))
                );
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_listKvdbs(
        JNIEnv *env, jobject thiz,
        jstring context_id,
        jobject paging_query) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(context_id, "Context ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &context_id, &paging_query]() {
                jclass pagingListCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/PagingList");
                jmethodID pagingListInitMID = ctx->GetMethodID(pagingListCls, "<init>",
                                                               "(Ljava/lang/Long;Ljava/util/List;)V");
                jclass arrayCls = ctx->FindClass("java/util/ArrayList");
                jmethodID initArrayMID = ctx->GetMethodID(arrayCls, "<init>", "()V");
                jmethodID addToArrayMID = ctx->GetMethodID(arrayCls, "add",
                                                           "(Ljava/lang/Object;)Z");

                auto kvdbs_c(
                        getKvdbApi(ctx, thiz)->listKvdbs(
                                ctx.jString2string(context_id),
                                parsePagingQuery(ctx, paging_query)
                        )
                );

                jobject array = ctx->NewObject(arrayCls, initArrayMID);
                for (auto &kvdb_c: kvdbs_c.readItems) {
                    ctx->CallBooleanMethod(array,
                                           addToArrayMID,
                                           privmx::wrapper::kvdb2Java(ctx, kvdb_c)
                    );
                }
                return ctx->NewObject(
                        pagingListCls,
                        pagingListInitMID,
                        ctx.long2jLong(kvdbs_c.totalAvailable),
                        array
                );
            });
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_getEntry(JNIEnv *env, jobject thiz,
                                                                      jstring kvdb_id,
                                                                      jstring key) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &kvdb_id, &key]() {
                auto item_c(getKvdbApi(ctx, thiz)->getEntry(ctx.jString2string(kvdb_id),
                                                            ctx.jString2string(key)));
                return privmx::wrapper::kvdbEntry2Java(ctx, item_c);
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_listEntriesKeys(JNIEnv *env,
                                                                             jobject thiz,
                                                                             jstring kvdb_id,
                                                                             jobject paging_query) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &kvdb_id, &paging_query]() {
                jclass pagingListCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/PagingList");
                jmethodID pagingListInitMID = ctx->GetMethodID(pagingListCls, "<init>",
                                                               "(Ljava/lang/Long;Ljava/util/List;)V");
                jclass arrayCls = ctx->FindClass("java/util/ArrayList");
                jmethodID initArrayMID = ctx->GetMethodID(arrayCls, "<init>", "()V");
                jmethodID addToArrayMID = ctx->GetMethodID(arrayCls, "add",
                                                           "(Ljava/lang/Object;)Z");

                auto itemKeys_c(
                        getKvdbApi(ctx, thiz)->listEntriesKeys(
                                ctx.jString2string(kvdb_id),
                                parseKvdbKeysPagingQuery(ctx, paging_query)
                        )
                );

                jobject array = ctx->NewObject(arrayCls, initArrayMID);
                for (auto &itemKey_c: itemKeys_c.readItems) {
                    ctx->CallBooleanMethod(array,
                                           addToArrayMID,
                                           ctx->NewStringUTF(itemKey_c.c_str())
                    );
                }
                return ctx->NewObject(
                        pagingListCls,
                        pagingListInitMID,
                        ctx.long2jLong(itemKeys_c.totalAvailable),
                        array
                );
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C" JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_listEntries(JNIEnv *env, jobject thiz,
                                                                         jstring kvdb_id,
                                                                         jobject paging_query) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return nullptr;
    }

    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx, &thiz, &kvdb_id, &paging_query]() {
                jclass pagingListCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/PagingList");
                jmethodID pagingListInitMID = ctx->GetMethodID(pagingListCls, "<init>",
                                                               "(Ljava/lang/Long;Ljava/util/List;)V");
                jclass arrayCls = ctx->FindClass("java/util/ArrayList");
                jmethodID initArrayMID = ctx->GetMethodID(arrayCls, "<init>", "()V");
                jmethodID addToArrayMID = ctx->GetMethodID(arrayCls, "add",
                                                           "(Ljava/lang/Object;)Z");

                auto items_c(
                        getKvdbApi(ctx, thiz)->listEntries(
                                ctx.jString2string(kvdb_id),
                                parseKvdbEntriesPagingQuery(ctx, paging_query)
                        )
                );

                jobject array = ctx->NewObject(arrayCls, initArrayMID);
                for (auto &item_c: items_c.readItems) {
                    ctx->CallBooleanMethod(array,
                                           addToArrayMID,
                                           privmx::wrapper::kvdbEntry2Java(ctx, item_c)
                    );
                }
                return ctx->NewObject(
                        pagingListCls,
                        pagingListInitMID,
                        ctx.long2jLong(items_c.totalAvailable),
                        array
                );
            }
    );
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_setEntry(JNIEnv *env, jobject thiz,
                                                                      jstring kvdb_id, jstring key,
                                                                      jbyteArray public_meta,
                                                                      jbyteArray private_meta,
                                                                      jbyteArray data,
                                                                      jlong version) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID") ||
        ctx.nullCheck(key, "Key") ||
        ctx.nullCheck(public_meta, "Public meta") ||
        ctx.nullCheck(private_meta, "Private meta")) {
        return;
    }

    ctx.callVoidEndpointApi(
            [
                    &ctx,
                    &thiz,
                    &kvdb_id,
                    &key,
                    &public_meta,
                    &private_meta,
                    &data,
                    &version]() {
                getKvdbApi(ctx, thiz)->setEntry(
                        ctx.jString2string(kvdb_id),
                        ctx.jString2string(key),
                        core::Buffer::from(ctx.jByteArray2String(public_meta)),
                        core::Buffer::from(ctx.jByteArray2String(private_meta)),
                        core::Buffer::from(ctx.jByteArray2String(data)),
                        version
                );
            }
    );
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_deleteEntry(JNIEnv *env, jobject thiz,
                                                                         jstring kvdb_id,
                                                                         jstring key) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID") ||
        ctx.nullCheck(key, "Key")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &kvdb_id, &key] {
        getKvdbApi(ctx, thiz)->deleteEntry(
                ctx.jString2string(kvdb_id),
                ctx.jString2string(key));
    });
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_deleteEntries(JNIEnv *env,
                                                                           jobject thiz,
                                                                           jstring kvdb_id,
                                                                           jobject keys) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID") ||
        ctx.nullCheck(keys, "Keys")) {
        return;
    }

    auto keys_arr = ctx.jObject2jArray(keys);
    auto keys_c = std::vector<std::string>();

    for (int i = 0; i < ctx->GetArrayLength(keys_arr); i++) {
        jobject arrayElement = ctx->GetObjectArrayElement(keys_arr, i);
        keys_c.push_back(ctx.jString2string((jstring) arrayElement));
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &kvdb_id, &keys_c] {
        getKvdbApi(ctx, thiz)->deleteEntries(
                ctx.jString2string(kvdb_id),
                keys_c);
    });
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_subscribeForKvdbEvents(JNIEnv *env,
                                                                                    jobject thiz) {
    JniContextUtils ctx(env);

    ctx.callVoidEndpointApi([&ctx, &thiz]() {
        getKvdbApi(ctx, thiz)->subscribeForKvdbEvents();
    });
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_unsubscribeFromKvdbEvents__(
        JNIEnv *env,
        jobject thiz) {
    JniContextUtils ctx(env);

    ctx.callVoidEndpointApi([&ctx, &thiz]() {
        getKvdbApi(ctx, thiz)->unsubscribeFromKvdbEvents();
    });
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_subscribeForEntryEvents(JNIEnv *env,
                                                                                     jobject thiz,
                                                                                     jstring kvdb_id) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &kvdb_id]() {
        getKvdbApi(ctx, thiz)->subscribeForEntryEvents(ctx.jString2string(kvdb_id));
    });
}
extern "C" JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_kvdb_KvdbApi_unsubscribeFromKvdbEvents__Ljava_lang_String_2(
        JNIEnv *env,
        jobject thiz,
        jstring kvdb_id) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(kvdb_id, "Kvdb ID")) {
        return;
    }

    ctx.callVoidEndpointApi([&ctx, &thiz, &kvdb_id]() {
        getKvdbApi(ctx, thiz)->unsubscribeFromEntryEvents(ctx.jString2string(kvdb_id));
    });
}
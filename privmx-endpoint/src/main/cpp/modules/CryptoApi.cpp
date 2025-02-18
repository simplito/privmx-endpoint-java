//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

#include <jni.h>
#include <privmx/endpoint/core/Exception.hpp>
#include <privmx/endpoint/crypto/CryptoApi.hpp>
#include "../utils.hpp"
#include "../parser.h"
#include "../exceptions.h"

using namespace privmx::endpoint;

crypto::CryptoApi *getCryptoApi(JniContextUtils &ctx, jobject thiz) {
    jclass cls = ctx->GetObjectClass(thiz);
    jfieldID apiFID = ctx->GetFieldID(cls, "api", "Ljava/lang/Long;");
    jobject apiLong = ctx->GetObjectField(thiz, apiFID);
    if (apiLong == nullptr) {
        throw IllegalStateException("CryptoApi cannot be used");
    }
    return (crypto::CryptoApi *) ctx.getObject(apiLong).getLongValue();
}


extern "C"
JNIEXPORT jobject JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_init(JNIEnv *env, jobject thiz) {
    JniContextUtils ctx(env);
    jobject result;
    ctx.callResultEndpointApi<jobject>(
            &result,
            [&ctx]() {
                auto cryptoApi = crypto::CryptoApi::create();
                auto cryptoApi_ptr = new crypto::CryptoApi();
                *cryptoApi_ptr = cryptoApi;
                return ctx.long2jLong((jlong) cryptoApi_ptr);
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_deinit(JNIEnv *env, jobject thiz) {
    try {
        JniContextUtils ctx(env);
        //if null go to catch
        auto api = getCryptoApi(ctx, thiz);
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
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_generatePrivateKey(
        JNIEnv *env,
        jobject thiz,
        jstring random_seed
) {
    JniContextUtils ctx(env);
    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &random_seed]() {
                std::optional<std::string> random_seed_c = std::nullopt;
                if (random_seed != nullptr) {
                    random_seed_c = ctx.jString2string(random_seed);
                }
                return ctx->NewStringUTF(
                        getCryptoApi(ctx, thiz)->generatePrivateKey(
                                random_seed_c
                        ).c_str()
                );
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_derivePublicKey(
        JNIEnv *env,
        jobject thiz,
        jstring private_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(private_key, "Private key")) {
        return nullptr;
    }
    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &private_key]() {
                return ctx->NewStringUTF(
                        getCryptoApi(ctx, thiz)->derivePublicKey(
                                ctx.jString2string(private_key)
                        ).c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_encryptDataSymmetric(
        JNIEnv *env,
        jobject thiz,
        jbyteArray data,
        jbyteArray symmetric_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(data, "Data") ||
        ctx.nullCheck(symmetric_key, "Symmetric key")) {
        return nullptr;
    }
    jbyteArray result;
    ctx.callResultEndpointApi<jbyteArray>(
            &result,
            [&ctx, &thiz, &data, &symmetric_key]() {
                auto response = getCryptoApi(ctx, thiz)->encryptDataSymmetric(
                        core::Buffer::from(ctx.jByteArray2String(data)),
                        core::Buffer::from(ctx.jByteArray2String(symmetric_key)));
                jbyteArray result = ctx->NewByteArray(response.size());
                ctx->SetByteArrayRegion(result, 0, response.size(), (jbyte *) response.data());
                return result;
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_decryptDataSymmetric(
        JNIEnv *env,
        jobject thiz,
        jbyteArray data,
        jbyteArray symmetric_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(data, "Data") ||
        ctx.nullCheck(symmetric_key, "Symmetric key")) {
        return nullptr;
    }
    jbyteArray result;
    ctx.callResultEndpointApi<jbyteArray>(
            &result,
            [&ctx, &thiz, &data, &symmetric_key]() {
                auto response = getCryptoApi(ctx, thiz)->decryptDataSymmetric(
                        core::Buffer::from(ctx.jByteArray2String(data)),
                        core::Buffer::from(ctx.jByteArray2String(symmetric_key))
                );
                jbyteArray result = ctx->NewByteArray(response.size());
                ctx->SetByteArrayRegion(result, 0, response.size(), (jbyte *) response.data());
                return result;
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_signData(
        JNIEnv *env,
        jobject thiz,
        jbyteArray data,
        jstring private_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(data, "Data") ||
        ctx.nullCheck(private_key, "Private key")) {
        return nullptr;
    }
    jbyteArray result;
    ctx.callResultEndpointApi<jbyteArray>(
            &result,
            [&ctx, &thiz, &data, &private_key]() {
                auto response = getCryptoApi(ctx, thiz)->signData(
                        core::Buffer::from(ctx.jByteArray2String(data)),
                        ctx.jString2string(private_key)
                );

                jbyteArray result = ctx->NewByteArray(response.size());
                ctx->SetByteArrayRegion(result, 0, response.size(), (jbyte *) response.data());
                return result;
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_convertPEMKeyToWIFKey(
        JNIEnv *env,
        jobject thiz,
        jstring pem_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(pem_key, "Pem key")) {
        return nullptr;
    }
    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &pem_key]() {
                std::string convertedKey = getCryptoApi(ctx, thiz)->convertPEMKeytoWIFKey(
                        ctx.jString2string(pem_key));
                return ctx->NewStringUTF(convertedKey.c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_derivePrivateKey(
        JNIEnv *env,
        jobject thiz,
        jstring password,
        jstring salt
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(password, "Password") ||
        ctx.nullCheck(salt, "Salt")) {
        return nullptr;
    }
    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &password, &salt]() {
                auto result = getCryptoApi(ctx, thiz)->derivePrivateKey(
                        ctx.jString2string(password),
                        ctx.jString2string(salt)
                );
                return ctx->NewStringUTF(result.c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_derivePrivateKey2(
        JNIEnv *env,
        jobject thiz,
        jstring password,
        jstring salt
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(password, "Password") || ctx.nullCheck(salt, "Salt")) {
        return nullptr;
    }
    jstring result;
    ctx.callResultEndpointApi<jstring>(
            &result,
            [&ctx, &thiz, &password, &salt]() {
                auto result = getCryptoApi(ctx, thiz)->derivePrivateKey2(
                        ctx.jString2string(password),
                        ctx.jString2string(salt)
                );
                return ctx->NewStringUTF(result.c_str());
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_generateKeySymmetric(
        JNIEnv *env,
        jobject thiz
) {
    JniContextUtils ctx(env);
    jbyteArray result;
    ctx.callResultEndpointApi<jbyteArray>(
            &result,
            [&ctx, &thiz]() {
                auto response = getCryptoApi(ctx, thiz)->generateKeySymmetric();
                jbyteArray result = ctx->NewByteArray(response.size());
                ctx->SetByteArrayRegion(result, 0, response.size(), (jbyte *) response.data());
                return result;
            });
    if (ctx->ExceptionCheck()) {
        return nullptr;
    }
    return result;
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_simplito_java_privmx_1endpoint_modules_crypto_CryptoApi_verifySignature(
        JNIEnv *env,
        jobject thiz,
        jbyteArray data,
        jbyteArray signature,
        jstring public_key
) {
    JniContextUtils ctx(env);
    if (ctx.nullCheck(data, "Data") ||
        ctx.nullCheck(signature, "Signature") ||
        ctx.nullCheck(public_key, "Public key")
            ) {
        return JNI_FALSE;
    }
    jboolean result;
    ctx.callResultEndpointApi<jboolean>(
            &result,
            [&ctx, &thiz, &data, &signature, &public_key]() {
                auto response = getCryptoApi(ctx, thiz)->verifySignature(
                        core::Buffer::from(ctx.jByteArray2String(data)),
                        core::Buffer::from(ctx.jByteArray2String(signature)),
                        ctx.jString2string(public_key)
                );
                return response ? JNI_TRUE : JNI_FALSE;
            });
    if (ctx->ExceptionCheck()) {
        return JNI_FALSE;
    }
    return result;
}
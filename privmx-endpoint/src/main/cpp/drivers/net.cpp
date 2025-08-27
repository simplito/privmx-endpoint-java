/*
PrivMX Endpoint.
Copyright © 2024 Simplito sp. z o.o.

This file is part of the PrivMX Platform (https://privmx.dev).
This software is Licensed under the PrivMX Free License.

See the License for the specific language governing permissions and
limitations under the License.
*/

#include "jni.h"
#include <cstdlib>
#include <mutex>
#include <condition_variable>
#include <atomic>
#include <thread>
#include <cstring>
#include "include/privmx/drv/net.h"
#include "../jniUtils.h"

JavaVM *g_vm = nullptr;

JavaVM *getCurrentVM() {
    return g_vm;
}

const std::string t_group_name("privmx-net-drv");
jclass httpCls = nullptr;
jclass wsCls = nullptr;
jclass httpOptionsCls = nullptr;
jclass httpHeaderCls = nullptr;
jclass httpRequestOptionsCls = nullptr;
jclass httpResponseCls = nullptr;
jclass wsOptionsCls = nullptr;
jclass listenerCls = nullptr;

jobject httpHeader2Java(JNIEnv *env, privmxDrvNet_HttpHeader options) {
    jmethodID httpHeaderInitMID = env->GetMethodID(
            httpHeaderCls,
            "<init>",
            "("
            "Ljava/lang/String;"
            "Ljava/lang/String;"
            ")V"
    );
    return env->NewObject(
            httpHeaderCls,
            httpHeaderInitMID,
            env->NewStringUTF(options.name),
            env->NewStringUTF(options.value)
    );
}

jobject httpRequestOptions2Java(JNIEnv *env, privmxDrvNet_HttpRequestOptions options) {
    jmethodID httpRequestOptionsInitMID = env->GetMethodID(
            httpRequestOptionsCls,
            "<init>",
            "("
            "Ljava/lang/String;"
            "Ljava/lang/String;"
            "Ljava/lang/String;"
            "Ljava/util/List;"
            "I"
            "Z"
            ")V"
    );

    jclass arrayCls = env->FindClass("java/util/ArrayList");
    jmethodID initArrayMID = env->GetMethodID(
            arrayCls,
            "<init>",
            "()V");
    jmethodID addToArrayMID = env->GetMethodID(
            arrayCls,
            "add",
            "(Ljava/lang/Object;)Z");
    jobject array = env->NewObject(arrayCls, initArrayMID);
    for (int i = 0; i < options.headerslen; ++i) {
        auto header_c = options.headers[i];
        env->CallBooleanMethod(
                array,
                addToArrayMID,
                httpHeader2Java(env, header_c)
        );
    }
    auto contentType =
            options.contentType != NULL ? options.contentType : "application/octet-stream";
    return env->NewObject(
            httpRequestOptionsCls,
            httpRequestOptionsInitMID,
            env->NewStringUTF(options.path),
            env->NewStringUTF(options.method),
            env->NewStringUTF(contentType),
            array,
            (jint) options.headerslen,
            (jboolean) options.keepAlive
    );
}

jobject wsOptions2Java(JNIEnv *env, privmxDrvNet_WsOptions options) {
    jmethodID wsOptionsInitMID = env->GetMethodID(
            wsOptionsCls,
            "<init>",
            "("
            "Ljava/lang/String;"
            ")V"
    );

    return env->NewObject(
            wsOptionsCls,
            wsOptionsInitMID,
            env->NewStringUTF(options.url)
    );
}

struct privmxDrvNet_Http {
    jobject httpClient;
    jstring uri;
};

struct privmxDrvNet_Ws {
    jobject websocket;

    void send(const char *data, int datalen);

    void disconnect();
};

struct privmxNativeListner {
    void (*onopen)(void *ctx);

    void (*onmessage)(void *ctx, const char *msg, int msglen);

    void (*onerror)(void *ctx, const char *msg, int msglen);

    void (*onclose)(void *ctx, int wasClean);

    void *ctx;
};

int privmxDrvNet_wsConnect(
        const privmxDrvNet_WsOptions *options,
        void(*onopen)(void *ctx),
        void(*onmessage)(void *ctx, const char *msg, int msglen),
        void(*onerror)(void *ctx, const char *msg, int msglen),
        void(*onclose)(void *ctx, int wasClean),
        void *ctx,
        privmxDrvNet_Ws **res
) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );
    privmxNativeListner *listener_c = new privmxNativeListner{
            onopen = onopen,
            onmessage = onmessage,
            onerror = onerror,
            onclose = onclose,
            ctx = ctx
    };
    jmethodID wsInitMID = env->GetMethodID(
            wsCls,
            "<init>",
            "(Ljava/lang/String;)V"
    );
    jobject ws = env->NewObject(
            wsCls,
            wsInitMID,
            env->NewStringUTF(options->url)
    );
    jmethodID wsConnectMID = env->GetMethodID(
            wsCls,
            "connect",
            "(Lcom/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_WsOptions;J)V"
    );

    env->CallVoidMethod(
            ws,
            wsConnectMID,
            wsOptions2Java(env, *options),
            //TODO(MEMORY_LEAK): Pass listener in other place here is memory leak
            (jlong) listener_c
    );

    *res = new privmxDrvNet_Ws{
            env->NewGlobalRef(ws)
    };

    return 0;
}

void privmxDrvNet_Ws::send(const char *data, int datalen) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );
    jmethodID wsSendMID = env->GetMethodID(wsCls, "send", "([B)V");
    jbyteArray jdata = env->NewByteArray(datalen);
    env->SetByteArrayRegion(
            jdata,
            0,
            datalen,
            (jbyte *) data
    );
    env->CallVoidMethod(websocket, wsSendMID, jdata);
}

void privmxDrvNet_Ws::disconnect() {
}


int privmxDrvNet_wsSend(privmxDrvNet_Ws *ws, const char *data, int datalen) {
    ws->send(data, datalen);
    return 0;
}

int privmxDrvNet_wsClose(privmxDrvNet_Ws *ws) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );
    jmethodID wsDisconnectMID = env->GetMethodID(wsCls, "disconnect", "()V");
    env->CallVoidMethod(ws->websocket, wsDisconnectMID);
    env->DeleteGlobalRef(ws->websocket);
    env->DeleteGlobalRef(httpCls);
    env->DeleteGlobalRef(wsCls);
    env->DeleteGlobalRef(listenerCls);
    env->DeleteGlobalRef(httpOptionsCls);
    env->DeleteGlobalRef(httpHeaderCls);
    env->DeleteGlobalRef(httpRequestOptionsCls);
    env->DeleteGlobalRef(httpResponseCls);
    env->DeleteGlobalRef(wsOptionsCls);
    return 0;
}

int privmxDrvNet_wsFree(privmxDrvNet_Ws *ws) {
    delete ws;
    return 0;
}

int privmxDrvNet_freeMem(void *ptr) {
    free(ptr);
    return 0;
}

int
privmxDrvNet_httpCreateSession(const privmxDrvNet_HttpOptions *options, privmxDrvNet_Http **res) {

    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );
    if (httpCls == nullptr || env == nullptr || wsCls == nullptr) return 2;
//    return 2;
    jmethodID httpInitMID = env->GetMethodID(
            httpCls,
            "<init>",
            "(Ljava/lang/String;)V"
    );
    if (env->ExceptionCheck()) {
        return 5;
    }
    if (httpInitMID == nullptr) return 8;
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        return 3;
    }
//    if(httpInitMID == nullptr) return 3;
    jstring baseUrl = env->NewStringUTF(options->baseUrl);
    if (baseUrl == nullptr) return 4;
    jobject http = env->NewObject(
            httpCls,
            httpInitMID,
            baseUrl
    );
    *res = new privmxDrvNet_Http{
            env->NewGlobalRef(http),
            baseUrl,
    };
    return 0;
}

int privmxDrvNet_httpDestroySession(privmxDrvNet_Http *http) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );
    env->DeleteGlobalRef(http->httpClient);
    return 0;
}

int privmxDrvNet_httpFree(privmxDrvNet_Http *http) {
    delete http;
    return 0;
}

int privmxDrvNet_httpRequest(
        privmxDrvNet_Http *http,
        const char *data,
        int datalen,
        const privmxDrvNet_HttpRequestOptions *options,
        int *statusCode,
        char **out,
        unsigned int *outlen
) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            getCurrentVM(),
            t_group_name
    );

    jmethodID requestMID = env->GetMethodID(
            httpCls,
            "makeRequest",
            "("
            "[B"
            "Lcom/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_HttpRequestOptions;"
            ")Lcom/simplito/java/privmx_endpoint/drv/net$HttpResponse;"
    );
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        return 4;
    }
    jbyteArray jdata = env->NewByteArray(datalen);
    env->SetByteArrayRegion(
            jdata,
            0,
            datalen,
            (jbyte *) data
    );
    if (options == nullptr) return 8;
    if (data == nullptr) return 9;

    jobject httpResponse = env->CallObjectMethod(
            http->httpClient,
            requestMID,
            jdata,
            httpRequestOptions2Java(env, *options)
    );
    if (env->ExceptionCheck()) {
        env->ExceptionDescribe();
        return 1;
    }

    jfieldID statusCodeFID = env->GetFieldID(
            httpResponseCls,
            "statusCode",
            "I"
    );
    jfieldID outFID = env->GetFieldID(
            httpResponseCls,
            "out",
            "[B"
    );

    jint jstatusCode = env->GetIntField(httpResponse, statusCodeFID);
    jbyteArray jout = reinterpret_cast<jbyteArray>(env->GetObjectField(httpResponse, outFID));
    jsize size = env->GetArrayLength(jout);
    jbyte *buf = reinterpret_cast<jbyte *>(malloc(size));
    env->GetByteArrayRegion(jout, 0, size, buf);

    *statusCode = (int) jstatusCode;
    *out = (char *) buf;
    *outlen = size;
    return 0;
}

int privmxDrvNet_version(unsigned int *version) {
    *version = 1;
    return 0; // version: 1
}

int privmxDrvNet_setConfig(const char *config) {
    //TODO: Not yet implemented
    return 0;
}

//Callbacks
privmxNativeListner *getListener(
        JNIEnv *env,
        jobject thiz //net.Websocket.WebsocketListener
) {
    jclass clazz = env->GetObjectClass(thiz);
    jfieldID listenerPtrFID = env->GetFieldID(clazz, "listener_ptr", "J");
    jlong listenerPtr = env->GetLongField(thiz, listenerPtrFID);
    //TODO: Throw null pointer exception (?)
    return (privmxNativeListner *) listenerPtr;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_drv_net_00024WebSocket_00024WebsocketListener_onClose(
        JNIEnv *env,
        jobject thiz,
        jboolean wasClean
) {
    privmxNativeListner *listener = getListener(env, thiz);
    listener->onclose(listener->ctx, wasClean == JNI_TRUE);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_drv_net_00024WebSocket_00024WebsocketListener_onError(
        JNIEnv *env,
        jobject thiz,
        jstring message
) {
    privmxNativeListner *listener = getListener(env, thiz);
    const char *tmp = env->GetStringUTFChars(message, NULL);
    std::string message_c(tmp);
    env->ReleaseStringUTFChars(message, tmp);
    listener->onerror(listener->ctx, message_c.c_str(), message_c.length());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_drv_net_00024WebSocket_00024WebsocketListener_onMessage(
        JNIEnv *env,
        jobject thiz,
        jbyteArray message
) {
    privmxNativeListner *listener = getListener(env, thiz);

    jsize size = env->GetArrayLength(message);
    jbyte *bytes = env->GetByteArrayElements(message, NULL);
    std::string message_c((const char *) bytes, size);
    env->ReleaseByteArrayElements(message, bytes, JNI_ABORT);

    listener->onmessage(listener->ctx, message_c.c_str(), size);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_simplito_java_privmx_1endpoint_drv_net_00024WebSocket_00024WebsocketListener_onOpen(
        JNIEnv *env,
        jobject thiz
) {
    privmxNativeListner *listener = getListener(env, thiz);
    listener->onopen(listener->ctx);
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            vm,
            t_group_name
    );
    g_vm = vm;
    //TODO: Optimize this to load single class and next loads should use classLoader from this class
    httpCls = (jclass) env->NewGlobalRef(
            env->FindClass("com/simplito/java/privmx_endpoint/drv/net$HttpSession"));
    wsCls = (jclass) env->NewGlobalRef(
            env->FindClass("com/simplito/java/privmx_endpoint/drv/net$WebSocket"));

    listenerCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$WebSocket$WebsocketListener"));
    httpOptionsCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_HttpOptions"));
    httpHeaderCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_HttpHeader"));
    httpRequestOptionsCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_HttpRequestOptions"));
    httpResponseCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$HttpResponse"));
    wsOptionsCls = (jclass) env->NewGlobalRef(env->FindClass(
            "com/simplito/java/privmx_endpoint/drv/net$privmxDrvNet_WsOptions"));

    return JNI_VERSION_1_6;
}

void JNI_OnUnload(JavaVM *vm, void *reserved){
    JNIEnv *env = privmx::wrapper::jni::AttachCurrentThreadIfNeeded(
            vm,
            t_group_name
    );
    env->DeleteGlobalRef(httpCls);
    env->DeleteGlobalRef(wsCls);
    env->DeleteGlobalRef(listenerCls);
    env->DeleteGlobalRef(httpOptionsCls);
    env->DeleteGlobalRef(httpHeaderCls);
    env->DeleteGlobalRef(httpRequestOptionsCls);
    env->DeleteGlobalRef(httpResponseCls);
    env->DeleteGlobalRef(wsOptionsCls);
}
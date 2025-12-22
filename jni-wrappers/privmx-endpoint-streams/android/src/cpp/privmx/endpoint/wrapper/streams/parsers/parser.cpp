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

#include "privmx/endpoint/wrapper/parsers/parser.h"

using namespace privmx::endpoint;

// streams
privmx::endpoint::stream::DeviceType parseDeviceType(JniContextUtils &ctx, jobject type) {
    jclass itemClass = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/streams/DeviceType)");
    jmethodID ordinalMethod = ctx->GetMethodID(itemClass, "ordinal", "()I");
    jint ordinalMID = ctx->CallIntMethod(type, ordinalMethod);

    privmx::endpoint::stream::DeviceType type_c = static_cast<privmx::endpoint::stream::DeviceType >(ordinalMID);

    return type_c;
}

privmx::endpoint::stream::MediaDevice parseMediaDevice(JniContextUtils &ctx, jobject mediaDevice) {
    auto result = privmx::endpoint::stream::MediaDevice();

    jclass mediaDeviceCls = ctx->GetObjectClass(mediaDevice);
    jfieldID name = ctx->GetFieldID(mediaDeviceCls, "name", "Ljava/lang/String;");
    jfieldID id = ctx->GetFieldID(mediaDeviceCls, "id", "Ljava/lang/String;");
    jfieldID type = ctx->GetFieldID(mediaDeviceCls, "type",
                                    "Lcom/simplito/java/privmx_endpoint/model/streams/DeviceType;");

    result.name = ctx.jString2string(
            (jstring) ctx->GetObjectField(mediaDevice, name));
    result.id = ctx.jString2string(
            (jstring) ctx->GetObjectField(mediaDevice, id));
    result.type = parseDeviceType(ctx, ctx->GetObjectField(mediaDevice, type));

    return result;
}

privmx::endpoint::stream::StreamHandle parseStreamHandle(
        JniContextUtils &ctx,
        jobject streamHandle
) {
    jclass streamHandleCls = ctx->GetObjectClass(streamHandle);
    jfieldID valueFID = ctx->GetFieldID(streamHandleCls, "value", "Ljava/lang/Long;");

    return jobject2long(ctx, ctx->GetObjectField(streamHandle, valueFID));
}

privmx::endpoint::stream::Settings parseSettings(JniContextUtils &ctx, jobject settings){
    auto result = privmx::endpoint::stream::Settings();
    return result;
}

privmx::endpoint::stream::StreamSettings parseStreamSettings(JNIEnv *env,jobject streamSettings){
    privmx::endpoint::stream::StreamSettings result;
    JniContextUtils ctx(env);
    jclass cls = ctx->GetObjectClass(streamSettings);
//    StreamSettingsJNI streamSettingsJni (env, streamSettings);

    jfieldID settingsFID = env->GetFieldID(
            env->GetObjectClass(streamSettings),
            "settings",
            "Lcom/simplito/java/privmx_endpoint/model/streams/Settings;"
    );

    jobject jsettings = ctx->GetObjectField(streamSettings, settingsFID);

    result.settings = parseSettings(ctx, jsettings);
    result.OnFrame = streamSettingsJni.OnFrame;
    result.OnVideo = streamSettingsJni.OnVideo;
    result.OnVideoRemove = streamSettingsJni.OnVideoRemove;

    return result;
}

privmx::endpoint::stream::StreamSubscription parseStreamSubscription(JniContextUtils &ctx, jobject streamSubscription){
 privmx::endpoint::stream::StreamSubscription result;
    jclass cls = ctx->GetObjectClass(streamSubscription);
    jfieldID streamIdFID = ctx->GetFieldID(
            ctx->GetObjectClass(streamSubscription),
            "streamId",
            "J"
    );

    jfieldID trackIdFID = ctx->GetFieldID(
            ctx->GetObjectClass(streamSubscription),
            "streamTrackId",
            "Ljava/lang/String;"
    );

    jobject streamId = ctx->GetObjectField(streamSubscription, streamIdFID);
    jobject streamTrackId =  ctx->GetObjectField(streamSubscription, trackIdFID);

    result.streamId = jobject2long(ctx, streamId);
    if(streamTrackId != nullptr) result.streamTrackId = jobject2string(ctx, streamTrackId);

    return result;
}


// java -> c++
template<typename T>
std::vector<T> jArrayToVector(
        JniContextUtils &ctx,
        jobjectArray jArray,
        std::function<T(JniContextUtils &, jobject)> fun
) {
    std::vector<T> result;

    for (int i = 0; i < ctx->GetArrayLength(jArray); i++) {
        jobject element = ctx->GetObjectArrayElement(jArray, i);
        result.push_back(fun(ctx, element));
    }
    return result;
}

int64_t jobject2long(JniContextUtils &ctx, jobject jLong) {
    jclass longClass = ctx->FindClass("java/lang/Long");
    jmethodID longValueMethod = ctx->GetMethodID(longClass, "longValue", "()J");
    jlong value = ctx->CallLongMethod(jLong, longValueMethod);
    return (int64_t) value;
}

std::string jobject2string(JniContextUtils &ctx, jobject jString) {
    auto js = (jstring) jString;
    return ctx.jString2string(js);
}


// c++ -> java
template<typename T, typename F>
jobject vectorTojArray(
        JniContextUtils &ctx,
        const std::vector<T> &vector,
        F fun
) {
    jclass arrayListCls = ctx->FindClass("java/util/ArrayList");
    jmethodID initMID = ctx->GetMethodID(arrayListCls, "<init>", "()V");
    jmethodID addToListMID = ctx->GetMethodID(arrayListCls, "add", "(Ljava/lang/Object;)Z");

    jobject listObj = ctx->NewObject(arrayListCls, initMID);

    for (const auto &item: vector) {
        jobject jItem = fun(ctx, item);
        ctx->CallBooleanMethod(listObj, addToListMID, jItem);
    }

    return listObj;
}

template<typename T, typename F>
jobject pagingList2Java(
        JniContextUtils &ctx,
        privmx::endpoint::core::PagingList<T> pagingList,
        F fun
) {
    jclass pagingListCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/PagingList");
    jmethodID pagingListInitMID = ctx->GetMethodID(pagingListCls, "<init>",
            "(Ljava/lang/Long;Ljava/util/List;)V"
    );

    jobject array = vectorTojArray(ctx, pagingList.readItems, fun);

    return ctx->NewObject(
            pagingListCls,
            pagingListInitMID,
            ctx.long2jLong(pagingList.totalAvailable),
            array
    );
}

jobject string2jobject(JniContextUtils &ctx, const std::string &str) {
    return ctx->NewStringUTF(str.c_str());
}

jobject long2jobject(JniContextUtils &ctx, const int64_t &lng) {
    return ctx.long2jLong(lng);
}
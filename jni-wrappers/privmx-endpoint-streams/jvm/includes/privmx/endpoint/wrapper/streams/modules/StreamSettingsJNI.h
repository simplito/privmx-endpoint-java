#include <jni.h>
#include <privmx/endpoint/stream/StreamApi.hpp>


#ifndef PRIVMXENDPOINT_STREAMSETTINGSJNI_H
#define PRIVMXENDPOINT_STREAMSETTINGSJNI_H

namespace privmx::wrapper::streams {
    struct StreamSettingsJNI : privmx::endpoint::stream::StreamSettings {
    public:
        StreamSettingsJNI(JNIEnv *env, jobject jstreamSettings);

//        StreamSettingsJNI(JNIEnv *env);
        ~StreamSettingsJNI();

        void onFrame2(JNIEnv *env, jobject a, jobject b, jobject c, jstring d);

        std::optional<std::function<void(const std::string&)>> OnVideo;
//        void OnVideo(JNIEnv *env, jobject a) override;

        void onVideoRemove2(JNIEnv *env, jobject a);

    private:
        jobject jstreamSettings;
        JavaVM *javaVM;
        jclass cls;
    };
};
#endif //PRIVMXENDPOINT_STREAMSETTINGSJNI_H

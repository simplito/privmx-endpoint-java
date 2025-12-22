#ifndef PRIVMXENDPOINT_STREAMSETTINGSJNI_H
#define PRIVMXENDPOINT_STREAMSETTINGSJNI_H


#include <jni.h>
#include <privmx/endpoint/stream/StreamApi.hpp>

    struct StreamSettingsJNI : privmx::endpoint::stream::StreamSettings{
    public:
//        StreamSettingsJNI(JNIEnv *env, jobject jstreamJoinSettings);
        StreamSettingsJNI(JNIEnv *env);
        ~StreamSettingsJNI();

        void onFrame2(JNIEnv *env, jobject a, jobject b, jobject c, jstring d);
        void onVideo2(JNIEnv *env, jobject a);
        void onVideoRemove2(JNIEnv *env, jobject a);

    private:
        JavaVM *javaVM;
        jclass cls;
    };

#endif //PRIVMXENDPOINT_STREAMSETTINGSJNI_H

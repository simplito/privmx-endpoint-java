//
// Created by Dawid Jenczewski on 13/02/2025.
//

#ifndef PRIVMXENDPOINT_WEBRTCINTERFACEJNI_H
#define PRIVMXENDPOINT_WEBRTCINTERFACEJNI_H

#include "jni.h"
#include <privmx/endpoint/stream/WebRTCInterface.hpp>

using namespace privmx::endpoint::stream;

class WebRTCInterfaceJNI : public WebRTCInterface {
public:
    WebRTCInterfaceJNI(JNIEnv *env, jobject jwebRTCInterface);

    std::string createOfferAndSetLocalDescription() override;

    std::string
    createAnswerAndSetDescriptions(const std::string &sdp, const std::string &type) override;

    void setAnswerAndSetRemoteDescription(const std::string &sdp, const std::string &type) override;

    void close() override;

    void updateKeys(const std::vector<Key> &keys) override;

private:
    jobject jwebRTCInterface;
    JavaVM *javaVM;
    jclass jwebRTCInterfaceClass;
};


#endif //PRIVMXENDPOINT_WEBRTCINTERFACEJNI_H

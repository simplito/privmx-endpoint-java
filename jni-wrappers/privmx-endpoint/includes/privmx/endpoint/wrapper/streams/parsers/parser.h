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

//#ifndef PRIVMX_POCKET_LIB_PARSER_H
//#define PRIVMX_POCKET_LIB_PARSER_H

#include "privmx/endpoint/wrapper/parsers/parser.h"
#include "privmx/endpoint/wrapper/utils/utils.hpp"
#include "privmx/endpoint/wrapper/streams/modules/StreamSettingsJNI.h"
#include <jni.h>
#include "model_native_initializers.h"

// streams
privmx::endpoint::stream::DeviceType parseDeviceType(JniContextUtils &ctx, jobject type);

privmx::endpoint::stream::MediaDevice parseMediaDevice(JniContextUtils &ctx, jobject mediaDevice);

privmx::endpoint::stream::StreamHandle parseStreamHandle(JniContextUtils &ctx, jobject streamHandle);

privmx::endpoint::stream::Settings parseSettings(JniContextUtils &ctx, jobject settings);

privmx::endpoint::stream::StreamSettings parseStreamSettings(JNIEnv *env, jobject streamSettings);

privmx::endpoint::stream::StreamSubscription parseStreamSubscription(JniContextUtils &ctx, jobject streamSubscription);

privmx::endpoint::stream::SdpWithTypeModel parseSdpWithTypeModel(JniContextUtils &ctx, jobject sdpWithTypeModel);

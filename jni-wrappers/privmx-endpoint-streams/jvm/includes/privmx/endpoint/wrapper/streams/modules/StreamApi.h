#include <jni.h>
#include <privmx/endpoint/stream/StreamApi.hpp>
//#include "../../../../../../../../privmx-endpoint/src/main/cpp/utils.hpp"
//#include "../../../../../../../../privmx-endpoint/src/main/cpp/model_native_initializers.h"
//#include "../../../../../../../../privmx-endpoint/src/main/cpp/modules/Connection.h"
//#include "../../../../../../../../privmx-endpoint/src/main/cpp/modules/EventApi.h"
#include "privmx/endpoint/modules/EventApi.h"
#include "privmx/endpoint/modules/Connection.h"
#include "privmx/endpoint/parsers/model_native_initializers.h"
#include "privmx/endpoint/utils/utils.hpp"

using namespace privmx::endpoint;

#ifndef PRIVMXENDPOINT_STREAMAPI_H
#define PRIVMXENDPOINT_STREAMAPI_H

privmx::endpoint::stream::StreamApi *getStreamApi(JniContextUtils &ctx, jobject streamApiInstance);

#endif //PRIVMXENDPOINT_STREAMAPI_H

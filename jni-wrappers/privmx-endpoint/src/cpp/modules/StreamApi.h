#include <jni.h>
#include <privmx/endpoint/stream/StreamApi.hpp>
#include "privmx/endpoint/wrapper/utils/utils.hpp"
#include "privmx/endpoint/wrapper/parsers/model_native_initializers.h"
#include "privmx/endpoint/wrapper/modules/Connection.h"
#include "privmx/endpoint/wrapper/modules/EventApi.h"

using namespace privmx::endpoint;

#ifndef PRIVMXENDPOINT_STREAMAPI_H
#define PRIVMXENDPOINT_STREAMAPI_H

privmx::endpoint::stream::StreamApi *getStreamApi(JniContextUtils &ctx, jobject streamApiInstance);

#endif //PRIVMXENDPOINT_STREAMAPI_H

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

//#ifndef PRIVMXENDPOINTWRAPPER_MODEL_NATIVE_INITIALIZERS_H
//#define PRIVMXENDPOINTWRAPPER_MODEL_NATIVE_INITIALIZERS_H

#include <jni.h>
#include "privmx/endpoint/wrapper/utils/utils.hpp"
#include "privmx/endpoint/wrapper/parsers/parser.h"
#include "privmx/endpoint/stream/Types.hpp"
#include "privmx/endpoint/stream/webrtc/Types.hpp"
#include "privmx/endpoint/stream/StreamApi.hpp"
#include "privmx/endpoint/stream/StreamApiLow.hpp"
#include "privmx/endpoint/stream/WebRTCInterface.hpp"
#include "privmx/endpoint/wrapper/parsers/model_native_initializers.h"

namespace privmx {
    namespace wrapper {
        namespace streams {
            //Stream
            jobject streamRoom2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamRoom streamRoom_c);

            jobject stream2Java(JniContextUtils &ctx, privmx::endpoint::stream::Stream stream_c);

            jobject deviceType2Java(JniContextUtils &ctx, privmx::endpoint::stream::DeviceType deviceType_c);

            jobject mediaDevice2Java(JniContextUtils &ctx, privmx::endpoint::stream::MediaDevice mediaDevice_c);

            jobject streamHandle2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamHandle streamHandle_c);

            jobject streamTrackInfo2Java(JniContextUtils &ctx,privmx::endpoint::stream::StreamTrackInfo streamTrackInfo_c);

            jobject streamInfo2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamInfo streamInfo_c);

            jobject publishedStreamData2Java(JniContextUtils &ctx, privmx::endpoint::stream::PublishedStreamData publishedStreamData_c);

            jobject streamPublishResult2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamPublishResult streamPublishResult_c);

            jobject remoteStreamId2Java(JniContextUtils &ctx, privmx::endpoint::stream::RemoteStreamId remoteStreamId_c);

            jobject frame2Java(JniContextUtils &ctx, privmx::endpoint::stream::Frame &frame_c);

            jobject keyType2Java(JniContextUtils &ctx, privmx::endpoint::stream::KeyType keyType_c);

            jobject key2Java(JniContextUtils &ctx, privmx::endpoint::stream::Key key_c);

            jobject
            streamRoom2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamRoom streamRoom_c);

            jobject
            stream2Java(JniContextUtils &ctx, privmx::endpoint::stream::Stream stream_c);

            jobject
            turnCredentials2Java(JniContextUtils &ctx, privmx::endpoint::stream::TurnCredentials turnCredentials_c);

            jobject
            sdpWithTypeModel2Java(JniContextUtils &ctx, privmx::endpoint::stream::SdpWithTypeModel sdpWithTypeModel_c);
            jobject
                    streamTrackModificationPair2Java(JniContextUtils & ctx, endpoint::stream::StreamTrackModificationPair
            streamTrackModificationPair);

            jobject
                    streamTrackModification2Java(JniContextUtils & ctx, endpoint::stream::StreamTrackModification
            streamTrackModification);

            jobject
                    streamUpdatedEventData2Java(JniContextUtils & ctx, privmx::endpoint::stream::StreamUpdatedEventData
            data);

            jobject
                    updatedStreamData2Java(JniContextUtils & ctx, endpoint::stream::UpdatedStreamData
            updatedStreamData);

            jobject
                    streamRoomDeletedEventData2Java(JniContextUtils & ctx, privmx::endpoint::stream::StreamRoomDeletedEventData
            data);

            jobject
                    streamPublishedEventData2Java(JniContextUtils & ctx, privmx::endpoint::stream::StreamPublishedEventData
            data);

            jobject
                    streamEventData2Java(JniContextUtils & ctx, privmx::endpoint::stream::StreamEventData
            data);


        } // streams
    } // wrapper
} // privmx

//#endif //PRIVMXENDPOINTWRAPPER_MODEL_NATIVE_INITIALIZERS_H

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
#include "privmx/endpoint/wrapper/streams/parsers/model_native_initializers.h"

namespace privmx {
    namespace wrapper {
        namespace streams {

            //Streams
            jobject keyType2Java(JniContextUtils &ctx, privmx::endpoint::stream::KeyType keyType_c) {
                jclass keyTypeClass = ctx.findClass("com/simplito/java/privmx_endpoint/model/KeyType");
                jfieldID caseFieldId = nullptr;
                switch (keyType_c) {
                    case privmx::endpoint::stream::KeyType::LOCAL:
                        caseFieldId = ctx->GetStaticFieldID(
                                keyTypeClass,
                                "LOCAL",
                                "Lcom/simplito/java/privmx_endpoint/model/KeyType;");
                        break;
                    default:
                        caseFieldId = ctx->GetStaticFieldID(
                                keyTypeClass,
                                "REMOTE",
                                "Lcom/simplito/java/privmx_endpoint/model/KeyType;");
                        break;
                }
                return ctx->GetStaticObjectField(keyTypeClass, caseFieldId);
            }

            jobject key2Java(JniContextUtils &ctx, privmx::endpoint::stream::Key key_c) {
                jclass keyCls = ctx.findClass(
                        "com/simplito/java/privmx_endpoint/model/Key");
                jmethodID initKeyMID = ctx->GetMethodID(
                        keyCls,
                        "<init>",
                        "(Ljava/lang/String;"
                        "[B"
                        "Lcom/simplito/java/privmx_endpoint/model/KeyType;"
                        ")V"
                );

                jbyteArray jKey = ctx->NewByteArray(key_c.key.size());
                ctx->SetByteArrayRegion(jKey, 0, key_c.key.size(), (jbyte *) key_c.key.data());

                return ctx->NewObject(
                        keyCls,
                        initKeyMID,
                        ctx->NewStringUTF(key_c.keyId.c_str()),
                        jKey,
                        keyType2Java(ctx, key_c.type)
                );
            }

//        jobject
//        streamRoom2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamRoom streamRoom_c) {
//            jclass streamRoomCls = ctx.findClass(
//                    "com/simplito/java/privmx_endpoint/model/StreamRoom");
//            jmethodID initStreamRoomMID = ctx->GetMethodID(
//                    streamRoomCls,
//                    "<init>",
//                    "("
//                    "Ljava/lang/String;"  //contextId
//                    "Ljava/lang/String;"  //streamRoomId
//                    "Ljava/lang/Long;"  //createDate
//                    "Ljava/lang/String;"  //creator
//                    "Ljava/lang/Long;"  //lastModificationDate
//                    "Ljava/lang/String;"  //lastModifier
//                    "Ljava/util/List;"  //users
//                    "Ljava/util/List;"  //managers
//                    "Ljava/lang/Long;"  //version
//                    "[B"  //publicMeta
//                    "[B"  //privateMeta
//                    "Lcom/simplito/java/privmx_endpoint/model/ContainerPolicy;"  //policy
//                    "Ljava/lang/Long;"  //statusCode
//                    ")V"
//            );
//
//            jclass arrayCls = ctx.findClass("java/util/ArrayList");
//            jmethodID initArrayMID = ctx->GetMethodID(
//                    arrayCls,
//                    "<init>",
//                    "()V");
//            jmethodID addToArrayMID = ctx->GetMethodID(
//                    arrayCls,
//                    "add",
//                    "(Ljava/lang/Object;)Z"
//            );
//            jstring contextId = ctx->NewStringUTF(streamRoom_c.contextId.c_str());
//            jstring streamRoomId = ctx->NewStringUTF(streamRoom_c.streamRoomId.c_str());
//            jstring creator = ctx->NewStringUTF(streamRoom_c.creator.c_str());
//            jstring lastModifier = ctx->NewStringUTF(streamRoom_c.lastModifier.c_str());
//            jobject users = ctx->NewObject(arrayCls, initArrayMID);
//            jobject managers = ctx->NewObject(arrayCls, initArrayMID);
//            jbyteArray publicMeta = ctx->NewByteArray(streamRoom_c.publicMeta.size());
//            jbyteArray privateMeta = ctx->NewByteArray(streamRoom_c.privateMeta.size());
//            ctx->SetByteArrayRegion(publicMeta, 0, streamRoom_c.publicMeta.size(),
//                    (jbyte *) streamRoom_c.publicMeta.data());
//            ctx->SetByteArrayRegion(privateMeta, 0, streamRoom_c.privateMeta.size(),
//                    (jbyte *) streamRoom_c.privateMeta.data());
//            for (auto &user: streamRoom_c.users) {
//                ctx->CallBooleanMethod(users,
//                        addToArrayMID,
//                        ctx->NewStringUTF(user.c_str()));
//            }
//            for (auto &manager: streamRoom_c.managers) {
//                ctx->CallBooleanMethod(managers,
//                        addToArrayMID,
//                        ctx->NewStringUTF(manager.c_str()));
//            }
//            return ctx->NewObject(
//                    streamRoomCls,
//                    initStreamRoomMID,
//                    contextId,
//                    streamRoomId,
//                    ctx.long2jLong(streamRoom_c.createDate),
//                    creator,
//                    ctx.long2jLong(streamRoom_c.lastModificationDate),
//                    lastModifier,
//                    users,
//                    managers,
//                    ctx.long2jLong(streamRoom_c.version),
//                    publicMeta,
//                    privateMeta,
//                    containerPolicy2Java(ctx, streamRoom_c.policy),
//                    ctx.long2jLong(streamRoom_c.statusCode)
//            );
//        }

            jobject stream2Java(JniContextUtils &ctx, privmx::endpoint::stream::Stream stream_c) {
                jclass streamCls = ctx.findClass(
                        "com/simplito/java/privmx_endpoint/model/Stream");
                jmethodID initStreamMID = ctx->GetMethodID(
                        streamCls,
                        "<init>",
                        "("
                        "Ljava/lang/Long;"  //streamId
                        "Ljava/lang/String;"  //userId
                        ")V"
                );
                return ctx->NewObject(
                        streamCls,
                        initStreamMID,
                        ctx.long2jLong(stream_c.streamId),
                        ctx->NewStringUTF(stream_c.userId.c_str())
                );
            }

            jobject
            turnCredentials2Java(JniContextUtils &ctx,
                    privmx::endpoint::stream::TurnCredentials turnCredentials_c) {
                jclass turnCredentialsCls = ctx.findClass(
                        "com/simplito/java/privmx_endpoint/model/TurnCredentials");
                jmethodID initTurnCredentialsMID = ctx->GetMethodID(
                        turnCredentialsCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"  //url
                        "Ljava/lang/String;"  //username
                        "Ljava/lang/String;"  //password
                        "Ljava/lang/Long;"  //expirationTime
                        ")V"
                );
                return ctx->NewObject(
                        turnCredentialsCls,
                        initTurnCredentialsMID,
                        ctx->NewStringUTF(turnCredentials_c.url.c_str()),
                        ctx->NewStringUTF(turnCredentials_c.username.c_str()),
                        ctx->NewStringUTF(turnCredentials_c.password.c_str()),
                        ctx.long2jLong(turnCredentials_c.expirationTime)
                );
            }

            jobject
            sdpWithTypeModel2Java(JniContextUtils &ctx,
                    privmx::endpoint::stream::SdpWithTypeModel sdpWithTypeModel_c) {
                jclass sdpWithTypeModelCls = ctx.findClass(
                        "com/simplito/java/privmx_endpoint/model/stream/SdpWithTypeModel");
                jmethodID initSdpWithTypeModelMID = ctx->GetMethodID(
                        sdpWithTypeModelCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"  //url
                        "Ljava/lang/String;"  //username
                        ")V"
                );
                return ctx->NewObject(
                        sdpWithTypeModelCls,
                        initSdpWithTypeModelMID,
                        ctx->NewStringUTF(sdpWithTypeModel_c.sdp.c_str()),
                        ctx->NewStringUTF(sdpWithTypeModel_c.type.c_str())
                );
            }

            // Stream
            jobject streamRoom2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamRoom streamRoom_c
            ) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamRoom");
                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"    // contextId
                        "Ljava/lang/String;"    // streamRoomId
                        "Ljava/lang/Long;"      // createDate
                        "Ljava/lang/String;"    // creator
                        "Ljava/lang/Long;"      // lastModificationDate
                        "Ljava/lang/String;"    // lastModifier
                        "Ljava/util/List;"      // users
                        "Ljava/util/List;"      // managers
                        "Ljava/lang/Long;"      // version
                        "[B"                    // publicMeta
                        "[B"                    // privateMeta
                        "Lcom/simplito/java/privmx_endpoint/model/ContainerPolicy;" // policy
                        "Ljava/lang/Long;"      // statusCode
                        "Ljava/lang/Long;"      // schemaVersion
                        ")V"
                );

                jbyteArray publicMeta = ctx->NewByteArray(streamRoom_c.publicMeta.size());
                jbyteArray privateMeta = ctx->NewByteArray(streamRoom_c.privateMeta.size());

                ctx->SetByteArrayRegion(publicMeta, 0, streamRoom_c.publicMeta.size(),
                        (jbyte *) streamRoom_c.publicMeta.data());
                ctx->SetByteArrayRegion(privateMeta, 0, streamRoom_c.privateMeta.size(),
                        (jbyte *) streamRoom_c.privateMeta.data());

                jobject users = vectorTojArray(ctx, streamRoom_c.users, string2jobject);
                jobject managers = vectorTojArray(ctx, streamRoom_c.managers, string2jobject);

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx->NewStringUTF(streamRoom_c.contextId.c_str()),
                        ctx->NewStringUTF(streamRoom_c.streamRoomId.c_str()),
                        ctx.long2jLong(streamRoom_c.createDate),
                        ctx->NewStringUTF(streamRoom_c.creator.c_str()),
                        ctx.long2jLong(streamRoom_c.lastModificationDate),
                        ctx->NewStringUTF(streamRoom_c.lastModifier.c_str()),
                        users,
                        managers,
                        ctx.long2jLong(streamRoom_c.version),
                        publicMeta,
                        privateMeta,
                        privmx::wrapper::containerPolicy2Java(ctx, streamRoom_c.policy),
                        ctx.long2jLong(streamRoom_c.statusCode),
                        ctx.long2jLong(streamRoom_c.schemaVersion)
                );
            }

            jobject deviceType2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::DeviceType deviceType_c
            ) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/DeviceType");

                jmethodID valuesMID = ctx->GetStaticMethodID(itemCls, "values",
                        "()[Lcom/simplito/java/privmx_endpoint/model/DeviceType;");

                jobjectArray enumValues = (jobjectArray) ctx->CallStaticObjectMethod(itemCls,
                        valuesMID);

                return (jobject) ctx->GetObjectArrayElement(enumValues, (int) deviceType_c);
            }


            jobject mediaDevice2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::MediaDevice mediaDevice_c
            ) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/MediaDevice");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"      // name
                        "Ljava/lang/String;"      // id
                        "Lcom/simplito/java/privmx_endpoint/model/DeviceType;"  // type
                        ")V"
                );

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx->NewStringUTF(mediaDevice_c.name.c_str()),
                        ctx->NewStringUTF(mediaDevice_c.id.c_str()),
                        deviceType2Java(ctx, mediaDevice_c.type)

                );
            }

            jobject streamHandle2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamHandle streamHandle_c
            ) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamHandle");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/Long;"      // value
                        ")V"
                );

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx.long2jLong(streamHandle_c)
                );
            }

            jobject streamTrackInfo2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamTrackInfo streamTrackInfo_c
            ) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamTrackInfo");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"        // type
                        "Ljava/lang/Long;"          // mindex
                        "Ljava/lang/String;"        // mid
                        "Ljava/lang/Boolean;"       // disabled         [optional]
                        "Ljava/lang/String;"        // codec            [optional]
                        "Ljava/lang/String;"        // description      [optional]
                        "Ljava/lang/Boolean;"       // moderated        [optional]
                        "Ljava/lang/Boolean;"       // simulcast        [optional]
                        "Ljava/lang/Boolean;"       // talking          [optional]
                        ")V"
                );

                jobject disabled = nullptr;
                jobject codec = nullptr;
                jobject description = nullptr;
                jobject moderated = nullptr;
                jobject simulcast = nullptr;
                jobject talking = nullptr;

                if (streamTrackInfo_c.disabled.has_value()) {
                    disabled = ctx.bool2jBoolean(streamTrackInfo_c.disabled.value());
                }

                if (streamTrackInfo_c.codec.has_value()) {
                    codec = ctx->NewStringUTF(streamTrackInfo_c.codec.value().c_str());
                }

                if (streamTrackInfo_c.description.has_value()) {
                    description = ctx->NewStringUTF(streamTrackInfo_c.description.value().c_str());
                }

                if (streamTrackInfo_c.moderated.has_value()) {
                    moderated = ctx.bool2jBoolean(streamTrackInfo_c.moderated.value());
                }

                if (streamTrackInfo_c.simulcast.has_value()) {
                    simulcast = ctx.bool2jBoolean(streamTrackInfo_c.simulcast.value());
                }

                if (streamTrackInfo_c.talking.has_value()) {
                    talking = ctx.bool2jBoolean(streamTrackInfo_c.talking.value());
                }

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx->NewStringUTF(streamTrackInfo_c.type.c_str()),
                        ctx.long2jLong(streamTrackInfo_c.mindex),
                        ctx->NewStringUTF(streamTrackInfo_c.mid.c_str()),
                        disabled,
                        codec,
                        description,
                        moderated,
                        simulcast,
                        talking
                );
            }

            jobject
            streamInfo2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamInfo streamInfo_c) {
                jclass arrayCls = ctx->FindClass("java/util/ArrayList");
                jmethodID initArrayMID = ctx->GetMethodID(
                        arrayCls,
                        "<init>",
                        "()V");
                jmethodID addToArrayMID = ctx->GetMethodID(
                        arrayCls,
                        "add",
                        "(Ljava/lang/Object;)Z"
                );

                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamInfo");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/Long;"          // id
                        "Ljava/lang/String;"        // userId
                        "Ljava/util/List;"          // tracks
                        "Ljava/lang/String;"        // metadata
                        "Ljava/lang/Boolean;"       // dummy
                        "Ljava/lang/Boolean;"       // talking
                        ")V"
                );

                jobject metadata = nullptr;
                jobject dummy = nullptr;
                jobject talking = nullptr;

                if (streamInfo_c.metadata.has_value()) {
                    metadata = ctx->NewStringUTF(streamInfo_c.metadata.value().c_str());
                }

                if (streamInfo_c.dummy.has_value()) {
                    dummy = ctx.bool2jBoolean(streamInfo_c.dummy.value());
                }

                if (streamInfo_c.talking.has_value()) {
                    talking = ctx.bool2jBoolean(streamInfo_c.talking.value());
                }

                jobject tracks = ctx->NewObject(arrayCls, initArrayMID);
                for (auto &track: streamInfo_c.tracks) {
                    ctx->CallBooleanMethod(
                            tracks,
                            addToArrayMID,
                            streamTrackInfo2Java(ctx, track)
                    );
                }

                // todo - check null?

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx.long2jLong(streamInfo_c.id),
                        ctx->NewStringUTF(streamInfo_c.userId.c_str()),
                        tracks,
                        metadata,
                        dummy,
                        talking
                );

            }

            jobject publishedStreamData2Java(JniContextUtils &ctx, privmx::endpoint::stream::PublishedStreamData publishedStreamData_c) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/PublishedStreamData");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"      // streamRoomId
                        "Lcom/simplito/java/privmx_endpoint/model/StreamInfo;"      // stream
                        "Ljava/lang/String;"      //userId
                        ")V"
                );

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx->NewStringUTF(publishedStreamData_c.streamRoomId.c_str()),
                        streamInfo2Java(ctx, publishedStreamData_c.stream),
                        ctx->NewStringUTF(publishedStreamData_c.userId.c_str())
                );
            }

            jobject streamPublishResult2Java(JniContextUtils &ctx, privmx::endpoint::stream::StreamPublishResult streamPublishResult_c) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamPublishResult");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/Boolean;"      // published
                        "Lcom/simplito/java/privmx_endpoint/model/PublishedStreamData;"      // data
                        ")V"
                );

                jobject data = nullptr;
                if (streamPublishResult_c.data.has_value()) {
                    data = publishedStreamData2Java(ctx, streamPublishResult_c.data.value());
                    return ctx->NewObject(
                            itemCls,
                            initItemMID,
                            ctx.bool2jBoolean(streamPublishResult_c.published),
                            data
                    );
                } else {
                    return ctx->NewObject(
                            itemCls,
                            initItemMID,
                            ctx.bool2jBoolean(streamPublishResult_c.published)
                    );
                }
            }

            jobject remoteStreamId2Java(JniContextUtils &ctx,
                    privmx::endpoint::stream::RemoteStreamId remoteStreamId_c) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/RemoteStreamId");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "("
                        "Ljava/lang/Long;"      // value
                        ")V"
                );

                return ctx->NewObject(
                        itemCls,
                        initItemMID,
                        ctx.long2jLong(remoteStreamId_c)
                );
            }

            jobject frame2Java(JniContextUtils &ctx, privmx::endpoint::stream::Frame &frame_c) {
                jclass itemCls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/Frame");

                jmethodID initItemMID = ctx->GetMethodID(
                        itemCls,
                        "<init>",
                        "()V"
                );

                return ctx->NewObject(
                        itemCls,
                        initItemMID
                );
            }

            jobject
            streamTrackModificationPair2Java(
                    JniContextUtils &ctx,
                    endpoint::stream::StreamTrackModificationPair streamTrackModificationPair
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamTrackModificationPair");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Lcom/privmx/stream/StreamTrackInfo;"
                        "Lcom/privmx/stream/StreamTrackInfo;"
                        ")V"
                );

                jobject before = streamTrackModificationPair.before
                        ? streamTrackInfo2Java(ctx, streamTrackModificationPair.before.value())
                        : nullptr;

                jobject after = streamTrackModificationPair.after
                        ? streamTrackInfo2Java(ctx, streamTrackModificationPair.after.value())
                        : nullptr;

                return ctx->NewObject(
                        cls,
                        ctor,
                        before,
                        after
                );
            }

            jobject
            streamTrackModification2Java(
                    JniContextUtils &ctx,
                    endpoint::stream::StreamTrackModification streamTrackModification
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/StreamTrackModification");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/Long;"
                        "Ljava/util/List;"
                        ")V"
                );

                jobject tracksList = vectorTojArray(
                        ctx,
                        streamTrackModification.tracks,
                        streamTrackModificationPair2Java
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx.long2jLong(streamTrackModification.streamId),
                        tracksList
                );
            }

            jobject
            updatedStreamData2Java(
                    JniContextUtils &ctx,
                    endpoint::stream::UpdatedStreamData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/UpdatedStreamData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/lang/Long;"
                        "Ljava/lang/String;"
                        "Ljava/lang/Boolean;"
                        "Ljava/lang/Boolean;"
                        "Ljava/lang/String;"
                        "Ljava/lang/Long;"
                        "Ljava/lang/String;"
                        "Ljava/lang/String;"
                        ")V"
                );

                jobject jCodec = data.codec
                        ? ctx->NewStringUTF(data.codec->c_str())
                        : nullptr;

                jobject jStreamId = data.streamId
                        ? ctx.long2jLong(data.streamId.value())
                        : nullptr;

                jobject jStreamMid = data.streamMid
                        ? ctx->NewStringUTF(data.streamMid->c_str())
                        : nullptr;

                jobject jStreamDisplay = data.stream_display
                        ? ctx->NewStringUTF(data.stream_display->c_str())
                        : nullptr;

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.type.c_str()),
                        ctx.long2jLong(data.mindex),
                        ctx->NewStringUTF(data.mid.c_str()),
                        ctx.bool2jBoolean(data.send),
                        ctx.bool2jBoolean(data.ready),
                        jCodec,
                        jStreamId,
                        jStreamMid,
                        jStreamDisplay
                );
            }

            jobject
            streamRoomDeletedEventData2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamRoomDeletedEventData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/StreamRoomDeletedEventData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        ")V"
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.streamRoomId.c_str())
                );
            }

            jobject
            streamPublishedEventData2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamPublishedEventData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/StreamPublishedEventData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Lcom/simplito/java/privmx_endpoint/model/StreamInfo;"
                        "Ljava/lang/String;"
                        ")V"
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.streamRoomId.c_str()),
                        streamInfo2Java(ctx, data.stream),
                        ctx->NewStringUTF(data.userId.c_str())
                );
            }

            jobject streamUpdatedEventData2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamUpdatedEventData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/StreamUpdatedEventData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/util/List;"
                        "Ljava/util/List;"
                        "Ljava/util/List;"
                        ")V"
                );

                jobject addedList = vectorTojArray(
                        ctx,
                        data.streamsAdded,
                        streamInfo2Java
                );

                jobject removedList = vectorTojArray(
                        ctx,
                        data.streamsRemoved,
                        streamInfo2Java
                );

                jobject modifiedList = vectorTojArray(
                        ctx,
                        data.streamsModified,
                        streamTrackModification2Java
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.streamRoomId.c_str()),
                        addedList,
                        removedList,
                        modifiedList
                );
            }

            jobject
            streamEventData2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamEventData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/StreamEventData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/util/List;"
                        "Ljava/lang/String;"
                        ")V"
                );

                jobject streamIds = vectorTojArray(
                        ctx,
                        data.streamIds,
                        long2jobject
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.streamRoomId.c_str()),
                        streamIds,
                        ctx->NewStringUTF(data.userId.c_str())
                );
            }

            jobject
            streamUnpublishedEventData2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamUnpublishedEventData data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/StreamUnpublishedEventData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/lang/Long;"
                        ")V"
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.streamRoomId.c_str()),
                        ctx.long2jLong(data.streamId)
                );
            }

            jobject
            newStreams2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::NewStreams data
            ) {
                jclass cls = ctx->FindClass(
                        "com/simplito/java/privmx_endpoint/model/events/NewStreams");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/util/List;"
                        ")V"
                );

                jobject streamsList = vectorTojArray(
                        ctx,
                        data.streams,
                        streamInfo2Java
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.room.c_str()),
                        streamsList
                );
            }

            jobject
            streamsUpdated2Java(
                    JniContextUtils &ctx,
                    privmx::endpoint::stream::StreamsUpdatedData data
            ) {
                jclass cls = ctx->FindClass("com/simplito/java/privmx_endpoint/model/events/StreamsUpdatedData");
                jmethodID ctor = ctx->GetMethodID(
                        cls,
                        "<init>",
                        "("
                        "Ljava/lang/String;"
                        "Ljava/util/List;"
                        ")V"
                );

                jobject streamsList = vectorTojArray(
                        ctx,
                        data.streams,
                        updatedStreamData2Java
                );

                return ctx->NewObject(
                        cls,
                        ctor,
                        ctx->NewStringUTF(data.room.c_str()),
                        streamsList
                );
            }

        } // streams
    } // wrapper
} // privmx
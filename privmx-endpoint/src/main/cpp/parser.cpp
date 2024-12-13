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

#include "parser.h"

using namespace privmx::endpoint;

std::vector<privmx::endpoint::core::UserWithPubKey>
usersToVector(JniContextUtils &ctx, jobjectArray users) {
    std::vector<privmx::endpoint::core::UserWithPubKey> users_c;
    for (int i = 0; i < ctx->GetArrayLength(users); i++) {

        jobject arrayElement = ctx->GetObjectArrayElement(users, i);
        jclass arrayElementCls = ctx->GetObjectClass(arrayElement);

        jfieldID pubKeyFID = ctx->GetFieldID(arrayElementCls, "pubKey", "Ljava/lang/String;");
        jfieldID userIdFID = ctx->GetFieldID(arrayElementCls, "userId", "Ljava/lang/String;");
        privmx::endpoint::core::UserWithPubKey user = privmx::endpoint::core::UserWithPubKey();
        user.userId = ctx.jString2string(
                (jstring) ctx->GetObjectField(arrayElement, userIdFID));
        user.pubKey = ctx.jString2string(
                (jstring) ctx->GetObjectField(arrayElement, pubKeyFID));

        users_c.push_back(user);
    }
    return users_c;
}

privmx::endpoint::core::ContainerPolicyWithoutItem
parseContainerPolicyWithoutItem(JniContextUtils &ctx, jobject containerPolicyWithoutItem) {
    auto result = privmx::endpoint::core::ContainerPolicyWithoutItem();
    if (containerPolicyWithoutItem == nullptr) return result;
    jclass policyClass = ctx->GetObjectClass(containerPolicyWithoutItem);
    jfieldID get = ctx->GetFieldID(policyClass, "get", "Ljava/lang/String;");
    jfieldID update = ctx->GetFieldID(policyClass, "update", "Ljava/lang/String;");
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete_", "Ljava/lang/String;");
    jfieldID updatePolicy = ctx->GetFieldID(policyClass, "updatePolicy", "Ljava/lang/String;");
    jfieldID updaterCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                               "updaterCanBeRemovedFromManagers",
                                                               "Ljava/lang/String;");
    jfieldID ownerCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                             "ownerCanBeRemovedFromManagers",
                                                             "Ljava/lang/String;");

    result.get = ctx.jString2string((jstring) ctx->GetObjectField(containerPolicyWithoutItem, get));
    result.update = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicyWithoutItem, update));
    result.delete_ = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicyWithoutItem, delete_));
    result.updatePolicy = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicyWithoutItem, updatePolicy));
    result.updaterCanBeRemovedFromManagers = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicyWithoutItem,
                                          updaterCanBeRemovedFromManagers));
    result.ownerCanBeRemovedFromManagers = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicyWithoutItem,
                                          ownerCanBeRemovedFromManagers));
    return result;
}

privmx::endpoint::core::ContainerPolicy
parseContainerPolicy(JniContextUtils &ctx, jobject containerPolicy) {
    auto result = privmx::endpoint::core::ContainerPolicy();
    if (containerPolicy == nullptr) return result;

    jclass policyClass = ctx->GetObjectClass(containerPolicy);
    jfieldID get = ctx->GetFieldID(policyClass, "get", "Ljava/lang/String;");
    jfieldID update = ctx->GetFieldID(policyClass, "update", "Ljava/lang/String;");
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete_", "Ljava/lang/String;");
    jfieldID updatePolicy = ctx->GetFieldID(policyClass, "updatePolicy", "Ljava/lang/String;");
    jfieldID updaterCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                               "updaterCanBeRemovedFromManagers",
                                                               "Ljava/lang/String;");
    jfieldID ownerCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                             "ownerCanBeRemovedFromManagers",
                                                             "Ljava/lang/String;");

    jfieldID item = ctx->GetFieldID(policyClass,
                                    "item",
                                    "Lcom/simplito/java/privmx_endpoint/model/ItemPolicy;");

    result.get = ctx.jString2string((jstring) ctx->GetObjectField(containerPolicy, get));
    result.update = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicy, update));
    result.delete_ = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicy, delete_));
    result.updatePolicy = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicy, updatePolicy));
    result.updaterCanBeRemovedFromManagers = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicy,
                                          updaterCanBeRemovedFromManagers));
    result.ownerCanBeRemovedFromManagers = ctx.jString2string(
            (jstring) ctx->GetObjectField(containerPolicy,
                                          ownerCanBeRemovedFromManagers));
    result.item = parseItemPolicy(ctx, ctx->GetObjectField(containerPolicy, item));
    return result;
}

privmx::endpoint::core::ItemPolicy
parseItemPolicy(JniContextUtils &ctx, jobject itemPolicy) {
    auto result = privmx::endpoint::core::ItemPolicy();
    if (itemPolicy == nullptr) return result;
    jclass policyClass = ctx->GetObjectClass(itemPolicy);
    jfieldID get = ctx->GetFieldID(policyClass, "get", "Ljava/lang/String;");
    jfieldID listMy = ctx->GetFieldID(policyClass, "listMy", "Ljava/lang/String;");
    jfieldID listAll = ctx->GetFieldID(policyClass, "listAll", "Ljava/lang/String;");
    jfieldID create = ctx->GetFieldID(policyClass, "create", "Ljava/lang/String;");
    jfieldID update = ctx->GetFieldID(policyClass, "update", "Ljava/lang/String;");
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete_", "Ljava/lang/String;");

    result.get = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, get));
    result.listMy = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, listMy));
    result.listAll = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, listAll));
    result.create = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, create));
    result.update = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, update));
    result.delete_ = ctx.jString2string((jstring) ctx->GetObjectField(itemPolicy, delete_));
    return result;
};

//privmx::endpoint::inbox::InboxCreateMeta
//parseInboxCreateMeta(JniContextUtils &ctx, jobject inboxCreateMeta) {
//    auto result = privmx::endpoint::inbox::InboxCreateMeta();
//    jclass inboxCreateMetaCls = ctx->FindClass(
//            "com/simplito/java/privmx_endpoint/model/InboxCreateMeta");
//    jfieldID nameFID = ctx->GetFieldID(inboxCreateMetaCls, "name", "Ljava/lang/String;");
//    jfieldID customDataFID = ctx->GetFieldID(inboxCreateMetaCls, "customData", "[B");
//    result.name = ctx.jString2string(
//            (jstring) ctx->GetObjectField(inboxCreateMeta, nameFID)
//    );
//    result.customData = core::Buffer::from(
//            ctx.jByteArray2String(
//                    (jbyteArray) ctx->GetObjectField(inboxCreateMeta, customDataFID)
//            )
//    );
//    return result;
//}
//
//privmx::endpoint::inbox::InboxOptions
//parseInboxOptions(JniContextUtils &ctx, jobject inboxOptions) {
//    auto result = privmx::endpoint::inbox::InboxOptions();
//    jclass inboxCreateMetaCls = ctx->FindClass(
//            "com/simplito/java/privmx_endpoint/model/InboxOptions");
//    jfieldID fileConfigFID = ctx->GetFieldID(inboxCreateMetaCls, "fileConfig",
//                                             "Lcom/simplito/java/privmx_endpoint/model/FileConfig;");
//    jfieldID publicCustomDataFID = ctx->GetFieldID(inboxCreateMetaCls, "publicCustomData", "[B");
//    result.fileConfig = parseFileConfig(ctx, ctx->GetObjectField(
//            inboxOptions, fileConfigFID)
//    );
//    result.publicCustomData = ctx.jByteArray2String(
//            (jbyteArray) ctx->GetObjectField(inboxOptions, publicCustomDataFID)
//    );
//    return result;
//}

privmx::endpoint::inbox::FilesConfig parseFilesConfig(JniContextUtils &ctx, jobject filesConfig) {
    auto result = privmx::endpoint::inbox::FilesConfig();
    jclass filesConfigCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/FilesConfig");
    jfieldID minCountFID = ctx->GetFieldID(filesConfigCls, "minCount", "Ljava/lang/Long;");
    jfieldID maxCountFID = ctx->GetFieldID(filesConfigCls, "maxCount", "Ljava/lang/Long;");
    jfieldID maxFileSizeFID = ctx->GetFieldID(filesConfigCls, "maxFileSize", "Ljava/lang/Long;");
    jfieldID maxWholeUploadSizeFID = ctx->GetFieldID(filesConfigCls, "maxWholeUploadSize",
                                                     "Ljava/lang/Long;");
    result.minCount = (jlong) ctx->GetObjectField(filesConfig, minCountFID);
    result.maxCount = (jlong) ctx->GetObjectField(filesConfig, maxCountFID);
    result.maxFileSize = (jlong) ctx->GetObjectField(filesConfig, maxFileSizeFID);
    result.maxWholeUploadSize = (jlong) ctx->GetObjectField(filesConfig, maxWholeUploadSizeFID);
    return result;
}

jobject initEvent(JniContextUtils &ctx, std::string type, std::string channel, int64_t connectionId,
                  jobject data_j) {
    if (type.empty()) return nullptr;
    jclass eventCls = ctx->FindClass("com/simplito/java/privmx_endpoint/model/Event");
    jmethodID eventInitMID = ctx->GetMethodID(eventCls, "<init>", "()V");
    jfieldID eventTypeFieldID = ctx->GetFieldID(eventCls, "type", "Ljava/lang/String;");
    jfieldID eventDataFieldID = ctx->GetFieldID(eventCls, "data", "Ljava/lang/Object;");
    jfieldID eventConnectionIdFieldID = ctx->GetFieldID(eventCls, "connectionId",
                                                        "Ljava/lang/Long;");
    jfieldID eventChannelFieldID = ctx->GetFieldID(eventCls, "channel", "Ljava/lang/String;");
    jobject event_j = ctx->NewObject(eventCls, eventInitMID);
    ctx->SetObjectField(
            event_j,
            eventTypeFieldID,
            ctx->NewStringUTF(type.c_str())
    );
    ctx->SetObjectField(
            event_j,
            eventDataFieldID,
            data_j
    );
    ctx->SetObjectField(
            event_j,
            eventConnectionIdFieldID,
            ctx.long2jLong(connectionId)
    );
    ctx->SetObjectField(
            event_j,
            eventChannelFieldID,
            ctx->NewStringUTF(channel.c_str())
    );
    return event_j;
}

jobject
parseEvent(JniContextUtils &ctx, std::shared_ptr<privmx::endpoint::core::Event> event) {
    try {
        if (thread::Events::isThreadCreatedEvent(event)) {
            privmx::endpoint::thread::ThreadCreatedEvent event_cast = thread::Events::extractThreadCreatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::thread2Java(ctx, event_cast.data)
            );
        } else if (thread::Events::isThreadUpdatedEvent(event)) {
            privmx::endpoint::thread::ThreadUpdatedEvent event_cast = thread::Events::extractThreadUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::thread2Java(ctx, event_cast.data)
            );
        } else if (thread::Events::isThreadStatsEvent(event)) {
            privmx::endpoint::thread::ThreadStatsChangedEvent event_cast = thread::Events::extractThreadStatsEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::threadStatsEventData2Java(ctx, event_cast.data)
            );
        } else if (thread::Events::isThreadDeletedEvent(event)) {
            privmx::endpoint::thread::ThreadDeletedEvent event_cast = thread::Events::extractThreadDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::threadDeletedEventData2Java(ctx, event_cast.data)
            );
        } else if (thread::Events::isThreadNewMessageEvent(event)) {
            privmx::endpoint::thread::ThreadNewMessageEvent event_cast = thread::Events::extractThreadNewMessageEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::message2Java(ctx, event_cast.data)
            );
            return nullptr;
        } else if (thread::Events::isThreadMessageUpdatedEvent(event)) {
            privmx::endpoint::thread::ThreadMessageUpdatedEvent event_cast = thread::Events::extractThreadMessageUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::message2Java(ctx, event_cast.data)
            );
            return nullptr;
        } else if (thread::Events::isThreadDeletedMessageEvent(event)) {
            privmx::endpoint::thread::ThreadMessageDeletedEvent event_cast = thread::Events::extractThreadMessageDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::threadDeletedMessageEventData2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreCreatedEvent(event)) {
            privmx::endpoint::store::StoreCreatedEvent event_cast = store::Events::extractStoreCreatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::store2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreUpdatedEvent(event)) {
            privmx::endpoint::store::StoreUpdatedEvent event_cast = store::Events::extractStoreUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::store2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreStatsChangedEvent(event)) {
            privmx::endpoint::store::StoreStatsChangedEvent event_cast = store::Events::extractStoreStatsChangedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::storeStatsChangedEventData2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreUpdatedEvent(event)) {
            privmx::endpoint::store::StoreUpdatedEvent event_cast = store::Events::extractStoreUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::store2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreDeletedEvent(event)) {
            privmx::endpoint::store::StoreDeletedEvent event_cast = store::Events::extractStoreDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::storeDeletedEventData2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreFileCreatedEvent(event)) {
            privmx::endpoint::store::StoreFileCreatedEvent event_cast = store::Events::extractStoreFileCreatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::file2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreFileUpdatedEvent(event)) {
            privmx::endpoint::store::StoreFileUpdatedEvent event_cast = store::Events::extractStoreFileUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::file2Java(ctx, event_cast.data)
            );
        } else if (store::Events::isStoreFileDeletedEvent(event)) {
            privmx::endpoint::store::StoreFileDeletedEvent event_cast = store::Events::extractStoreFileDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::storeFileDeletedEventData2Java(ctx, event_cast.data)
            );
        } else if (inbox::Events::isInboxCreatedEvent(event)) {
            privmx::endpoint::inbox::InboxCreatedEvent event_cast = inbox::Events::extractInboxCreatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::inbox2Java(ctx, event_cast.data)
            );
        } else if (inbox::Events::isInboxUpdatedEvent(event)) {
            privmx::endpoint::inbox::InboxUpdatedEvent event_cast = inbox::Events::extractInboxUpdatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::inbox2Java(ctx, event_cast.data)
            );
        } else if (inbox::Events::isInboxDeletedEvent(event)) {
            privmx::endpoint::inbox::InboxDeletedEvent event_cast = inbox::Events::extractInboxDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::inboxDeletedEventData2Java(ctx, event_cast.data)
            );
        } else if (inbox::Events::isInboxEntryCreatedEvent(event)) {
            privmx::endpoint::inbox::InboxEntryCreatedEvent event_cast = inbox::Events::extractInboxEntryCreatedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::inboxEntry2Java(ctx, event_cast.data)
            );
        } else if (inbox::Events::isInboxEntryDeletedEvent(event)) {
            privmx::endpoint::inbox::InboxEntryDeletedEvent event_cast = inbox::Events::extractInboxEntryDeletedEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::inboxEntryDeletedEventData2Java(ctx, event_cast.data)
            );
        } else {
            return initEvent(
                    ctx,
                    event->type,
                    event->channel,
                    event->connectionId,
                    nullptr
            );
        }

//        else if (core::Events::isLibPlatformDisconnectedEvent(event)) {
//            return initEvent(
//                    ctx,
//                    event->type,
//                    event->channel,
//                    nullptr
//            );
//        }
//        else if (core::Events::libs(event)) {
//            return initEvent(
//                    ctx,
//                    event->type,
//                    event->channel,
//                    nullptr
//            );
//        } else if (event->type == "libDisconnected") {
//            return initEvent(
//                    ctx,
//                    event->type,
//                    event->channel,
//                    nullptr
//            );
//        }
    } catch (const std::exception &e) {
        throw e;
//        return nullptr;
    }
    return nullptr;
}
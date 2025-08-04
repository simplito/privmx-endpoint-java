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

privmx::endpoint::core::PKIVerificationOptions
parsePKIVerificationOptions(JniContextUtils &ctx, jobject pkiVerificationOptions) {
    auto result = privmx::endpoint::core::PKIVerificationOptions();
    if (pkiVerificationOptions == nullptr) return result;

    jclass pkiVerificationOptionsClass = ctx->GetObjectClass(pkiVerificationOptions);
    jfieldID bridgePubKey = ctx->GetFieldID(
            pkiVerificationOptionsClass,
            "bridgePubKey",
            "Ljava/lang/String;");
    jfieldID bridgeInstanceId = ctx->GetFieldID(
            pkiVerificationOptionsClass,
            "bridgeInstanceId",
            "Ljava/lang/String;");

    jstring value;
    if ((value = (jstring) ctx->GetObjectField(pkiVerificationOptions, bridgePubKey)) != NULL) {
        result.bridgePubKey = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(pkiVerificationOptions, bridgeInstanceId)) != NULL) {
        result.bridgeInstanceId = ctx.jString2string(value);
    }

    return result;
}

privmx::endpoint::core::ContainerPolicyWithoutItem
parseContainerPolicyWithoutItem(JniContextUtils &ctx, jobject containerPolicyWithoutItem) {
    auto result = privmx::endpoint::core::ContainerPolicyWithoutItem();
    if (containerPolicyWithoutItem == nullptr) return result;
    jclass policyClass = ctx->GetObjectClass(containerPolicyWithoutItem);
    jfieldID get = ctx->GetFieldID(policyClass, "get", "Ljava/lang/String;");
    jfieldID update = ctx->GetFieldID(policyClass, "update", "Ljava/lang/String;");
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete", "Ljava/lang/String;");
    jfieldID updatePolicy = ctx->GetFieldID(policyClass, "updatePolicy", "Ljava/lang/String;");
    jfieldID updaterCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                               "updaterCanBeRemovedFromManagers",
                                                               "Ljava/lang/String;");
    jfieldID ownerCanBeRemovedFromManagers = ctx->GetFieldID(policyClass,
                                                             "ownerCanBeRemovedFromManagers",
                                                             "Ljava/lang/String;");
    jstring value;
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem, get)) != NULL) {
        result.get = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem, update)) != NULL) {
        result.update = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem, delete_)) != NULL) {
        result.delete_ = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem, updatePolicy)) != NULL) {
        result.updatePolicy = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem,
                                               updaterCanBeRemovedFromManagers)) != NULL) {
        result.updaterCanBeRemovedFromManagers = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicyWithoutItem,
                                               ownerCanBeRemovedFromManagers)) != NULL) {
        result.ownerCanBeRemovedFromManagers = ctx.jString2string(value);
    }
    return result;
}

privmx::endpoint::core::ContainerPolicy
parseContainerPolicy(JniContextUtils &ctx, jobject containerPolicy) {
    auto result = privmx::endpoint::core::ContainerPolicy();
    if (containerPolicy == nullptr) return result;

    jclass policyClass = ctx->GetObjectClass(containerPolicy);
    jfieldID get = ctx->GetFieldID(policyClass, "get", "Ljava/lang/String;");
    jfieldID update = ctx->GetFieldID(policyClass, "update", "Ljava/lang/String;");
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete", "Ljava/lang/String;");
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
    jstring value;
    if ((value = (jstring) ctx->GetObjectField(containerPolicy, get)) != NULL) {
        result.get = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicy, update)) != NULL) {
        result.update = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicy, delete_)) != NULL) {
        result.delete_ = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicy, updatePolicy)) != NULL) {
        result.updatePolicy = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicy,
                                               updaterCanBeRemovedFromManagers)) != NULL) {
        result.updaterCanBeRemovedFromManagers = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(containerPolicy,
                                               ownerCanBeRemovedFromManagers)) != NULL) {
        result.ownerCanBeRemovedFromManagers = ctx.jString2string(value);
    }
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
    jfieldID delete_ = ctx->GetFieldID(policyClass, "delete", "Ljava/lang/String;");

    jstring value;
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, get)) != NULL) {
        result.get = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, listMy)) != NULL) {
        result.listMy = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, listAll)) != NULL) {
        result.listAll = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, create)) != NULL) {
        result.create = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, update)) != NULL) {
        result.update = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(itemPolicy, delete_)) != NULL) {
        result.delete_ = ctx.jString2string(value);
    }
    return result;
}

privmx::endpoint::inbox::FilesConfig parseFilesConfig(JniContextUtils &ctx, jobject filesConfig) {
    auto result = privmx::endpoint::inbox::FilesConfig();
    jclass filesConfigCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/FilesConfig");
    jfieldID minCountFID = ctx->GetFieldID(filesConfigCls, "minCount", "Ljava/lang/Long;");
    jfieldID maxCountFID = ctx->GetFieldID(filesConfigCls, "maxCount", "Ljava/lang/Long;");
    jfieldID maxFileSizeFID = ctx->GetFieldID(filesConfigCls, "maxFileSize", "Ljava/lang/Long;");
    jfieldID maxWholeUploadSizeFID = ctx->GetFieldID(filesConfigCls, "maxWholeUploadSize",
                                                     "Ljava/lang/Long;");
    result.minCount = ctx.getObject(ctx->GetObjectField(filesConfig, minCountFID)).getLongValue();
    result.maxCount = ctx.getObject(ctx->GetObjectField(filesConfig, maxCountFID)).getLongValue();
    result.maxFileSize = ctx.getObject(
            ctx->GetObjectField(filesConfig, maxFileSizeFID)).getLongValue();
    result.maxWholeUploadSize = ctx.getObject(
            ctx->GetObjectField(filesConfig, maxWholeUploadSizeFID)).getLongValue();
    return result;
}

privmx::endpoint::store::EventType parseStoreEventType(JniContextUtils &ctx, jobject eventType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/StoreEventType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case privmx::endpoint::store::EventType::STORE_CREATE:
            return privmx::endpoint::store::EventType::STORE_CREATE;
        case privmx::endpoint::store::EventType::STORE_UPDATE:
            return privmx::endpoint::store::EventType::STORE_UPDATE;
        case privmx::endpoint::store::EventType::STORE_DELETE:
            return privmx::endpoint::store::EventType::STORE_DELETE;
        case privmx::endpoint::store::EventType::STORE_STATS:
            return privmx::endpoint::store::EventType::STORE_STATS;
        case privmx::endpoint::store::EventType::FILE_CREATE:
            return privmx::endpoint::store::EventType::FILE_CREATE;
        case privmx::endpoint::store::EventType::FILE_UPDATE:
            return privmx::endpoint::store::EventType::FILE_UPDATE;
        case privmx::endpoint::store::EventType::FILE_DELETE:
            return privmx::endpoint::store::EventType::FILE_DELETE;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::thread::EventType parseThreadEventType(JniContextUtils &ctx, jobject eventType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/ThreadEventType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case privmx::endpoint::thread::THREAD_CREATE:
            return privmx::endpoint::thread::EventType::THREAD_CREATE;
        case privmx::endpoint::thread::THREAD_UPDATE:
            return privmx::endpoint::thread::EventType::THREAD_UPDATE;
        case privmx::endpoint::thread::THREAD_DELETE:
            return privmx::endpoint::thread::EventType::THREAD_DELETE;
        case privmx::endpoint::thread::THREAD_STATS:
            return privmx::endpoint::thread::EventType::THREAD_STATS;
        case privmx::endpoint::thread::MESSAGE_CREATE:
            return privmx::endpoint::thread::EventType::MESSAGE_CREATE;
        case privmx::endpoint::thread::MESSAGE_UPDATE:
            return privmx::endpoint::thread::EventType::MESSAGE_UPDATE;
        case privmx::endpoint::thread::MESSAGE_DELETE:
            return privmx::endpoint::thread::EventType::MESSAGE_DELETE;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::inbox::EventType parseInboxEventType(JniContextUtils &ctx, jobject eventType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/InboxEventType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case privmx::endpoint::inbox::INBOX_CREATE:
            return privmx::endpoint::inbox::EventType::INBOX_CREATE;
        case privmx::endpoint::inbox::INBOX_UPDATE:
            return privmx::endpoint::inbox::EventType::INBOX_UPDATE;
        case privmx::endpoint::inbox::INBOX_DELETE:
            return privmx::endpoint::inbox::EventType::INBOX_DELETE;
        case privmx::endpoint::inbox::ENTRY_CREATE:
            return privmx::endpoint::inbox::EventType::ENTRY_CREATE;
        case privmx::endpoint::inbox::ENTRY_DELETE:
            return privmx::endpoint::inbox::EventType::ENTRY_DELETE;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::kvdb::EventType parseKvdbEventType(JniContextUtils &ctx, jobject eventType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/KvdbEventType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case privmx::endpoint::kvdb::KVDB_CREATE:
            return privmx::endpoint::kvdb::EventType::KVDB_CREATE;
        case privmx::endpoint::kvdb::KVDB_UPDATE:
            return privmx::endpoint::kvdb::EventType::KVDB_UPDATE;
        case privmx::endpoint::kvdb::KVDB_DELETE:
            return privmx::endpoint::kvdb::EventType::KVDB_DELETE;
        case privmx::endpoint::kvdb::KVDB_STATS:
            return privmx::endpoint::kvdb::EventType::KVDB_STATS;
        case privmx::endpoint::kvdb::ENTRY_CREATE:
            return privmx::endpoint::kvdb::EventType::ENTRY_CREATE;
        case privmx::endpoint::kvdb::ENTRY_UPDATE:
            return privmx::endpoint::kvdb::EventType::ENTRY_UPDATE;
        case privmx::endpoint::kvdb::ENTRY_DELETE:
            return privmx::endpoint::kvdb::EventType::ENTRY_DELETE;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::store::EventSelectorType parseStoreEventSelectorType(JniContextUtils &ctx, jobject eventSelectorType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/StoreEventSelectorType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventSelectorType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case (privmx::endpoint::store::EventSelectorType::CONTEXT_ID):
            return privmx::endpoint::store::EventSelectorType::CONTEXT_ID;
        case (privmx::endpoint::store::EventSelectorType::STORE_ID):
            return privmx::endpoint::store::EventSelectorType::STORE_ID;
        case (privmx::endpoint::store::EventSelectorType::FILE_ID):
            return privmx::endpoint::store::EventSelectorType::FILE_ID;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::thread::EventSelectorType parseThreadEventSelectorType(JniContextUtils &ctx, jobject eventSelectorType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/ThreadEventSelectorType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventSelectorType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case (privmx::endpoint::thread::EventSelectorType::CONTEXT_ID):
            return privmx::endpoint::thread::EventSelectorType::CONTEXT_ID;
        case (privmx::endpoint::thread::EventSelectorType::THREAD_ID):
            return privmx::endpoint::thread::EventSelectorType::THREAD_ID;
        case (privmx::endpoint::thread::EventSelectorType::MESSAGE_ID):
            return privmx::endpoint::thread::EventSelectorType::MESSAGE_ID;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::inbox::EventSelectorType parseInboxEventSelectorType(JniContextUtils &ctx, jobject eventSelectorType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/InboxEventSelectorType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventSelectorType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case (privmx::endpoint::inbox::EventSelectorType::CONTEXT_ID):
            return privmx::endpoint::inbox::EventSelectorType::CONTEXT_ID;
        case (privmx::endpoint::inbox::EventSelectorType::INBOX_ID):
            return privmx::endpoint::inbox::EventSelectorType::INBOX_ID;
        case (privmx::endpoint::inbox::EventSelectorType::ENTRY_ID):
            return privmx::endpoint::inbox::EventSelectorType::ENTRY_ID;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::kvdb::EventSelectorType parseKvdbEventSelectorType(JniContextUtils &ctx, jobject eventSelectorType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/KvdbeEventSelectorType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventSelectorType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case (privmx::endpoint::kvdb::EventSelectorType::CONTEXT_ID):
            return privmx::endpoint::kvdb::EventSelectorType::CONTEXT_ID;
        case (privmx::endpoint::kvdb::EventSelectorType::KVDB_ID):
            return privmx::endpoint::kvdb::EventSelectorType::KVDB_ID;
        case (privmx::endpoint::kvdb::EventSelectorType::ENTRY_ID):
            return privmx::endpoint::kvdb::EventSelectorType::ENTRY_ID;
        default:
            return {};  // todo - throw exception?
    }
}

privmx::endpoint::event::EventSelectorType parseEventSelectorType(JniContextUtils &ctx, jobject eventSelectorType) {
    jclass eventTypeCls = ctx->FindClass(
            "com/simplito/java/privmx_endpoint/model/EventSelectorType");

    jmethodID ordinalMID = ctx->GetMethodID(eventTypeCls, "ordinal", "()I");
    auto ordinal = ctx->CallIntMethod(eventSelectorType, ordinalMID);
    int ordinal_c = (int)ordinal;

    switch (ordinal_c) {
        case (privmx::endpoint::kvdb::EventSelectorType::CONTEXT_ID):
            return privmx::endpoint::event::EventSelectorType::CONTEXT_ID;
            return {};  // todo - throw exception?
    }
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
        if (event::Events::isContextCustomEvent(event)) {
            privmx::endpoint::event::ContextCustomEvent event_cast = event::Events::extractContextCustomEvent(
                    event);
            return initEvent(
                    ctx,
                    event_cast.type,
                    event_cast.channel,
                    event_cast.connectionId,
                    privmx::wrapper::contextCustomEventData2Java(ctx, event_cast.data)
            );
        } else if (thread::Events::isThreadCreatedEvent(event)) {
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
        } else if (thread::Events::isThreadMessageDeletedEvent(event)) {
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
    } catch (const std::exception &e) {
        throw e;
    }
    return nullptr;
}

privmx::endpoint::core::PagingQuery
parsePagingQuery(JniContextUtils &ctx, jobject pagingQuery) {
    auto result = privmx::endpoint::core::PagingQuery();
    if (pagingQuery == nullptr) return result;
    jclass queryClass = ctx->GetObjectClass(pagingQuery);
    jfieldID skipFID = ctx->GetFieldID(queryClass, "skip", "Ljava/lang/Long;");
    jfieldID limitFID = ctx->GetFieldID(queryClass, "limit", "Ljava/lang/Long;");
    jfieldID sortOrderFID = ctx->GetFieldID(queryClass, "sortOrder", "Ljava/lang/String;");
    jfieldID lastIdFID = ctx->GetFieldID(queryClass, "lastId", "Ljava/lang/String;");
    jfieldID queryAsJsonFID = ctx->GetFieldID(queryClass, "queryAsJson", "Ljava/lang/String;");
    jfieldID sortByFID = ctx->GetFieldID(queryClass, "sortBy", "Ljava/lang/String;");

    result.skip = ctx.getObject(ctx->GetObjectField(pagingQuery, skipFID)).getLongValue();
    result.limit = ctx.getObject(ctx->GetObjectField(pagingQuery, limitFID)).getLongValue();
    result.sortOrder = ctx.jString2string((jstring) ctx->GetObjectField(pagingQuery, sortOrderFID));

    jstring value;
    if ((value = (jstring) ctx->GetObjectField(pagingQuery, lastIdFID)) != NULL) {
        result.lastId = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(pagingQuery, queryAsJsonFID)) != NULL) {
        result.queryAsJson = ctx.jString2string(value);
    }
    if ((value = (jstring) ctx->GetObjectField(pagingQuery, sortByFID)) != NULL) {
        result.sortBy = ctx.jString2string(value);
    }

    return result;
}
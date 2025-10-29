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

#ifndef PRIVMX_POCKET_LIB_PARSER_H
#define PRIVMX_POCKET_LIB_PARSER_H

#include "utils.hpp"

#include <jni.h>
#include "model_native_initializers.h"

std::vector<privmx::endpoint::core::UserWithPubKey>
usersToVector(JniContextUtils &ctx, jobjectArray users);

privmx::endpoint::core::PKIVerificationOptions
parsePKIVerificationOptions(JniContextUtils &ctx, jobject pkiVerificationOptions);

privmx::endpoint::core::ContainerPolicyWithoutItem
parseContainerPolicyWithoutItem(JniContextUtils &ctx, jobject containerPolicyWithoutItem);

privmx::endpoint::core::ContainerPolicy
parseContainerPolicy(JniContextUtils &ctx, jobject containerPolicy);

privmx::endpoint::core::ItemPolicy parseItemPolicy(JniContextUtils &ctx, jobject itemPolicy);

privmx::endpoint::inbox::FilesConfig parseFilesConfig(JniContextUtils &ctx, jobject filesConfig);

jobject parseEvent(JniContextUtils &ctx, std::shared_ptr<privmx::endpoint::core::Event> event);

privmx::endpoint::core::PagingQuery parsePagingQuery(JniContextUtils &ctx, jobject pagingQuery);

// java -> c++
template<typename T>
std::vector<T> jArrayToVector(JniContextUtils &ctx, jobjectArray jArray,
                              std::function<T(JniContextUtils &, jobject)> fun);

int64_t jobject2long(JniContextUtils &ctx, jobject jLong);
std::string jobject2string(JniContextUtils &ctx, jobject jString);


// c++ -> java
template<typename T, typename F>
jobject vectorTojArray(JniContextUtils &ctx, const std::vector<T> &vector,F fun);
template<typename T, typename F>
jobject pagingList2Java(JniContextUtils &ctx, privmx::endpoint::core::PagingList<T> pagingList,F fun);

jobject string2jobject(JniContextUtils &ctx,const std::string &cstring);
jobject long2jobject(JniContextUtils &ctx,const int64_t &clong);

#endif //PRIVMX_POCKET_LIB_PARSER_H

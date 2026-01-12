# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("privmx-endpoint-java")
set(CMAKE_PROJECT_NAME "privmx-endpoint-java")
add_library(${CMAKE_PROJECT_NAME} SHARED
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/parsers/parser.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/parsers/model_native_initializers.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/utils/exceptions.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/utils/jniUtils.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/utils/utils.cpp

#        src/cpp/parsers/parser.cpp
#        src/cpp/parsers/model_native_initializers.cpp
#        src/cpp/utils/exceptions.cpp
#        src/cpp/utils/jniUtils.cpp
#        src/cpp/utils/utils.cpp

        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/Connection.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/CryptoApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/ThreadApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/StoreApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/InboxApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/EventQueue.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/BackendRequester.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/EventApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/UserVerifierInterfaceJNI.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/ExtKey.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/Utils.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/src/cpp/modules/KvdbApi.cpp
)
target_include_directories(${CMAKE_PROJECT_NAME} PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/includes PRIVATE ${PRIVMX_INCLUDES_DIR})
set_target_properties(${CMAKE_PROJECT_NAME} PROPERTIES POSITION_INDEPENDENT_CODE True)

#target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC ssl crypto)
#target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC gmp)
#target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto)
target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE privmxendpointcrypto privmxendpointcore privmxendpointstore privmxendpointthread privmxendpointinbox privmxendpointevent privmxendpointkvdb)

install(TARGETS ${CMAKE_PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/lib)
install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/includes DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/)
install(IMPORTED_RUNTIME_ARTIFACTS crypto ssl gmp PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto Pson libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointthread privmxendpointinbox privmxendpointevent privmxendpointkvdb DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/lib)
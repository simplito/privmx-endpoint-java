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
        src/cpp/streams/modules/StreamApiLow.cpp
        src/cpp/streams/modules/WebRTCInterfaceJNI.cpp
        src/cpp/streams/modules/StreamSettingsJNI.cpp
        src/cpp/streams/parsers/parser.cpp
        src/cpp/streams/parsers/model_native_initializers.cpp
)
target_include_directories(${CMAKE_PROJECT_NAME} PUBLIC ${CMAKE_CURRENT_SOURCE_DIR}/includes)

add_library(ssl SHARED IMPORTED GLOBAL)
set_target_properties(ssl PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libssl.${file_extension})

add_library(crypto SHARED IMPORTED GLOBAL)
set_target_properties(crypto PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libcrypto.${file_extension})

add_library(gmp SHARED IMPORTED GLOBAL)
set_target_properties(gmp PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libgmp.${file_extension})

add_library(PocoFoundation SHARED IMPORTED GLOBAL)
set_target_properties(PocoFoundation PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoFoundation.${file_extension})

add_library(PocoJSON SHARED IMPORTED GLOBAL)
set_target_properties(PocoJSON PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoJSON.${file_extension})

add_library(PocoNet SHARED IMPORTED GLOBAL)
set_target_properties(PocoNet PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoNet.${file_extension})

add_library(PocoCrypto SHARED IMPORTED GLOBAL)
set_target_properties(PocoCrypto PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoCrypto.${file_extension})

add_library(PocoXML SHARED IMPORTED GLOBAL)
set_target_properties(PocoXML PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoXML.${file_extension})

add_library(PocoNetSSL SHARED IMPORTED GLOBAL)
set_target_properties(PocoNetSSL PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoNetSSL.${file_extension})

add_library(PocoUtil SHARED IMPORTED GLOBAL)
set_target_properties(PocoUtil PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPocoUtil.${file_extension})

add_library(Pson SHARED IMPORTED GLOBAL)
set_target_properties(Pson PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libPson.${file_extension})

add_library(libprivmx SHARED IMPORTED GLOBAL)
set_target_properties(libprivmx PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmx.${file_extension})

add_library(privmxendpointcore SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointcore PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointcore.${file_extension})

add_library(privmxendpointcrypto SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointcrypto PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointcrypto.${file_extension})

add_library(privmxendpointstore SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointstore PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointstore.${file_extension})

add_library(privmxendpointthread SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointthread PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointthread.${file_extension})

add_library(privmxendpointinbox SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointinbox PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointinbox.${file_extension})

add_library(privmxendpointevent SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointevent PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointevent.${file_extension})

add_library(privmxendpointkvdb SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointkvdb PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointkvdb.${file_extension})

add_library( privmxendpointstream SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointstream PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointstream.${file_extension})

include_directories(${PRIVMX_INCLUDES_DIR})

target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE ssl crypto)
target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE gmp)
target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto)
target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE Pson libprivmx privmxendpointcrypto privmxendpointcore privmxendpointstore privmxendpointthread privmxendpointinbox privmxendpointevent privmxendpointkvdb privmxendpointstream)

install(TARGETS ${CMAKE_PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/lib)
install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/includes DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/include)
install(IMPORTED_RUNTIME_ARTIFACTS crypto ssl gmp PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto Pson libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointthread privmxendpointinbox privmxendpointevent privmxendpointkvdb privmxendpointstream DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java/lib)
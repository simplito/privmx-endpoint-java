cmake_minimum_required(VERSION 3.15)
project("privmx-endpoint-java")

set(CMAKE_CXX_STANDARD 17)
if ("${JAVA_HOME}" STREQUAL "")
    SET(JAVA_HOME "$ENV{JAVA_HOME}")
endif ()

find_package(privmxendpoint REQUIRED)
add_library(${CMAKE_PROJECT_NAME} SHARED
        ${CMAKE_CURRENT_SOURCE_DIR}/parser.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/utils.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/model_native_initializers.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/Connection.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/CryptoApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/ThreadApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/StoreApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/InboxApi.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/EventQueue.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/exceptions.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/modules/BackendRequester.cpp
)

include_directories(${CMAKE_CURRENT_SOURCE_DIR}/libs/include ${JAVA_HOME}/include ${JAVA_HOME}/include/darwin)

target_link_libraries(${CMAKE_PROJECT_NAME} PUBLIC
        privmxendpoint::privmxendpointcore
        privmxendpoint::privmxendpointstore
        privmxendpoint::privmxendpointinbox
        privmxendpoint::privmxendpointthread
        privmxendpoint::privmxendpointcrypto
)

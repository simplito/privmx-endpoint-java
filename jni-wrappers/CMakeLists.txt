# Set the minimum required version of CMake
cmake_minimum_required(VERSION 3.10)

# Set the project name
project("jni-wrappers")

set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

if (NOT DEFINED CMAKE_DESTINATION_OS)
    set(CMAKE_DESTINATION_OS ${CMAKE_SYSTEM_NAME})
endif ()

if (NOT DEFINED CMAKE_DESTINATION_ARCHITECTURE)
    if (CMAKE_DESTINATION_OS STREQUAL "Android" AND DEFINED CMAKE_ANDROID_ARCH_ABI)
        set(CMAKE_DESTINATION_ARCHITECTURE ${CMAKE_ANDROID_ARCH_ABI})
    else ()
        set(CMAKE_DESTINATION_ARCHITECTURE ${CMAKE_SYSTEM_PROCESSOR})
    endif ()
endif ()

if ("${CMAKE_DESTINATION_OS}" STREQUAL "Darwin")
    set(file_extension "dylib")
    set(CMAKE_MACOSX_RPATH 1)
    set(CMAKE_INSTALL_RPATH "@loader_path")
elseif ("${CMAKE_DESTINATION_OS}" STREQUAL "Windows")
    set(file_extension "dll")
else ()
    set(file_extension "so")
endif ()

set(PRIVMX_INCLUDES_DIR "${CMAKE_CURRENT_SOURCE_DIR}/privmx-libs/${CMAKE_DESTINATION_OS}/${CMAKE_DESTINATION_ARCHITECTURE}/include")
set(PRIVMX_LIBS_DIR "${CMAKE_CURRENT_SOURCE_DIR}/privmx-libs/${CMAKE_DESTINATION_OS}/${CMAKE_DESTINATION_ARCHITECTURE}/lib")

message(DEBUG "CMAKE DEST OS ${CMAKE_DESTINATION_OS}")
message(DEBUG "CMAKE DEST PROCESSOR ${CMAKE_DESTINATION_ARCHITECTURE}")

include_directories(${JAVA_HOME}/include ${JAVA_HOME}/include/darwin)

# Add the subdirectories that contain the libraries
#add_subdirectory(utils)
add_subdirectory(privmx-endpoint-streams/jvm)
add_subdirectory(privmx-endpoint-streams/android)
add_subdirectory(privmx-endpoint)
message("Install to ${CMAKE_INSTALL_PREFIX}")
#install(TARGETS privmx-endpoint DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-java)
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("privmx-endpoint-drv-net")

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

message(DEBUG "CMAKE DEST OS ${CMAKE_DESTINATION_OS}")
message(DEBUG "CMAKE DEST PROCESSOR ${CMAKE_DESTINATION_ARCHITECTURE}")

include_directories(${JAVA_HOME}/include ${JAVA_HOME}/include/darwin)

add_library(${CMAKE_PROJECT_NAME} SHARED
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniUtils.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/net.cpp
)

# Android Debugging
#find_library( log-lib log )
#target_link_libraries(${CMAKE_PROJECT_NAME} ${log-lib} ${log})
message(DEBUG "Install to ${CMAKE_INSTALL_PREFIX}")
install(TARGETS ${CMAKE_PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_PREFIX})
#install(IMPORTED_RUNTIME_ARTIFACTS crypto ssl gmp PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto Pson libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointthread privmxendpointinbox privmxendpointevent privmxendpointkvdb DESTINATION ${CMAKE_INSTALL_PREFIX})
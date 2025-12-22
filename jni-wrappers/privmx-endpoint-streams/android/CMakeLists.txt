# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html.
# For more examples on how to use CMake, see https://github.com/android/ndk-samples.

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.22.1)

# Declares the project name. The project name can be accessed via ${ PROJECT_NAME},
# Since this is the top level CMakeLists.txt, the project name is also accessible
# with ${CMAKE_PROJECT_NAME} (both CMake variables are in-sync within the top level
# build script scope).
project("privmx-endpoint-streams-android")
set(CMAKE_PROJECT_NAME "privmx-endpoint-streams-android")

add_library(${CMAKE_PROJECT_NAME} SHARED
        src/cpp/privmx/endpoint/wrapper/streams/modules/StreamApiLow.cpp
        src/cpp/privmx/endpoint/wrapper/streams/modules/WebRTCInterfaceJNI.cpp
        src/cpp/privmx/endpoint/wrapper/streams/modules/StreamSettingsJNI.cpp
        src/cpp/privmx/endpoint/wrapper/streams/parsers/parser.cpp
        src/cpp/privmx/endpoint/wrapper/streams/parsers/model_native_initializers.cpp
        src/cpp/privmx/endpoint/wrapper/streams/utils/utils.cpp
)
target_include_directories(${CMAKE_PROJECT_NAME} PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}/includes
)

add_library( privmxendpointstream SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointstream PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointstream.${file_extension})

include_directories(${PRIVMX_INCLUDES_DIR})
target_link_libraries(${CMAKE_PROJECT_NAME} PRIVATE privmx-endpoint-java privmxendpointstream)

install(TARGETS ${CMAKE_PROJECT_NAME} DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-streams/android/lib)
install(DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}/includes DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-streams/android/include)
install(IMPORTED_RUNTIME_ARTIFACTS privmxendpointstream DESTINATION ${CMAKE_INSTALL_PREFIX}/privmx-endpoint-streams/android/lib)

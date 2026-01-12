# Set the minimum required version of CMake
cmake_minimum_required(VERSION 3.10)

# Set the project name
project("jni-wrappers")

option(BUILD_ENDPOINT "Building endpoint" OFF)
option(BUILD_ANDROID_STREAM "Building Android streams" OFF)
option(BUILD_JVM_STREAM "Building Desktop streams" OFF)

#set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} -s")
#set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -s")

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
set_target_properties(libprivmx PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmx.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
)

add_library(privmxendpointcore SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointcore PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointcore.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx
)

add_library(privmxendpointcrypto SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointcrypto PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointcrypto.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore
)

add_library(privmxendpointstore SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointstore PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointstore.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto
)

add_library(privmxendpointthread SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointthread PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointthread.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore
)

add_library(privmxendpointinbox SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointinbox PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointinbox.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointinbox
)

add_library(privmxendpointevent SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointevent PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointevent.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointinbox privmxendpointevent
)

add_library(privmxendpointkvdb SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointkvdb PROPERTIES IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointkvdb.${file_extension}
        INTERFACE_LINK_LIBRARIES
        ssl crypto
        gmp
        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointinbox privmxendpointevent privmxendpointkvdb
)

add_library( privmxendpointstream SHARED IMPORTED GLOBAL)
set_target_properties(privmxendpointstream PROPERTIES
        IMPORTED_LOCATION ${PRIVMX_LIBS_DIR}/libprivmxendpointstream.${file_extension}
#        INTERFACE_LINK_LIBRARIES
#        ssl crypto
#        gmp
#        PocoFoundation PocoXML PocoJSON PocoNet PocoNetSSL PocoUtil PocoCrypto
#        Pson
#        libprivmx privmxendpointcore privmxendpointcrypto privmxendpointstore privmxendpointinbox privmxendpointevent privmxendpointkvdb
)


# Add the subdirectories that contain the libraries
#if(BUILD_ENDPOINT)
    add_subdirectory(privmx-endpoint)
#endif ()

#if(BUILD_ANDROID_STREAM)
    add_subdirectory(privmx-endpoint-streams/android)
#endif ()
#
#if(BUILD_JVM_STREAM)
#    add_subdirectory(privmx-endpoint-streams/jvm)
#endif ()
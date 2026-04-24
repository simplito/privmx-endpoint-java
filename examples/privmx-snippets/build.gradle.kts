plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.privmx.install.native)
}

group = "com.simplito.privmx-endpoint-snippets"
version = "2.2.0"

android {
    namespace = "com.simplito.privmx.endpoint.snippets"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }
}

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":privmx-endpoint-extra"))
                implementation(libs.kotlinx.serialization.json)
            }
        }

        androidMain {
            dependencies {
                implementation(project(":privmx-endpoint-streams:android"))
                implementation(project(":privmx-endpoint-android"))
                implementation(libs.privmx.endpoint.webrtc)
            }
        }
    }
}

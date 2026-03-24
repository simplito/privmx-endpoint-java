plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.android.lint)
}
group = "com.simplito.privmx-endpoint-snippets"
version = "2.2.0"

kotlin {
    androidLibrary {
        namespace = "com.simplito.privmx-endpoint-snippets"
        compileSdk = 36
        minSdk = 24
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(project(":privmx-endpoint-extra"))
            }
        }

        androidMain {
            dependencies {
                implementation(project(":privmx-endpoint-streams:android"))
                implementation("com.simplito.webrtc:webrtc-android:1.0.0")
                implementation(project(":privmx-endpoint-android"))
            }
        }

    }

}
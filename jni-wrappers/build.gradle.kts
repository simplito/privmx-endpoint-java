//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

import org.gradle.internal.jvm.Jvm
import org.gradle.kotlin.dsl.register
import java.util.Properties

import javax.inject.Inject

//This task add native libraries to Android Studio IDE to enable cxx code writing features
plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.simplito.privmx_endpoint_jni"
    compileSdk = 36
    ndkVersion = "29.0.13599879"
    defaultConfig {
        minSdk = 24
        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
                this.arguments.addAll(listOf(
                    "-DBUILD_ENDPOINT=ON",
                    "-DBUILD_ANDROID_STREAM=ON"
                ))
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

abstract class NativeBuild @Inject constructor(
    @Input
    val exec: ExecOperations
) : DefaultTask() {

    private val archs = mutableSetOf<Arch>()
    private lateinit var target: NativePrivMXTarget

    @get:Input
    abstract val androidNdkPath: Property<String>

    @get:Input
    abstract val androidToolChainPath: Property<String>

    @get:Input
    abstract val androidAPILevel: Property<String>

    @get:Input
    abstract val privmxEndpointJavaVersion: Property<String>

    private val compileDir = project.layout.buildDirectory.dir("native/compile").get()
    private val installDir = project.layout.buildDirectory.dir("native/install").get()

    @TaskAction
    fun execute() {
        println(Jvm.current().javaHome)
        for (arch in archs) {
            println("Build ${arch.getSystemName()} - ${arch.getArchName()}")
            val platformCompileDir =
                compileDir.dir("${arch.getSystemName()}/${privmxEndpointJavaVersion.get()}/${arch.getArchName()}").asFile
            val platformInstallDir =
                installDir.dir("${arch.getSystemName()}/${privmxEndpointJavaVersion.get()}/${arch.getArchName()}").asFile
            println("Build command ${buildCommand(arch,platformCompileDir,platformInstallDir)}")

            exec.exec {
                workingDir = project.layout.projectDirectory.asFile
                commandLine = buildList {
                    add("sh")
                    add("-c")
                    add(buildCommand(arch,platformCompileDir,platformInstallDir))
                }
            }
            exec.exec {
                workingDir = platformCompileDir
                commandLine("sh", "-c", "make -s -j8")
            }
            exec.exec {
                workingDir = platformCompileDir
                commandLine("sh", "-c", "make -s install")
            }
        }
    }

    fun buildCommand(
        arch: Arch,
        platformCompileDir: File,
        platformInstallDir: File

    ): String = buildString {
        append("cmake")
        append(" -B${platformCompileDir.absolutePath}")
        append(" -DCMAKE_INSTALL_PREFIX=\"${platformInstallDir.absolutePath}\"")
        append(" -DCMAKE_BUILD_TYPE=Release")
        append(" -DCMAKE_CXX_FLAGS=-std=c++17")
        append(" -DCMAKE_DESTINATION_OS=${arch.getSystemName()}")
        append(" -DCMAKE_DESTINATION_ARCHITECTURE=\"${arch.getArchName()}\"")
        append(" -DJAVA_HOME=\"${Jvm.current().javaHome}\"")
        append(" -D${target.buildOption}=ON")
        if (arch is Arch.AndroidArch) {
            append(" -DANDROID_NDK=\"${androidNdkPath.get()}\"")
            append(" -DCMAKE_TOOLCHAIN_FILE=\"${androidToolChainPath.get()}\"")
            append(" -DANDROID_PLATFORM=\"android-${androidAPILevel.get()}\"")
            append(" -DANDROID_ABI=\"${arch.getArchName()}\"")
        }
    }


    fun setArchs(
        vararg archs: Arch
    ) {
        this.archs.addAll(archs)
    }

    fun setTarget(target: NativePrivMXTarget) {
        this.target = target
    }

    fun validateInput() {
//        if(!::target.isInitialized) throw IncompleteArgumentException("Target is not specified (select one value from NativePrivMXTarget enum)")
    }


    sealed interface Arch {
        fun getArchName(): String
        fun getSystemName(): String

        enum class AndroidArch : Arch {
            `arm64-v8a`, `armeabi-v7a`, x86, x86_64;

            override fun getArchName() = name

            override fun getSystemName(): String = "Android"
        }

        enum class DarwinArch : Arch {
            arm64;

            override fun getArchName(): String = name

            override fun getSystemName(): String = "Darwin"
        }
    }

    enum class NativePrivMXTarget {
        ENDPOINT, JVM_STREAM, ANDROID_STREAM;

        val buildOption get() = "BUILD_${this.name}"
    }

}

val Project.privmxEndpointJavaVersion get() = project(":privmx-endpoint").version.toString()

fun allPlatforms(): Array<NativeBuild.Arch> {
    return arrayOf(*NativeBuild.Arch.AndroidArch.values(), *NativeBuild.Arch.DarwinArch.values())
}

class PropertiesConfig {
    private val properties = Properties().apply {
        load(File(rootDir, "local.properties").inputStream())
    }
    val sdkPath get() = properties["sdk.dir"]
    val ndkVersion get() = properties["ndk.version"]
    val ndkPath get() = "$sdkPath/ndk/$ndkVersion"
    val androidToolChainPath get() = "$ndkPath/build/cmake/android.toolchain.cmake"
}

val PropertiesConfig = PropertiesConfig()


tasks.register<NativeBuild>("buildJNIEndpoint") {
    setArchs(*allPlatforms())
    setTarget(NativeBuild.NativePrivMXTarget.ENDPOINT)
    androidAPILevel.set("24")
    androidNdkPath.set(PropertiesConfig.ndkPath)
    androidToolChainPath.set(PropertiesConfig.androidToolChainPath)
    privmxEndpointJavaVersion.set(project.privmxEndpointJavaVersion)
}

tasks.register<NativeBuild>("buildJNIStreamsJVM") {
    setArchs(*NativeBuild.Arch.DarwinArch.values())
    setTarget(NativeBuild.NativePrivMXTarget.JVM_STREAM)
    androidAPILevel.set("24")
    androidNdkPath.set(PropertiesConfig.ndkPath)
    androidToolChainPath.set(PropertiesConfig.androidToolChainPath)
    privmxEndpointJavaVersion.set(project.privmxEndpointJavaVersion)
}

tasks.register<NativeBuild>("buildJNIStreamsAndroid") {
    dependsOn("buildJNIEndpoint")
    setArchs(*NativeBuild.Arch.AndroidArch.values())
    setTarget(NativeBuild.NativePrivMXTarget.ANDROID_STREAM)
    androidAPILevel.set("24")
    androidNdkPath.set(PropertiesConfig.ndkPath)
    androidToolChainPath.set(PropertiesConfig.androidToolChainPath)
    privmxEndpointJavaVersion.set(project.privmxEndpointJavaVersion)
}

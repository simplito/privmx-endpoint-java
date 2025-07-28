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

package com.simplito.java.privmx_endpoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LibLoader {
    static private File libsDir = null;

    static {
        try {
            Class.forName("android.os.Bundle");
        } catch (Throwable e) {
            extractLibraries();
        }
    }

    public static void loadPrivmxLibraries() {
        System.loadLibrary("privmx-endpoint-java");
    }

    private static String getPlatformLibsResourceDirPath() throws UnsatisfiedLinkError {
        String os = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        if (os == null) throw new NoSuchFieldError("Cannot find os name");
        if (arch == null) throw new NoSuchFieldError("Cannot find arch name");
        os = os.toLowerCase();
        if (os.startsWith("mac os")) {
            if (arch.equalsIgnoreCase("aarch64")) return "/lib/Darwin/arm64";
        }
        throw new UnsatisfiedLinkError("os: " + os + ", arch: " + arch + " is not supported.");
    }


    private static void extractResource(String resourcePath) throws UnsatisfiedLinkError {
        File localLibFile = new File(libsDir, resourcePath.substring(resourcePath.lastIndexOf("/")));
        try (InputStream is = LibLoader.class.getResourceAsStream(resourcePath)) {
            if (is == null) throw new UnsatisfiedLinkError("Cannot find resource " + resourcePath);
            if (localLibFile.exists()) localLibFile.delete();
            if (localLibFile.createNewFile()) {
                byte[] data = new byte[1024];
                int read;
                try (OutputStream oS = new FileOutputStream(localLibFile)) {
                    while ((read = is.read(data)) >= 0) {
                        oS.write(Arrays.copyOf(data, read));
                    }
                } catch (IOException e) {
                    throw new UnsatisfiedLinkError("Cannot extract PrivMX binaries");
                }
            }
        } catch (IOException | NullPointerException e) {
            localLibFile.delete();
            throw new UnsatisfiedLinkError("Cannot extract PrivMX binaries");
        }
    }

    private static Stream<String> getBinaryResourcePaths(String platformLibsResourceDirPath) throws IOException {
        String fileNames = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(
                                LibLoader.class.getResourceAsStream(
                                        platformLibsResourceDirPath + "/" + "fileNames.txt"
                                )
                        )
                )
        ).readLine();

        return Arrays.stream(
                fileNames.split(";")
        ).map(
                fileName -> platformLibsResourceDirPath + "/" + fileName
        );
    }

    static private boolean extractLibraries() throws UnsatisfiedLinkError {
        if (libsDir != null) return true;
        String librariesDirectoryPath = System.getProperty("java.library.path");
        if (Pattern.compile(":?\\.(?::?|$)").matcher(librariesDirectoryPath).find()) {
            librariesDirectoryPath = System.getProperty("user.dir");
        } else {
            librariesDirectoryPath = librariesDirectoryPath.substring(librariesDirectoryPath.lastIndexOf(":") + 1);
        }
        File librariesDirectory = new File(librariesDirectoryPath);

        if (!librariesDirectory.exists() && !librariesDirectory.mkdirs()) {
            return false;
        } else {
            libsDir = librariesDirectory;
            libsDir.deleteOnExit();
            try {
                getBinaryResourcePaths(
                        getPlatformLibsResourceDirPath()
                ).forEach(LibLoader::extractResource);
            } catch (IOException e) {
                throw new UnsatisfiedLinkError("Cannot read binary resources");
            }
        }

        return true;
    }
}


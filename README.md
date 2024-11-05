# PrivMX Endpoint Java

This repository provides Java wrappers for the native C++ library used by PrivMX to handle
end-to-end (e2e) encryption. PrivMX is a privacy-focused platform designed to offer secure
collaboration solutions by integrating robust encryption across various data types and communication
methods. This project enables seamless integration of PrivMX’s encryption functionalities in
Java/Kotlin
applications, preserving the security and performance of the original C++ library while making its
capabilities accessible in the JVM ecosystem.

## About PrivMX

[PrivMX](https://privmx.dev) allows developers to build end-to-end encrypted apps used for
communication. The Platform works according to privacy-by-design mindset, so all of our solutions
are based on Zero-Knowledge architecture. This project extends PrivMX’s commitment to security by
making its encryption features accessible to developers using Java/Kotlin.

### Key Features

- End-to-End Encryption: Ensures that data is encrypted at the source and can only be decrypted by
  the intended recipient.
- Native C++ Library Integration: Leverages the performance and security of C++ while making it
  accessible in Java/Kotlin applications.
- Cross-Platform Compatibility: Designed to support PrivMX on multiple operating systems and
  environments.
- Simple API: Easy-to-use interface for Java/Kotlin developers without compromising security.

## Modules

### 1. PrivMX Endpoint Java Extra

PrivMX Endpoint Java Extra is the fundamental **recommended library** for utilizing the platform in
the majority of cases.
It encompasses all the essential logic that simplifies and secures the usage of our libraries.
It can be utilized on Java Virtual Machines (JVM).

#### This library implements:

- Enums and static fields to minimize errors while invoking the methods.
- `PrivMXEndpoint` for managing the connection and registering callbacks for any events.
- `PrivMXEndpointContainer` for managing the global session with an implemented event loop.
- Classes to simplify reading and writing to files using byte arrays and InputStream/OutputStream.

### 2. PrivMX Endpoint Java Android

PrivMX Endpoint Java Android is an extension of `PrivMX Endpoint Java Extra` with logic specifically
for **Android**.

#### This library implements:

- `PrivMXEndpointService` - an Android Service that manages the PrivMX Endpoint Java Extra library
  and handles app lifecycle changes.
- `PrivMXEndpointBaseActivity` - an Android Activity that configures and binds to
  `PrivMXEndpointService`.

### 3. PrivMX Endpoint Java

PrivMX Endpoint Java is the fundamental wrapper library, essential for the Platform’s operational
functionality. It utilizes JNI to declare native functions in Java.
As the most minimalist library available, it provides the highest degree of flexibility in
customizing the Platform to meet your specific requirements.
It is compatible with Java Virtual Machines (JVM).

This library implements models, exception catching, and the following modules:

- `CryptoApi` - Cryptographic methods used to encrypt/decrypt and sign your data or generate keys to
  work with PrivMX Bridge.
- `Connection` - Methods for managing connection with PrivMX Bridge.
- `ThreadApi` - Methods for managing Threads and sending/reading messages.
- `StoreApi` - Methods for managing Stores and sending/reading files.
- `InboxApi` - Methods for managing Inboxes and entries.

## Usage

1. Add `mavenCentral()` repository to your `settings.gradle`:

```groovy
dependencyResolutionManagement {
  repositories {
    mavenCentral()
  }
}
```

2. Add dependency to `build.gradle`:

```groovy
dependencies {
  def privmxLibVersion = "2.0" // privmx-endpoint version
  implementation("com.simplito.java:privmx-endpoint-extra:$privmxLibVersion")
  //implementation("com.simplito.java:privmx-endpoint:$privmxLibVersion")  //for base Java library 
  //implementation("com.simplito.java:privmx-endpoint-android:$privmxLibVersion") //for Android Java library 
}
```

\
For more details on PrivMX Platform, including setup guides and API reference,
visit [PrivMX documentation](https://docs.privmx.dev).

## License information

**PrivMX Endpoint Java**\
Copyright © 2024 Simplito sp. z o.o.

This project is part of the PrivMX Platform (https://privmx.dev). \
This project is Licensed under the MIT License.

PrivMX Endpoint and PrivMX Bridge are licensed under the PrivMX Free License.\
See the License for the specific language governing permissions and limitations under the License.

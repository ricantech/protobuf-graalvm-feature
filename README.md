# Protobuf GraalVM Feature [![](https://jitpack.io/v/ricantech/protobuf-graalvm-feature.svg)](https://jitpack.io/#ricantech/protobuf-graalvm-feature)

Simple library to register protobuf generated classes for runtime reflection.

More details on the topic can be
found [here](https://www.graalvm.org/latest/reference-manual/native-image/dynamic-features/Reflection/#configuration-with-features)

## Content
 - [Usage](#usage)
 - [Configuration](#usage)


## Usage

#### 1) Add [jitpack.io](https://jitpack.io/) repository

**Gradle (Groovy)**

```
repositories {
  maven { url 'https://jitpack.io' }
}
```

**Gradle (Kotlin):**

```
repositories {
  maven { url = uri("https://jitpack.io") }
}
```
**Maven**

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

#### 2) Add dependency into your project:
   **Gradle (Groovy)**

```
implementation 'com.github.ricantech:protobuf-graalvm-feature:${version}'
```

**Gradle (Kotlin):**

```
implementation("com.github.ricantech:protobuf-graalvm-feature:${version}")
```

**Maven**

```
<dependency>
    <groupId>com.github.ricantech</groupId>
    <artifactId>protobuf-graalvm-feature</artifactId>
    <version>${version}</version>
</dependency>
```

**That's it**

Feature should be auto-registered thanks to [native-image.properties](lib/src/main/resources/META-INF/native-image/io.github.ricantech/protobuf-graalvm-feature/native-image.properties)
In case of any troubles, you can try to register the feature manually with:
```
--features=io.github.ricantech.ProtobufFeature
```

## Configuration

By default, all classes which extends `com.google.protobuf.GeneratedMessageV3` and `com.google.protobuf.GeneratedMessageV3.Builder` are registered for runtime reflection.

If you need only subset of the generated classes, you can create file `protobug-packages.properties` file under folder `resources/features` and define packages to be scanned for the generated classes.

Example file can be found [here](lib/src/test/resources/features/protobuf-packages.properties) - At this case we would only register classes within `package.a` and `package.b`
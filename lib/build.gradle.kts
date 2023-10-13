plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.graalvm.nativeimage:svm:23.1.0")
    compileOnly("com.google.protobuf:protobuf-java:3.24.4")
    implementation("org.reflections:reflections:0.10.2")

}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ricantech/REPOSITORY")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.ricantech"
            artifactId = "protobuf-graalvm-feature"
            version = "0.0.1-SNAPSHOT-1"

            from(components["java"])
        }
    }
}

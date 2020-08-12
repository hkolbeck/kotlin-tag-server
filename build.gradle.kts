import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72"
    id("com.google.protobuf") version "0.8.12"
    id("com.google.cloud.tools.jib") version "2.5.0"
    application
}

val kotlinxSerializationVersion by extra("0.20.0")
val protobufVersion by extra("3.11.1")
val pbandkVersion by extra("0.8.1")

repositories {
    jcenter()
    mavenCentral()
    maven("https://jitpack.io")
}

application {
    mainClassName = "dev.cbeck.tags.TagServer"
    applicationName = "TagServer"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    implementation("io.github.microutils:kotlin-logging:1.6.22")
    implementation("io.dropwizard:dropwizard-core:1.3.8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")
    implementation("com.google.inject:guice:4.2.0")
    implementation("org.postgresql:postgresql:42.2.4.jre7")
    implementation("io.dropwizard:dropwizard-jdbi3:2.0.12")
    implementation("org.jdbi:jdbi3-core:3.14.1")
    implementation("com.github.streem.pbandk:pbandk-runtime-jvm:$pbandkVersion") {
        capabilities {
            requireCapabilities("com.github.streem.pbandk:pbandk-runtime-jvm:jvm-api")
        }
    }
}

protobuf {
    generatedFilesBaseDir = "$projectDir/src"
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("kotlin") {
            artifact = "com.github.streem.pbandk:protoc-gen-kotlin-jvm:$pbandkVersion:jvm8@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.builtins {
                remove("java")
            }
            task.plugins {
                id("kotlin") {
                    option("kotlin_package=dev.cbeck.proto")
                }
            }
        }
    }
}

configurations.named("compileProtoPath") {
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    }
}

configurations.named("testCompileProtoPath") {
    attributes {
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_API))
        attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
    }
}

tasks {
    compileJava {
        enabled = false
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

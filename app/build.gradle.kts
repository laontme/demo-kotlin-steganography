plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version "1.6.21"

    // Apply com.github.johnrengelman.shadow to compile fat/uber jar
    id("com.github.johnrengelman.shadow") version "7.1.2"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

application {
    // Define the main class for the application.
    mainClass.set("me.laont.demo.kotlin.steganography.MainKt")
}

tasks.jar {
    // Set Main-Class attr in MANIFEST.MF to avoid `no main manifest attribute` error
    manifest {
        attributes["Main-Class"] = "me.laont.demo.kotlin.steganography.MainKt"
    }
}
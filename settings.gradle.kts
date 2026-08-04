pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
    }

    plugins {
        id("net.fabricmc.fabric-loom") version "1.17.17"
        id("de.florianmichael.baseproject.BaseProject") version "1.4.2"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "iZeMod"

include("izemod-api")

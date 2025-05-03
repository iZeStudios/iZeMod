plugins {
    `java-library`
    id("fabric-loom")
}

group = property("maven_group") as String
version = property("maven_version") as String
description = property("maven_description") as String

repositories {
    mavenCentral()
    maven("https://repo.viaversion.com")
    maven("https://maven.lenni0451.net/everything")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://maven.parchmentmc.org")
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.Oryxel")
            includeGroup("com.github.iZeStudios")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${project.property("parchment_version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

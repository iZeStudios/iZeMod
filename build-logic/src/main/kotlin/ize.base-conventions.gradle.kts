plugins {
    `java-library`
    id("fabric-loom")
}

group = property("maven_group") as String
version = property("maven_version") as String
description = property("maven_description") as String

repositories {
    maven("https://maven.parchmentmc.org")
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

import de.florianmichael.baseproject.*

plugins {
    id("fabric-loom")
    id("de.florianmichael.baseproject.BaseProject")
}

allprojects {

    setupProject()
    setupFabric()

}

val jij = configureJij()

repositories {
    mavenCentral()
    maven("https://repo.viaversion.com")
    maven("https://maven.lenni0451.net/everything")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.Oryxel")
            includeGroup("com.github.iZeStudios")
        }
    }
}

dependencies {
    jij("com.github.iZeStudios:discord-ipc:b0d38c7edc")

    modImplementation("com.viaversion:viafabricplus-api:4.1.4")
    modRuntimeOnly("com.viaversion:viafabricplus:4.1.3")

    includeFabricSubmodule(":izemod-api")
}

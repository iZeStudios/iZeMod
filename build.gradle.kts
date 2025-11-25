import de.florianmichael.baseproject.configureJij
import de.florianmichael.baseproject.includeFabricSubmodule
import de.florianmichael.baseproject.runGitCommand
import de.florianmichael.baseproject.setupFabric
import de.florianmichael.baseproject.setupProject

plugins {
    id("fabric-loom")
    id("de.florianmichael.baseproject.BaseProject")
}

allprojects {

    setupProject()
    setupFabric()

}

repositories {
    mavenCentral()
    maven("https://repo.viaversion.com")
    maven("https://maven.lenni0451.net/everything")
    maven("https://repo.opencollab.dev/maven-snapshots")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.oryxel1")
            includeGroup("com.github.iZeStudios")
        }
    }
}

includeFabricSubmodule(":izemod-api")

val jij = configureJij()

dependencies {
    jij("com.github.iZeStudios:discord-ipc:e2f57644a6")

    modImplementation("com.viaversion:viafabricplus-api:4.3.4")
    modRuntimeOnly("com.viaversion:viafabricplus:4.3.4")
}

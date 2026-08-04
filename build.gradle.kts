import de.florianmichael.baseproject.configureJij
import de.florianmichael.baseproject.setupFabric
import de.florianmichael.baseproject.setupProject

plugins {
    id("net.fabricmc.fabric-loom")
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

val jij = configureJij()

dependencies {
    api(project(":izemod-api"))
    include(project(":izemod-api"))

    jij("com.github.iZeStudios:discord-ipc:e2f57644a6")

    implementation("com.viaversion:viafabricplus-api:4.6.1")
    runtimeOnly("com.viaversion:viafabricplus:4.6.1")
}

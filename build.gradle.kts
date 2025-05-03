plugins {
    id("ize.base-conventions")
    idea
}

loom {
    accessWidenerPath.set(file("src/main/resources/izemod.accesswidener"))
}

val library: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
    configurations.include.get().extendsFrom(this)
}

dependencies {
    library("com.github.iZeStudios:discord-ipc:b0d38c7edc")

    // Mods we softly depend on
    modImplementation("com.viaversion:viafabricplus-api:4.1.3")
    modRuntimeOnly("com.viaversion:viafabricplus:4.1.3")

    // Subprojects, as Fabric mods (mainly for game code access)
    implementation(project(":izemod-api", configuration = "namedElements"))
    include(project(":izemod-api"))
}

tasks {
    jar {
        val projectName = project.name

        // Rename the project's license file to LICENSE_<project_name> to avoid conflicts
        from("LICENSE") {
            rename { "LICENSE_${projectName}" }
        }
    }

    processResources {
        val projectVersion = project.version
        val projectDescription = project.description
        val mcVersion = mcVersion()
        filesMatching("fabric.mod.json") {
            expand(
                "version" to projectVersion,
                "description" to projectDescription,
                "mcVersion" to mcVersion
            )
        }
    }
}

idea {
    module {
        excludeDirs.add(file("run"))
    }
}

fun mcVersion(): String {
    return if (project.hasProperty("supported_versions") && (project.property("supported_versions") as String).isNotEmpty()) {
        project.property("supported_versions") as String
    } else {
        project.property("minecraft_version") as String
    }
}

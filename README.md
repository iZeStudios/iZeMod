# BaseProject
Gradle template project with jar signing, publishing to maven central and GitHub workflows.

## Setup the required environment variables
You need to add the following environment variables to your gradle.properties file, which is located in the .gradle folder
in your home directory.

```properties
# Signing properties, see https://docs.gradle.org/current/userguide/signing_plugin.html#sec:signatory_credentials
signing.keyId=<the last 8 digits of your key id>
signing.password=<your key password>
signing.secretKeyRingFile=<path to your keyring file>

# Reposilite credentials (In case you want to publish to a reposilite instance)
reposiliteUsername=<your account name>
reposilitePassword=<your account password>

# Maven central credentials (In case you want to publish to maven central)
# See https://central.sonatype.org/publish/publish-maven-gradle/
# This will also require to set up the signing properties
ossrhUsername=<your account name>
ossrhPassword=<your account password> # This is an access token nowadays
```

## Publishing
Use `./gradlew publishAllPublicationsToOssrhRepository` to publish to maven central.

Use `./gradlew publishAllPublicationsToReposiliteRepository` to publish to a reposilite instance.

Use `./gradlew publish` to publish to both.

## Building
Use `./gradlew clean build` to build the project.

## Final steps
- Change all mentions of `BaseProject` to your project name.
- Change the maven coordinates in the `gradle.properties` file.
- Change the author/email in all files. (Replace `FlorianMichael/EnZaXD` with your own name, ...)
- Update the license header located in `.idea/copyright/Apache_2_0.xml`
- Update the publishing section in the build.gradle file.
- Delete the all sections between the `Setup the required environment variables` and `Final steps` sections.
- Update the `How to add this to your project` section to only mention the repositories you are publishing to.

# Why another base project?
I wanted to have a template project for my own projects, which I can use to quickly start a new project.

## Contact
If you encounter any issues, please report them on the [issue tracker](https://github.com/FlorianMichael/BaseProject/issues). If you just want to talk or need help with BaseProject feel free to join my [Discord](https://discord.gg/BwWhCHUKDf).

## How to add this to your project
### Gradle/Maven
To use BaseProject with Gradle/Maven you can use [Maven Central](https://mvnrepository.com/artifact/de.florianmichael/BaseProject) or [Lenni0451's Maven](https://maven.lenni0451.net/#/releases/de/florianmichael/BaseProject).  
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from the GitHub [Actions](https://github.com/FlorianMichael/BaseProject/actions) or use the [Release](https://github.com/FlorianMichael/BaseProject/releases).

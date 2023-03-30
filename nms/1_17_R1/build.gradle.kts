repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

plugins {
    id("io.papermc.paperweight.userdev") version "1.5.3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))

    // Paper Development Bundle
    paperweight.paperDevBundle("1.17.1-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

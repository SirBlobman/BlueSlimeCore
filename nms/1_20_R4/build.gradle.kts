repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

plugins {
    id("io.papermc.paperweight.userdev") version "1.6.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // Paper Development Bundle
    paperweight.paperDevBundle("1.20.5-R0.1-SNAPSHOT")
}

tasks.named("assemble") {
    dependsOn("reobfJar")
}

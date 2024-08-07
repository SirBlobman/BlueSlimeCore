repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

plugins {
    id("io.papermc.paperweight.userdev") version "1.7.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // Paper Development Bundle
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks.named("assemble") {
    dependsOn("reobfJar")
}

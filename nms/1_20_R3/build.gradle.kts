repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

plugins {
    id("io.papermc.paperweight.userdev") version "1.5.10"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17;
    targetCompatibility = JavaVersion.VERSION_17;
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // Paper Development Bundle
    paperweight.paperDevBundle("1.20.3-R0.1-SNAPSHOT")
}

tasks.named("assemble") {
    dependsOn("reobfJar")
}

import io.github.patrick.gradle.remapper.RemapTask

plugins {
    id("io.github.patrick.remapper") version "1.4.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // SpigotMC Mojang Mapped NMS
    compileOnly("org.spigotmc:spigot:1.21.8-R0.1-SNAPSHOT:remapped-mojang")
}

tasks {
    named<RemapTask>("remap") {
        version.set("1.21.8")
        action.set(RemapTask.Action.MOJANG_TO_SPIGOT)
        inputTask.set(named<Jar>("jar"))
        archiveName.set("remap.jar")
    }

    named("assemble") {
        dependsOn("remap")
    }
}

configurations {
    create("remap") {
        isCanBeConsumed = true
        isCanBeResolved = true
    }
}

artifacts {
    add("remap", layout.buildDirectory.file("libs/remap.jar")) {
        builtBy("remap")
    }
}

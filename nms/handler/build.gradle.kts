import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // Local Dependencies
    compileOnly(project(":paper-helper"))

    // NMS Abstract + Modern NBT
    implementation(project(":nms:abstract"))
    implementation(project(":nms:modern-nbt"))

    // NMS BossBar + Scoreboard
    implementation(project(":bossbar"))
    implementation(project(":nms:scoreboard"))

    // NMS Versions
    implementation(project(":nms:fallback")) // NMS Fallback
    implementation(project(":nms:paper")) // NMS Paper
    implementation(project(":nms:1_8_R3")) // NMS 1.8.8
    implementation(project(":nms:1_12_R1")) // NMS 1.12.2
    implementation(project(":nms:1_16_R3")) // NMS 1.16.5
    implementation(project(path = ":nms:1_17_R1", configuration = "remap")) // NMS 1.17.1
    implementation(project(path = ":nms:1_18_R2", configuration = "remap")) // NMS 1.18.2
    implementation(project(path = ":nms:1_19_R3", configuration = "remap")) // NMS 1.19.4
    implementation(project(path = ":nms:1_20_R4", configuration = "remap")) // NMS 1.20.6
    implementation(project(path = ":nms:1_21_R3", configuration = "remap")) // NMS 1.21.4
    implementation(project(path = ":nms:1_21_R4", configuration = "remap")) // NMS 1.21.5
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set(null as String?)
    }

    named("build") {
        dependsOn("shadowJar")
    }
}

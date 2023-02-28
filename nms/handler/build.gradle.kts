import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.0"
}

dependencies {
    // Bukkit API Base
    compileOnly("org.bukkit:bukkit:1.7.10-R0.1-SNAPSHOT")

    // Boss Bar
    implementation(project(":bossbar"))

    // NMS Abstract
    implementation(project(":nms:abstract"))
    implementation(project(":nms:scoreboard"))

    // NMS Versions
    implementation(project(":nms:fallback"))
    implementation(project(":nms:1_7_R4"))
    implementation(project(":nms:1_8_R3"))
    implementation(project(":nms:1_9_R2"))
    implementation(project(":nms:1_10_R1"))
    implementation(project(":nms:1_11_R1"))
    implementation(project(":nms:1_12_R1"))
    implementation(project(":nms:1_13_R2"))
    implementation(project(":nms:1_14_R1"))
    implementation(project(":nms:1_15_R1"))
    implementation(project(":nms:1_16_R3"))
    implementation(project(path = ":nms:1_17_R1", configuration = "reobf"))
    implementation(project(path = ":nms:1_18_R2", configuration = "reobf"))
    implementation(project(path = ":nms:1_19_R1", configuration = "reobf"))
    implementation(project(path = ":nms:1_19_R2", configuration = "reobf"))
}

tasks {
    named<Jar>("jar") {
        enabled = false;
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn("shadowJar")
    }
}

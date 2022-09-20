import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")

    // Utilities
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))

    // BossBar
    implementation(project(":bossbar"))

    // NMS Abstract
    implementation(project(":nms"))
    implementation(project(":nms:abstract"))

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
    implementation(project(":nms:1_17_R1"))
    implementation(project(":nms:1_18_R2"))
    implementation(project(":nms:1_19_R1"))

    // NMS Handler
    implementation(project(":nms:scoreboard"))
    implementation(project(":nms:handler"))

    // Factions
    implementation(project(":factions"))
    implementation(project(":factions:abstract"))

    // Factions Versions
    implementation(project(":factions:LegacyFactions"))
    implementation(project(":factions:MassiveCore-Factions"))
    implementation(project(":factions:SaberFactions"))
    implementation(project(":factions:FactionsUUID"))
    implementation(project(":factions:FactionsUUID-Legacy"))
    implementation(project(":factions:FactionsX"))

    // Factions Handler
    implementation(project(":factions:handler"))

    // Regular Modules
    implementation(project(":configuration"))
    implementation(project(":language"))
    implementation(project(":update"))
    implementation(project(":item"))
    implementation(project(":menu"))
    implementation(project(":plugin"))
    implementation(project(":command"))
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar"){
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn(shadowJar)
    }
}

publishing {
    repositories {
        maven {
            name = "sirblobman-public"
            url = uri("https://nexus.sirblobman.xyz/repository/public/")

            credentials {
                var currentUsername = System.getenv("MAVEN_DEPLOY_USERNAME")
                if(currentUsername == null) {
                    currentUsername = property("mavenUsernameSirBlobman") as String
                }

                var currentPassword = System.getenv("MAVEN_DEPLOY_PASSWORD")
                if (currentPassword == null) {
                    currentPassword = property("mavenPasswordSirBlobman") as String
                }

                username = currentUsername
                password = currentPassword
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "$group"
            artifactId = "core"
            artifact(tasks["shadowJar"])
        }
    }
}

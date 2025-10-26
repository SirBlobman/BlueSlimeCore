import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("de.eldoria.plugin-yml.bungee") version "0.8.0"
}

bungee {
    name = "BlueSlimeCore"
    author = "SirBlobman"
    version = rootProject.ext.get("calculatedVersion").toString()
    main = "com.github.sirblobman.api.bungeecord.core.CorePlugin"
}

dependencies {
    // Local Dependencies
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))

    // BungeeCord Modules
    implementation(project(":bungeecord:abstract"))
    implementation(project(":bungeecord:BungeePerms"))
    implementation(project(":bungeecord:LuckPerms"))
    implementation(project(":bungeecord:PremiumVanish"))
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        val calculatedVersion = rootProject.ext.get("calculatedVersion")
        archiveFileName.set("BlueSlimeBungeeCore-$calculatedVersion.jar")
        archiveClassifier.set(null as String?)
    }

    named("build") {
        dependsOn("shadowJar")
    }
}

publishing {
    repositories {
        maven("https://nexus.sirblobman.xyz/public/") {
            credentials {
                username = rootProject.ext.get("mavenUsername") as String
                password = rootProject.ext.get("mavenPassword") as String
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sirblobman.api.bungeecord"
            artifactId = "core"
            version = rootProject.ext.get("apiVersion") as String
            artifact(tasks["shadowJar"])
        }
    }
}

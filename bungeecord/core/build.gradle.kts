import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
}

dependencies {
    // Utilities
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))

    // BungeeCord
    implementation(project(":bungeecord:abstract"))
    implementation(project(":bungeecord:BungeePerms"))
    implementation(project(":bungeecord:LuckPerms"))
    implementation(project(":bungeecord:PremiumVanish"))
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
    publications {
        create<MavenPublication>("maven") {
            groupId = "$group"
            artifactId = "core"
            artifact(tasks["shadowJar"])
        }
    }
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.gradleup.shadow") version "9.3.1"
}

dependencies {
    // Local Dependencies
    implementation(project(":factions:abstract"))

    // Factions Implementations
    implementation(project(":factions:FactionsUUID"))
    implementation(project(":factions:FactionsUUID-Legacy"))
    implementation(project(":factions:FactionsUUID-4"))
    implementation(project(":factions:FactionsX"))
    implementation(project(":factions:LegacyFactions"))
    implementation(project(":factions:MassiveCore-Factions"))
    implementation(project(":factions:SaberFactions"))
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn("shadowJar")
    }
}

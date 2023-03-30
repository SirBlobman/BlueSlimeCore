import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")

    // Pre-Shaded Modules
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(path = ":nms:handler", configuration = "shadow"))
    implementation(project(path = ":factions:handler", configuration = "shadow"))

    // Included Modules
    implementation(project(":utility"))
    implementation(project(":paper-helper"))
    implementation(project(":configuration"))
    implementation(project(":language"))
    implementation(project(":update"))
    implementation(project(":item"))
    implementation(project(":menu"))
    implementation(project(":plugin"))
    implementation(project(":command"))

    // Local Dependencies
    compileOnly(project(":bossbar"))
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:scoreboard"))
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        val calculatedVersion = rootProject.ext.get("calculatedVersion")
        archiveFileName.set("BlueSlimeCore-$calculatedVersion.jar")
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching("plugin.yml") {
            val pluginName = findProperty("bukkit.plugin.name") as String
            val pluginPrefix = findProperty("bukkit.plugin.prefix") as String
            val pluginDescription = findProperty("plugin.description") as String
            val pluginWebsite = findProperty("bukkit.plugin.website") as String
            val pluginMainClass = findProperty("bukkit.plugin.main") as String
            val calculatedVersion = rootProject.ext.get("calculatedVersion")

            expand(mapOf(
                "pluginName" to pluginName,
                "pluginPrefix" to pluginPrefix,
                "pluginWebsite" to pluginWebsite,
                "pluginMainClass" to pluginMainClass,
                "pluginDescription" to pluginDescription,
                "pluginVersion" to calculatedVersion
            ))
        }
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
            groupId = "com.github.sirblobman.api"
            artifactId = "core"
            version = rootProject.ext.get("apiVersion") as String
            artifact(tasks["shadowJar"])
        }
    }
}

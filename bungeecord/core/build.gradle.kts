import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens

val jenkinsBuildNumber = System.getenv("BUILD_NUMBER") ?: "Unknown"
val baseVersion = rootProject.property("version.base") as String
val betaVersionString = rootProject.property("version.beta") as String
val betaVersion = betaVersionString.toBoolean()

var calculatedVersion = ("$baseVersion.$jenkinsBuildNumber")
if (betaVersion) {
    calculatedVersion += "-Beta"
}

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.0"
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

    named<ShadowJar>("shadowJar") {
        archiveFileName.set("BlueSlimeBungeeCore-$calculatedVersion.jar")
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching("bungee.yml") {
            val bungeePluginName = rootProject.property("bungee.plugin.name") as String
            val bungeePluginPrefix = rootProject.property("bungee.plugin.prefix") as String
            val bungeePluginDescription = rootProject.property("plugin.description") as String
            val bungeePluginMain = rootProject.property("bukkit.plugin.main") as String

            filter<ReplaceTokens>(
                "tokens" to mapOf(
                    "bungee.plugin.version" to calculatedVersion,
                    "bungee.plugin.name" to bungeePluginName,
                    "bungee.plugin.prefix" to bungeePluginPrefix,
                    "plugin.description" to bungeePluginDescription,
                    "bungee.plugin.main" to bungeePluginMain
                )
            )
        }
    }
}

publishing {
    repositories {
        maven {
            url = uri("https://nexus.sirblobman.xyz/repository/public-snapshots/")

            credentials {
                var currentUsername = System.getenv("MAVEN_DEPLOY_USR")
                if (currentUsername == null) {
                    currentUsername = property("mavenUsernameSirBlobman") as String
                }

                var currentPassword = System.getenv("MAVEN_DEPLOY_PSW")
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

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
    id("com.github.johnrengelman.shadow") version "7.1.2"
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
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        archiveFileName.set("BlueSlimeCore-$calculatedVersion.jar")
        archiveClassifier.set(null as String?)
    }

    build {
        dependsOn(shadowJar)
    }

    processResources {
        filesMatching("plugin.yml") {
            val bukkitPluginName = rootProject.property("bukkit.plugin.name") as String
            val bukkitPluginPrefix = rootProject.property("bukkit.plugin.prefix") as String
            val bukkitPluginDescription = rootProject.property("plugin.description") as String
            val bukkitPluginMain = rootProject.property("bukkit.plugin.main") as String

            filter<ReplaceTokens>("tokens" to mapOf(
                "bukkit.plugin.version" to calculatedVersion,
                "bukkit.plugin.name" to bukkitPluginName,
                "bukkit.plugin.prefix" to bukkitPluginPrefix,
                "bukkit.plugin.description" to bukkitPluginDescription,
                "bukkit.plugin.main" to bukkitPluginMain
            ))
        }
    }
}

publishing {
    repositories {
        maven {
            name = "sirblobman-public"
            url = uri("https://nexus.sirblobman.xyz/repository/public-snapshots/")

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

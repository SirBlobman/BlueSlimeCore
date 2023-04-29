import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.hangarpublishplugin.model.Platforms

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
}

plugins {
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.hangar-publish-plugin") version "0.0.5"
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
    named("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        val calculatedVersion = rootProject.ext.get("calculatedVersion")
        archiveFileName.set("BlueSlimeCore-$calculatedVersion.jar")
        archiveClassifier.set(null as String?)
    }

    named("build") {
        dependsOn("shadowJar")
    }

    processResources {
        filesMatching("plugin.yml") {
            val pluginName = findProperty("bukkit.plugin.name") as String
            val pluginPrefix = findProperty("bukkit.plugin.prefix") as String
            val pluginDescription = findProperty("plugin.description") as String
            val pluginWebsite = findProperty("bukkit.plugin.website") as String
            val pluginMainClass = findProperty("bukkit.plugin.main") as String
            val calculatedVersion = rootProject.ext.get("calculatedVersion")

            expand(
                mapOf(
                    "pluginName" to pluginName,
                    "pluginPrefix" to pluginPrefix,
                    "pluginWebsite" to pluginWebsite,
                    "pluginMainClass" to pluginMainClass,
                    "pluginDescription" to pluginDescription,
                    "pluginVersion" to calculatedVersion
                )
            )
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

hangarPublish {
    val envApiKey = System.getenv("HANGAR_API_KEY")
    val beta = rootProject.ext.get("isBeta") as Boolean
    if (envApiKey != null && beta) {
        publications.register("plugin") {
            apiKey.set(envApiKey)
            namespace("SirBlobman", "BlueSlimeCore")
            version.set(rootProject.ext.get("calculatedVersion") as String)
            channel.set("Beta")

            platforms {
                register(Platforms.PAPER) {
                    // url.set("https://jenkins.sirblobman.xyz/job/SirBlobman/job/BlueSlimeCore/job/main/");
                    jar.set(tasks.named<ShadowJar>("shadowJar").flatMap {
                        it.archiveFile
                    })

                    platformVersions.set(listOf("1.19.4", "1.18.2", "1.17.1", "1.16.5", "1.12.2", "1.8"))
                    changelog.set("https://jenkins.sirblobman.xyz/job/SirBlobman/job/BlueSlimeCore/job/main/changes")

                    this.dependencies {
                        url("Factions (Massive)", "https://www.spigotmc.org/resources/83459/") {
                            required.set(false)
                        }

                        url("Factions (Saber)", "https://www.spigotmc.org/resources/69771/") {
                            required.set(false)
                        }

                        url("Factions (UUID)", "https://www.spigotmc.org/resources/1035/") {
                            required.set(false)
                        }

                        url("FactionsX", "https://www.spigotmc.org/resources/83459/") {
                            required.set(false)
                        }

                        url("LegacyFactions", "https://www.spigotmc.org/resources/40122/") {
                            required.set(false)
                        }

                        url("PlaceholderAPI", "https://www.spigotmc.org/resources/6245/") {
                            required.set(false)
                        }
                    }
                }
            }
        }
    }
}

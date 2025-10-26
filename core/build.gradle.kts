import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import io.papermc.hangarpublishplugin.model.Platforms
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/service/local/repositories/snapshots/content/")
}

plugins {
    id("maven-publish")
    id("de.eldoria.plugin-yml.bukkit") version "0.8.0"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
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
    implementation(project(":menu:modern"))
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

        manifest {
            attributes["paperweight-mappings-namespace"] = "spigot"
        }
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
            groupId = "com.github.sirblobman.api"
            artifactId = "core"
            version = rootProject.ext.get("apiVersion") as String
            artifact(tasks["shadowJar"])
        }
    }
}

bukkit {
    name = "BlueSlimeCore"
    prefix = "Blue Slime Core"
    description="A core plugin with useful libraries."
    website = "https://www.spigotmc.org/resources/83189/"

    main = "com.github.sirblobman.api.core.CorePlugin"
    version = rootProject.ext.get("calculatedVersion").toString()
    apiVersion = "1.19"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    foliaSupported = true
    authors = listOf("SirBlobman")

    softDepend = listOf("Factions", "FactionsUUID", "FactionsX", "LegacyFactions", "PlaceholderAPI")

    commands {
        register("blueslimecore") {
            description = "The main command for the BlueSlimeCore plugin."
            usage = "/<command> help"
            aliases = listOf("bscore", "bsapi", "bsc", "bs")
        }

        register("item-info") {
            description = "A debug command to show information about an item."
            usage = "/<command>"
            aliases = listOf("iteminfo", "iteminformation", "item-information")
        }

        register("item-to-base64") {
            description = "A debug command to show an item as a binary Base64 string."
            usage = "/<command>"
            aliases = listOf("item-to-b64", "itemtobase64", "itemtob64")
        }

        register("item-to-nbt") {
            description = "A debug command to show an item as an NBT/JSON string."
            usage = "/<command> [pretty]"
            aliases = listOf("itemtonbt", "itemtojson", "item-to-json")
        }

        register("item-to-yml") {
            description = "A debug command to show an item as a YML configuration value."
            usage = "/<command>"
            aliases = listOf("itemtoyml")
        }

        register("debug-event") {
            description = "Show information about an event class."
            usage = "/<command> <priority> <full.package.with.ClassNameEvent>"
            aliases = listOf("debugevent")
        }

        register("global-gamerule") {
            description = "Change a gamerule for every world on your server at once."
            usage = "/<command> <gamerule> <value>"
            aliases = listOf("globalgamerule")
        }
    }

    permissions {
        register("blue.slime.core.command.blueslimecore") {
            description = "Access to the '/blueslimecore' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.help") {
            description = "Access to the '/blueslimecore help' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.language-test") {
            description = "Access to the '/blueslimecore language-test' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.reload") {
            description = "Access to the '/blueslimecore reload' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.version") {
            description = "Access to the '/blueslimecore version' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-info") {
            description = "Access to the '/item-info' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-base64") {
            description = "Access to the '/item-to-base64' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-nbt") {
            description = "Access to the '/item-to-nbt' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-yml") {
            description = "Access to the '/item-to-yml' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.global-gamerule") {
            description = "Access to the '/global-gamerule' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.debug-event") {
            description = "Access to the '/debug-event' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

paper {
    name = "BlueSlimeCore"
    prefix = "Blue Slime Core"
    description="A core plugin with useful libraries."
    website = "https://www.spigotmc.org/resources/83189/"

    main = "com.github.sirblobman.api.core.CorePlugin"
    version = rootProject.ext.get("calculatedVersion").toString()
    apiVersion = "1.19"

    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    foliaSupported = true
    authors = listOf("SirBlobman")

    dependencies {
        serverDependencies {
            register("Factions") {
                load = PaperPluginDescription.RelativeLoadOrder.AFTER
                required = false
            }

            register("FactionsUUID") {
                load = PaperPluginDescription.RelativeLoadOrder.AFTER
                required = false
            }

            register("FactionsX") {
                load = PaperPluginDescription.RelativeLoadOrder.AFTER
                required = false
            }

            register("LegacyFactions") {
                load = PaperPluginDescription.RelativeLoadOrder.AFTER
                required = false
            }

            register("PlaceholderAPI") {
                load = PaperPluginDescription.RelativeLoadOrder.AFTER
                required = false
            }
        }
    }

    permissions {
        register("blue.slime.core.command.blueslimecore") {
            description = "Access to the '/blueslimecore' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.help") {
            description = "Access to the '/blueslimecore help' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.language-test") {
            description = "Access to the '/blueslimecore language-test' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.reload") {
            description = "Access to the '/blueslimecore reload' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.blueslimecore.version") {
            description = "Access to the '/blueslimecore version' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-info") {
            description = "Access to the '/item-info' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-base64") {
            description = "Access to the '/item-to-base64' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-nbt") {
            description = "Access to the '/item-to-nbt' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.item-to-yml") {
            description = "Access to the '/item-to-yml' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.global-gamerule") {
            description = "Access to the '/global-gamerule' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }

        register("blue.slime.core.command.debug-event") {
            description = "Access to the '/debug-event' command."
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

hangarPublish {
    val envApiKey = System.getenv("HANGAR_API_KEY")
    val beta = rootProject.ext.get("isBeta") as Boolean
    if (envApiKey != null && beta) {
        publications.register("plugin") {
            apiKey.set(envApiKey)
            version.set(rootProject.ext.get("calculatedVersion") as String)
            channel.set("Beta")
            id.set("SirBlobman/BlueSlimeCore")

            platforms {
                register(Platforms.PAPER) {
                    // url.set("https://jenkins.sirblobman.xyz/job/SirBlobman/job/BlueSlimeCore/job/main/");
                    jar.set(tasks.named<ShadowJar>("shadowJar").flatMap {
                        it.archiveFile
                    })

                    platformVersions.set(listOf("1.19.4", "1.20.4", "1.20.6", "1.21.10"))
                    changelog.set("https://jenkins.sirblobman.xyz/job/SirBlobman/job/BlueSlimeCore/job/main/changes")

                    this.dependencies {
                        url("PlaceholderAPI", "https://www.spigotmc.org/resources/6245/") {
                            required.set(false)
                        }

                        url("Factions (Massive)", "https://www.spigotmc.org/resources/83459/") {
                            required.set(false)
                        }

                        url("Factions (Saber)", "https://www.spigotmc.org/resources/69771/") {
                            required.set(false)
                        }

                        url("Factions (UUID)", "https://www.spigotmc.org/resources/1035/") {
                            required.set(false)
                        }

                        url("Factions (X)", "https://www.spigotmc.org/resources/83459/") {
                            required.set(false)
                        }

                        url("Factions (Legacy)", "https://www.spigotmc.org/resources/40122/") {
                            required.set(false)
                        }
                    }
                }
            }
        }
    }
}

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

fun getEnvOrProp(variableName: String, propertyName: String): String {
    val environmentProvider = providers.environmentVariable(variableName)
    val propertyProvider = providers.gradleProperty(propertyName)
    return environmentProvider.orElse(propertyProvider).orElse("").get()
}

fun getProp(propertyName: String): String {
    val propertyProvider = providers.gradleProperty(propertyName)
    return propertyProvider.get()
}

val pluginVersion = rootProject.version.toString()

plugins {
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("de.eldoria.plugin-yml.bungee") version "0.8.0"
}

bungee {
    name = "BlueSlimeCore"
    author = "SirBlobman"
    version = pluginVersion
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
        archiveFileName.set("BlueSlimeBungeeCore-$pluginVersion.jar")
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
                username = getEnvOrProp("MAVEN_DEPLOY_USR", "maven.username.sirblobman")
                password = getEnvOrProp("MAVEN_DEPLOY_PSW", "maven.password.sirblobman")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sirblobman.api.bungeecord"
            artifactId = "core"
            version = getProp("version.api")
            artifact(tasks["shadowJar"])
        }
    }
}

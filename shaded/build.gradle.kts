import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.github.sirblobman.api"
version = "2.6-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // XSeries
    implementation("com.github.cryptomorin:XSeries:9.2.0")

    // Adventure
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.2.0")
    implementation("net.kyori:adventure-text-serializer-plain:4.12.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.0")
    implementation("org.bstats:bstats-bungeecord:3.0.0")
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        relocate("com.cryptomorin.xseries", "com.github.sirblobman.api.xseries")
        relocate("net.kyori", "com.github.sirblobman.api.adventure")
        relocate("org.bstats", "com.github.sirblobman.api.bstats")
    }

    build {
        dependsOn(shadowJar)
    }
}

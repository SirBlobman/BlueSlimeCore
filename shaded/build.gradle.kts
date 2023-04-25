import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven("https://nexus.sirblobman.xyz/public/")
}

dependencies {
    // XSeries
    implementation("com.github.cryptomorin:XSeries:9.3.1")

    // Adventure
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.0")
    implementation("net.kyori:adventure-text-serializer-plain:4.13.1")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")

    // bStats
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("org.bstats:bstats-bungeecord:3.0.2")

    // Folia Helper
    implementation("com.github.sirblobman.api:folia-helper:1.0.0-SNAPSHOT")
}

tasks {
    named<Jar>("jar") {
        enabled = false
    }

    named<ShadowJar>("shadowJar") {
        val shadePrefix = "com.github.sirblobman.api.shaded"
        relocate("com.cryptomorin.xseries", "$shadePrefix.xseries")
        relocate("net.kyori.adventure", "$shadePrefix.adventure")
        relocate("net.kyori.examination", "$shadePrefix.examination")
        relocate("org.bstats", "$shadePrefix.bstats")
    }

    build {
        dependsOn("shadowJar")
    }
}

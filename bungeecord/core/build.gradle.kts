plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
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

    build {
        dependsOn(shadowJar)
    }
}

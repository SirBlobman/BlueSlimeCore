repositories {
    maven("https://nexus.sirblobman.xyz/jitpack-mirror/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":utility"))
    compileOnly(project(":bungeecord:abstract"))

    // PremiumVanish API
    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.9.18-2")
}

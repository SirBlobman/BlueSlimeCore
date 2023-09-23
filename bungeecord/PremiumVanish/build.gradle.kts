repositories {
    maven("https://nexus.sirblobman.xyz/proxy-jitpack/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":utility"))
    compileOnly(project(":bungeecord:abstract"))

    // PremiumVanish API
    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.8.8")
}

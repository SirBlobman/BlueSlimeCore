repositories {
    maven("https://nexus.sirblobman.xyz/public/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // LegacyFactions
    compileOnly("com.github.redstone:LegacyFactions:1.4.7")
}

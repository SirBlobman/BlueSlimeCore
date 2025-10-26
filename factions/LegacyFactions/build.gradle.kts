repositories {
    maven("https://nexus.sirblobman.xyz/proxy-public/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // LegacyFactions
    compileOnly("com.github.redstone:LegacyFactions:v1.4.4") {
        exclude("mkremins", "fanciful")
    }
}

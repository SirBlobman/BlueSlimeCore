repositories {
    maven("https://nexus.sirblobman.xyz/private/") {
        credentials {
            username = rootProject.ext.get("mavenUsername") as String
            password = rootProject.ext.get("mavenPassword") as String
        }
    }
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // FactionsX
    compileOnly("net.prosavage.factionsx:FactionsX:1.2-STABLE")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // Spigot API: Required for some Javadocs
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT") {
        exclude("net.md-5", "bungeecord-chat")
    }

    // MassiveCore + Factions
    compileOnly("com.massivecraft.massivecore:MassiveCore:2.14.0")
    compileOnly("com.massivecraft.factions:Factions:2.14.0")
}

repositories {
    maven("https://repo.aikar.co/nexus/content/groups/aikar/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":menu"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
}

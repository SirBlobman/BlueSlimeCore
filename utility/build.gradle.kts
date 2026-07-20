repositories {
    maven("https://repo.aikar.co/nexus/content/groups/aikar/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(path = ":shaded", configuration = "shadow"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

repositories {
    maven("https://repo.aikar.co/nexus/content/groups/aikar/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
}

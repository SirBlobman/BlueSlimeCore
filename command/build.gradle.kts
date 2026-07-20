repositories {
    maven("https://repo.aikar.co/nexus/content/groups/aikar/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))
    compileOnly(project(":language"))
    compileOnly(project(":plugin"))
    compileOnly(project(":paper-helper"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT")
}

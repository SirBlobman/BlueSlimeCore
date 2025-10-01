repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/service/local/repositories/snapshots/content/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":menu"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
}

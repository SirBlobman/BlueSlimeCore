repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
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

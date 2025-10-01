repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/service/local/repositories/snapshots/content/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))
    compileOnly(project(":paper-helper"))
    compileOnly(project(":language"))
    compileOnly(project(":item"))
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:handler"))
    compileOnly(project(":plugin"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

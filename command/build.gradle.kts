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

    // NMS
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:handler"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT")
}

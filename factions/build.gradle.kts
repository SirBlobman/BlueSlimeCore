allprojects {
    repositories {
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://nexus.sirblobman.xyz/public/")
    }

    dependencies {
        // Local Dependencies
        compileOnly(project(":utility"))

        // Spigot API
        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    }
}

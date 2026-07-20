tasks.named("jar") {
    enabled = false
}

subprojects {
    repositories {
        maven("https://repo.aikar.co/nexus/content/groups/aikar/")
        maven("https://nexus.sirblobman.xyz/public/")
    }

    dependencies {
        // Local Dependencies
        compileOnly(project(":utility"))

        // Spigot API
        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    }
}

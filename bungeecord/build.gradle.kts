tasks.named("jar") {
    enabled = false
}

subprojects {
    repositories {
        maven("https://libraries.minecraft.net/")
        maven("https://repo.aikar.co/nexus/content/groups/aikar/")
    }

    dependencies {
        // BungeeCord API
        compileOnly("net.md-5:bungeecord-api:1.21-R0.4")
    }
}

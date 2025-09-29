tasks.named("jar") {
    enabled = false
}

subprojects {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots/") // BungeeCord API
        maven("https://libraries.minecraft.net/") // BungeeCord Protocol
    }

    dependencies {
        // BungeeCord API
        compileOnly("net.md-5:bungeecord-api:1.21-R0.4")
    }
}

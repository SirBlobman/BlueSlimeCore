java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/service/local/repositories/snapshots/content/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT") {
        exclude("net.md-5", "bungeecord-chat")
    }
}

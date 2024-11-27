repositories {
    maven("https://repo.wea-ondara.net/repository/maven-releases/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":utility"))
    compileOnly(project(":bungeecord:abstract"))

    // BungeePerms
    compileOnly("net.alpenblock:BungeePerms:4.0-dev-146")
}

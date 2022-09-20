repositories {
    maven {
        name = "bungeeperms-repo"
        url = uri("https://repo.wea-ondara.net/repository/public/")
    }
}

dependencies {
    implementation(project(":utility"))
    implementation(project(":bungeecord:abstract"))
    compileOnly("net.alpenblock:BungeePerms:4.0-dev-140")
}

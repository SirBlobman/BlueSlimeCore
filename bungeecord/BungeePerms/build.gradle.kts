repositories {
    maven("https://repo.wea-ondara.net/repository/public/")
}

dependencies {
    compileOnly(project(":utility"))
    compileOnly(project(":bungeecord:abstract"))
    compileOnly("net.alpenblock:BungeePerms:4.0-dev-140")
}

dependencies {
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))
    compileOnly(project(":language"))
    compileOnly(project(":plugin"))
    compileOnly("org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT")
}

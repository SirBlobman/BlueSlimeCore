dependencies {
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))
    compileOnly(project(":paper-helper"))
    compileOnly(project(":language"))
    compileOnly(project(":item"))
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:handler"))
    compileOnly(project(":plugin"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

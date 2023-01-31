dependencies {
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))
    compileOnly(project(":paper-helper"))
    compileOnly(project(":nms:abstract"))
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
}

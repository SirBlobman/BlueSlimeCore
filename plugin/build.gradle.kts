group = "com.github.sirblobman.api"
version = "2.6-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Base Spigot API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // Shaded Dependencies
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(path = ":nms:handler", configuration = "shadow"))

    // Normal Dependencies
    compileOnly(project(":utility"))
    compileOnly(project(":configuration"))
    compileOnly(project(":language"))
}

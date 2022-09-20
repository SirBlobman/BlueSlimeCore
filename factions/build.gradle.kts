allprojects {
    group = "com.github.sirblobman.api.factions"

    dependencies {
        implementation(project(":utility"))
        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    }
}

allprojects {
    group = "com.github.sirblobman.api.factions"

    dependencies {
        implementation(project(":utility"))
        compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
    }
}

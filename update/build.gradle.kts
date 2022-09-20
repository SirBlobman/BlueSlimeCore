plugins {
    id("java")
}

group = "com.github.sirblobman.api"
version = "2.6-SNAPSHOT"

dependencies {
    implementation(project(":utility"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

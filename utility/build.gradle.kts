group = "com.github.sirblobman.api"
version = "2.6-SNAPSHOT"

dependencies {
    implementation(project(path = ":shaded", configuration = "shadow"))
    compileOnly("org.jetbrains:annotations:24.0.0")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

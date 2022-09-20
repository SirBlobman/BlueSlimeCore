dependencies {
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))
    implementation(project(":language"))
    implementation(project(":plugin"))
    compileOnly("org.spigotmc:spigot-api:1.9.4-R0.1-SNAPSHOT")
}

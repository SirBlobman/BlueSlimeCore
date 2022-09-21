dependencies {
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))
    implementation(project(":paper-helper"))
    implementation(project(":nms:abstract"))
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
}

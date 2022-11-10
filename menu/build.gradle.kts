dependencies {
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))
    implementation(project(":paper-helper"))
    implementation(project(":language"))
    implementation(project(":item"))
    implementation(project(":nms:abstract"))
    implementation(project(":nms:handler"))
    implementation(project(":plugin"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

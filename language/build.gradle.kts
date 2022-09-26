repositories {
    maven {
        name = "placeholderapi"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
}

dependencies {
    implementation(project(path = ":shaded", configuration = "shadow"))
    implementation(project(":utility"))
    implementation(project(":configuration"))
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.2")
}

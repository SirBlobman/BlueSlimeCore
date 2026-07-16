repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // SpigotMC 26.2
    compileOnly("org.spigotmc:spigot:26.2-R0.1-SNAPSHOT")
}

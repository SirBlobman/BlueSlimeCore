repositories {
//    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://libraries.minecraft.net/")
}

//plugins {
//    id("io.papermc.paperweight.userdev") version "1.5.5"
//}

java {
    sourceCompatibility = JavaVersion.VERSION_17;
    targetCompatibility = JavaVersion.VERSION_17;
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))

    // Spigot NMS
    compileOnly("org.spigotmc:spigot:1.20-R0.1-SNAPSHOT")

    // Paper Development Bundle
    // paperweight.paperDevBundle("1.20-R0.1-SNAPSHOT")
}

//tasks.named("assemble") {
//    dependsOn("reobfJar")
//}

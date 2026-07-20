java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

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

configurations {
    create("remap") {
        isCanBeConsumed = true
        isCanBeResolved = true
    }
}

artifacts {
    add("remap", layout.buildDirectory.file("libs/26_1_R1.jar")) {
        builtBy("remap")
    }
}

tasks {
    register("remap") {
        description = "Dummy task for copying the jar"
        dependsOn("jar")
    }
}

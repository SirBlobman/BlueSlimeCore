repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("io.papermc.paperweight.userdev") version "1.3.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17;
    targetCompatibility = JavaVersion.VERSION_17;
}

dependencies {
    implementation(project(":nms:abstract"))
    paperDevBundle("1.18.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
}

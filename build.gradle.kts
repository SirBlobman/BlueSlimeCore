group = "com.github.sirblobman.api"
version = "2.6-SNAPSHOT"

plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    repositories {
        mavenCentral()

        maven {
            name = "sirblobman-public"
            url = uri("https://nexus.sirblobman.xyz/repository/public/")
        }

        maven {
            name = "spigot-repo"
            url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        }

        maven {
            name = "oss-sonatype-snapshots"
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:23.0.0")
    }
}

plugins {
    id("java")
}

allprojects {
    group = "com.github.sirblobman.api"
    version = findProperty("version.api") as String

    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withSourcesJar()
        withJavadocJar()
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
        compileOnly("org.jetbrains:annotations:24.0.1")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        javadoc {
            options {
                val standard = this as StandardJavadocDocletOptions
                standard.addStringOption("Xdoclint:none", "-quiet")
            }
        }
    }
}

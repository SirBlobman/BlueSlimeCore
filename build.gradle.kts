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
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://nexus.sirblobman.xyz/repository/public/")
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:24.0.1")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        javadoc {
            val standard = options as StandardJavadocDocletOptions
            standard.addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

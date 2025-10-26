tasks.named("jar") {
    enabled = false
}

subprojects {
    repositories {
        maven("https://nexus.sirblobman.xyz/private/") {
            credentials {
                username = rootProject.ext.get("mavenUsername") as String
                password = rootProject.ext.get("mavenPassword") as String
            }
        }

        mavenLocal()
    }

    dependencies {
        // Local Dependencies
        compileOnly(project(path = ":shaded", configuration = "shadow"))
        compileOnly(project(":utility"))
    }
}

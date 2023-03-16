allprojects {
    group = "com.github.sirblobman.api.nms"

    repositories {
        maven {
            url = uri("https://nexus.sirblobman.xyz/private/")

            credentials {
                var currentUsername = System.getenv("MAVEN_DEPLOY_USR")
                if (currentUsername == null) {
                    currentUsername = property("mavenUsernameSirBlobman") as String
                }

                var currentPassword = System.getenv("MAVEN_DEPLOY_PSW")
                if (currentPassword == null) {
                    currentPassword = property("mavenPasswordSirBlobman") as String
                }

                username = currentUsername
                password = currentPassword
            }
        }
    }

    dependencies {
        compileOnly(project(path = ":shaded", configuration = "shadow"))
        compileOnly(project(":utility"))
    }
}

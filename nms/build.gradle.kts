group = "com.github.sirblobman.api.nms"

allprojects {
    repositories {
        maven {
            name = "sirblobman-private"
            url = uri("https://nexus.sirblobman.xyz/repository/private/")

            credentials {
                username = property("mavenUsernameSirBlobmanPrivate") as String
                password = property("mavenPasswordSirBlobmanPrivate") as String
            }
        }
    }

    dependencies {
        implementation(project(path = ":shaded", configuration = "shadow"))
        implementation(project(":utility"))
    }
}

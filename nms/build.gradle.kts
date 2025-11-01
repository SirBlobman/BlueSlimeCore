fun getEnvOrProp(variableName: String, propertyName: String): String {
    val environmentProvider = providers.environmentVariable(variableName)
    val propertyProvider = providers.gradleProperty(propertyName)
    return environmentProvider.orElse(propertyProvider).orElse("").get()
}

tasks.named("jar") {
    enabled = false
}

subprojects {
    repositories {
        maven("https://nexus.sirblobman.xyz/private/") {
            credentials {
                username = getEnvOrProp("MAVEN_DEPLOY_USR", "maven.username.sirblobman")
                password = getEnvOrProp("MAVEN_DEPLOY_PSW", "maven.password.sirblobman")
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

fun getEnvOrProp(variableName: String, propertyName: String): String {
    val environmentProvider = providers.environmentVariable(variableName)
    val propertyProvider = providers.gradleProperty(propertyName)
    return environmentProvider.orElse(propertyProvider).orElse("").get()
}

repositories {
    maven("https://nexus.sirblobman.xyz/private/") {
        credentials {
            username = getEnvOrProp("MAVEN_DEPLOY_USR", "maven.username.sirblobman")
            password = getEnvOrProp("MAVEN_DEPLOY_PSW", "maven.password.sirblobman")
        }
    }
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // FactionsX
    compileOnly("net.prosavage.factionsx:FactionsX:1.2-STABLE")
}

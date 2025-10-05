val apiVersion = fetchProperty("version.api", "invalid")
rootProject.ext.set("apiVersion", apiVersion)

val mavenUsername = fetchEnv("MAVEN_DEPLOY_USR", "mavenUsernameSirBlobman", "")
rootProject.ext.set("mavenUsername", mavenUsername)

val mavenPassword = fetchEnv("MAVEN_DEPLOY_PSW", "mavenPasswordSirBlobman", "")
rootProject.ext.set("mavenPassword", mavenPassword)

val baseVersion = fetchProperty("version.base", "invalid")
val betaString = fetchProperty("version.beta", "false")
val jenkinsBuildNumber = fetchEnv("BUILD_NUMBER", null, "Unofficial")

val betaBoolean = betaString.toBoolean()
val betaVersion = if (betaBoolean) "Beta-" else ""
val calculatedVersion = "$baseVersion.$betaVersion$jenkinsBuildNumber"
rootProject.ext.set("isBeta", betaBoolean)
rootProject.ext.set("calculatedVersion", calculatedVersion)

fun fetchProperty(propertyName: String, defaultValue: String): String {
    val found = findProperty(propertyName)
    if (found != null) {
        return found.toString()
    }

    return defaultValue
}

fun fetchEnv(envName: String, propertyName: String?, defaultValue: String): String {
    val found = System.getenv(envName)
    if (found != null) {
        return found
    }

    if (propertyName != null) {
        return fetchProperty(propertyName, defaultValue)
    }

    return defaultValue
}

plugins {
    id("java")
}

tasks.named("jar") {
    enabled = false
}

allprojects {
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        withSourcesJar()
        withJavadocJar()
    }
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:26.0.2-1") // JetBrains Annotations
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:deprecation")
        }

        withType<Javadoc> {
            val standard = options as StandardJavadocDocletOptions
            standard.addBooleanOption("Xdoclint:none", true)
        }
    }
}

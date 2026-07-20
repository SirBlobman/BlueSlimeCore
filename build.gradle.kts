val baseVersion = fetchProperty("version.base", "invalid")
val betaString = fetchProperty("version.beta", "false")
val jenkinsBuildNumber = fetchEnv("BUILD_NUMBER", null, "Unofficial")

val betaBoolean = betaString.toBoolean()
rootProject.ext.set("isBeta", betaBoolean)
val betaVersion = if (betaBoolean) "Beta-" else ""
rootProject.version = "$baseVersion.$betaVersion$jenkinsBuildNumber"


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
        sourceCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
        withSourcesJar()
        withJavadocJar()
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:26.1.0") // JetBrains Annotations
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:deprecation")
            options.compilerArgs.add("-Xlint:-options")
        }

        withType<Javadoc> {
            val standard = options as StandardJavadocDocletOptions
            standard.addBooleanOption("Xdoclint:none", true)
        }
    }
}

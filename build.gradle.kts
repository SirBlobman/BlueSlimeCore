val apiVersion = findProperty("version.api") ?: "invalid"
rootProject.ext.set("apiVersion", apiVersion)

val baseVersion = findProperty("version.base") ?: "invalid"
val betaString = ((findProperty("version.beta") ?: "false") as String)
val jenkinsBuildNumber = System.getenv("BUILD_NUMBER") ?: "Unofficial"

val betaBoolean = betaString.toBoolean()
val betaVersion = if (betaBoolean) "Beta-" else ""
val calculatedVersion = "$baseVersion.$betaVersion$jenkinsBuildNumber"
rootProject.ext.set("calculatedVersion", calculatedVersion)

val mavenUsername = System.getenv("MAVEN_DEPLOY_USR") ?: findProperty("mavenUsernameSirBlobman") ?: ""
rootProject.ext.set("mavenUsername", mavenUsername)

val mavenPassword = System.getenv("MAVEN_DEPLOY_PSW") ?: findProperty("mavenPasswordSirBlobman") ?: ""
rootProject.ext.set("mavenPassword", mavenPassword)

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
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:24.0.1") // JetBrains Annotations
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.compilerArgs.add("-Xlint:deprecation")
        }

        javadoc {
            val standard = options as StandardJavadocDocletOptions
            standard.addStringOption("Xdoclint:none", "-quiet")
        }
    }
}

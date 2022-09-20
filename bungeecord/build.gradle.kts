group = "com.github.sirblobman.api.bungeecord"

allprojects {
    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    dependencies {
        compileOnly("net.md-5:bungeecord-api:1.19-R0.1-SNAPSHOT")
    }
}

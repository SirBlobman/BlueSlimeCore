repositories {
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io/")
    }
}

dependencies {
    implementation(project(":utility"))
    implementation(project(":bungeecord:abstract"))
    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.7.11-2")
}

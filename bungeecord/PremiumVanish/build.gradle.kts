repositories {
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly(project(":utility"))
    compileOnly(project(":bungeecord:abstract"))
    compileOnly("com.github.LeonMangler:PremiumVanishAPI:2.7.11-2")
}

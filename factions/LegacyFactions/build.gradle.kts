repositories {
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io/")
    }
}

dependencies {
    compileOnly(project(":factions:abstract"))
    compileOnly("com.github.redstone:LegacyFactions:v1.4.4")
}

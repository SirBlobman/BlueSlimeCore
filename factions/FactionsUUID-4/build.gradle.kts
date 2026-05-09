repositories {
    exclusiveContent {
        forRepository {
            maven("https://dependency.download/releases/")
        }

        filter {
            includeGroup("dev.kitteh")
        }
    }
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // FactionsUUID (v4)
    compileOnly("dev.kitteh:factions:4.0.0")

    // Adventure Extras
    compileOnly("net.kyori:adventure-text-minimessage:4.26.1")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.26.1")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.26.1")
}

repositories {
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":factions:abstract"))

    // FactionsUUID (Legacy)
    compileOnly("com.massivecraft:Factions:1.6.9.5-U0.2.1-RC")
}

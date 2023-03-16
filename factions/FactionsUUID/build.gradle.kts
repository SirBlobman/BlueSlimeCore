repositories {
    maven("https://ci.ender.zone/plugin/repository/everything/")
}

dependencies {
    compileOnly(project(":factions:abstract"))
    compileOnly("com.massivecraft:Factions:1.6.9.5-U0.6.27")
}

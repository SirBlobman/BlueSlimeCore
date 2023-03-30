repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(path = ":shaded", configuration = "shadow"))
    compileOnly(project(":utility"))

    // Paper API
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

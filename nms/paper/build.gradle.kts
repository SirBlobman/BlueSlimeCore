repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // Local Dependencies
    compileOnly(project(":nms:abstract"))
    compileOnly(project(":nms:modern-nbt"))
    compileOnly(project(":paper-helper"))

    // Paper API
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
}

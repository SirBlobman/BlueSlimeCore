# BlueSlimeCore

BlueSlimeCore is a library plugin that helps with Minecraft NMS and other features to support multiple Spigot versions.
This library is used in many of my projects, such as CombatLogX or Disco Armor.

## Supported Versions

Only Spigot and Paper are supported. Custom forks may work, but they are untested and support will not be provided.
The following Minecraft versions are currently supported:
- 1.7.10
- 1.8.8
- 1.9.4
- 1.10.2
- 1.11.2
- 1.12.2
- 1.13.2
- 1.14.4
- 1.15.2
- 1.16.5
- 1.17.1
- 1.18.2
- 1.19.2

## Dependency Information

You can use my library in your projects as well.
Just make sure to install the BlueSlimeCore jar on your server!

### Repositories

```xml
<repositories>
    <!-- SirBlobman Public Repository -->
    <repository>
        <id>sirblobman-public</id>
        <url>https://nexus.sirblobman.xyz/repository/public/</url>
    </repository>
</repositories>
```

### Dependencies

```xml
<dependencies>
    <!-- SirBlobmanCore Plugin -->
    <dependency>
        <groupId>com.github.sirblobman.api</groupId>
        <artifactId>core</artifactId>
        <version>2.3-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Bukkit plugin.yml

```yml
depend:
  - "BlueSlimeCore"
```

## Compilation Instructions

If you wish to compile the plugin yourself, you can run the following commands:
```shell
git clone "https://github.com/SirBlobman/BlueSlimeCore.git"
cd BlueSlimeCore
mvn package
```

# SirBlobmanAPI
An API that helps with minecraft NMS and other stuff.  
This API is used in many of my other projects, such as CombatLogX or Disco Armor.

## Supported Versions
Only Spigot and Paper are supported. Other forks may work but we do not not provide support or test with them.

The following Minecraft versions are supported:
- 1.17.10
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
- 1.18
- 1.19

## Maven Information
You can use SirBlobmanAPI in your projects.  
Just make sure to install the SirBlobmanCore plugin jar on your server!

**pom.xml**
```xml
<repositories>
    <!-- SirBlobman Public Repository -->
    <repository>
        <id>sirblobman-public</id>
        <url>https://nexus.sirblobman.xyz/repository/public/</url>
    </repository>
</repositories>

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

**plugin.yml**
```yml
depend:
  - "SirBlobmanCore"
```

# SirBlobmanAPI
An API that helps with minecraft NMS and other stuff.  
This API is used in many of my other projects, such as CombatLogX or Disco Armor.

## Maven Information
You can use SirBlobmanAPI in your projects.  
Just make sure to install the SirBlobmanCore plugin jar on your server!

**pom.xml**
```xml
<repositories>
    <!-- CodeMC Public Repository -->
    <repository>
        <id>codemc-public</id>
        <url>https://repo.codemc.io/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <!-- SirBlobmanCore Plugin -->
    <dependency>
        <groupId>com.github.sirblobman.api</groupId>
        <artifactId>core</artifactId>
        <version>2.1-SNAPSHOT</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**plugin.yml**
```yml
depend:
  - "SirBlobmanCore"
```
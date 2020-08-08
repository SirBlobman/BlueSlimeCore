# SirBlobmanAPI

An API that helps with minecraft NMS and other stuff.  
This API is used in many of my other projects, such as CombatLogX or Enderpearl Cooldown.  
Most of the time it will be shaded into a different package to prevent interference between plugins.

## Maven Information
You can use SirBlobmanAPI in your projects. Just make sure to shade it into your jar!
```xml
<repositories>
    <!-- CodeMC Public Repository -->
    <repository>
        <id>codemc-public</id>
        <url>https://repo.codemc.io/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.SirBlobman.api</groupId>
        <artifactId>SirBlobmanAPI</artifactId>
        <version>1.13-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```
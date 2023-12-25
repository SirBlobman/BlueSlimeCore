# BlueSlimeCore
![Jenkins](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.sirblobman.xyz%2Fjob%2FSirBlobman%2Fjob%2FBlueSlimeCore%2Fjob%2Fmain%2F&style=plastic)
![Maven Metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fnexus.sirblobman.xyz%2Fpublic%2Fcom%2Fgithub%2Fsirblobman%2Fapi%2Fcore%2Fmaven-metadata.xml&style=plastic)

## Description:

BlueSlimeCore is a library plugin with many features that are necessary for my other plugins.
This library is also free for other developers to use.

### Downloads

- [SpigotMC](https://www.spigotmc.org/resources/83189/)
- [Hangar](https://hangar.papermc.io/SirBlobman/BlueSlimeCore)
- [Jenkins Beta Builds](https://jenkins.sirblobman.xyz/job/SirBlobman/job/BlueSlimeCore/job/main/)

### Version Information

BlueSlimeCore supports Spigot, Paper, and Folia. Custom forks may work, but they are untested. 
The following table is a list of versions that are included in code. 
Unsupported versions are planned for removal at a future date.

| NMS Version | Alias  | Supported          | Tested             | Recommended        | 
|-------------|--------|--------------------|--------------------|--------------------|
| 1_8_R3      | 1.8.8  | :x:                | :x:                | :x:                |
| 1_12_R1     | 1.12.2 | :x:                | :x:                | :x:                |
| 1_16_R3     | 1.16.5 | :x:                | :x:                | :x:                |
| 1_17_R1     | 1.17.1 | :x:                | :x:                | :x:                |
| 1_18_R2     | 1.18.2 | :white_check_mark: | :x:                | :x:                |
| 1_19_R3     | 1.19.4 | :white_check_mark: | :white_check_mark: | :white_check_mark: |
| 1_20_R1     | 1.20.1 | :x:                | :x:                | :x:                |
| 1_20_R2     | 1.20.2 | :x:                | :x:                | :x:                |
| 1_20_R3     | 1.20.4 | :white_check_mark: | :white_check_mark: | :white_check_mark: |

We only provide support for tested software and recommended versions.

### Features for Servers
* '/item-to-nbt' command to view NBT data tags on items.
* '/item-to-base64' command to convert items to Base64 strings.
* '/debug-event' command to find plugins and listeners for events.

### Features for Developers
* Configuration management system for saving and loading of multiple files.
* Language management system with support for per-player languages, MiniMessage, and PlaceholderAPI.
* Ability to send action bars, titles, and sounds through the language API.
* Command system with player checking and sub commands.
* Plugin update checking with SpigotMC or Hangar.
* Menu API for creation of menus with buttons or pages and a configurable item format.
* Factions Handler API to aid support with many different Factions plugin.
* Item builders to quickly create items, leather armor, potions, and player heads.
* Multiple version handler for spawning entities, changing max health, checking absorption.
* Multiple version handler for sending cooldown packets and checking server TPS.
* Utility methods for dealing with block/entity locations, items, experience, hex colors, and versions.

## Dependency Information

BlueSlimeCore can also be used as a library in your own plugins.
Just make sure to install the BlueSlimeCore jar on your server!

[Developers Guide / Wiki](https://github.com/SirBlobman/BlueSlimeCore/wiki)

### Compilation Instructions

If you wish to compile the plugin yourself, you can run the following commands:

```shell
git clone "https://github.com/SirBlobman/BlueSlimeCore.git"
cd BlueSlimeCore
./gradlew clean build
```

### Further Instructions

If you want to review API examples and other information,
please visit the [wiki](https://github.com/SirBlobman/BlueSlimeCore/wiki/) tab for this project.
You can also ask questions on my [Discord](https://discord.gg/XMq2agT) or create an issue on this page.

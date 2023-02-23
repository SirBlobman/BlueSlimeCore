rootProject.name = "BlueSlimeCore"

// Utilities
include("shaded")
include("utility")
include("paper-helper")

// BossBar
include("bossbar")

// NMS Abstract
include("nms")
include("nms:abstract")

// NMS Versions
include("nms:fallback")
include("nms:1_7_R4")
include("nms:1_8_R3")
include("nms:1_9_R2")
include("nms:1_10_R1")
include("nms:1_11_R1")
include("nms:1_12_R1")
include("nms:1_13_R2")
include("nms:1_14_R1")
include("nms:1_15_R1")
include("nms:1_16_R3")
include("nms:1_17_R1")
include("nms:1_18_R2")
include("nms:1_19_R1")
include("nms:1_19_R2")

// NMS Handler
include("nms:scoreboard")
include("nms:handler")

// Factions
include("factions")
include("factions:abstract")

// Factions Versions
include("factions:LegacyFactions")
include("factions:MassiveCore-Factions")
include("factions:SaberFactions")
include("factions:FactionsUUID")
include("factions:FactionsUUID-Legacy")
include("factions:FactionsX")

// Factions Handler
include("factions:handler")

// Regular Modules
include("configuration")
include("language")
include("update")
include("item")
include("menu")
include("plugin")
include("command")

// Core / Plugin
include("core")

// BungeeCord
include("bungeecord")
include("bungeecord:abstract")

// BungeeCord Hooks
include("bungeecord:BungeePerms")
include("bungeecord:LuckPerms")
include("bungeecord:PremiumVanish")

// BungeeCord Core
include("bungeecord:core")

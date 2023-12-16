package com.github.sirblobman.api.utility;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public final class VersionUtility {
    static {
        String bukkitVersion = Bukkit.getBukkitVersion();
        if (bukkitVersion.contains("-pre") || bukkitVersion.contains("-rc")) {
            Logger logger = Bukkit.getLogger();
            logger.warning("[BlueSlimeCore] You are using a '-pre' or '-rc' version of spigot.");
            logger.warning("[BlueSlimeCore] Bugs may occur when using a preview version.");
        }
    }

    /**
     * @return The current Minecraft version of the server (Example: 1.16.5)
     */
    public static @NotNull String getMinecraftVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        int firstDash = bukkitVersion.indexOf('-');
        return bukkitVersion.substring(0, firstDash);
    }

    /**
     * @return The current NMS version of the server (Example: 1_16_R3)
     */
    public static @NotNull String getNetMinecraftServerVersion() {
        Server server = Bukkit.getServer();
        Class<? extends Server> serverClass = server.getClass();
        Package serverPackage = serverClass.getPackage();
        String serverPackageName = serverPackage.getName();

        int lastPeriodIndex = serverPackageName.lastIndexOf('.');
        int nextIndex = (lastPeriodIndex + 2);
        return serverPackageName.substring(nextIndex);
    }

    /**
     * @return The current 'major.minor' version of the server (Example: 1.16)
     */
    public static @NotNull String getMajorMinorVersion() {
        String minecraftVersion = getMinecraftVersion();
        int lastPeriodIndex = minecraftVersion.lastIndexOf('.');
        return (lastPeriodIndex < 2 ? minecraftVersion : minecraftVersion.substring(0, lastPeriodIndex));
    }

    /**
     * @return The current major version of the server as an integer (Example: {@code 1})
     */
    public static int getMajorVersion() {
        String majorMinorVersion = getMajorMinorVersion();
        int periodIndex = majorMinorVersion.indexOf('.');

        String majorString = majorMinorVersion.substring(0, periodIndex);
        return Integer.parseInt(majorString);
    }

    /**
     * @return The current minor version of the server as an integer (Example: {@code 16})
     */
    public static int getMinorVersion() {
        String majorMinorVersion = getMajorMinorVersion();
        int periodIndex = majorMinorVersion.indexOf('.');
        int nextIndex = (periodIndex + 1);

        String minorString = majorMinorVersion.substring(nextIndex);
        return Integer.parseInt(minorString);
    }
}

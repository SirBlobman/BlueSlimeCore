package com.github.sirblobman.api.utility;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.Server;

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
        try {
            return getNewMinecraftVersion();
        } catch (ReflectiveOperationException ex) {
            String bukkitVersion = Bukkit.getBukkitVersion();
            int firstDash = bukkitVersion.indexOf('-');
            return bukkitVersion.substring(0, firstDash);
        }
    }

    /**
     * @return The current Minecraft version. (Example: 1.20.5)
     * @throws ReflectiveOperationException This method only works on Paper servers. Spigot will throw an error.
     */
    public static @NotNull String getNewMinecraftVersion() throws ReflectiveOperationException {
        Server server = Bukkit.getServer();
        Class<? extends Server> serverClass = server.getClass();
        Method method_getMinecraftVersion = serverClass.getMethod("getMinecraftVersion");
        return (String) method_getMinecraftVersion.invoke(server);
    }

    /**
     * @return The current NMS version of the server (Example: 1_16_R3)
     */
    public static @NotNull String getNetMinecraftServerVersion() {
        try {
            String newMinecraftVersion = getNewMinecraftVersion();

            String version = "Unsupported";
            switch(newMinecraftVersion) {
                case "1.16.5": version = "1_16_R3"; break;
                case "1.17.1": version = "1_17_R1"; break;
                case "1.18.2": version = "1_19_R2"; break;
                case "1.19.4": version = "1_19_R3"; break;
                case "1.20.2": version = "1_20_R2"; break;
                case "1.20.4": version = "1_20_R3"; break;
                case "1.20.6": version = "1_20_R4"; break;
                case "1.21": version = "1_21_R1"; break;
            }

            return version;
        } catch (ReflectiveOperationException ex) {
            Server server = Bukkit.getServer();
            Class<? extends Server> serverClass = server.getClass();
            Package serverPackage = serverClass.getPackage();
            String serverPackageName = serverPackage.getName();

            int lastPeriodIndex = serverPackageName.lastIndexOf('.');
            int nextIndex = (lastPeriodIndex + 2);
            return serverPackageName.substring(nextIndex);
        }
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

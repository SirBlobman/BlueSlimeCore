package com.SirBlobman.api.nms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public final class VersionUtil {
    private static final Pattern MC_VERSION_PATTERN = Pattern.compile("(\\(MC: )([\\d.]+)(\\))");
    public static String getMinecraftVersion() {
        String bukkitVersion = Bukkit.getVersion();
        Matcher matcher = MC_VERSION_PATTERN.matcher(bukkitVersion);
        if(!matcher.find()) return "";
        
        return matcher.group(2);
    }
    
    public static String getBaseVersion() {
        String version = getMinecraftVersion();
        int lastPeriodIndex = version.lastIndexOf('\u002E');
        
        return (lastPeriodIndex < 2 ? version : version.substring(0, lastPeriodIndex));
    }
    
    public static String getNetMinecraftServerVersion() {
        Server server = Bukkit.getServer();
        Class<?> serverClass = server.getClass();
        
        Package serverPackage = serverClass.getPackage();
        String packageName = serverPackage.getName();
        
        int periodIndex = (packageName.lastIndexOf('\u002E') + 2);
        return packageName.substring(periodIndex);
    }
    
    public static int getMajorVersion() {
        String baseVersion = getBaseVersion();
        int indexOfPeriod = baseVersion.indexOf('\u002E');
        
        String majorString = baseVersion.substring(0, indexOfPeriod);
        return Integer.parseInt(majorString);
    }
    
    public static int getMinorVersion() {
        String baseVersion = getBaseVersion();
        int indexOfPeriod = (baseVersion.indexOf('\u002E') + 1);
        
        String minorString = baseVersion.substring(indexOfPeriod);
        return Integer.parseInt(minorString);
    }
}
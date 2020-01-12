package com.SirBlobman.api.nms;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.SirBlobman.api.nms.boss.bar.BossBarHandler;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public abstract class NMS_Handler {
    private static final Pattern MC_VERSION_PATTERN = Pattern.compile("(\\(MC: )([\\d.]+)(\\))");
    public static boolean WARNING_SENT = false;
    
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
    
    public static NMS_Handler getHandler() {
        String nmsVersion = getNetMinecraftServerVersion();
        String className = ("com.SirBlobman.api.nms.NMS_" + nmsVersion);
        
        try {
            Class<?> handlerClass = Class.forName(className);
            Object instance = handlerClass.newInstance();
            return (NMS_Handler) instance;
        } catch(ReflectiveOperationException ex1) {
            if(!WARNING_SENT) {
                WARNING_SENT = true;
                Logger logger = Logger.getLogger("SirBlobmanAPI");
                logger.warning("Could not find valid NMS handler for version '" + nmsVersion + "'. Using fallback handler...");
            }
            
            return getFallbackHandler();
        }
    }
    
    public static NMS_Handler getFallbackHandler() {
        try {
            Class<?> handlerClass = Class.forName("com.SirBlobman.api.nms.NMS_Fallback");
            Object instance = handlerClass.newInstance();
            return (NMS_Handler) instance;
        } catch(ReflectiveOperationException ex) {
            Logger logger = Logger.getLogger("SirBlobmanAPI");
            logger.log(Level.SEVERE, "An error occurred while getting the fallback NMS Handler!", ex);
            return null;
        }
    }
    
    public abstract BossBarHandler getBossBarHandler();
    public abstract PlayerHandler getPlayerHandler();
    public abstract EntityHandler getEntityHandler();
    public abstract ScoreboardHandler getScoreboardHandler();
}
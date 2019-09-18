package com.SirBlobman.api.nms;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.google.gson.JsonObject;

public abstract class NMS_Handler {
    public static final String getMinecraftVersion() {
        String bukkitVersion = Bukkit.getVersion();
        Pattern pattern = Pattern.compile("(\\(MC: )([\\d\\.]+)(\\))");
        Matcher matcher = pattern.matcher(bukkitVersion);
        
        if(matcher.find()) return matcher.group(2);
        return "";
    }

    public static final String baseVersion() {
        String version = getMinecraftVersion();
        int last = version.lastIndexOf('.');
        String base = (last < 2) ? version : version.substring(0, last);
        return base;
    }
    
    public static final String getNetMinecraftServerVersion() {
        Server server = Bukkit.getServer();
        Class<?> server_class = server.getClass();
        Package server_package = server_class.getPackage();
        String packageName = server_package.getName();
        
        int index = (packageName.lastIndexOf('\u002E') + 2);
        return packageName.substring(index);
    }

    public static final int getMajorVersion() {
        String baseVersion = baseVersion();
        String majorString = baseVersion.substring(0, baseVersion.indexOf("."));
        return Integer.parseInt(majorString);
    }

    public static final int getMinorVersion() {
        String baseVersion = baseVersion();
        String minorString = baseVersion.substring(baseVersion.indexOf(".") + 1);
        return Integer.parseInt(minorString);
    }
    
    public static boolean WARNING_SENT = false;
    public static NMS_Handler getHandler() {
        String nmsVersion = getNetMinecraftServerVersion();
        String className = "com.SirBlobman.api.nms.NMS_" + nmsVersion;
        try {
            Class<?> handler_class = Class.forName(className);
            return (NMS_Handler) handler_class.newInstance();
        } catch (ReflectiveOperationException ex1) {
            try {
                if(!WARNING_SENT) {
                    WARNING_SENT = true;
                    Logger.getLogger("SirBlobmanAPI").warning("Could not find valid NMS handler for '" + nmsVersion + "'. Using fallback handler...");
                }
                
                return (NMS_Handler) Class.forName("com.SirBlobman.api.nms.NMS_Fallback").newInstance();
            } catch(ReflectiveOperationException ex2) {
                Logger.getLogger("SirBlobmanAPI").severe("An error occurred while getting the NMS Handler!");
                ex2.printStackTrace();
                return null;
            }
        }
    }
    
    public final void removeBossBar(Player player) {
        BossBar.removeBossBar(player);
    }
    
    public final String toJson(String text) {
        if(text == null) text = "";
        
        JsonObject object = new JsonObject();
        object.addProperty("text", text);
        return object.toString();
    }
    
    public abstract void sendActionBar(Player player, String message);
    public abstract void sendNewBossBar(Player player, String title, double progress, String colorName, String styleName);
    public abstract void setTab(Player player, String header, String footer);
    
    public abstract double getMaxHealth(LivingEntity entity);
    public abstract void setMaxHealth(LivingEntity entity, double maxHealth);
    public abstract void forceRespawn(Player player);

    public abstract Objective createScoreboardObjective(Scoreboard scoreboard, String name, String criteria, String displayName);
}
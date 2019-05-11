package com.SirBlobman.api.utility;

import org.bukkit.ChatColor;

public class MessageUtil {
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String undoColor(String message) {
        return message.replace(ChatColor.COLOR_CHAR, '&');
    }
    
    public static String removeColor(String message) {
        return ChatColor.stripColor(message);
    }
    
    public static String[] color(Object... messages) {
        String[] colorArray = new String[messages.length];
        
        int arrayId = 0;
        for(Object messageObj : messages) {
            String message = messageObj.toString();
            String color = color(message);
            colorArray[arrayId++] = color;
        }
        
        return colorArray;
    }
}
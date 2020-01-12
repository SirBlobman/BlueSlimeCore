package com.SirBlobman.api.utility;

import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;

public final class MessageUtil {
    public static String color(String message) {
        if(message == null) return "";
        
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    public static String[] colorArray(String... messageArray) {
        int messageArrayLength = messageArray.length;
        String[] colorArray = new String[messageArrayLength];
        
        for(int i = 0; i < messageArrayLength; i++) {
            String message = messageArray[i];
            colorArray[i] = color(message);
        }
        
        return colorArray;
    }
    
    public static List<String> colorList(Collection<String> messageList) {
        List<String> colorList = Util.newList();
        
        for(String message : messageList) {
            String color = color(message);
            colorList.add(color);
        }
        
        return colorList;
    }
}
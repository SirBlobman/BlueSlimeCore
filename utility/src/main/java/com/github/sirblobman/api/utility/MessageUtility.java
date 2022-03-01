package com.github.sirblobman.api.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

public final class MessageUtility {
    /**
     * @param message The message that will be colored
     * @return A new string containing {@code message} but with the color codes replaced, or an empty string if message
     * was {@code null}.
     * @see ChatColor#translateAlternateColorCodes(char, String)
     * @see HexColorUtility#replaceHexColors(char, String)
     */
    public static String color(String message) {
        if(message == null || message.isEmpty()) {
            return "";
        }
        
        int minorVersion = VersionUtility.getMinorVersion();
        if(minorVersion < 16) {
            return org.bukkit.ChatColor.translateAlternateColorCodes('&', message);
        }
        
        String messageReplaced = HexColorUtility.replaceHexColors('&', message);
        return net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', messageReplaced);
    }
    
    /**
     * @param messageArray The array of messages that will be colored
     * @return A new array containing every message in the input array, but with color codes replaced.
     */
    public static String[] colorArray(String... messageArray) {
        int messageArrayLength = messageArray.length;
        String[] colorMessageArray = new String[messageArrayLength];
        
        for(int i = 0; i < messageArrayLength; i++) {
            String message = messageArray[i];
            colorMessageArray[i] = color(message);
        }
        
        return colorMessageArray;
    }
    
    
    /**
     * @param messageList The iterable of messages that will be colored
     * @return A {@code List<String>} containing every message in the input iterable, but with color codes replaced.
     * @see List
     * @see java.util.Collection
     */
    public static List<String> colorList(Iterable<String> messageList) {
        List<String> colorList = new ArrayList<>();
        for(String message : messageList) {
            String color = color(message);
            colorList.add(color);
        }
        return colorList;
    }
    
    /**
     * @param messageArray The array of messages that will be colored
     * @return A {@code List<String>} containing every message in the input array, but with color codes replaced.
     */
    public static List<String> colorList(String... messageArray) {
        List<String> messageList = Arrays.asList(messageArray);
        return colorList(messageList);
    }
}

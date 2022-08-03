package com.github.sirblobman.api.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import org.apache.commons.lang.Validate;

public final class MessageUtility {
    /**
     * @param message The message that will be colored
     * @return A new string containing {@code message} but with the color codes replaced,
     * or an empty string if {@code message} was {@code null}.
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

    /**
     * Copies all elements from the iterable collection of originals to a
     * new {@link List}
     *
     * @param <T> the collection of strings
     * @param token String to search for
     * @param originals An iterable collection of strings to filter.
     * @return the list of all matches.
     * @throws IllegalArgumentException if any parameter is null
     * @throws IllegalArgumentException if originals contains a null element.
     */
    public static List<String> getMatches(String token, Iterable<String> originals) {
        Validate.notNull(token, "Search token cannot be null");
        Validate.notNull(originals, "Originals cannot be null");
        List<String> collection = new ArrayList<>();

        for (String string : originals) {
            if (startsWithIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    /**
     * This method uses a region to check case-insensitive equality. This
     * means the internal array does not need to be copied like a
     * toLowerCase() call would.
     *
     * @param string String to check
     * @param prefix Prefix of string to compare
     * @return true if provided string starts with, ignoring case, the prefix
     *     provided
     * @throws NullPointerException if prefix is null
     * @throws IllegalArgumentException if string is null
     */
    public static boolean startsWithIgnoreCase(String string, String prefix) {
        Validate.notNull(string, "Cannot check a null string for a match");
        if (string.length() < prefix.length()) {
            return false;
        }

        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }
}

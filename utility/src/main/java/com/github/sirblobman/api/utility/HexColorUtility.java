package com.github.sirblobman.api.utility;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

public final class HexColorUtility {
    /**
     * @param colorChar The character that will be used for color codes (usually {@code '&'})
     * @param string The {@link String} that will have its values replaced.
     * @return A new {@link String} with the hex color codes being replaced.
     */
    public static String replaceHexColors(char colorChar, String string) {
        Pattern pattern = getReplaceAllRgbPattern(colorChar);
        Matcher matcher = pattern.matcher(string);
        
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()) {
            if(matcher.group(1) != null) {
                matcher.appendReplacement(buffer, colorChar + "#$2");
                continue;
            }
            
            try {
                String hexCodeString = matcher.group(2);
                String hexCode = parseHexColor(hexCodeString);
                matcher.appendReplacement(buffer, hexCode);
            } catch(NumberFormatException ignored) {}
        }
        
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    
    private static Pattern getReplaceAllRgbPattern(char colorChar) {
        String colorCharString = Character.toString(colorChar);
        String colorCharPattern = Pattern.quote(colorCharString);
        
        String patternString = ("(" + colorCharPattern + ")?" + colorCharPattern + "#([0-9a-fA-F]{6})");
        return Pattern.compile(patternString);
    }
    
    private static String parseHexColor(String string) throws NumberFormatException {
        if(string.startsWith("#")) string = string.substring(1);
        if(string.length() != 6) throw new NumberFormatException("Invalid hex length");
        
        Color.decode("#" + string);
        StringBuilder assembled = new StringBuilder();
        
        assembled.append(ChatColor.COLOR_CHAR);
        assembled.append("x");
        
        char[] charArray = string.toCharArray();
        for(char character : charArray) {
            assembled.append(ChatColor.COLOR_CHAR);
            assembled.append(character);
        }
        
        return assembled.toString();
    }
}
package com.SirBlobman.api.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class Util {
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static String undoColor(String string) {
		return string.replace(ChatColor.COLOR_CHAR, '&');
	}
	
	public static String removeColor(String string) {
		return ChatColor.stripColor(string);
	}
	
	public static <L> List<L> newList(Collection<L> oldList) {
		return new ArrayList<>(oldList);
	}
	
	@SafeVarargs
	public static <L> List<L> newList(L... objects) {
		List<L> newList = new ArrayList<>();
		for(L object : objects) newList.add(object);
		return newList;
	}
	
	public static <K, V> Map<K, V> newMap(Map<K, V> oldMap) {
		return new HashMap<>(oldMap);
	}
	
	public static <K, V> Map<K, V> newMap() {
		return new HashMap<>();
	}
}
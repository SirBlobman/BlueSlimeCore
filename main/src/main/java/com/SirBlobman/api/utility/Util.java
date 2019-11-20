package com.SirBlobman.api.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

public class Util {
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
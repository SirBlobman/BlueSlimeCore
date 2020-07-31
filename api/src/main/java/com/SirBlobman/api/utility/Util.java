package com.SirBlobman.api.utility;

import java.util.*;

public final class Util {
    public static <L> List<L> newList(Collection<L> oldList) {
        return new ArrayList<>(oldList);
    }
    
    @SafeVarargs
    public static <L> List<L> newList(L... oldArray) {
        List<L> newList = new ArrayList<>();
        Collections.addAll(newList, oldArray);
        return newList;
    }
    
    public static <K, V> Map<K, V> newMap(Map<K, V> oldMap) {
        return new HashMap<>(oldMap);
    }
    
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }
}
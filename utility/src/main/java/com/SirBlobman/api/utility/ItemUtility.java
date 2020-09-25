package com.SirBlobman.api.utility;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtility {
    public static boolean isAir(ItemStack item) {
        if(item == null) return true;
        Material material = item.getType();
        String materialName = material.name();

        List<String> airList = Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR");
        return airList.contains(materialName);
    }

    public static ItemStack getAir() {
        return new ItemStack(Material.AIR);
    }

    public static boolean hasItemMeta(ItemStack item) {
        return (item != null && item.hasItemMeta());
    }

    public static boolean hasDisplayName(ItemStack item) {
        if(hasItemMeta(item)) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && meta.hasDisplayName());
        }

        return false;
    }

    public static boolean hasLore(ItemStack item) {
        if(hasItemMeta(item)) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && meta.hasLore());
        }

        return false;
    }

    public static boolean doesAnyLoreContain(ItemStack item, String string) {
        if(hasLore(item)) {
            ItemMeta meta = item.getItemMeta();
            if(meta == null) return false;

            List<String> loreList = meta.getLore();
            if(loreList == null || loreList.isEmpty()) return false;

            return loreList.stream().anyMatch(line -> line.contains(string));
        }

        return false;
    }
}
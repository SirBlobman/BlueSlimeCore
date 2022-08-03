package com.github.sirblobman.api.utility;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtility {
    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code true} if the item is null or has an AIR material, {@code false} otherwise.
     */
    public static boolean isAir(ItemStack item) {
        if (item == null) return true;
        Material material = item.getType();
        String materialName = material.name();

        List<String> airList = Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR");
        return airList.contains(materialName);
    }

    /**
     * @return an {@link ItemStack} with the type set to {@link Material#AIR}
     */
    public static ItemStack getAir() {
        return new ItemStack(Material.AIR);
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing {@link ItemMeta}, {@code true} otherwise.
     */
    public static boolean hasItemMeta(ItemStack item) {
        return (item != null && item.hasItemMeta());
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing a display name, {@code true} otherwise.
     */
    public static boolean hasDisplayName(ItemStack item) {
        if (hasItemMeta(item)) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && meta.hasDisplayName());
        }

        return false;
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing a lore, {@code true} otherwise.
     */
    public static boolean hasLore(ItemStack item) {
        if (hasItemMeta(item)) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && meta.hasLore());
        }

        return false;
    }

    /**
     * @param item  The {@link ItemStack} to check.
     * @param query A {@link String} to check for in each line.
     * @return {@code true} if a line contains the string, {@code false} otherwise.
     */
    public static boolean doesAnyLoreContain(ItemStack item, String query) {
        if (hasLore(item)) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null) return false;

            List<String> loreList = meta.getLore();
            if (loreList == null || loreList.isEmpty()) return false;

            for (String line : loreList) {
                if (line.contains(query)) return true;
            }
        }

        return false;
    }
}

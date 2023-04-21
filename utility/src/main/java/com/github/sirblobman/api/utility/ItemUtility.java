package com.github.sirblobman.api.utility;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class ItemUtility {
    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code true} if the item is null or has an AIR material, {@code false} otherwise.
     */
    @Contract("null -> true")
    public static boolean isAir(@Nullable ItemStack item) {
        if (item == null) {
            return true;
        }

        Material material = item.getType();
        String materialName = material.name();

        List<String> airList = Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR");
        return airList.contains(materialName);
    }

    /**
     * @return an {@link ItemStack} with the type set to {@link Material#AIR}
     */
    public static @NotNull ItemStack getAir() {
        return new ItemStack(Material.AIR);
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing {@link ItemMeta}, {@code true} otherwise.
     */
    @Contract("null -> false")
    public static boolean hasItemMeta(@Nullable ItemStack item) {
        if (isAir(item)) {
            return false;
        }

        return item.hasItemMeta();
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing a display name, {@code true} otherwise.
     */
    @Contract("null -> false")
    public static boolean hasDisplayName(@Nullable ItemStack item) {
        if (!hasItemMeta(item)) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        return itemMeta.hasDisplayName();
    }

    /**
     * @param item The {@link ItemStack} to check.
     * @return {@code false} if the item is null or is missing a lore, {@code true} otherwise.
     */
    @Contract("null -> false")
    public static boolean hasLore(@Nullable ItemStack item) {
        if (!hasItemMeta(item)) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        return itemMeta.hasLore();
    }

    /**
     * @param item  The {@link ItemStack} to check.
     * @param query A {@link String} to check for in each line.
     * @return {@code true} if a line contains the string, {@code false} otherwise.
     */
    @Contract("null,_ -> false")
    public static boolean doesAnyLoreContain(@Nullable ItemStack item, @NotNull String query) {
        if (!hasLore(item)) {
            return false;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        List<String> loreList = itemMeta.getLore();
        if (loreList == null || loreList.isEmpty()) {
            return false;
        }

        for (String line : loreList) {
            if (line.contains(query)) {
                return true;
            }
        }

        return false;
    }
}

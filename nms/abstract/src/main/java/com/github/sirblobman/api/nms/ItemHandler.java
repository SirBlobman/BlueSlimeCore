package com.github.sirblobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemHandler extends Handler {
    public ItemHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getLocalizedName(org.bukkit.inventory.ItemStack item);
    public abstract String getKeyString(org.bukkit.inventory.ItemStack item);

    public abstract String toNBT(org.bukkit.inventory.ItemStack item);
    public abstract org.bukkit.inventory.ItemStack fromNBT(String nbtJSON);

    public abstract org.bukkit.inventory.ItemStack setCustomNBT(org.bukkit.inventory.ItemStack item, String key, String value);
    public abstract org.bukkit.inventory.ItemStack removeCustomNBT(org.bukkit.inventory.ItemStack item, String key);
    public abstract String getCustomNBT(org.bukkit.inventory.ItemStack item, String key, String defaultValue);

    public abstract org.bukkit.inventory.ItemStack fromBase64String(String string);
    public abstract String toBase64String(org.bukkit.inventory.ItemStack item);
}
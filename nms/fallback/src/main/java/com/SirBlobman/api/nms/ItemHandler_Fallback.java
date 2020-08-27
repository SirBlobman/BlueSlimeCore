package com.SirBlobman.api.nms;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemHandler_Fallback extends ItemHandler {
    public ItemHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        return "N/A";
    }

    @Override
    public String toNBT(ItemStack item) {
        return "{}";
    }

    @Override
    public ItemStack fromNBT(String nbtJSON) {
        return null;
    }

    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        return item;
    }

    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        return defaultValue;
    }

    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        return item;
    }

}
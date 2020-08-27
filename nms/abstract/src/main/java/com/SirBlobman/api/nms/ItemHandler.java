package com.SirBlobman.api.nms;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemHandler extends Handler {
    public ItemHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getLocalizedName(ItemStack item);

    public abstract String toNBT(ItemStack item);
    public abstract ItemStack fromNBT(String nbtJSON);

    public abstract ItemStack setCustomNBT(ItemStack item, String key, String value);
    public abstract String getCustomNBT(ItemStack item, String key, String defaultValue);
    public abstract ItemStack removeCustomNBT(ItemStack item, String key);
}
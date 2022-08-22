package com.github.sirblobman.api.nms;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;

public abstract class ItemHandler extends Handler {
    public ItemHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract String getLocalizedName(org.bukkit.inventory.ItemStack item);

    public abstract String getKeyString(org.bukkit.inventory.ItemStack item);

    public abstract String toNBT(org.bukkit.inventory.ItemStack item);

    public abstract org.bukkit.inventory.ItemStack fromNBT(String nbtJSON);

    // TODO @Deprecated
    public abstract org.bukkit.inventory.ItemStack setCustomNBT(org.bukkit.inventory.ItemStack item, String key,
                                                                String value);

    // TODO @Deprecated
    public abstract org.bukkit.inventory.ItemStack removeCustomNBT(org.bukkit.inventory.ItemStack item, String key);

    // TODO public abstract CustomNbtContainer createNewCustomNbtContainer();

    // TODO
//    public abstract org.bukkit.inventory.ItemStack setCustomNbt(org.bukkit.inventory.ItemStack item,
//                                                                CustomNbtContainer customNbtContainer);

    public abstract String getCustomNBT(org.bukkit.inventory.ItemStack item, String key, String defaultValue);

    public abstract org.bukkit.inventory.ItemStack fromBase64String(String string);

    public abstract String toBase64String(org.bukkit.inventory.ItemStack item);

    public abstract org.bukkit.inventory.ItemStack setDisplayName(org.bukkit.inventory.ItemStack item,
                                                                  Component displayName);

    public abstract org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item,
                                                           List<Component> lore);
}

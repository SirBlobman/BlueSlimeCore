package com.github.sirblobman.api.nms;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.shaded.adventure.text.Component;
import com.github.sirblobman.api.nbt.CustomNbtContainer;


public final class ItemHandler_Fallback extends ItemHandler {
    public ItemHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(ItemStack item) {
        return "N/A";
    }

    @Override
    public String getKeyString(ItemStack item) {
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
    public ItemStack fromBase64String(String string) {
        return null;
    }

    @Override
    public String toBase64String(ItemStack item) {
        return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack setDisplayName(org.bukkit.inventory.ItemStack item,
                                                         Component displayName) {
        return item;
    }

    @Override
    public org.bukkit.inventory.ItemStack setLore(org.bukkit.inventory.ItemStack item,
                                                  List<Component> lore) {
        return item;
    }

    @Override
    public CustomNbtContainer createNewCustomNbtContainer() {
        return null;
    }

    @Override
    public org.bukkit.inventory.ItemStack setCustomNbt(org.bukkit.inventory.ItemStack item,
                                                       CustomNbtContainer customNbtContainer) {
        return item;
    }

    @Override
    public CustomNbtContainer getCustomNbt(org.bukkit.inventory.ItemStack item) {
        return null;
    }
}

package com.SirBlobman.api.nms;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemHandler_Fallback extends ItemHandler {
    public ItemHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Material matchMaterial(String string) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public void setDamage(ItemStack item, int damage) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public String getLocalizedName(ItemStack item) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public String toNBT(ItemStack item) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack fromNBT(String string) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack getPlayerHead(String username) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack getBase64Head(String base64) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
    
    @Override
    public ItemStack getBase64Head(String base64, UUID uuid) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
}
package com.SirBlobman.api.nms;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemHandler_Fallback extends ItemHandler {
    public ItemHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Material matchMaterial(String string) {
        return Material.matchMaterial(string);
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
        short playerHead = 3;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, playerHead);
        
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(username);
        
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        String username = player.getName();
        return getPlayerHead(username);
    }
    
    @Override
    public ItemStack getBase64Head(String base64) {
        throw new UnsupportedOperationException("Unsupported NMS version!");
    }
}
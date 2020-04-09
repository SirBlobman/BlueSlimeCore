package com.SirBlobman.api.nms;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import net.minecraft.server.v1_14_R1.MojangsonParser;
import net.minecraft.server.v1_14_R1.NBTTagCompound;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class ItemHandler_1_14_R1 extends ItemHandler {
    private static final UUID CUSTOM_HEAD_UUID = UUID.fromString("8f0fa41c-ef7a-4cda-80e7-c7b6109c2b6f");
    public ItemHandler_1_14_R1(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Material matchMaterial(String string) {
        return Material.matchMaterial(string, false);
    }
    
    @Override
    public String getLocalizedName(ItemStack item) {
        if(item == null) return "Air";
        
        ItemMeta meta = item.getItemMeta();
        if(meta != null) return (meta.hasDisplayName() ? meta.getDisplayName() : meta.getLocalizedName());
        
        net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName().getText();
    }
    
    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        
        nmsItem.save(nbtData);
        return nbtData.toString();
    }
    
    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            net.minecraft.server.v1_14_R1.ItemStack nmsItem = net.minecraft.server.v1_14_R1.ItemStack.a(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(CommandSyntaxException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }
    
    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;
        
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
    
        JavaPlugin plugin = getPlugin();
        NamespacedKey customKey = new NamespacedKey(plugin, key);
        PersistentDataContainer customData = meta.getPersistentDataContainer();
        
        customData.set(customKey, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) return defaultValue;
    
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return defaultValue;
    
        JavaPlugin plugin = getPlugin();
        NamespacedKey customKey = new NamespacedKey(plugin, key);
        PersistentDataContainer customData = meta.getPersistentDataContainer();
        if(!customData.has(customKey, PersistentDataType.STRING)) return defaultValue;
        
        String string = customData.get(customKey, PersistentDataType.STRING);
        return (string == null ? defaultValue : string);
    }
    
    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) return item;
    
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return item;
    
        JavaPlugin plugin = getPlugin();
        NamespacedKey customKey = new NamespacedKey(plugin, key);
        PersistentDataContainer customData = meta.getPersistentDataContainer();
    
        customData.remove(customKey);
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public ItemStack getPlayerHead(String username) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(username);
        return getPlayerHead(player);
    }
    
    @Override
    public ItemStack getPlayerHead(OfflinePlayer player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
    
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(meta == null) return item;
        meta.setOwningPlayer(player);
    
        item.setItemMeta(meta);
        return item;
    }
    
    @Override
    public ItemStack getBase64Head(String base64) {
        GameProfile gameProfile = new GameProfile(CUSTOM_HEAD_UUID, "Base64");
        PropertyMap properties = gameProfile.getProperties();
    
        Property property = new Property("textures", base64);
        properties.put("textures", property);
        
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if(meta == null) return item;
        
        try {
            Class<? extends SkullMeta> metaClass = meta.getClass();
            Field profileField = metaClass.getDeclaredField("profile");
            
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch(ReflectiveOperationException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while creating a Base64 head.", ex);
            return item;
        }
        
        item.setItemMeta(meta);
        return item;
    }
}
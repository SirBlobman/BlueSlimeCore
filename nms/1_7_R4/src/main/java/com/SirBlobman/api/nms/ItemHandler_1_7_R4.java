package com.SirBlobman.api.nms;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import net.minecraft.server.v1_7_R4.MojangsonParser;
import net.minecraft.server.v1_7_R4.NBTBase;
import net.minecraft.server.v1_7_R4.NBTTagCompound;

import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.minecraft.util.com.mojang.authlib.properties.PropertyMap;

public class ItemHandler_1_7_R4 extends ItemHandler {
    public ItemHandler_1_7_R4(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public Material matchMaterial(String string) {
        return Material.matchMaterial(string);
    }
    
    @Override
    public void setDamage(ItemStack item, int damage) {
        short durability = Integer.valueOf(damage).shortValue();
        item.setDurability(durability);
    }
    
    @Override
    public String getLocalizedName(ItemStack item) {
        if(item == null) return "Air";
        
        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.hasDisplayName()) return meta.getDisplayName();
    
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName();
    }
    
    @Override
    public String toNBT(ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        
        nmsItem.save(nbtData);
        return nbtData.toString();
    }
    
    @Override
    public ItemStack fromNBT(String string) {
        try {
            NBTBase parse = MojangsonParser.parse(string);
            if(!(parse instanceof NBTTagCompound)) {
                JavaPlugin plugin = getPlugin();
                Logger logger = plugin.getLogger();
                logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", new IllegalStateException("'" + string + "' is not an NBTTagCompound."));
                return new ItemStack(Material.AIR);
            }
            
            NBTTagCompound nbtData = (NBTTagCompound) parse;
            net.minecraft.server.v1_7_R4.ItemStack nmsItem = net.minecraft.server.v1_7_R4.ItemStack.createStack(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(Exception ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new ItemStack(Material.AIR);
        }
    }
    
    @Override
    public ItemStack setCustomNBT(ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) return item;
        
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
        
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
        
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.setString(key, value);
        nbtData.set(pluginName, customData);
        
        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
    
    @Override
    public String getCustomNBT(ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) return defaultValue;
    
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
    
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
    
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        String string = customData.getString(key);
        return (string == null ? defaultValue : string);
    }
    
    @Override
    public ItemStack removeCustomNBT(ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) return item;
    
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if(nbtData == null) nbtData = new NBTTagCompound();
    
        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();
    
        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.remove(key);
        
        if (customData.isEmpty()) nbtData.remove(pluginName);
        else nbtData.set(pluginName, customData);
    
        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
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
        byte[] base64Bytes = base64.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(base64Bytes);
        return getBase64Head(base64, uuid);
    }
    
    @Override
    public ItemStack getBase64Head(String base64, UUID uuid) {
        GameProfile gameProfile = new GameProfile(uuid, "custom");
        Property property = new Property("textures", base64);
        
        PropertyMap properties = gameProfile.getProperties();
        properties.put("textures", property);
        return createGameProfileHead(gameProfile);
    }
    
    private ItemStack createGameProfileHead(GameProfile gameProfile) {
        short playerHead = 3;
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, playerHead);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        
        try {
            Class<? extends SkullMeta> metaClass = meta.getClass();
            Field profileField = metaClass.getDeclaredField("profile");
            
            profileField.setAccessible(true);
            profileField.set(meta, gameProfile);
        } catch(ReflectiveOperationException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while creating a GameProfile head.", ex);
            return item;
        }
        
        item.setItemMeta(meta);
        return item;
    }
}
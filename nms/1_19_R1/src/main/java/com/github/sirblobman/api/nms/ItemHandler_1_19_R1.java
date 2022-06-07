package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage;

import com.github.sirblobman.api.utility.ItemUtility;

public class ItemHandler_1_19_R1 extends ItemHandler {
    public ItemHandler_1_19_R1(JavaPlugin plugin) {
        super(plugin);
    }
    
    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsItem = ItemStack.EMPTY;
        if(!ItemUtility.isAir(item)) {
            nmsItem = CraftItemStack.asNMSCopy(item);
        }
        
        Component hoverName = nmsItem.getHoverName();
        return CraftChatMessage.fromComponent(hoverName);
    }
    
    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        Material material = Material.AIR;
        if(!ItemUtility.isAir(item)) {
            material = item.getType();
        }
        
        NamespacedKey key = material.getKey();
        return key.toString();
    }
    
    @Override
    public String toNBT(org.bukkit.inventory.ItemStack item) {
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        CompoundTag compoundTag = new CompoundTag();
        nmsItem.save(compoundTag);
        
        return compoundTag.toString();
    }
    
    @Override
    public org.bukkit.inventory.ItemStack fromNBT(String string) {
        try {
            StringReader stringReader = new StringReader(string);
            TagParser tagParser = new TagParser(stringReader);
            CompoundTag compoundTag = tagParser.readStruct();
            
            ItemStack nmsItem = ItemStack.of(compoundTag);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch(CommandSyntaxException ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item:", ex);
            logger.log(Level.WARNING, "returning AIR....");
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }
    
    @Override
    public org.bukkit.inventory.ItemStack setCustomNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        if(item == null || key == null || key.isEmpty() || value == null) {
            return item;
        }
        
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return item;
        }
        
        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(namespacedKey, PersistentDataType.STRING, value);
        
        item.setItemMeta(itemMeta);
        return item;
    }
    
    @Override
    public String getCustomNBT(org.bukkit.inventory.ItemStack item, String key, String defaultValue) {
        if(item == null || key == null || key.isEmpty()) {
            return defaultValue;
        }
        
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return defaultValue;
        }
        
        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.getOrDefault(namespacedKey, PersistentDataType.STRING, defaultValue);
    }
    
    @Override
    public org.bukkit.inventory.ItemStack removeCustomNBT(org.bukkit.inventory.ItemStack item, String key) {
        if(item == null || key == null || key.isEmpty()) {
            return item;
        }
        
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null) {
            return item;
        }
        
        JavaPlugin plugin = getPlugin();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.remove(namespacedKey);
        
        item.setItemMeta(itemMeta);
        return item;
    }
    
    @Override
    public org.bukkit.inventory.ItemStack fromBase64String(String string) {
        if(string == null || string.isBlank()) {
            return null;
        }
        
        CompoundTag compoundTag;
        byte[] decode = Base64.getDecoder().decode(string);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(decode);
        
        try {
            compoundTag = NbtIo.readCompressed(inputStream);
        } catch(Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to decode an item from a string because an error occurred:", ex);
            return null;
        }
        
        ItemStack nmsItem = ItemStack.of(compoundTag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
    
    @Override
    public String toBase64String(org.bukkit.inventory.ItemStack item) {
        if(ItemUtility.isAir(item)) {
            return null;
        }
        
        CompoundTag compoundTag = new CompoundTag();
        CraftItemStack.asNMSCopy(item).save(compoundTag);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            NbtIo.writeCompressed(compoundTag, outputStream);
        } catch(Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to encode an item to a string because an error occurred:", ex);
            return null;
        }
        
        byte[] encode = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(encode);
    }
}

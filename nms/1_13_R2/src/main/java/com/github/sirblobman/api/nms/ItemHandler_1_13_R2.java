package com.github.sirblobman.api.nms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.MojangsonParser;
import net.minecraft.server.v1_13_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import com.github.sirblobman.api.utility.ItemUtility;

public class ItemHandler_1_13_R2 extends ItemHandler {
    public ItemHandler_1_13_R2(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getLocalizedName(org.bukkit.inventory.ItemStack item) {
        if (item == null) return "Air";

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.hasDisplayName()) return meta.getDisplayName();

        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return nmsItem.getName().getText();
    }

    @Override
    public String getKeyString(org.bukkit.inventory.ItemStack item) {
        if (item == null) return "minecraft:air";
        Material material = item.getType();
        NamespacedKey key = material.getKey();
        return key.toString();
    }

    @Override
    public String toNBT(org.bukkit.inventory.ItemStack item) {
        NBTTagCompound nbtData = new NBTTagCompound();
        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);

        nmsItem.save(nbtData);
        return nbtData.toString();
    }

    @Override
    public org.bukkit.inventory.ItemStack fromNBT(String string) {
        try {
            NBTTagCompound nbtData = MojangsonParser.parse(string);
            ItemStack nmsItem = ItemStack.a(nbtData);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } catch (CommandSyntaxException ex) {
            JavaPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "Failed to parse an NBT string to an item, returning AIR...", ex);
            return new org.bukkit.inventory.ItemStack(Material.AIR);
        }
    }

    @Override
    public org.bukkit.inventory.ItemStack setCustomNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        if (item == null || key == null || key.isEmpty() || value == null) return item;

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        customData.setString(key, value);
        nbtData.set(pluginName, customData);

        nmsItem.setTag(nbtData);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String getCustomNBT(org.bukkit.inventory.ItemStack item, String key, String defaultValue) {
        if (item == null || key == null || key.isEmpty()) return defaultValue;

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) nbtData = new NBTTagCompound();

        JavaPlugin plugin = getPlugin();
        String pluginName = plugin.getName();

        NBTTagCompound customData = nbtData.getCompound(pluginName);
        String string = customData.getString(key);
        return (string == null ? defaultValue : string);
    }

    @Override
    public org.bukkit.inventory.ItemStack removeCustomNBT(org.bukkit.inventory.ItemStack item, String key) {
        if (item == null || key == null || key.isEmpty()) return item;

        ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound nbtData = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        if (nbtData == null) nbtData = new NBTTagCompound();

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
    public org.bukkit.inventory.ItemStack fromBase64String(String string) {
        if (string == null || string.isEmpty()) return null;

        NBTTagCompound nbtTagCompound;
        byte[] decode = Base64.getDecoder().decode(string);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);

        try {
            nbtTagCompound = NBTCompressedStreamTools.a(byteArrayInputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to decode an item from a string because an error occurred:", ex);
            return null;
        }

        ItemStack nmsItem = ItemStack.a(nbtTagCompound);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public String toBase64String(org.bukkit.inventory.ItemStack item) {
        if (ItemUtility.isAir(item)) return null;

        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CraftItemStack.asNMSCopy(item).save(nbtTagCompound);

        try {
            NBTCompressedStreamTools.a(nbtTagCompound, byteArrayOutputStream);
        } catch (Exception ex) {
            Logger logger = getPlugin().getLogger();
            logger.log(Level.WARNING, "Failed to encode an item to a string because an error occurred:", ex);
            return null;
        }

        byte[] encode = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(encode);
    }
}
